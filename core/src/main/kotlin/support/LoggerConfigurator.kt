package eu.wynq.arkascendedservermanager.core.support

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import org.slf4j.LoggerFactory

object LoggerConfigurator {
    const val CONSOLE_LOG_FORMAT = """{"timestamp":"%d{HH:mm:ss}","thread":"%thread","level":"%level","logger":"%logger{36}","message":"%msg"}%n"""
    const val FILE_LOG_FORMAT = """{"timestamp":"%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX}","thread":"%thread","level":"%level","logger":"%logger{36}","message":"%msg"}%n"""

    fun configureLogging(logFilePath: String) {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext

        val consoleEncoder = PatternLayoutEncoder().apply {
            setContext(context)
                pattern = CONSOLE_LOG_FORMAT
            start()
        }
        val consoleAppender = ConsoleAppender<ILoggingEvent>().apply {
            setContext(context)
            encoder = consoleEncoder
            start()
        }

        val fileEncoder = PatternLayoutEncoder().apply {
            setContext(context)
            pattern = FILE_LOG_FORMAT
            start()
        }
        val fileAppender = FileAppender<ILoggingEvent>().apply {
            setContext(context)
            encoder = fileEncoder
            file = logFilePath
            start()
        }

        val rootLogger = context.getLogger("ROOT")
        rootLogger.detachAndStopAllAppenders()
        rootLogger.addAppender(consoleAppender)
        rootLogger.addAppender(fileAppender)
    }
}