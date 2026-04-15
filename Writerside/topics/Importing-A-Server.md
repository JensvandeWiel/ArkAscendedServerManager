# Importing a server
This guide will show you how to import a server to the manager. This is useful if you already have a server set up and want to manage it with the manager, or if you want to move a server from one machine to another.

## Step 0: Open the manager
Open the manager by looping it up in the start menu, when it's open, it should look like this:
![manager_info_page.png](manager_info_page.png)

## Step 1: Open the servers overview
Click on the servers overview in the left sidebar.
![servers_overview.png](servers_overview.png)

## Step 2: Backup the server
Before you import a server, it's always a good idea to make a backup of the server files, this way you can always restore the server to its previous state if something goes wrong during the import process. To backup the server, simply copy the server directory to a safe location.

## Step 3: Click on the Import server button
Click on the import server button in the top right corner, this will open a dialog where you can select the server directory you want to import.
![import_server_button.png](import_server_button.png)

### Step 3.1: Select the server directory
Select the server directory you want to import in the dialog by clicking the input field:
![import_server.png](import_server.png)

> Make sure you select the root folder of the server installation. You can identify the installation path by looking for the 'ShooterGame' and 'Engine' folder in the server installation directory. A installed server directory should look like this:
> ![server_directory](server_directory.png)
{style="warning"}

## Step 4: Check configuration
After you have selected the server directory, the manager will try to read the server configuration and fill in the fields in the configuration page. Make sure to check if all fields are filled in correctly, if not, you can fill in the missing fields manually.

> Missing fields for other options?
> Don't worry, the manage respects the imported ini values, so if you have custom ini values that are not supported by the manager, they will still be applied to the server and you can manage them by editing the ini files directly in the server directory.
> Watch out, make sure the manager is not running when you edit the ini files.
> More options will be added in the future.

## Step 5: (Re)Install the server
> While the server may already be installed, the manager expects items to be in the correct location, also the server manager installs specific items to make features in the server manager work.
{style="note"}

Now click the install/update button, the installer will start and install/update the server. This process may take a while, and you can follow the progress on the info page.

During the installation process it will look like this:
![installing_server](installing_server.png)
> Do not close the manager while the installer is running.
{style="warning"}


## Step 5: Start the server
After you have configured the server, you can start it by clicking on the start button in the top right corner. This will start the server and you can follow the progress on the info page.
> If you enabled Ark Api during the configuration, make sure to update the server and check if the api shows as installed.
