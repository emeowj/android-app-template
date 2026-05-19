package com.template.ui.theme

import androidx.annotation.FontRes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object ThemeEngine {
    fun calculateFontSizes(baseSize: BaseSize): FontSizes {
        val scale = baseSize.bodySizeSp / BaseSize.MEDIUM.bodySizeSp.toFloat()
        return FontSizes(
            labelSmall = scaledSp(11f, scale),
            labelMedium = scaledSp(12f, scale),
            labelLarge = scaledSp(14f, scale),
            bodySmall = scaledSp(13f, scale),
            bodyMedium = scaledSp(15f, scale),
            bodyLarge = scaledSp(17f, scale),
            titleSmall = scaledSp(14f, scale),
            titleMedium = scaledSp(16f, scale),
            titleLarge = scaledSp(22f, scale),
            headlineSmall = scaledSp(24f, scale),
            headlineMedium = scaledSp(28f, scale),
            headlineLarge = scaledSp(32f, scale),
            displaySmall = scaledSp(36f, scale),
            displayMedium = scaledSp(45f, scale),
            displayLarge = scaledSp(57f, scale),
        )
    }

    private fun scaledSp(value: Float, scale: Float): TextUnit = (value * scale).sp

    fun createFontFamily(
        fontFamily: AppFontFamily,
        width: Int? = null,
        grade: Int? = null,
        rond: Int? = null,
    ): FontFamily {
        if (fontFamily.fontResources.isNotEmpty()) {
            return createResourceFontFamily(fontFamily, width, grade, rond)
        }

        return if (
            fontFamily.supportsVariableSettings && (width != null || grade != null || rond != null)
        ) {
            createVariableFontFamily(
                fontFamily,
                width ?: FontAxisConfig.DEFAULT_DISPLAY_WIDTH,
                grade ?: FontAxisConfig.DEFAULT_DISPLAY_GRADE,
                rond ?: FontAxisConfig.DEFAULT_DISPLAY_ROND,
            )
        } else {
            createStandardFontFamily(fontFamily.fontRes)
        }
    }

    private fun createResourceFontFamily(
        fontFamily: AppFontFamily,
        width: Int?,
        grade: Int?,
        rond: Int?,
    ): FontFamily = FontFamily(
        fontFamily.fontResources.map { resource ->
            val settings =
                buildList {
                    if (resource.supportsWeightAxis) {
                        add(FontVariation.weight(resource.weight.weight))
                    }
                    fontFamily.getAxisConfig(FontAxis.WIDTH)?.let {
                        add(FontVariation.width((width ?: it.default.toInt()).toFloat()))
                    }
                    fontFamily.getAxisConfig(FontAxis.GRADE)?.let {
                        add(FontVariation.grade(grade ?: it.default.toInt()))
                    }
                    fontFamily.getAxisConfig(FontAxis.ROND)?.let {
                        add(
                            FontVariation.Setting(
                                "ROND",
                                (rond ?: it.default.toInt()).toFloat(),
                            ),
                        )
                    }
                }
            if (settings.isEmpty()) {
                Font(
                    resId = resource.fontRes,
                    weight = resource.weight,
                    style = resource.style,
                )
            } else {
                Font(
                    resId = resource.fontRes,
                    weight = resource.weight,
                    style = resource.style,
                    variationSettings = FontVariation.Settings(*settings.toTypedArray()),
                )
            }
        },
    )

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
                style = FontStyle.Normal,
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
        fun TextUnit.lineHeight(multiplier: Float): TextUnit = (value * multiplier).sp
        return Typography(
            displayLarge = base.displayLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.displayLarge, fontWeight = FontWeight.Normal, lineHeight = sizes.displayLarge.lineHeight(1.05f), letterSpacing = 0.sp),
            displayLargeEmphasized = base.displayLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displayLarge, fontWeight = FontWeight.Normal, lineHeight = sizes.displayLarge.lineHeight(1.05f), letterSpacing = 0.sp),
            displayMedium = base.displayMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.displayMedium, fontWeight = FontWeight.Normal, lineHeight = sizes.displayMedium.lineHeight(1.05f), letterSpacing = 0.sp),
            displayMediumEmphasized = base.displayMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displayMedium, fontWeight = FontWeight.Normal, lineHeight = sizes.displayMedium.lineHeight(1.05f), letterSpacing = 0.sp),
            displaySmall = base.displaySmall.copy(fontFamily = displayFontFamily, fontSize = sizes.displaySmall, fontWeight = FontWeight.Medium, lineHeight = sizes.displaySmall.lineHeight(1.10f), letterSpacing = 0.sp),
            displaySmallEmphasized = base.displaySmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.displaySmall, fontWeight = FontWeight.Medium, lineHeight = sizes.displaySmall.lineHeight(1.10f), letterSpacing = 0.sp),
            headlineLarge = base.headlineLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineLarge, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineLarge.lineHeight(1.15f), letterSpacing = 0.sp),
            headlineLargeEmphasized = base.headlineLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineLarge, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineLarge.lineHeight(1.15f), letterSpacing = 0.sp),
            headlineMedium = base.headlineMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineMedium, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineMedium.lineHeight(1.15f), letterSpacing = 0.sp),
            headlineMediumEmphasized = base.headlineMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineMedium, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineMedium.lineHeight(1.15f), letterSpacing = 0.sp),
            headlineSmall = base.headlineSmall.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineSmall, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineSmall.lineHeight(1.20f), letterSpacing = 0.sp),
            headlineSmallEmphasized = base.headlineSmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.headlineSmall, fontWeight = FontWeight.Medium, lineHeight = sizes.headlineSmall.lineHeight(1.20f), letterSpacing = 0.sp),
            titleLarge = base.titleLarge.copy(fontFamily = displayFontFamily, fontSize = sizes.titleLarge, fontWeight = FontWeight.Medium, lineHeight = sizes.titleLarge.lineHeight(1.25f), letterSpacing = 0.sp),
            titleLargeEmphasized = base.titleLargeEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleLarge, fontWeight = FontWeight.Medium, lineHeight = sizes.titleLarge.lineHeight(1.25f), letterSpacing = 0.sp),
            titleMedium = base.titleMedium.copy(fontFamily = displayFontFamily, fontSize = sizes.titleMedium, fontWeight = FontWeight.Medium, lineHeight = sizes.titleMedium.lineHeight(1.30f), letterSpacing = 0.sp),
            titleMediumEmphasized = base.titleMediumEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleMedium, fontWeight = FontWeight.Medium, lineHeight = sizes.titleMedium.lineHeight(1.30f), letterSpacing = 0.sp),
            titleSmall = base.titleSmall.copy(fontFamily = displayFontFamily, fontSize = sizes.titleSmall, fontWeight = FontWeight.SemiBold, lineHeight = sizes.titleSmall.lineHeight(1.30f), letterSpacing = 0.1.sp),
            titleSmallEmphasized = base.titleSmallEmphasized.copy(fontFamily = displayFontFamily, fontSize = sizes.titleSmall, fontWeight = FontWeight.SemiBold, lineHeight = sizes.titleSmall.lineHeight(1.30f), letterSpacing = 0.1.sp),
            bodyLarge = base.bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyLarge, fontWeight = FontWeight.Normal, lineHeight = sizes.bodyLarge.lineHeight(1.55f), letterSpacing = 0.sp),
            bodyLargeEmphasized = base.bodyLargeEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyLarge, fontWeight = FontWeight.Medium, lineHeight = sizes.bodyLarge.lineHeight(1.55f), letterSpacing = 0.sp),
            bodyMedium = base.bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyMedium, fontWeight = FontWeight.Normal, lineHeight = sizes.bodyMedium.lineHeight(1.50f), letterSpacing = 0.sp),
            bodyMediumEmphasized = base.bodyMediumEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodyMedium, fontWeight = FontWeight.Medium, lineHeight = sizes.bodyMedium.lineHeight(1.50f), letterSpacing = 0.sp),
            bodySmall = base.bodySmall.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodySmall, fontWeight = FontWeight.Normal, lineHeight = sizes.bodySmall.lineHeight(1.45f), letterSpacing = 0.sp),
            bodySmallEmphasized = base.bodySmallEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.bodySmall, fontWeight = FontWeight.Medium, lineHeight = sizes.bodySmall.lineHeight(1.45f), letterSpacing = 0.sp),
            labelLarge = base.labelLarge.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelLarge, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelLarge.lineHeight(1.20f), letterSpacing = 0.1.sp),
            labelLargeEmphasized = base.labelLargeEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelLarge, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelLarge.lineHeight(1.20f), letterSpacing = 0.1.sp),
            labelMedium = base.labelMedium.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelMedium, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelMedium.lineHeight(1.20f), letterSpacing = 0.4.sp),
            labelMediumEmphasized = base.labelMediumEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelMedium, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelMedium.lineHeight(1.20f), letterSpacing = 0.4.sp),
            labelSmall = base.labelSmall.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelSmall, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelSmall.lineHeight(1.20f), letterSpacing = 1.5.sp),
            labelSmallEmphasized = base.labelSmallEmphasized.copy(fontFamily = bodyFontFamily, fontSize = sizes.labelSmall, fontWeight = FontWeight.SemiBold, lineHeight = sizes.labelSmall.lineHeight(1.20f), letterSpacing = 1.5.sp),
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
