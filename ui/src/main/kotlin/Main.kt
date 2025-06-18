package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.composefluent.ExperimentalFluentApi
import org.slf4j.LoggerFactory
import root.RootContent
import ui.root.RootComponent

fun configureLogging(logFilePath: String) {
    val context = LoggerFactory.getILoggerFactory() as LoggerContext

    // Console appender
    val consoleEncoder = PatternLayoutEncoder().apply {
        setContext(context)
        pattern = "%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
        start()
    }
    val consoleAppender = ConsoleAppender<ILoggingEvent>().apply {
        setContext(context)
        encoder = consoleEncoder
        start()
    }

    // File appender
    val fileEncoder = PatternLayoutEncoder().apply {
        setContext(context)
        pattern = "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"
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

@OptIn(ExperimentalDecomposeApi::class, ExperimentalFluentApi::class)
fun main() {
    configureLogging(System.getProperty("user.home") + "/.aasm/app.log")
    val lifecycle = LifecycleRegistry()
    val rootComponent = RootComponent(DefaultComponentContext(lifecycle))

    application {
        val windowState = rememberWindowState()

        // Bind the lifecycle of the root component to the window
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            title = "Ark Ascended Server Manager",
            state = windowState
        ) {
            RootContent(rootComponent)
        }
    }
}
