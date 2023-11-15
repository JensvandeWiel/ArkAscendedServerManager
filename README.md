# Ark Ascended Server Manager
##### A multi server manager for the game: ARK: Survival Ascended
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/53059031/63e303e8-f2a8-4cdc-9c2c-4eb68050b051" width="600" ></img>
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/53059031/2d7a2314-9b87-45b4-9469-f018c4fedb0d" width="600" ></img>
### Why
For ARK: SE there only existed a closed source with relic like code🧓. When they made the announcement that they won't make a new version/update their software for ARK: Ascended we stepped in to fill the void, our goal is to make this software easy to use and cross platform (when we get linux binaries for the servers).
### How to use
For now the only way is to use the [nightly](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases/tag/nightly) release, this is because we are not yet ready to release an alpha version. You can download the installer and install! **(nightly is unstable and does not have auto updating at this time!)**

#### Setting steamcmd path for first use
Steamcmd is not yet packaged with the app, if you want to create a server with AASM you can install steamcmd to your path on your computer (e.g.: with [scoop](https://scoop.sh/#/apps?q=steamcmd&id=aed594e2b74d756901130cc098dfb2f70679d8dc)) or you can follow the guide below.

 1. Install the app using the installer found [here](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases)
 2. Run the app for the first time
 3. Click [windows key] + R and enter `%localappdata%\ArkAscendedServerManager`
 4. Open config.json
 5. Edit the "steamCMDPath" to contain something like this: `"steamCMDPath": "C:\\steamcmd\\steamcmd.exe"` (**WATCH OUT** A backslash only works like `\\` this is because `\` on its own is a ecape char)
 6. Start the app again and create a server.
 7. :tada: You can run your server!
### Contributing
Check out the [Contributing Guide](https://github.com/JensvandeWiel/ArkAscendedServerManager/blob/main/CONTRIBUTING.md) to get started with contributing! Once your first contribution is merged you can head over to our [Discord Server](https://discord.gg/vcfNeZ3SDN) and ask for the 'Contributor' role :D
### :warning: Watch out :warning:
This project is still only in very early alpha, so there are still many features to come!
