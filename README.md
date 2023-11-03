# Ark Ascended Server Manager
##### A multi server manager for the game: ARK: Survival Ascended
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/53059031/7cbc93de-64a8-4b3a-8bfc-4da871bb255f" width="600" ></img>
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/53059031/bc82cb23-02a8-495c-99d5-0ca84ef663a2" width="600" ></img>
### Why
For ARK: SE there only existed a closed source with relic like code🧓. When they made the announcement that they won't make a new version/update their software for ARK: Ascended we stepped in to fill the void, our goal is to make this software easy to use and cross platform (when we get linux binaries for the servers).
### How to use
For now the only way is to use the [nightly](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases/tag/nightly) release, this is because we are not yet ready to release an alpha version. You can download the installer and install! **(nightly is unstable and does not have auto updating at this time!)**

#### Setting steamcmd path for first use
Steamcmd is not yet packaged with the app, if you want to create a server with AASM you can install steamcmd to your path on your computer (e.g.: with [scoop](https://scoop.sh/#/apps?q=steamcmd&id=aed594e2b74d756901130cc098dfb2f70679d8dc)) or you can follow the guide below.

 1. Install the app using the installer
 2. Run the app for the first time
 3. Click [windows key] + R and enter `%localappdata%\ArkAscendedServerManager`
 4. Open config.json
 5. Edit the "steamCMDPath" to contain something like this: `"steamCMDPath": "C:\\steamcmd\\steamcmd.exe"` (**WATCH OUT** A backslash only works like `\\` this is because `\` on its own is a ecape char)
 6. Start the app again and create a server.
 7. :tada: You can run your server!
### Contributing
Ask one of **the core contributors** in the [discord server](https://discord.gg/pBfmFuK2Wa)  to give you contributor role, then you can ask what to do. Or you can just make a pull request with some code you've written. 
