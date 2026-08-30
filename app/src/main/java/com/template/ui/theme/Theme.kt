package com.template.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.DensityKey
import com.template.data.settings.DynamicColorEnabledKey
import com.template.data.settings.TypePairingKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference

val LocalColorRoles = compositionLocalOf { ColorPreset.DEFAULT.roles(isDark = false) }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pairing: AppTypePairing = AppTypePairing.Editorial,
    density: AppDensity = AppDensity.Comfortable,
    colors: AppColors = if (darkTheme) AppColors.Dark else AppColors.Light,
    typography: AppTypography = AppTypography.create(pairing),
    shapes: AppShapes = AppShapes(),
    materialColorScheme: ColorScheme? = null,
    materialTypography: Typography? = null,
    content: @Composable () -> Unit,
) {
    val resolvedColorScheme = materialColorScheme ?: colors.toMaterialColorScheme()
    val resolvedTypography = materialTypography ?: typography.toMaterialTypography()
    val legacyColorRoles = colors.toColorRoles()

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes,
        LocalAppDensity provides AppDensityTokens(density),
        LocalAppTypePairing provides pairing,
        LocalColorRoles provides legacyColorRoles,
    ) {
        MaterialExpressiveTheme(
            colorScheme = resolvedColorScheme,
            typography = resolvedTypography,
            shapes = appMaterialShapes(),
            content = content,
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current

    val density: AppDensityTokens
        @Composable
        get() = LocalAppDensity.current

    val pairing: AppTypePairing
        @Composable
        get() = LocalAppTypePairing.current
}

@Composable
fun TemplateTheme(
    typography: Typography? = null,
    darkTheme: Boolean? = null,
    colorScheme: ColorScheme? = null,
    colorRoles: ColorRoles? = null,
    colors: AppColors? = null,
    pairing: AppTypePairing? = null,
    density: AppDensity? = null,
    content: @Composable () -> Unit,
) {
    val resolvedDarkTheme = darkTheme ?: darkThemeFromSettings()
    val resolvedColors = colors ?: if (resolvedDarkTheme) AppColors.Dark else AppColors.Light
    val settingsPairing by rememberEnumPreference(TypePairingKey)
    val settingsDensity by rememberEnumPreference(DensityKey)
    val resolvedPairing = pairing ?: settingsPairing
    val resolvedDensity = density ?: settingsDensity
    val dynamicColor by rememberPreference(DynamicColorEnabledKey, false)
    val context = LocalContext.current

    val resolvedColorScheme = colorScheme ?: if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (resolvedDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        null
    }

    AppTheme(
        darkTheme = resolvedDarkTheme,
        pairing = resolvedPairing,
        density = resolvedDensity,
        colors = resolvedColors,
        materialColorScheme = resolvedColorScheme,
        materialTypography = typography,
        content = content,
    )
}

@Composable
fun MuralTheme(
    typography: Typography? = null,
    darkTheme: Boolean? = null,
    colorScheme: ColorScheme? = null,
    colorRoles: ColorRoles? = null,
    colors: AppColors? = null,
    pairing: AppTypePairing? = null,
    density: AppDensity? = null,
    content: @Composable () -> Unit,
) = TemplateTheme(
    typography = typography,
    darkTheme = darkTheme,
    colorScheme = colorScheme,
    colorRoles = colorRoles,
    colors = colors,
    pairing = pairing,
    density = density,
    content = content,
)

fun AppColors.toColorRoles(): ColorRoles = ColorRoles(
    bg = background,
    surface = surface,
    surfaceAlt = surface,
    ink = ink,
    inkSoft = inkSoft,
    inkMuted = inkMuted,
    hairline = hairline,
    accent = accent,
    accentSoft = accent.copy(alpha = 0.6f),
    onAccent = if (isDark) background else Color.White,
    good = Color(0xFF5A7A52),
    warn = danger,
)

@Composable
fun darkThemeFromSettings(): Boolean {
    val darkMode by rememberEnumPreference(DarkModeKey)
    return when (darkMode) {
        DarkMode.SYSTEM -> isSystemInDarkTheme()
        DarkMode.LIGHT -> false
        DarkMode.DARK -> true
    }
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
