package eu.wynq.arkascendedservermanager.ui.features.info

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import eu.wynq.arkascendedservermanager.ui.notifications.ToastIconAction
import eu.wynq.arkascendedservermanager.ui.notifications.ToastLinkAction
import org.jetbrains.jewel.ui.icons.AllIconsKeys

class InfoComponent(componentContext: ComponentContext) : ComponentContext by componentContext {

	fun showSampleInfoToast() {
		ToastBannerManager.show(
			type = ToastBannerType.INFO,
			title = "Heads up",
			text = "This is an info toast with actions and a 5 second timeout.",
			timeoutMillis = 5_000L,
			linkActions = listOf(
				ToastLinkAction(label = "Action A", onClick = {}),
				ToastLinkAction(label = "Action B", dismissOnClick = false, onClick = {}),
			),
			iconActions = listOf(
				ToastIconAction(
					iconKey = AllIconsKeys.General.Gear,
					contentDescription = "Settings",
					onClick = {},
				)
			),
		)
	}

	fun showPersistentErrorToast() {
		ToastBannerManager.show(
			type = ToastBannerType.ERROR,
			title = "Persistent error",
			text = "This one does not auto-dismiss. Use the close button.",
			timeoutMillis = null,
		)
	}

	fun clearToasts() {
		ToastBannerManager.dismissAll()
	}

}