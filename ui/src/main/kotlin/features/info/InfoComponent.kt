package eu.wynq.arkascendedservermanager.ui.features.info

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import eu.wynq.arkascendedservermanager.ui.notifications.ToastIconAction
import eu.wynq.arkascendedservermanager.ui.notifications.ToastLinkAction
import org.jetbrains.jewel.ui.icons.AllIconsKeys

class InfoComponent(componentContext: ComponentContext) : ComponentContext by componentContext {

	fun showSampleInfoToast(
		title: String,
		text: String,
		actionALabel: String,
		actionBLabel: String,
		settingsContentDescription: String,
	) {
		ToastBannerManager.show(
			type = ToastBannerType.INFO,
			title = title,
			text = text,
			timeoutMillis = 5_000L,
			linkActions = listOf(
				ToastLinkAction(label = actionALabel, onClick = {}),
				ToastLinkAction(label = actionBLabel, dismissOnClick = false, onClick = {}),
			),
			iconActions = listOf(
				ToastIconAction(
					iconKey = AllIconsKeys.General.Gear,
					contentDescription = settingsContentDescription,
					onClick = {},
				)
			),
		)
	}

	fun showPersistentErrorToast(title: String, text: String) {
		ToastBannerManager.show(
			type = ToastBannerType.ERROR,
			title = title,
			text = text,
			timeoutMillis = null,
		)
	}

	fun clearToasts() {
		ToastBannerManager.dismissAll()
	}

}