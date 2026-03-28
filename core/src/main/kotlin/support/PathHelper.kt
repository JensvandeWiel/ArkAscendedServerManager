package eu.wynq.arkascendedservermanager.core.support

object PathHelper {
    const val ROOT_PATH = "/.aasm"
    const val LOG_PATH = "$ROOT_PATH/app.log"
    const val DB_PATH = "$ROOT_PATH/app.db"

    fun getAppPath() = System.getProperty("user.home") + ROOT_PATH

    fun getLogFilePath() = System.getProperty("user.home") + LOG_PATH
    fun getDbFilePath() = System.getProperty("user.home") + DB_PATH

}