package eu.wynq.arkascendedservermanager.ui.helpers

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_power_state_crashed
import arkascendedservermanager.ui.generated.resources.server_details_power_state_running
import arkascendedservermanager.ui.generated.resources.server_details_power_state_starting
import arkascendedservermanager.ui.generated.resources.server_details_power_state_stopped
import arkascendedservermanager.ui.generated.resources.server_details_power_state_stopping
import arkascendedservermanager.ui.generated.resources.server_details_power_state_unknown
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import org.jetbrains.compose.resources.stringResource
import java.awt.Desktop
import java.io.File

fun openFolderInExplorer(path: String): Result<Unit> {
    val file = File(path)
    if (file.exists() && Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(file)
        return Result.success(Unit)
    } else {
        return Result.failure(IllegalArgumentException("File does not exist or is not supported"))
    }
}

@Composable
fun getPowerStateLabel(powerState: PowerState): String {
    val powerStateRunning = stringResource(Res.string.server_details_power_state_running)
    val powerStateStarting = stringResource(Res.string.server_details_power_state_starting)
    val powerStateStopping = stringResource(Res.string.server_details_power_state_stopping)
    val powerStateStopped = stringResource(Res.string.server_details_power_state_stopped)
    val powerStateUnknown = stringResource(Res.string.server_details_power_state_unknown)
    val powerStateCrashed = stringResource(Res.string.server_details_power_state_crashed)

    return when (powerState) {
        PowerState.Running -> powerStateRunning
        PowerState.Starting -> powerStateStarting
        PowerState.Stopping -> powerStateStopping
        PowerState.Stopped -> powerStateStopped
        PowerState.Unknown -> powerStateUnknown
        PowerState.Crashed -> powerStateCrashed
    }
}