package com.template.ui.theme

import androidx.annotation.FontRes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.pow

object ThemeEngine {
    private const val TYPOGRAPHY_RATIO = 1.200f

    fun calculateFontSizes(baseSize: BaseSize): FontSizes {
        val base = baseSize.bodySizeSp.toFloat()
        val ratio = TYPOGRAPHY_RATIO
        return FontSizes(
            labelSmall = (base / ratio.pow(2)).sp,
            labelMedium = (base / ratio.pow(1.5f)).sp,
            labelLarge = (base / ratio).sp,
            bodySmall = (base / ratio.pow(0.5f)).sp,
            bodyMedium = base.sp,
            bodyLarge = (base * ratio.pow(0.5f)).sp,
            titleSmall = (base * ratio).sp,
            titleMedium = (base * ratio.pow(1.5f)).sp,
            titleLarge = (base * ratio.pow(2)).sp,
            headlineSmall = (base * ratio.pow(2.5f)).sp,
            headlineMedium = (base * ratio.pow(3)).sp,
            headlineLarge = (base * ratio.pow(3.5f)).sp,
            displaySmall = (base * ratio.pow(4)).sp,
            displayMedium = (base * ratio.pow(4.5f)).sp,
            displayLarge = (base * ratio.pow(5)).sp,
        )
    }

    fun createFontFamily(
        fontFamily: AppFontFamily,
        width: Int? = null,
        grade: Int? = null,
        rond: Int? = null,
    ): FontFamily = if (
        fontFamily.supportsVariableSettings && (width != null || grade != null || rond != null)
    ) {
        createVariableFontFamily(
            fontFamily,
            width ?: DefaultDisplayFontWidth,
            grade ?: DefaultDisplayFontGrade,
            rond ?: DefaultDisplayFontRond,
        )
    } else {
        createStandardFontFamily(fontFamily.fontRes)
    }

    private fun createVariableFontFamily(
        fontFamily: AppFontFamily,
        width: Int,
        grade: Int,
        rond: Int,
    ): FontFamily = FontFamily(
        FontWeights.map { weight ->
            Font(
                resId = fontFamily.fontRes,
                weight = weight,
                variationSettings =
                    FontVariation.Settings(
                        *buildList {
                            add(FontVariation.weight(weight.weight))
                            fontFamily.getAxisConfig(FontAxis.WIDTH)?.let {
                                add(FontVariation.width(width.toFloat()))
                            }
                            fontFamily.getAxisConfig(FontAxis.GRADE)?.let {
                                add(FontVariation.grade(grade))
                            }
                            fontFamily.getAxisConfig(FontAxis.ROND)?.let {
                                add(FontVariation.Setting("ROND", rond.toFloat()))
                            }
                        }
                            .toTypedArray(),
                    ),
            )
        },
    )

    private fun createStandardFontFamily(@FontRes fontRes: Int): FontFamily = FontFamily(
        FontWeights.map { weight ->
            Font(
                resId = fontRes,
                weight = weight,
                variationSettings =
                    FontVariation.Settings(FontVariation.weight(weight.weight)),
            )
        },
    )

    fun createTypography(
        sizes: FontSizes,
        displayFontFamily: FontFamily,
        bodyFontFamily: FontFamily,
    ): Typography {
        val base = Typography()
        return Typography(
            displayLarge = base.displayLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.displayLarge),
            displayLargeEmphasized = base.displayLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displayLarge),
            displayMedium = base.displayMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.displayMedium),
            displayMediumEmphasized = base.displayMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displayMedium),
            displaySmall = base.displaySmall.copy(fontFamily = displayFontFamily, fontSize = sizes.displaySmall),
            displaySmallEmphasized = base.displaySmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displaySmall),
            headlineLarge = base.headlineLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineLarge),
            headlineLargeEmphasized = base.headlineLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineLarge),
            headlineMedium = base.headlineMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineMedium),
            headlineMediumEmphasized = base.headlineMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineMedium),
            headlineSmall = base.headlineSmall.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineSmall),
            headlineSmallEmphasized = base.headlineSmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineSmall),
            titleLarge = base.titleLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.titleLarge),
            titleLargeEmphasized = base.titleLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleLarge),
            titleMedium = base.titleMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.titleMedium),
            titleMediumEmphasized = base.titleMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleMedium),
            titleSmall = base.titleSmall.copy(fontFamily = displayFontFamily, fontSize = sizes.titleSmall),
            titleSmallEmphasized = base.titleSmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleSmall),
            bodyLarge = base.bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyLarge),
            bodyLargeEmphasized = base.bodyLargeEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyLarge),
            bodyMedium = base.bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyMedium),
            bodyMediumEmphasized = base.bodyMediumEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyMedium),
            bodySmall = base.bodySmall.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodySmall),
            bodySmallEmphasized = base.bodySmallEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodySmall),
            labelLarge = base.labelLarge.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelLarge),
            labelLargeEmphasized = base.labelLargeEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelLarge),
            labelMedium = base.labelMedium.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelMedium),
            labelMediumEmphasized = base.labelMediumEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelMedium),
            labelSmall = base.labelSmall.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelSmall),
            labelSmallEmphasized = base.labelSmallEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelSmall),
        )
    }

    private val FontWeights =
        listOf(
            FontWeight.Normal,
            FontWeight.Medium,
            FontWeight.SemiBold,
            FontWeight.Bold,
            FontWeight.Black,
        )
}

data class FontSizes(
    val labelSmall: TextUnit,
    val labelMedium: TextUnit,
    val labelLarge: TextUnit,
    val bodySmall: TextUnit,
    val bodyMedium: TextUnit,
    val bodyLarge: TextUnit,
    val titleSmall: TextUnit,
    val titleMedium: TextUnit,
    val titleLarge: TextUnit,
    val headlineSmall: TextUnit,
    val headlineMedium: TextUnit,
    val headlineLarge: TextUnit,
    val displaySmall: TextUnit,
    val displayMedium: TextUnit,
    val displayLarge: TextUnit,
)

@Composable
fun rememberAppTypography(
    baseSize: BaseSize,
    displayFontFamily: AppFontFamily,
    bodyFontFamily: AppFontFamily,
    displayWidth: Int,
    displayGrade: Int,
    displayRond: Int,
    bodyWidth: Int,
    bodyGrade: Int,
    bodyRond: Int,
): Typography = remember(
    baseSize,
    displayFontFamily,
    bodyFontFamily,
    displayWidth,
    displayGrade,
    displayRond,
    bodyWidth,
    bodyGrade,
    bodyRond,
) {
    val sizes = ThemeEngine.calculateFontSizes(baseSize)
    val displayFont =
        ThemeEngine.createFontFamily(
            displayFontFamily,
            if (displayFontFamily.supportsVariableSettings) displayWidth else null,
            if (displayFontFamily.supportsVariableSettings) displayGrade else null,
            if (displayFontFamily.supportsVariableSettings) displayRond else null,
        )
    val bodyFont =
        ThemeEngine.createFontFamily(
            bodyFontFamily,
            if (bodyFontFamily.supportsVariableSettings) bodyWidth else null,
            if (bodyFontFamily.supportsVariableSettings) bodyGrade else null,
            if (bodyFontFamily.supportsVariableSettings) bodyRond else null,
        )
    ThemeEngine.createTypography(sizes, displayFont, bodyFont)
}
