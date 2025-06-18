package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.Icon
import io.github.composefluent.icons.Icons
import io.github.composefluent.icons.regular.ChevronDown
import io.github.composefluent.surface.Card
import io.github.composefluent.surface.CardColor
import io.github.composefluent.surface.CardDefaults

/**
 * A collapsible card component using Fluent UI styling.
 *
 * @param title The title displayed in the header of the card
 * @param initiallyExpanded Whether the card should start in an expanded state
 * @param modifier Modifier for the entire card
 * @param content The content to be displayed when the card is expanded
 */
@Composable
fun CollapsibleCard(
    title: @Composable () -> Unit,
    initiallyExpanded: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = "rotation"
    )
    val default = CardColor(
        fillColor = if (FluentTheme.colors.darkMode) Color(0xff303030) else Color(0xffededed),
        contentColor = FluentTheme.colors.text.text.primary,
        borderBrush = SolidColor(FluentTheme.colors.stroke.card.default)
    )


    val cardColors = CardDefaults.cardColors(
        default,
        default.copy(),
        default.copy(),
        default.copy(),
    )

    Card(
        onClick = { expanded = !expanded },
        modifier = modifier,
        disabled = false,
        cardColors = cardColors,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart
                ) {
                    title()
                }

                Icon(
                    imageVector = Icons.Default.ChevronDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotationState)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    content()
                }
            }
        }
    }
}
