package com.template.ui.theme

import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.template.R

enum class AppTypePairing(
    @FontRes val displayFontFamilyRes: Int,
    @FontRes val bodyFontFamilyRes: Int,
    @StringRes val nameRes: Int,
) {
    Editorial(
        displayFontFamilyRes = R.font.roboto_serif,
        bodyFontFamilyRes = R.font.hanken_grotesk,
        nameRes = R.string.type_pairing_editorial,
    ),
    Literary(
        displayFontFamilyRes = R.font.cormorant_garamond,
        bodyFontFamilyRes = R.font.hanken_grotesk,
        nameRes = R.string.type_pairing_literary,
    ),
    Modern(
        displayFontFamilyRes = R.font.space_grotesk,
        bodyFontFamilyRes = R.font.ibm_plex_sans,
        nameRes = R.string.type_pairing_modern,
    ),
}

@Immutable
data class AppTypography(
    val displayClock: TextStyle,
    val titleLg: TextStyle,
    val titleMd: TextStyle,
    val titleSm: TextStyle,
    val bodyLg: TextStyle,
    val bodyMd: TextStyle,
    val bodySm: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle,
    val numeric: TextStyle,
) {
    fun toMaterialTypography(): Typography {
        val base = Typography()
        return base.copy(
            displayLarge = displayClock,
            displayMedium = displayClock.copy(fontSize = 48.sp),
            displaySmall = displayClock.copy(fontSize = 36.sp),
            headlineLarge = titleLg.copy(fontSize = 28.sp),
            headlineMedium = titleLg.copy(fontSize = 24.sp),
            headlineSmall = titleLg,
            titleLarge = titleLg,
            titleMedium = titleMd,
            titleSmall = titleSm,
            bodyLarge = bodyLg,
            bodyMedium = bodyMd,
            bodySmall = bodySm,
            labelLarge = bodyMd.copy(fontWeight = FontWeight.SemiBold),
            labelMedium = bodySm.copy(fontWeight = FontWeight.SemiBold),
            labelSmall = caption,
        )
    }

    companion object {
        fun create(pairing: AppTypePairing = AppTypePairing.Editorial): AppTypography {
            val displayFamily = FontFamily(Font(pairing.displayFontFamilyRes))
            val bodyFamily = FontFamily(Font(pairing.bodyFontFamilyRes))

            return AppTypography(
                displayClock = TextStyle(
                    fontFamily = displayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 64.sp,
                    lineHeight = 64.sp,
                    letterSpacing = (-0.03).em,
                    fontFeatureSettings = "tnum",
                ),
                titleLg = TextStyle(
                    fontFamily = displayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 21.sp,
                    lineHeight = 25.2.sp,
                    letterSpacing = 0.em,
                ),
                titleMd = TextStyle(
                    fontFamily = displayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.sp,
                    lineHeight = 23.75.sp,
                    letterSpacing = 0.em,
                ),
                titleSm = TextStyle(
                    fontFamily = displayFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 20.7.sp,
                    letterSpacing = (-0.01).em,
                ),
                bodyLg = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    letterSpacing = (-0.005).em,
                ),
                bodyMd = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
                    letterSpacing = 0.em,
                ),
                bodySm = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.5.sp,
                    lineHeight = 18.9.sp,
                    letterSpacing = 0.em,
                ),
                caption = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.5.sp,
                    lineHeight = 18.1.sp,
                    letterSpacing = 0.em,
                ),
                overline = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    letterSpacing = 0.10.em,
                ),
                numeric = TextStyle(
                    fontFamily = bodyFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    fontFeatureSettings = "tnum",
                ),
            )
        }
    }
}

val LocalAppTypePairing = staticCompositionLocalOf { AppTypePairing.Editorial }
val LocalAppTypography = staticCompositionLocalOf { AppTypography.create(AppTypePairing.Editorial) }
