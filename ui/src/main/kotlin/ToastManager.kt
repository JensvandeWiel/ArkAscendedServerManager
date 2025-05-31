package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.konyaco.fluent.FluentTheme

/**
 * Manager for handling toast notifications application wide.
 */
class ToastManager {
    // Functions to show various types of toasts
    fun toast(message: String, type: ToastType = ToastType.Info) {
        toasterState?.show(message, type = type)
    }

    // Internal state management
    private var toasterState: ToasterState? = null

    internal fun setToasterState(state: ToasterState) {
        this.toasterState = state
    }

    companion object {
        // Singleton instance
        private val instance by lazy { ToastManager() }

        fun get(): ToastManager = instance
    }
}


@Composable
fun ToastHost() {
    val toasterState = rememberToasterState()
    val manager = remember { ToastManager.get() }

    manager.setToasterState(toasterState)

    Toaster(
        state = toasterState,
        richColors = true,
        alignment = Alignment.BottomEnd,
        darkTheme = FluentTheme.colors.darkMode
    )
}
