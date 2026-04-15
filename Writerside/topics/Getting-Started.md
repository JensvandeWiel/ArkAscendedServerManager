# Getting Started

## Overview

This quickstart guides you through:

* [Installing Prerequisites](#installing-prerequisites)
* [Installing the Manager](#installing-the-manager)

This is intended for server owners starting to use this app. It assumes that you have basic knowledge of:

* [Setting up Ark Servers](https://ark.wiki.gg/wiki/Dedicated_server_setup)
* [Configuring Ark Servers](https://ark.wiki.gg/wiki/Server_Configuration)
* [(Optional) Ark API](https://ark-server-api.com/threads/asa-server-api.33/)

## Before you start

Before continuing, make sure you have the following prerequisites:

* Windows 10 22H2 or later or Windows Server 2019 (with GUI) or later
* Make sure your OS is 64-bit

## Recommendations

We recommend the following for hosting at least 2 ark servers and the manager:

* 32GB RAM
* 100GB SSD (Nvme preferred)
* High Single Core Performance CPU

## Installing Prerequisites

In this section we will install the prerequisites for ARK server in general and for the Manager.

### Part 1: Installing Microsoft Visual C++ Redistributables 2013 and 2019
Get both installers from the links below.
#### VC Redistributables 2013
You can download the installer from [here](https://aka.ms/highdpimfc2013x64enu).
#### VC Redistributables 2019
You can download the installer from [here](https://aka.ms/vs/17/release/vc_redist.x64.exe).

#### Step 2: Run the installers
For both installers, open them and follow the instructions. Until the installer is finished, and you see the following:
![vc_installed](vc_installed.png)
(Version number may vary)

### Part 2: Installing DirectX End-User Runtimes (June 2010)
An ark server requires DirectX End-User Runtimes (June 2010) to run. You can download the installer from [here](https://www.microsoft.com/en-us/download/details.aspx?id=8109).

#### Step 1: Run the extractor
Run the installed executable and click the following:
![dx_install_aceept_agreements](dx_install_aceept_agreements.png)
Then click the following:
![dx_install_location](dx_install_location.png)
Then choose a location to extract the files to, we recommend `Downloads`.
#### Step 2: Run the installer
After this process is finished, you can open the folder where you extracted the files and run `DXSETUP.exe` to install DirectX End-User Runtimes (June 2010).

Follow the instructions on the installer. And when its finished, you should see the following:
![dx_installed.png](dx_installed.png)

### Part 3: Installing Certificates (Windows Server Only)
If you are running Windows Server, you will need to install a certificate from [here](http://crl.r2m02.amazontrust.com/r2m02.crl).

After downloading the certificate, right click on the file and select `Place all certificates in the following store and selecting Trusted Root Certification Authorities.` A system restart may be required and is suggested after this process.

## Installing the Manager
To install the manager, follow the following steps: 
- Go to the latest release from [here](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases/latest)
- Scroll down to the assets section and download the latest `arkascendedservermanager-x.x.x-win-x64-nsis.exe` installer.
- Start the installer you will get a message saying that the installer is signed by an unknown publisher.
> Watch out for this message, if you downloaded the installer from the official github releases page, this is a false positive and you can safely ignore it. If you downloaded the installer from another source, we recommend not to run it and to download the installer from the official github releases page at [](https://github.com/JensvandeWiel/ArkAscendedServerManager/releases).
> ![windows_protection.png](windows_protection.png)
{style="warning"}

- Click `More Info` and then `Run Anyway`.
- Follow the instructions on the installer.
- After the installation is finished, the manager will automatically start.

![aasm_info_page.png](aasm_info_page.png)

## Next steps

Now that you've completed this quickstart, set up your first server!

* Setting up a server: [](Setting-up-your-first-server.md)
* Importing a server: [](Importing-A-Server.md)