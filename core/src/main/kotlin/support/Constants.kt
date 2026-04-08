package eu.wynq.arkascendedservermanager.core.support

object Constants {
    const val SERVER_EXECUTABLE_NAME = "ArkAscendedServer.exe"
    const val SERVER_BINARY_PATH = "ShooterGame\\Binaries\\Win64"
    const val ARK_APP_ID = 2430930
    const val CONFIG_PATH = "ShooterGame\\Saved\\Config\\WindowsServer"
    const val GAME_USER_SETTINGS_FILE = "GameUserSettings.ini"
    const val GAME_USER_SETTINGS_PATH = "$CONFIG_PATH\\$GAME_USER_SETTINGS_FILE"
    const val SERVER_VERSION_FILE = "version.txt"
    const val ASA_API_VERSION_FILE = "api_version.txt"
    const val SERVER_VERSION_PATH = "$SERVER_BINARY_PATH\\$SERVER_VERSION_FILE"
    const val ASA_API_VERSION_PATH = "$SERVER_BINARY_PATH\\$ASA_API_VERSION_FILE"
    const val STARTUP_SCRIPT_FILE = "startup.bat"
    const val STARTUP_SCRIPT_PATH = "$SERVER_BINARY_PATH\\$STARTUP_SCRIPT_FILE"
    const val ASA_API_EXECUTABLE_NAME = "AsaApiLoader.exe"
}