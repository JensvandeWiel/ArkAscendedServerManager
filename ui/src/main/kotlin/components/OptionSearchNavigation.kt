package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.jewel.foundation.theme.JewelTheme

class OptionSearchNavigation(
    val scrollState: ScrollState = ScrollState(0),
) {
    var targetLabel: String? by mutableStateOf(null)
        private set
    var targetTabName: String? by mutableStateOf(null)
        private set
    var targetToken: Int by mutableIntStateOf(0)
        private set
    var viewportTop: Int by mutableIntStateOf(0)

    fun select(tabName: String, label: String) {
        targetLabel = label
        targetTabName = tabName
        targetToken += 1
    }

    fun clear() {
        targetLabel = null
        targetTabName = null
    }
}

val LocalOptionSearchNavigation = compositionLocalOf<OptionSearchNavigation?> { null }

@Composable
fun optionScrollState(): ScrollState =
    LocalOptionSearchNavigation.current?.scrollState ?: remember { ScrollState(0) }

/**
 * Attaches a scroll target and highlight border to a form field when its label is
 * the currently selected search result.
 */
@Composable
fun Modifier.optionSearchAnchor(label: String?): Modifier {
    val navigation = LocalOptionSearchNavigation.current ?: return this
    if (label == null || navigation.targetLabel != label) return this

    var lastY by remember { mutableIntStateOf(0) }
    var handledToken by remember { mutableIntStateOf(-1) }

    LaunchedEffect(navigation.targetToken, lastY, navigation.viewportTop) {
        if (lastY > 0 && navigation.viewportTop > 0 && handledToken != navigation.targetToken) {
            handledToken = navigation.targetToken
            // Let the target tab finish layout before scrolling.
            delay(40)
            val targetOffset = (
                navigation.scrollState.value +
                    lastY -
                    navigation.viewportTop -
                    24
                ).coerceAtLeast(0)
            navigation.scrollState.animateScrollTo(targetOffset)
        }
    }

    return this
        .onGloballyPositioned { lastY = it.positionInRoot().y.toInt() }
        .border(
            width = 1.5.dp,
            color = JewelTheme.globalColors.outlines.focused,
            shape = RoundedCornerShape(4.dp),
        )
}
