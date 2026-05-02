package com.template.ui.theme

import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.template.R

enum class BaseSize(val bodySizeSp: Int, @StringRes val displayNameRes: Int) {
    SMALL(14, R.string.base_size_small),
    MEDIUM(16, R.string.base_size_medium),
    LARGE(18, R.string.base_size_large);

    companion object {
        val DEFAULT = MEDIUM
    }
}

enum class FontAxis(val tag: String) {
    WIDTH("wdth"),
    GRADE("GRAD"),
    ROND("ROND"),
}

data class FontAxisConfig(
    val axis: FontAxis,
    val min: Float,
    val max: Float,
    val default: Float,
)

enum class AppFontFamily(
    @FontRes val fontRes: Int,
    @StringRes val displayNameRes: Int,
    val supportedAxes: Set<FontAxisConfig> = emptySet(),
) {
    GOOGLE_SANS_FLEX(
        fontRes = R.font.google_sans_flex,
        displayNameRes = R.string.font_google_sans_flex,
        supportedAxes =
            setOf(
                FontAxisConfig(FontAxis.WIDTH, min = 25f, max = 151f, default = 100f),
                FontAxisConfig(FontAxis.GRADE, min = 0f, max = 100f, default = 0f),
                FontAxisConfig(FontAxis.ROND, min = 0f, max = 100f, default = 0f),
            ),
    ),
    HANKEN_GROTESK(
        fontRes = R.font.hanken_grotesk,
        displayNameRes = R.string.font_hanken_grotesk,
    ),
    SPACE_GROTESK(
        fontRes = R.font.space_grotesk,
        displayNameRes = R.string.font_space_grotesk,
    ),
    FREDOKA(
        fontRes = R.font.fredoka,
        displayNameRes = R.string.font_fredoka,
        supportedAxes =
            setOf(FontAxisConfig(FontAxis.WIDTH, min = 75f, max = 125f, default = 100f)),
    ),
    FASCINATE(
        fontRes = R.font.fascinate,
        displayNameRes = R.string.font_fascinate,
    ),
    LOBSTER(
        fontRes = R.font.lobster,
        displayNameRes = R.string.font_lobster,
    ),
    ROBOTO_SERIF(
        fontRes = R.font.roboto_serif,
        displayNameRes = R.string.font_roboto_serif,
        supportedAxes =
            setOf(
                FontAxisConfig(FontAxis.WIDTH, min = 50f, max = 150f, default = 100f),
                FontAxisConfig(FontAxis.GRADE, min = -50f, max = 100f, default = 0f),
            ),
    );

    val supportsVariableSettings: Boolean
        get() = supportedAxes.isNotEmpty()

    fun getAxisConfig(axis: FontAxis): FontAxisConfig? = supportedAxes.find { it.axis == axis }

    companion object {
        val DEFAULT = GOOGLE_SANS_FLEX
    }
}

data class ColorPreset(val id: String, @StringRes val nameRes: Int, val color: Color) {
    companion object {
        val DEFAULT = ColorPreset(
            id = "teal",
            nameRes = R.string.color_preset_teal,
            color = Color(0xFF264653),
        )
        val OPTIONS =
            listOf(
                DEFAULT,
                ColorPreset(
                    id = "cyan",
                    nameRes = R.string.color_preset_cyan,
                    color = Color(0xFF2A9D8F),
                ),
                ColorPreset(
                    id = "gold",
                    nameRes = R.string.color_preset_gold,
                    color = Color(0xFFE9C46A),
                ),
                ColorPreset(
                    id = "pink",
                    nameRes = R.string.color_preset_pink,
                    color = Color(0xFFFF006E),
                ),
                ColorPreset(
                    id = "indigo",
                    nameRes = R.string.color_preset_indigo,
                    color = Color(0xFF303F9F),
                ),
                ColorPreset(
                    id = "forest",
                    nameRes = R.string.color_preset_forest,
                    color = Color(0xFF388E3C),
                ),
            )
    }
}

const val DefaultDisplayFontWidth = 92
const val DefaultDisplayFontGrade = 40
const val DefaultDisplayFontRond = 98
const val DefaultBodyFontWidth = 92
const val DefaultBodyFontGrade = 10
const val DefaultBodyFontRond = 32
