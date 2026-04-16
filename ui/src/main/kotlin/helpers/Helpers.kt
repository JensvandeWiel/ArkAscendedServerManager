package eu.wynq.arkascendedservermanager.ui.helpers

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