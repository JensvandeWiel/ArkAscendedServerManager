@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wynq.arkascendedservermanager.ui.features.server.general.ClusterSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.ClusterTributeSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.MapAndModsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.MotdSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.NameAndPasswordsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.PortsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.SavesSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.ServerOptionsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.TributeDownloadSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.TributeUploadSection
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun GeneralTabContent(component: ServerComponent) {
    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding(), bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            NameAndPasswordsSection(component)
            PortsSection(component)
            MapAndModsSection(component)
            SavesSection(component)
            MotdSection(component)
            ClusterSection(component)
            TributeDownloadSection(component)
            TributeUploadSection(component)
            ClusterTributeSection(component)
            ServerOptionsSection(component)
        }
    }
}