package com.template.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

@Immutable
data class AppColors(
    val background: Color,
    val surface: Color,
    val ink: Color,
    val inkMuted: Color,
    val border: Color,
    val accent: Color,
    val danger: Color,
    val inkFixed: Color = Color(0xFF13141A),
    val surfaceFixed: Color = Color(0xFFFFFFFF),
    val isDark: Boolean = false,
) {
    val hairline: Color = ink.copy(alpha = if (isDark) 0.14f else 0.10f)
    val ink04: Color = ink.copy(alpha = if (isDark) 0.05f else 0.04f)
    val ink08: Color = ink.copy(alpha = if (isDark) 0.09f else 0.08f)
    val ink14: Color = ink.copy(alpha = if (isDark) 0.16f else 0.14f)
    val ink56: Color = ink.copy(alpha = if (isDark) 0.60f else 0.56f)
    val inkSoft: Color = ink.copy(alpha = 0.64f)
    val accent12: Color = accent.copy(alpha = 0.12f)
    val accent20: Color = accent.copy(alpha = 0.20f)
    val danger12: Color = danger.copy(alpha = 0.12f)
    val danger14: Color = danger.copy(alpha = 0.14f)

    fun toMaterialColorScheme(): ColorScheme {
        val base = if (isDark) darkColorScheme() else lightColorScheme()
        return base.copy(
            primary = ink,
            onPrimary = background,
            primaryContainer = surface,
            onPrimaryContainer = ink,
            inversePrimary = background,
            secondary = accent,
            onSecondary = Color.White,
            secondaryContainer = accent12,
            onSecondaryContainer = ink,
            tertiary = accent,
            onTertiary = Color.White,
            tertiaryContainer = accent12,
            onTertiaryContainer = ink,
            background = background,
            onBackground = ink,
            surface = surface,
            onSurface = ink,
            surfaceVariant = surface,
            onSurfaceVariant = inkMuted,
            surfaceTint = accent,
            inverseSurface = ink,
            inverseOnSurface = background,
            error = danger,
            onError = Color.White,
            errorContainer = danger12,
            onErrorContainer = danger,
            outline = border,
            outlineVariant = hairline,
            scrim = ink56,
            surfaceBright = surface,
            surfaceDim = background,
            surfaceContainer = background,
            surfaceContainerHigh = surface,
            surfaceContainerHighest = surface,
            surfaceContainerLow = background,
            surfaceContainerLowest = background,
        )
    }

    companion object {
        val Light = AppColors(
            background = Color(0xFFF4F2EE),
            surface = Color(0xFFFFFFFF),
            ink = Color(0xFF13141A),
            inkMuted = Color(0xFF7C7F87),
            border = Color(0xFFDEDCD9),
            accent = Color(0xFF3E5C76),
            danger = Color(0xFFA13D2E),
            inkFixed = Color(0xFF13141A),
            surfaceFixed = Color(0xFFFFFFFF),
            isDark = false,
        )

        val Dark = deriveDarkColors(Light)

        fun deriveDarkColors(light: AppColors): AppColors {
            val bg = Color(0xFF13141A)
            val fg = Color(0xFFF4F2EE)
            val surface = mixOklab(bg, fg, 0.16f) // mix(bg 84%, fg 16%) ≈ #221F27
            val border = mixOklab(bg, fg, 0.22f) // mix(bg 78%, fg 22%) ≈ #2C2933
            val inkMuted = mixOklab(bg, fg, 0.56f) // mix(fg 56%, bg 44%) ≈ #948E92
            val accent = mixOklab(Color(0xFF3E5C76), fg, 0.40f) // mix(#3E5C76 60%, fg 40%) ≈ #63687C
            val danger = mixOklab(Color(0xFFA13D2E), fg, 0.38f) // mix(#A13D2E 62%, fg 38%) ≈ #9C6259

            return AppColors(
                background = bg,
                surface = surface,
                ink = fg,
                inkMuted = inkMuted,
                border = border,
                accent = accent,
                danger = danger,
                inkFixed = Color(0xFF13141A),
                surfaceFixed = Color(0xFFFFFFFF),
                isDark = true,
            )
        }

        fun mixOklab(c1: Color, c2: Color, ratio: Float): Color {
            val oklab1 = c1.convert(ColorSpaces.Oklab)
            val oklab2 = c2.convert(ColorSpaces.Oklab)
            val l = oklab1.red * (1f - ratio) + oklab2.red * ratio
            val a = oklab1.green * (1f - ratio) + oklab2.green * ratio
            val b = oklab1.blue * (1f - ratio) + oklab2.blue * ratio
            val alpha = oklab1.alpha * (1f - ratio) + oklab2.alpha * ratio
            return Color(
                red = l,
                green = a,
                blue = b,
                alpha = alpha,
                colorSpace = ColorSpaces.Oklab,
            ).convert(ColorSpaces.Srgb)
        }
    }
}

val LocalAppColors = staticCompositionLocalOf { AppColors.Light }
