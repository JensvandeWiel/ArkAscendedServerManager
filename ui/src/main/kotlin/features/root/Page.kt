package ui.features.root

import androidx.compose.runtime.Composable
import org.jetbrains.jewel.ui.icon.IconKey

enum class PageSection {
    TOP,
    BOTTOM
}

data class Page<T : RootComponent.Child>(
    val title: String,
    val config: RootComponent.Config,
    val iconKey: IconKey,
    val section: PageSection,
    val content: @Composable (child: T) -> Unit
) {
    @Composable
    fun render(child: RootComponent.Child) {
        @Suppress("UNCHECKED_CAST")
        content(child as T)
    }
}
