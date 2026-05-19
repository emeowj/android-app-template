package com.template.ui.theme

import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.template.R

enum class BaseSize(val bodySizeSp: Int, @StringRes val displayNameRes: Int) {
    SMALL(14, R.string.base_size_small),
    MEDIUM(16, R.string.base_size_medium),
    LARGE(18, R.string.base_size_large),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}

enum class FontAxis(val tag: String) {
    WIDTH("wdth"),
    GRADE("GRAD"),
    ROND("ROND"),
}

data class FontAxisConfig(val axis: FontAxis, val min: Float, val max: Float, val default: Float) {
    companion object {
        const val DEFAULT_DISPLAY_WIDTH = 100
        const val DEFAULT_DISPLAY_GRADE = -25
        const val DEFAULT_DISPLAY_ROND = 0
        const val DEFAULT_BODY_WIDTH = 100
        const val DEFAULT_BODY_GRADE = 0
        const val DEFAULT_BODY_ROND = 0
    }
}

data class AppFontResource(
    @FontRes val fontRes: Int,
    val weight: FontWeight,
    val style: FontStyle = FontStyle.Normal,
    val supportsWeightAxis: Boolean = false,
)

enum class AppFontFamily(
    @FontRes val fontRes: Int,
    @StringRes val displayNameRes: Int,
    val fontResources: List<AppFontResource> = emptyList(),
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
    FIGTREE(
        fontRes = R.font.figtree,
        displayNameRes = R.string.font_figtree,
        fontResources =
            variableWeightResources(
                regularFontRes = R.font.figtree,
                italicFontRes = R.font.figtree_italic,
            ),
    ),
    INTER(
        fontRes = R.font.inter,
        displayNameRes = R.string.font_inter,
    ),
    IBM_PLEX_SANS(
        fontRes = R.font.ibm_plex_sans,
        displayNameRes = R.string.font_ibm_plex_sans,
        supportedAxes =
            setOf(FontAxisConfig(FontAxis.WIDTH, min = 75f, max = 100f, default = 100f)),
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
    ),
    AVERIA_SERIF_LIBRE(
        fontRes = R.font.averia_serif_libre_regular,
        displayNameRes = R.string.font_averia_serif_libre,
        fontResources =
            listOf(
                AppFontResource(R.font.averia_serif_libre_light, FontWeight.Light),
                AppFontResource(R.font.averia_serif_libre_regular, FontWeight.Normal),
                AppFontResource(R.font.averia_serif_libre_bold, FontWeight.Bold),
                AppFontResource(
                    R.font.averia_serif_libre_light_italic,
                    FontWeight.Light,
                    FontStyle.Italic,
                ),
                AppFontResource(
                    R.font.averia_serif_libre_italic,
                    FontWeight.Normal,
                    FontStyle.Italic,
                ),
                AppFontResource(
                    R.font.averia_serif_libre_bold_italic,
                    FontWeight.Bold,
                    FontStyle.Italic,
                ),
            ),
    ),
    FRAUNCES(
        fontRes = R.font.fraunces,
        displayNameRes = R.string.font_fraunces,
    ),
    CORMORANT_GARAMOND(
        fontRes = R.font.cormorant_garamond,
        displayNameRes = R.string.font_cormorant_garamond,
    ),
    ;

    val supportsVariableSettings: Boolean
        get() = supportedAxes.isNotEmpty()

    fun getAxisConfig(axis: FontAxis): FontAxisConfig? = supportedAxes.find { it.axis == axis }

    companion object {
        val DEFAULT_DISPLAY = ROBOTO_SERIF
        val DEFAULT_BODY = HANKEN_GROTESK
    }
}

data class ColorPreset(val id: String, @StringRes val nameRes: Int, val color: Color) {
    fun roles(isDark: Boolean): ColorRoles = when (id) {
        "sage" -> if (isDark) SageDark else SageLight
        "ink" -> if (isDark) InkDark else InkLight
        "clay" -> if (isDark) ClayDark else ClayLight
        else -> if (isDark) MidnightDark else MidnightLight
    }

    companion object {
        val DEFAULT = ColorPreset(
            id = "midnight",
            nameRes = R.string.color_preset_midnight,
            color = Color(0xFF3E5C76),
        )
        val OPTIONS =
            listOf(
                DEFAULT,
                ColorPreset(
                    id = "sage",
                    nameRes = R.string.color_preset_sage,
                    color = Color(0xFF5A6B5C),
                ),
                ColorPreset(
                    id = "ink",
                    nameRes = R.string.color_preset_ink,
                    color = Color(0xFF1A1A1A),
                ),
                ColorPreset(
                    id = "clay",
                    nameRes = R.string.color_preset_clay,
                    color = Color(0xFF8B5A3C),
                ),
            )
    }
}

data class ColorRoles(
    val bg: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val ink: Color,
    val inkSoft: Color,
    val inkMuted: Color,
    val hairline: Color,
    val accent: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val good: Color,
    val warn: Color,
)

private val MidnightLight =
    ColorRoles(
        bg = Color(0xFFF4F2EE),
        surface = Color.White,
        surfaceAlt = Color(0xFFEDEAE3),
        ink = Color(0xFF13141A),
        inkSoft = Color(0xFF43464F),
        inkMuted = Color(0xFF7C7F87),
        hairline = Color(0x1A13141A),
        accent = Color(0xFF3E5C76),
        accentSoft = Color(0xFF7B8FA8),
        onAccent = Color.White,
        good = Color(0xFF5A7A52),
        warn = Color(0xFFA06B3D),
    )

private val MidnightDark =
    ColorRoles(
        bg = Color(0xFF0E1116),
        surface = Color(0xFF161A21),
        surfaceAlt = Color(0xFF1E232C),
        ink = Color(0xFFEDEAE3),
        inkSoft = Color(0xFFB8BAC0),
        inkMuted = Color(0xFF777A82),
        hairline = Color(0x1AEDEAE3),
        accent = Color(0xFFA8C0D6),
        accentSoft = Color(0xFF7B8FA8),
        onAccent = Color(0xFF0E1116),
        good = Color(0xFF9BB892),
        warn = Color(0xFFD4A878),
    )

private val SageLight =
    ColorRoles(
        bg = Color(0xFFFBF8F4),
        surface = Color.White,
        surfaceAlt = Color(0xFFEDE8DE),
        ink = Color(0xFF1F1B16),
        inkSoft = Color(0xFF5A554C),
        inkMuted = Color(0xFF8B857A),
        hairline = Color(0x1A1F1B16),
        accent = Color(0xFF5A6B5C),
        accentSoft = Color(0xFF8FA294),
        onAccent = Color.White,
        good = Color(0xFF5A6B5C),
        warn = Color(0xFFA06B3D),
    )

private val SageDark =
    ColorRoles(
        bg = Color(0xFF13110F),
        surface = Color(0xFF1B1916),
        surfaceAlt = Color(0xFF2A2723),
        ink = Color(0xFFEDEAE3),
        inkSoft = Color(0xFFB8B3A8),
        inkMuted = Color(0xFF7E7A72),
        hairline = Color(0x1AEDEAE3),
        accent = Color(0xFFA2B5A4),
        accentSoft = Color(0xFF7B8E7D),
        onAccent = Color(0xFF13110F),
        good = Color(0xFFA2B5A4),
        warn = Color(0xFFD4A878),
    )

private val InkLight =
    ColorRoles(
        bg = Color.White,
        surface = Color.White,
        surfaceAlt = Color(0xFFF2F2F2),
        ink = Color(0xFF0A0A0A),
        inkSoft = Color(0xFF3A3A3A),
        inkMuted = Color(0xFF7A7A7A),
        hairline = Color(0x1F000000),
        accent = Color(0xFF0A0A0A),
        accentSoft = Color(0xFF5A5A5A),
        onAccent = Color.White,
        good = Color(0xFF0A0A0A),
        warn = Color(0xFF7A4D2A),
    )

private val InkDark =
    ColorRoles(
        bg = Color.Black,
        surface = Color(0xFF0E0E0E),
        surfaceAlt = Color(0xFF1A1A1A),
        ink = Color.White,
        inkSoft = Color(0xFFC8C8C8),
        inkMuted = Color(0xFF7A7A7A),
        hairline = Color(0x1FFFFFFF),
        accent = Color.White,
        accentSoft = Color(0xFFB8B8B8),
        onAccent = Color.Black,
        good = Color.White,
        warn = Color(0xFFD4A878),
    )

private val ClayLight =
    ColorRoles(
        bg = Color(0xFFF4ECDC),
        surface = Color(0xFFFBF5E7),
        surfaceAlt = Color(0xFFE8DCC0),
        ink = Color(0xFF2C2114),
        inkSoft = Color(0xFF5A4732),
        inkMuted = Color(0xFF8C7A5E),
        hairline = Color(0x1F2C2114),
        accent = Color(0xFF8B5A3C),
        accentSoft = Color(0xFFB58968),
        onAccent = Color(0xFFFBF5E7),
        good = Color(0xFF6F7A4E),
        warn = Color(0xFFA0533C),
    )

private val ClayDark =
    ColorRoles(
        bg = Color(0xFF1A1410),
        surface = Color(0xFF241C15),
        surfaceAlt = Color(0xFF33271C),
        ink = Color(0xFFF0E5D2),
        inkSoft = Color(0xFFC2B294),
        inkMuted = Color(0xFF8C7E66),
        hairline = Color(0x1FF0E5D2),
        accent = Color(0xFFD4A574),
        accentSoft = Color(0xFFA88556),
        onAccent = Color(0xFF1A1410),
        good = Color(0xFFB3BD92),
        warn = Color(0xFFE0A878),
    )

private fun variableWeightResources(
    @FontRes regularFontRes: Int,
    @FontRes italicFontRes: Int? = null,
): List<AppFontResource> = buildList {
    VariableFontWeights.forEach { weight ->
        add(AppFontResource(regularFontRes, weight, supportsWeightAxis = true))
    }
    if (italicFontRes != null) {
        VariableFontWeights.forEach { weight ->
            add(
                AppFontResource(
                    italicFontRes,
                    weight,
                    FontStyle.Italic,
                    supportsWeightAxis = true,
                ),
            )
        }
    }
}

private val VariableFontWeights =
    listOf(
        FontWeight.Normal,
        FontWeight.Medium,
        FontWeight.SemiBold,
        FontWeight.Bold,
        FontWeight.Black,
    )

data class FontPairing(val display: AppFontFamily, val body: AppFontFamily) {
    companion object {
        val PRESETS =
            listOf(
                FontPairing(AppFontFamily.ROBOTO_SERIF, AppFontFamily.HANKEN_GROTESK),
                FontPairing(AppFontFamily.AVERIA_SERIF_LIBRE, AppFontFamily.FIGTREE),
                FontPairing(AppFontFamily.FRAUNCES, AppFontFamily.INTER),
                FontPairing(AppFontFamily.CORMORANT_GARAMOND, AppFontFamily.IBM_PLEX_SANS),
            )
    }
}
