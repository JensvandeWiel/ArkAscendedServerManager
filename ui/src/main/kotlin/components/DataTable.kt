package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

/**
 * A compact, project-styled table with fixed columns and a scrollable body.
 *
 * The cells are ordinary composables, so callers can use the same Jewel form
 * controls as the rest of the settings tabs without giving up the table layout.
 */
data class DataTableColumn<T>(
    val title: String,
    val weight: Float,
    val cell: @Composable (index: Int, row: T) -> Unit,
)

@Composable
fun <T> DataTable(
    columns: List<DataTableColumn<T>>,
    rows: List<T>,
    emptyText: String,
    modifier: Modifier = Modifier,
    maxBodyHeight: Int = 420,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .background(JewelTheme.globalColors.toolwindowBackground)
            .border(1.dp, JewelTheme.globalColors.borders.normal, RectangleShape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(JewelTheme.globalColors.panelBackground)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            columns.forEach { column ->
                Text(
                    column.title,
                    modifier = Modifier.weight(column.weight),
                    style = JewelTheme.typography.labelTextStyle,
                )
            }
        }
        Divider(Orientation.Horizontal, Modifier.fillMaxWidth())
        if (rows.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp)) {
                Text(emptyText, style = JewelTheme.typography.labelTextStyle)
            }
        } else {
            LazyColumn(Modifier.fillMaxWidth().heightIn(max = maxBodyHeight.dp)) {
                itemsIndexed(rows) { index, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (index % 2 == 0) {
                                    JewelTheme.globalColors.toolwindowBackground
                                } else {
                                    JewelTheme.globalColors.panelBackground
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        columns.forEach { column ->
                            Box(Modifier.weight(column.weight)) {
                                column.cell(index, row)
                            }
                        }
                    }
                    Divider(Orientation.Horizontal, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

