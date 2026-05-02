package com.template.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
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
import com.template.data.settings.SeedColorKey
import com.template.data.settings.UseDynamicColorKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference

@Composable
fun TemplateTheme(
    typography: Typography? = null,
    darkTheme: Boolean? = null,
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit,
) {
    val resolvedDarkTheme = darkTheme ?: darkThemeFromSettings()
    val resolvedColorScheme = colorScheme ?: colorSchemeFromSettings(resolvedDarkTheme)
    val resolvedTypography = typography ?: typographyFromSettings()

    MaterialExpressiveTheme(
        colorScheme = resolvedColorScheme,
        typography = resolvedTypography,
        shapes = appMaterialShapes(),
        content = content,
    )
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
private fun colorSchemeFromSettings(isDark: Boolean): ColorScheme {
    val context = LocalContext.current
    val useDynamicColor by rememberPreference(UseDynamicColorKey, true)
    val colorPresetId by rememberPreference(ColorPresetIdKey, ColorPreset.DEFAULT.id)
    val seedColorInt by rememberPreference(SeedColorKey, ColorPreset.DEFAULT.color.toArgb())

    val useSystemDynamic = useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    return when {
        useSystemDynamic ->
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        else -> {
            val preset = ColorPreset.OPTIONS.firstOrNull { it.id == colorPresetId }
            val seedColor = preset?.color ?: Color(seedColorInt)
            rememberDynamicColorScheme(
                seedColor = seedColor,
                isDark = isDark,
                isAmoled = false,
                style = PaletteStyle.TonalSpot,
            )
        }
    }
}

@Composable
private fun typographyFromSettings(): Typography {
    val baseSize by rememberEnumPreference(BaseSizeKey)
    val displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    val bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)
    val displayWidth by rememberPreference(DisplayFontWidthKey, DefaultDisplayFontWidth)
    val displayGrade by rememberPreference(DisplayFontGradeKey, DefaultDisplayFontGrade)
    val displayRond by rememberPreference(DisplayFontRondKey, DefaultDisplayFontRond)
    val bodyWidth by rememberPreference(BodyFontWidthKey, DefaultBodyFontWidth)
    val bodyGrade by rememberPreference(BodyFontGradeKey, DefaultBodyFontGrade)
    val bodyRond by rememberPreference(BodyFontRondKey, DefaultBodyFontRond)

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
    val extraSmall = 2.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 36.dp
}
