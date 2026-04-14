package eu.wynq.arkascendedservermanager.ui.features.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.aasm_sq_r
import arkascendedservermanager.ui.generated.resources.info_app_version_format
import arkascendedservermanager.ui.generated.resources.page_info
import eu.wynq.arkascendedservermanager.ui.helpers.AppChangelog
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.markdown.standalone.ProvideMarkdownStyling
import org.jetbrains.jewel.markdown.Markdown
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.typography

@OptIn(ExperimentalJewelApi::class)
@Composable
fun InfoScreen(component: InfoComponent) {
    val pageInfo = stringResource(Res.string.page_info)
    val appVersionLabel = stringResource(Res.string.info_app_version_format, AppBuildInfo.version)

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(pageInfo, style = JewelTheme.typography.h2TextStyle)
        Column(
            modifier = Modifier.fillMaxWidth().background(
                JewelTheme.globalColors.panelBackground,
                RoundedCornerShape(12.dp)
            ).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    painterResource(Res.drawable.aasm_sq_r),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Column {
                    Text(appVersionLabel, style = JewelTheme.typography.h4TextStyle)
                    ExternalLink(
                        "Github",
                        "https://github.com/JensvandeWiel/ArkAscendedServerManager/releases/tag/v${AppBuildInfo.version}"
                    )
                    ExternalLink(
                        "Docs",
                        "https://example.com/"
                    )
                }
            }
        }
        Text("Changelog", style = JewelTheme.typography.h2TextStyle)
        VerticallyScrollableContainer(
            modifier = Modifier.fillMaxSize().background(JewelTheme.globalColors.panelBackground, RoundedCornerShape(12.dp)).padding(12.dp)
        ) {
            ProvideMarkdownStyling {
                Markdown(
                    AppChangelog.forCurrentVersion(AppBuildInfo.version)
                )
            }
        }
    }
}