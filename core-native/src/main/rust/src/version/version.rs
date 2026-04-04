use std::fs::File;
use std::io::{Read, Seek, SeekFrom};
use std::path::PathBuf;

pub(crate) fn get_asa_version(exe_path: &PathBuf) -> anyhow::Result<String> {
    let mut file = File::open(exe_path)?;

    // "ArkVersion" in Unicode encoding
    let target_bytes = [
        0x41, 0x00, 0x72, 0x00, 0x6B, 0x00, 0x56, 0x00, 0x65, 0x00, 0x72, 0x00, 0x73, 0x00, 0x69,
        0x00, 0x6F, 0x00, 0x6E, 0x00, 0x00, 0x00,
    ];

    let mut buffer = vec![0u8; 1024 * 1024];
    let overlap = target_bytes.len() - 1;

    let mut valid_len = 0;
    while valid_len < buffer.len() {
        let n = file.read(&mut buffer[valid_len..])?;
        if n == 0 { break; }
        valid_len += n;
    }

    let mut file_offset = 0u64;
    let mut found_offset = None;

    loop {
        let valid_data = &buffer[..valid_len];
        let mut pos = 0;

        while let Some(idx) = valid_data[pos..].iter().position(|&b| b == target_bytes[0]) {
            pos += idx;
            if pos + target_bytes.len() <= valid_len {
                if valid_data[pos..pos + target_bytes.len()] == target_bytes {
                    found_offset = Some(file_offset + pos as u64);
                    break;
                }
            } else {
                break;
            }
            pos += 1;
        }

        if found_offset.is_some() {
            break;
        }

        if valid_len < buffer.len() {
            break; // EOF reached
        }

        buffer.copy_within(valid_len - overlap..valid_len, 0);
        file_offset += (valid_len - overlap) as u64;

        valid_len = overlap;
        while valid_len < buffer.len() {
            let n = file.read(&mut buffer[valid_len..])?;
            if n == 0 { break; }
            valid_len += n;
        }

        std::thread::yield_now();
    }

    if let Some(offset) = found_offset {
        file.seek(SeekFrom::Start(offset + target_bytes.len() as u64))?;

        let mut reader = std::io::BufReader::new(file);
        let mut version = String::new();
        let mut buf = [0u8; 2];
        while reader.read_exact(&mut buf).is_ok() {
            let unicode_val = u16::from_le_bytes(buf);
            if unicode_val == 0 {
                break;
            }
            if let Some(char) = char::from_u32(unicode_val as u32) {
                version.push(char);
            } else {
                return Err(anyhow::anyhow!(
                    "Failed to convert UTF-16 code unit while reading version: {unicode_val:#06X}"
                ));
            }
        }
        return Ok(version);
    }

    Err(anyhow::anyhow!("Failed to find ArkVersion string in the executable"))
}