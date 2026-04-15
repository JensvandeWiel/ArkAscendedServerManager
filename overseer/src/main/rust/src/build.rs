extern crate winres;

fn main() {
    let mut res = winres::WindowsResource::new();

    let version = std::env::var("CARGO_PKG_VERSION").unwrap();

    let win_version = if version.matches('.').count() == 2 {
        format!("{}.0", version)
    } else {
        version
    };

    res.set("FileVersion", &win_version);
    res.set("ProductVersion", &win_version);
    res.set("ProductName", "AASM Overseer");
    res.set("FileDescription", "Overseer is a too to capture and manage the output and lifecycle of an ark server");

    res.compile().unwrap();
}