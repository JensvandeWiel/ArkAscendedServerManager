package settings

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    /**
     * Path to where SteamCMD is installed or where to install it.
     * */
    val steamCmdPath: String = "C:\\steamcmd",
    /**
     * Path to where the application data is stored.
     * This data contains the server configuration files, logs, and other data related to the application.
     * The global application configuration files are stored separately in the user's home directory.
     * */
    val applicationDataPath: String = "C:\\aasm",
)
