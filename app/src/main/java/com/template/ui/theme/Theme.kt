package com.template.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.data.settings.BaseSizeKey
import com.template.data.settings.BodyFontFamilyKey
import com.template.data.settings.BodyFontGradeKey
import com.template.data.settings.BodyFontRondKey
import com.template.data.settings.BodyFontWidthKey
import com.template.data.settings.ColorPresetIdKey
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.DisplayFontFamilyKey
import com.template.data.settings.DisplayFontGradeKey
import com.template.data.settings.DisplayFontRondKey
import com.template.data.settings.DisplayFontWidthKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference

val LocalColorRoles = compositionLocalOf { ColorPreset.DEFAULT.roles(isDark = false) }

@Composable
fun TemplateTheme(
    typography: Typography? = null,
    darkTheme: Boolean? = null,
    colorScheme: ColorScheme? = null,
    colorRoles: ColorRoles? = null,
    content: @Composable () -> Unit,
) {
    val resolvedDarkTheme = darkTheme ?: darkThemeFromSettings()
    val resolvedColorRoles = colorRoles ?: colorRolesFromSettings(resolvedDarkTheme)
    val resolvedColorScheme =
        colorScheme ?: resolvedColorRoles.toMaterialColorScheme(resolvedDarkTheme)
    val resolvedTypography = typography ?: typographyFromSettings()

    CompositionLocalProvider(LocalColorRoles provides resolvedColorRoles) {
        MaterialExpressiveTheme(
            colorScheme = resolvedColorScheme,
            typography = resolvedTypography,
            shapes = appMaterialShapes(),
            content = content,
        )
    }
}

@Composable
fun darkThemeFromSettings(): Boolean {
    val darkMode by rememberEnumPreference(DarkModeKey)
    return when (darkMode) {
        DarkMode.SYSTEM -> isSystemInDarkTheme()
        DarkMode.LIGHT -> false
        DarkMode.DARK -> true
    }
}

@Composable
private fun colorRolesFromSettings(isDark: Boolean): ColorRoles {
    val colorPresetId by rememberPreference(ColorPresetIdKey, ColorPreset.DEFAULT.id)
    val preset = ColorPreset.OPTIONS.firstOrNull { it.id == colorPresetId } ?: ColorPreset.DEFAULT
    return preset.roles(isDark)
}

fun ColorRoles.toMaterialColorScheme(isDark: Boolean): ColorScheme {
    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = accent,
        onPrimary = onAccent,
        primaryContainer = accentSoft,
        onPrimaryContainer = ink,
        inversePrimary = accent,
        secondary = accentSoft,
        onSecondary = onAccent,
        secondaryContainer = surfaceAlt,
        onSecondaryContainer = ink,
        tertiary = good,
        onTertiary = onAccent,
        tertiaryContainer = surfaceAlt,
        onTertiaryContainer = ink,
        background = bg,
        onBackground = ink,
        surface = surface,
        onSurface = ink,
        surfaceVariant = surfaceAlt,
        onSurfaceVariant = inkMuted,
        surfaceTint = accent,
        inverseSurface = ink,
        inverseOnSurface = bg,
        error = warn,
        onError = onAccent,
        errorContainer = warn.copy(alpha = 0.22f),
        onErrorContainer = ink,
        outline = hairline,
        outlineVariant = hairline,
        scrim = Color.Black,
        surfaceBright = surfaceAlt,
        surfaceDim = bg,
        surfaceContainer = bg,
        surfaceContainerHigh = surface,
        surfaceContainerHighest = surfaceAlt,
        surfaceContainerLow = bg,
        surfaceContainerLowest = bg,
    )
}

@Composable
private fun typographyFromSettings(): Typography {
    val baseSize by rememberEnumPreference(BaseSizeKey)
    val displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    val bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)
    val displayWidth by rememberPreference(
        DisplayFontWidthKey,
        FontAxisConfig.DEFAULT_DISPLAY_WIDTH,
    )
    val displayGrade by rememberPreference(
        DisplayFontGradeKey,
        FontAxisConfig.DEFAULT_DISPLAY_GRADE,
    )
    val displayRond by rememberPreference(
        DisplayFontRondKey,
        FontAxisConfig.DEFAULT_DISPLAY_ROND,
    )
    val bodyWidth by rememberPreference(BodyFontWidthKey, FontAxisConfig.DEFAULT_BODY_WIDTH)
    val bodyGrade by rememberPreference(BodyFontGradeKey, FontAxisConfig.DEFAULT_BODY_GRADE)
    val bodyRond by rememberPreference(BodyFontRondKey, FontAxisConfig.DEFAULT_BODY_ROND)

    return rememberAppTypography(
        baseSize = baseSize,
        displayFontFamily = displayFontFamily,
        bodyFontFamily = bodyFontFamily,
        displayWidth = displayWidth,
        displayGrade = displayGrade,
        displayRond = displayRond,
        bodyWidth = bodyWidth,
        bodyGrade = bodyGrade,
        bodyRond = bodyRond,
    )
}

object Padding {
    val hairline = 1.dp
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp

    val extraSmall = xxs
    val small = sm
    val medium = md
    val large = lg
    val extraLarge = xl
}
