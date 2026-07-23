use std::env;
use std::ffi::OsStr;
use std::ffi::OsString;
use std::fs::{File, OpenOptions};
use std::io::{self, Write};
use std::os::windows::ffi::OsStrExt;
use std::path::{Path, PathBuf};
use std::process;
use std::sync::{
    Arc, Mutex,
    atomic::{AtomicBool, Ordering},
};
use std::time::{Duration, SystemTime, UNIX_EPOCH};

use winptyrs::{AgentConfig, MouseMode, PTY, PTYArgs, PTYBackend};

mod output_sanitizer;

use output_sanitizer::OutputSanitizer;

#[cfg(not(windows))]
compile_error!("Overseer currently supports Windows only.");

const SERVER_EXE: &str = "AsaApiLoader.exe";
const LOG_DIR_NAME: &str = "logs";
const LOG_FILE_NAME: &str = "ark_server.log";
const OVERSEER_LOG_FILE_NAME: &str = "overseer.log";

struct LaunchTarget {
    executable: PathBuf,
    cwd: PathBuf,
}

fn main() {
    let args: Vec<OsString> = env::args_os().skip(1).collect();
    let server_log_path = default_log_path();
    let overseer_log_path = default_overseer_log_path();
    let shutdown_requested = Arc::new(AtomicBool::new(false));

    if let Err(err) = ctrlc::set_handler({
        let shutdown_requested = Arc::clone(&shutdown_requested);
        move || {
            shutdown_requested.store(true, Ordering::SeqCst);
        }
    }) {
        eprintln!("Failed to install Ctrl+C handler: {err}");
        process::exit(1);
    }

    let exit_code = match run_server(
        SERVER_EXE,
        &args,
        &server_log_path,
        &overseer_log_path,
        &shutdown_requested,
    ) {
        Ok(code) => code,
        Err(err) => {
            eprintln!("Failed to run {SERVER_EXE}: {err}");
            1
        }
    };

    process::exit(exit_code);
}

fn default_log_path() -> PathBuf {
    default_log_path_for(LOG_FILE_NAME)
}

fn default_overseer_log_path() -> PathBuf {
    default_log_path_for(OVERSEER_LOG_FILE_NAME)
}

fn default_log_path_for(file_name: &str) -> PathBuf {
    if let Some(exe_dir) = executable_directory() {
        return exe_dir.join(LOG_DIR_NAME).join(file_name);
    }

    PathBuf::from(file_name)
}

fn executable_directory() -> Option<PathBuf> {
    env::current_exe().ok()?.parent().map(Path::to_path_buf)
}

fn run_server(
    server_exe: &str,
    args: &[OsString],
    server_log_path: &Path,
    overseer_log_path: &Path,
    shutdown_requested: &Arc<AtomicBool>,
) -> io::Result<i32> {
    let server_log_file = open_server_log_file(server_log_path)?;
    let server_writer = Arc::new(Mutex::new(server_log_file));
    let overseer_log_file = open_overseer_log_file(overseer_log_path)?;
    let overseer_writer = Arc::new(Mutex::new(overseer_log_file));
    let launch = resolve_launch_target(server_exe)?;

    write_meta_line(
        &overseer_writer,
        &format!(
            "starting {} with {} args; cwd={}; server_log={}; overseer_log={}",
            launch.executable.display(),
            args.len(),
            launch.cwd.display(),
            server_log_path.display(),
            overseer_log_path.display()
        ),
    )?;

    #[cfg(windows)]
    {
        return run_server_with_pty(
            &launch,
            args,
            &server_writer,
            &overseer_writer,
            shutdown_requested,
        );
    }

    #[cfg(not(windows))]
    {
        Err(io::Error::other("windows-only build"))
    }
}

#[cfg(windows)]
fn run_server_with_pty(
    launch: &LaunchTarget,
    args: &[OsString],
    server_writer: &Arc<Mutex<File>>,
    overseer_writer: &Arc<Mutex<File>>,
    shutdown_requested: &Arc<AtomicBool>,
) -> io::Result<i32> {
    let pty_args = PTYArgs {
        cols: 512,
        rows: 40,
        mouse_mode: MouseMode::WINPTY_MOUSE_MODE_NONE,
        timeout: 10000,
        agent_config: AgentConfig::WINPTY_FLAG_COLOR_ESCAPES,
    };

    let mut pty = PTY::new_with_backend(&pty_args, PTYBackend::ConPTY)
        .map_err(|e| io::Error::other(format!("failed to create PTY: {}", e.to_string_lossy())))?;

    let appname = launch.executable.as_os_str().to_os_string();
    let cmdline = build_cmdline(args);
    let cwd = Some(launch.cwd.as_os_str().to_os_string());

    pty.spawn(appname, cmdline, cwd, None)
        .map_err(|e| io::Error::other(format!("failed to spawn PTY child: {}", e.to_string_lossy())))?;

    let pid = pty.get_pid();
    write_meta_line(overseer_writer, &format!("child pid={pid} (pty)"))?;

    let mut sanitizer = OutputSanitizer::new();
    let mut total_bytes = 0_usize;
    let mut termination_requested = false;

    loop {
        match pty.read(false) {
            Ok(output) if !output.is_empty() => {
                let bytes = os_string_to_bytes(&output);
                total_bytes += bytes.len();
                let cleaned = sanitizer.filter(&bytes);
                write_log_data(server_writer, &cleaned)?;
            }
            Err(_) => break,
            _ => {}
        }

        if !pty.is_alive().unwrap_or(false) {
            if let Ok(output) = pty.read(true) {
                let bytes = os_string_to_bytes(&output);
                total_bytes += bytes.len();
                let cleaned = sanitizer.filter(&bytes);
                write_log_data(server_writer, &cleaned)?;
            }
            break;
        }

        if shutdown_requested.load(Ordering::SeqCst) && !termination_requested {
            kill_process_by_pid(pid);
            termination_requested = true;
        }

        std::thread::sleep(Duration::from_millis(50));
    }

    let tail = sanitizer.finish();
    if !tail.is_empty() {
        write_log_data(server_writer, &tail)?;
    }

    let exit_code = pty
        .get_exitstatus()
        .ok()
        .flatten()
        .and_then(|c| i32::try_from(c).ok())
        .unwrap_or(1);

    write_meta_line(
        overseer_writer,
        &format!("child exited with code={exit_code}; captured pty_bytes={total_bytes}"),
    )?;

    if total_bytes == 0 {
        write_meta_line(
            overseer_writer,
            "warning: child produced no PTY output; for Ark, include -log and inspect engine log files",
        )?;
    }

    Ok(exit_code)
}

fn build_cmdline(args: &[OsString]) -> Option<OsString> {
    if args.is_empty() {
        return None;
    }
    let parts: Vec<String> = args
        .iter()
        .map(|a| {
            let s = a.to_string_lossy();
            if s.contains(' ') {
                format!("\"{}\"", s)
            } else {
                s.to_string()
            }
        })
        .collect();
    Some(parts.join(" ").into())
}

fn os_string_to_bytes(s: &OsStr) -> Vec<u8> {
    let wide: Vec<u16> = s.encode_wide().collect();
    String::from_utf16_lossy(&wide).into_bytes()
}

fn kill_process_by_pid(pid: u32) {
    let _ = std::process::Command::new("taskkill")
        .args(["/F", "/PID", &pid.to_string()])
        .output();
}

fn write_log_data(writer: &Arc<Mutex<File>>, data: &[u8]) -> io::Result<()> {
    if data.is_empty() {
        return Ok(());
    }
    let mut log = writer
        .lock()
        .map_err(|_| io::Error::other("failed to lock log file writer"))?;
    log.write_all(data)?;
    log.flush()?;
    Ok(())
}

fn resolve_launch_target(server_exe: &str) -> io::Result<LaunchTarget> {
    let launcher_dir = env::current_exe()?
        .parent()
        .map(Path::to_path_buf)
        .ok_or_else(|| io::Error::other("failed to determine launcher directory"))?;

    let preferred = launcher_dir.join(server_exe);
    let executable = if preferred.exists() {
        preferred
    } else {
        PathBuf::from(server_exe)
    };

    let cwd = executable
        .parent()
        .map(Path::to_path_buf)
        .unwrap_or(launcher_dir);

    Ok(LaunchTarget { executable, cwd })
}

fn open_server_log_file(log_path: &Path) -> io::Result<File> {
    ensure_parent_dir(log_path)?;

    OpenOptions::new()
        .create(true)
        .write(true)
        .truncate(true)
        .open(log_path)
}

fn open_overseer_log_file(log_path: &Path) -> io::Result<File> {
    ensure_parent_dir(log_path)?;

    OpenOptions::new()
        .create(true)
        .write(true)
        .append(true)
        .open(log_path)
}

fn ensure_parent_dir(path: &Path) -> io::Result<()> {
    if let Some(parent) = path.parent() {
        std::fs::create_dir_all(parent)?;
    }
    Ok(())
}

fn write_meta_line(writer: &Arc<Mutex<File>>, message: &str) -> io::Result<()> {
    let now = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .map(|d| d.as_secs())
        .unwrap_or(0);
    let line = format!("[overseer {now}] {message}\n");

    let mut log = writer
        .lock()
        .map_err(|_| io::Error::other("failed to lock log file writer"))?;
    log.write_all(line.as_bytes())?;
    log.flush()?;
    Ok(())
}

#[cfg(test)]
mod tests {
    use super::run_server;
    use std::ffi::OsString;
    use std::fs;
    use std::path::PathBuf;
    use std::sync::{Arc, atomic::AtomicBool};
    use std::time::{SystemTime, UNIX_EPOCH};

    #[cfg(windows)]
    #[test]
    fn captures_stdout_and_stderr_with_separate_persistent_overseer_log() {
        let server_log_path = unique_test_log_path("server");
        let overseer_log_path = unique_test_log_path("overseer");

        fs::write(&server_log_path, b"stale content from a previous run")
            .expect("seed server log file");
        fs::write(&overseer_log_path, b"old overseer line\n").expect("seed overseer log file");
        let shutdown_requested = Arc::new(AtomicBool::new(false));

        let args = vec![
            OsString::from("/C"),
            OsString::from("echo stdout-line & echo stderr-line 1>&2 & exit /B 7"),
        ];

        let code = run_server(
            "cmd.exe",
            &args,
            &server_log_path,
            &overseer_log_path,
            &shutdown_requested,
        )
        .expect("cmd.exe should run");
        assert_eq!(code, 7);

        let server_content =
            fs::read_to_string(&server_log_path).expect("server log should be readable");
        assert!(server_content.contains("stdout-line"));
        assert!(server_content.contains("stderr-line"));
        assert!(!server_content.contains("[overseer "));

        let overseer_content =
            fs::read_to_string(&overseer_log_path).expect("overseer log should be readable");
        assert!(overseer_content.contains("old overseer line"));
        assert!(overseer_content.contains("[overseer "));

        let _ = fs::remove_file(server_log_path);
        let _ = fs::remove_file(overseer_log_path);
    }

    fn unique_test_log_path(kind: &str) -> PathBuf {
        let now = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .expect("system time should be after epoch")
            .as_nanos();
        std::env::temp_dir().join(format!("overseer-test-{kind}-{now}.log"))
    }
}
