# Ark Ascended Server Manager
##### A multi server manager for the game: ARK: Survival Ascended
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/85284773/689f1028-1e27-422d-bfff-9e9604ff043b" width="600" ></img>
<img src="https://github.com/JensvandeWiel/ArkAscendedServerManager/assets/85284773/561453ef-26ca-40ed-984c-4ab47b5c7dc7" width="600" ></img>
### Why
For ARK: SE there only existed a closed source with relic like code🧓. When they made the announcement that they won't make a new version/update their software for ARK: Ascended we stepped in to fill the void, our goal is to make this software easy to use and cross platform (when we get linux binaries for the servers).
### How to use
For now the only way is to use the [nightly](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases/tag/nightly) release, this is because we are not yer ready to release a alpha. You can download the installer and install! **(nightly is unstable and does not have auto updating at this time!)**

#### Setting steamcmd path for first use
Steamcmd is not yet packaged with the app, if you want to install a server you can install steamcmd to your path on your computer (e.g.: with [scoop](https://scoop.sh/#/apps?q=steamcmd&id=aed594e2b74d756901130cc098dfb2f70679d8dc)) or you can follow the guide below.

 1. Install the app using the installer
 2. Run the app for the first time
 3. Click [windows key] + R and enter `%localappdata%\ArkAscendedServerManager`
 4. Open config.json
 5. Edit the "steamCMDPath" to contain something like this: `"steamCMDPath": "C:\\steamcmd\\steamcmd.exe"` (**WATCH OUT** A backslash only works like `\\` this is because `\` on its own is a ecape char)
 6. Start the app again and create a server.
 7. :tada: You can run your server!
### Contributing
Ask one of **the core contributors** in the [discord](https://discord.gg/pBfmFuK2Wa)  to give you contributor role, then you can ask what to do. Or you can just make a pull request with some code you've written. 
### :warning: Watch out :warning:
We have not officialy released anything yet, the alpha release is planned but not yet released! This codebase is very young and is not yet release ready.
