package eu.wynq.arkascendedservermanager.core.db

import eu.wynq.arkascendedservermanager.core.support.PathHelper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.flywaydb.core.api.FlywayException
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.jvm.Throws

object DatabaseHelper {
    private val _logger = KotlinLogging.logger {}
    const val DRIVER = "org.h2.Driver"
    const val USER = "sa"
    const val PASSWORD = ""

    private fun isAutoServerEnabled(): Boolean {
        return System.getenv("AASM_DB_AUTOSERVER")?.toBooleanStrictOrNull() ?: false
    }

    fun getDbConnectionString(): String {
        val base = "jdbc:h2:${PathHelper.getDbFilePath()};DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE"
        val autoServerEnabled = isAutoServerEnabled()
        if (autoServerEnabled) _logger.info { "Auto server enabled" }
        return if (autoServerEnabled) "$base;AUTO_SERVER=TRUE" else base
    }

    @Throws(FlywayException::class)
    fun migrate() {
        val flyway = org.flywaydb.core.Flyway.configure()
            .dataSource(getDbConnectionString(), USER, PASSWORD)
            .locations("classpath:db/migrations")
            .load()

        flyway.migrate()
    }

    fun connect(): Database {
        return Database.connect(getDbConnectionString(), DRIVER, USER, PASSWORD)
    }
}