package eu.wynq.arkascendedservermanager.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.nunito
import io.github.kdroidfilter.nucleus.darkmodedetector.isSystemInDarkMode
import org.jetbrains.compose.resources.Font
import org.jetbrains.jewel.foundation.BorderColors
import org.jetbrains.jewel.foundation.DisabledAppearanceValues
import org.jetbrains.jewel.foundation.GlobalColors
import org.jetbrains.jewel.foundation.OutlineColors
import org.jetbrains.jewel.foundation.TextColors
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.foundation.theme.ThemeIconData
import org.jetbrains.jewel.intui.core.theme.IntUiDarkTheme
import org.jetbrains.jewel.intui.core.theme.IntUiLightTheme
import org.jetbrains.jewel.intui.standalone.theme.dark
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.light
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.ui.ComponentStyling
import eu.wynq.arkascendedservermanager.ui.utils.AccentColor
import eu.wynq.arkascendedservermanager.ui.utils.islandsComponentStyling

object ThemeUtils {
    enum class ThemeMode {
        System,
        Light,
        Dark,
    }

    /**
     * Provides the app's default text style (centralized so callers don't repeat it).
     */
    @Composable
    fun defaultTextStyle(): TextStyle =
        TextStyle(
            fontFamily =
                FontFamily(
                    Font(resource = Res.font.nunito),
                ),
        )

    @Composable
    fun isDarkTheme(): Boolean {
        if (LocalInspectionMode.current) return false
        return isSystemInDarkMode()
    }

    @Composable
    private fun resolveIsDark(mode: ThemeMode): Boolean =
        when (mode) {
            ThemeMode.System -> isDarkTheme()
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }

    /**
     * Jewel's primary accent color blended at 25% over the panel background,
     * used as the title bar gradient tint.
     */
    @Composable
    fun titleBarGradientColor(): Color {
        val accent = JewelTheme.globalColors.outlines.focused
        val bg = JewelTheme.globalColors.panelBackground
        val t = 0.25f
        return Color(
            red = bg.red * (1f - t) + accent.red * t,
            green = bg.green * (1f - t) + accent.green * t,
            blue = bg.blue * (1f - t) + accent.blue * t,
            alpha = 1f,
        )
    }

    /** Builds the Islands-based Jewel theme definition for the active light/dark mode. */
    @Composable
    fun buildThemeDefinition(mode: ThemeMode = ThemeMode.System) =
        run {
            val isDark = resolveIsDark(mode)
            val accent = AccentColor.Default.resolveColor(isDark)
            val disabledValues = if (isDark) DisabledAppearanceValues.dark() else DisabledAppearanceValues.light()
            val iconData = accentIconData(accent, isDark)

            if (isDark) {
                JewelTheme.darkThemeDefinition(
                    colors = islandsDarkGlobalColors(accent),
                    iconData = iconData,
                    disabledAppearanceValues = disabledValues,
                )
            } else {
                JewelTheme.lightThemeDefinition(
                    colors = lightIslandsGlobalColors(accent),
                    iconData = iconData,
                    disabledAppearanceValues = disabledValues,
                )
            }
        }

    /** Builds the Islands component styling for the current mode and accent color. */
    @Composable
    fun buildComponentStyling(mode: ThemeMode = ThemeMode.System): ComponentStyling {
        val isDark = resolveIsDark(mode)
        val accent = AccentColor.Default.resolveColor(isDark)
        return islandsComponentStyling(isDark, accent)
    }

    /** GlobalColors for the dark variant of the Islands palette. */
    private fun islandsDarkGlobalColors(accent: Color): GlobalColors =
        GlobalColors.dark(
            borders =
                BorderColors.dark(
                    normal = Color(0xFF3C3F41),
                    focused = accent,
                    disabled = Color(0xFF2B2D30),
                ),
            outlines =
                OutlineColors.dark(
                    focused = accent,
                    focusedWarning = Color(0xFFE8A33E),
                    focusedError = Color(0xFFF75464),
                    warning = Color(0xFFE8A33E),
                    error = Color(0xFFF75464),
                ),
            text =
                TextColors.dark(
                    normal = Color(0xFFBCBEC4),
                    selected = Color(0xFFBCBEC4),
                    disabled = Color(0xFF7A7E85),
                    info = Color(0xFF7A7E85),
                    error = Color(0xFFF75464),
                ),
            panelBackground = Color(0xFF1E1F22),
            toolwindowBackground = Color(0xFF181A1D),
        )

    /**
     * GlobalColors for the light Islands palette.
     * Canvas is slightly darker than panel to show rounded card edges.
     */
    private fun lightIslandsGlobalColors(accent: Color): GlobalColors =
        GlobalColors.light(
            outlines =
                OutlineColors.light(
                    focused = accent,
                    focusedWarning = Color(0xFFE8A33E),
                    focusedError = Color(0xFFF75464),
                    warning = Color(0xFFE8A33E),
                    error = Color(0xFFF75464),
                ),
            borders = BorderColors.light(focused = accent),
            toolwindowBackground = Color(0xFFE8E9EB),
        )

    /**
     * Builds a [ThemeIconData] that patches checkbox/radio SVG colors
     * to use the given accent color for their selected state.
     */
    private fun accentIconData(
        accent: Color,
        isDark: Boolean,
    ): ThemeIconData {
        val hex = accent.toHexString()
        val base = if (isDark) IntUiDarkTheme.iconData else IntUiLightTheme.iconData
        return ThemeIconData(
            iconOverrides = base.iconOverrides,
            colorPalette =
                base.colorPalette +
                    mapOf(
                        "Checkbox.Background.Selected" to hex,
                        "Checkbox.Border.Selected" to hex,
                        "Checkbox.Focus.Thin.Selected" to hex,
                    ),
            selectionColorPalette = base.selectionColorPalette,
        )
    }

    private fun Color.toHexString(): String {
        val r = (red * 255).toInt()
        val g = (green * 255).toInt()
        val b = (blue * 255).toInt()
        return "#%02X%02X%02X".format(r, g, b)
    }
}