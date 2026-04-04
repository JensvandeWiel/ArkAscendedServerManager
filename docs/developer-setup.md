# Developer Setup

This project is still under active development, so the app is not ready for normal use yet.

## Prerequisites

- **JBR 25 / JDK 25** — the `ui` module is configured to use Java 25.
- **Rust toolchain** — install Rust with `rustup` so `rustc` and `cargo` are available on your PATH.
- **Cargo** — used to build the native `core-native` module.
- **Visual Studio Build Tools** — install the C++ workload and Windows SDK for native Rust builds on Windows.
- **Git** — for cloning the repository.

### Suggested Windows install steps

- Install **JBR 25** through JetBrains Toolbox or another JDK 25 distribution, then point IntelliJ IDEA's Gradle JVM to it.
- Install Rust with `rustup`, then make sure the MSVC toolchain is selected:

```powershell
rustup default stable-x86_64-pc-windows-msvc
rustup target add x86_64-pc-windows-msvc
```

- In Visual Studio Installer, add **Desktop development with C++** and the **Windows 10/11 SDK**.

## Recommended local tooling

- Use the Gradle wrapper that ships with the repo (`gradlew.bat` on Windows).
- In IntelliJ IDEA, point the project/Gradle JVM to JBR 25 so the toolchain matches the build.

## Credentials

Gradle can read GitHub Packages credentials from any of these sources:

1. Environment variables: `GITHUB_USERNAME` and `GITHUB_TOKEN`
2. Gradle properties: `gpr.user` and `gpr.key`
3. A local `.env` file copied from [`.env.example`](../.env.example)

The `.env` file is optional, but it is the easiest local fallback.

## Verify your install

Run these commands in PowerShell:

```powershell
rustc --version
cargo --version
java -version
```

If you are using IntelliJ IDEA, confirm that the Gradle JVM is set to JBR 25.

## First-time setup

```powershell
git clone <repo-url>
cd ArkAscendedServerManager
Copy-Item .env.example .env
# Edit .env if you need GitHub Packages credentials
```

## Build

```powershell
.\gradlew.bat build
```

The `core-native` module will invoke Cargo automatically during the build.

## Notes

- If Cargo cannot find the native toolchain, make sure the MSVC C++ build tools are installed.
- If dependency resolution fails for `GitHubPackages`, check that both credential values are set.

