use std::ffi::{CStr, CString, c_char};
use std::path::PathBuf;
use crate::version::get_asa_version;

mod version;

#[unsafe(no_mangle)]
pub extern "C" fn core_native_get_asa_version(exec_path: *const c_char) -> *mut c_char {
    if exec_path.is_null() {
        return std::ptr::null_mut();
    }

    let exec_path_str = unsafe { CStr::from_ptr(exec_path) };
    let exec_path_buf = PathBuf::from(exec_path_str.to_string_lossy().as_ref());

    let found_version = match get_asa_version(&exec_path_buf) {
        Ok(version) => version,
        Err(_) => return std::ptr::null_mut(),
    };

    match CString::new(found_version) {
        Ok(version_cstr) => version_cstr.into_raw(),
        Err(_) => std::ptr::null_mut(),
    }
}

#[unsafe(no_mangle)]
pub extern "C" fn core_native_free_string(raw: *mut c_char) {
    if raw.is_null() {
        return;
    }

    unsafe {
        drop(CString::from_raw(raw));
    }
}
