# ARK Ascended Server Manager

A desktop application for managing ARK: Survival Ascended dedicated servers built with Kotlin and Compose Desktop.

## Features

- **Server Profile Management**: Create, configure, and manage multiple ARK server profiles
- **Server Installation**: Automated server installation using SteamCMD integration
- **Configuration Management**: Easy-to-use interface for editing server settings
- **Server Control**: Start, stop, and monitor server processes
- **Modern UI**: Fluent Design System UI components for a native Windows looking experience

## Technologies Used

- **Kotlin**: Primary programming language
- **Compose Desktop**: Modern declarative UI framework
- **Decompose**: Navigation and component lifecycle management
- **Fluent UI**: Microsoft Fluent Design System components
- **SteamCMD**: Automated game server installation and updates
- **Coroutines**: Asynchronous programming
- **Kotlinx Serialization**: JSON and configuration file handling

## Requirements

- Windows 10/11
- Java 21 or higher
- SteamCMD (automatically managed by the application)

## Installation

### Download Release
1. Download the latest MSI installer from the [Releases](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases) page
2. Run the installer and follow the setup wizard
3. Launch the application from the Start Menu or Desktop shortcut

## Configuration

### Application Settings
User settings are stored in:
- Windows: `%USERPROFILE%\.aasm\`
- Logs: `%USERPROFILE%\.aasm\app.log`

## Development

### Prerequisites
- JDK 21+
- Gradle 8.0+

### Dev Environment Variables
Create a `.env` file in the project root for development:
```
GITHUB_USERNAME=your-username
GITHUB_TOKEN=your-token
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [ARK: Survival Ascended](https://store.steampowered.com/app/2399830/ARK_Survival_Ascended/) by Studio Wildcard
- [Compose Desktop](https://www.jetbrains.com/lp/compose-desktop/) by JetBrains
- [Fluent UI](https://github.com/compose-fluent/compose-fluent-ui) components
- [SteamCMD](https://developer.valvesoftware.com/wiki/SteamCMD) by Valve

## Support

For support, bug reports, or feature requests, please open an issue on the [GitHub Issues](https://github.com/JensvandeWiel/ArkAscendedServerManager/issues) page.
