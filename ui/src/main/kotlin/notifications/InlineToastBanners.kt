package eu.wynq.arkascendedservermanager.ui.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.toast_action_close_content_description
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InlineErrorBanner
import org.jetbrains.jewel.ui.component.InlineInformationBanner
import org.jetbrains.jewel.ui.component.InlineSuccessBanner
import org.jetbrains.jewel.ui.component.InlineWarningBanner
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.theme.inlineBannerStyle

enum class ToastBannerType {
    INFO,
    SUCCESS,
    WARNING,
    ERROR,
}

data class ToastLinkAction(
    val label: String,
    val tooltip: String? = null,
    val dismissOnClick: Boolean = true,
    val onClick: () -> Unit = {},
)

data class ToastIconAction(
    val iconKey: IconKey,
    val contentDescription: String,
    val tooltip: String? = null,
    val dismissOnClick: Boolean = false,
    val onClick: () -> Unit = {},
)

data class ToastBanner(
    val id: Long,
    val type: ToastBannerType,
    val text: String,
    val title: String? = null,
    val showIcon: Boolean = true,
    val iconKey: IconKey? = null,
    val linkActions: List<ToastLinkAction> = emptyList(),
    val iconActions: List<ToastIconAction> = emptyList(),
    val timeoutMillis: Long? = 4_000L,
)

object ToastBannerManager {
    private var nextId = 0L
    private val _toasts = MutableStateFlow<List<ToastBanner>>(emptyList())
    val toasts: StateFlow<List<ToastBanner>> = _toasts.asStateFlow()

    fun show(toast: ToastBanner): Long {
        val id = if (toast.id == 0L) generateId() else toast.id
        _toasts.value = _toasts.value + toast.copy(id = id)
        return id
    }

    fun show(
        type: ToastBannerType,
        text: String,
        title: String? = null,
        showIcon: Boolean = true,
        iconKey: IconKey? = null,
        linkActions: List<ToastLinkAction> = emptyList(),
        iconActions: List<ToastIconAction> = emptyList(),
        timeoutMillis: Long? = 3_000L,
    ): Long = show(
        ToastBanner(
            id = 0L,
            type = type,
            text = text,
            title = title,
            showIcon = showIcon,
            iconKey = iconKey,
            linkActions = linkActions,
            iconActions = iconActions,
            timeoutMillis = timeoutMillis,
        )
    )

    fun dismiss(id: Long) {
        _toasts.value = _toasts.value.filterNot { it.id == id }
    }

    fun dismissAll() {
        _toasts.value = emptyList()
    }

    private fun generateId(): Long {
        nextId += 1L
        return nextId
    }
}

@Composable
fun InlineToastBannerHost(
    modifier: Modifier = Modifier,
    manager: ToastBannerManager = ToastBannerManager,
    width: Dp = 360.dp,
) {
    val toasts by manager.toasts.collectAsState()

    if (toasts.isEmpty()) return

    Column(
        modifier = modifier.width(width),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        toasts.forEach { toast ->
            LaunchedEffect(toast.id, toast.timeoutMillis) {
                val timeout = toast.timeoutMillis
                if (timeout != null && timeout > 0L) {
                    delay(timeout)
                    manager.dismiss(toast.id)
                }
            }

            ToastInlineBanner(
                toast = toast,
                onDismiss = { manager.dismiss(toast.id) },
            )
        }
    }
}

@Composable
private fun ToastInlineBanner(
    toast: ToastBanner,
    onDismiss: () -> Unit,
) {
    val closeDescription = stringResource(Res.string.toast_action_close_content_description)
    val defaultIconActions = listOf(
        ToastIconAction(
            iconKey = AllIconsKeys.General.Close,
            contentDescription = closeDescription,
            dismissOnClick = true,
        )
    )
    val iconActions = toast.iconActions + defaultIconActions

    when (toast.type) {
        ToastBannerType.INFO -> when {
            !toast.showIcon -> InlineInformationBanner(
                style = JewelTheme.inlineBannerStyle.information,
                title = toast.title,
                text = toast.text,
                icon = null,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            toast.iconKey != null -> InlineInformationBanner(
                style = JewelTheme.inlineBannerStyle.information,
                title = toast.title,
                text = toast.text,
                icon = { Icon(toast.iconKey, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) },
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            else -> InlineInformationBanner(
                style = JewelTheme.inlineBannerStyle.information,
                title = toast.title,
                text = toast.text,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )
        }

        ToastBannerType.SUCCESS -> when {
            !toast.showIcon -> InlineSuccessBanner(
                style = JewelTheme.inlineBannerStyle.success,
                title = toast.title,
                text = toast.text,
                icon = null,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            toast.iconKey != null -> InlineSuccessBanner(
                style = JewelTheme.inlineBannerStyle.success,
                title = toast.title,
                text = toast.text,
                icon = { Icon(toast.iconKey, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) },
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            else -> InlineSuccessBanner(
                style = JewelTheme.inlineBannerStyle.success,
                title = toast.title,
                text = toast.text,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )
        }

        ToastBannerType.WARNING -> when {
            !toast.showIcon -> InlineWarningBanner(
                style = JewelTheme.inlineBannerStyle.warning,
                title = toast.title,
                text = toast.text,
                icon = null,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            toast.iconKey != null -> InlineWarningBanner(
                style = JewelTheme.inlineBannerStyle.warning,
                title = toast.title,
                text = toast.text,
                icon = { Icon(toast.iconKey, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) },
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            else -> InlineWarningBanner(
                style = JewelTheme.inlineBannerStyle.warning,
                title = toast.title,
                text = toast.text,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )
        }

        ToastBannerType.ERROR -> when {
            !toast.showIcon -> InlineErrorBanner(
                style = JewelTheme.inlineBannerStyle.error,
                title = toast.title,
                text = toast.text,
                icon = null,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            toast.iconKey != null -> InlineErrorBanner(
                style = JewelTheme.inlineBannerStyle.error,
                title = toast.title,
                text = toast.text,
                icon = { Icon(toast.iconKey, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) },
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )

            else -> InlineErrorBanner(
                style = JewelTheme.inlineBannerStyle.error,
                title = toast.title,
                text = toast.text,
                linkActions = {
                    toast.linkActions.forEach { action ->
                        action(action.label) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
                iconActions = {
                    iconActions.forEach { action ->
                        iconAction(action.iconKey, action.contentDescription, action.tooltip) {
                            action.onClick()
                            if (action.dismissOnClick) onDismiss()
                        }
                    }
                },
            )
        }
    }
}
