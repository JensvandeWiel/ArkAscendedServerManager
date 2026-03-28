package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme

@Composable
fun Card(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .background(JewelTheme.globalColors.toolwindowBackground)
            .padding(vertical = 4.dp, horizontal = 4.dp),
    ) {
        content()
    }
}