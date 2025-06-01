package server

class ServerManager(private val profile: ServerProfile) {
    fun isInstalled(): Boolean {
        // Check if the server is installed by verifying the installation location
        return profile.installationLocation.isNotEmpty() && java.io.File(profile.installationLocation).exists()
    }
}