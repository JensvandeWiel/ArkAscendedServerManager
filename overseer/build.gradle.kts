plugins {
    id("base")
    alias(libs.plugins.rust)
}

rust {
    crateName.set("overseer")
    toolchain.set("1.94.1-x86_64-pc-windows-msvc")
    isLibrary.set(false)
}


