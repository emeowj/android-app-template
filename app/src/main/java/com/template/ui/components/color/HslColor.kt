package com.template.ui.components.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Represents a color in the HSL (Hue, Saturation, Lightness) color space.
 *
 * @param hue Hue angle in degrees, normalized to `0f..360f`.
 * @param saturation Saturation ratio, clamped to `0f..1f`.
 * @param lightness Lightness ratio, clamped to `0f..1f`.
 */
@Immutable
data class HslColor(
    val hue: Float,
    val saturation: Float,
    val lightness: Float,
) {
    init {
        require(hue.isFinite() && saturation.isFinite() && lightness.isFinite()) {
            "HSL values must be finite numbers."
        }
    }

    val normalizedHue: Float = ((hue % 360f) + 360f) % 360f
    val clampedSaturation: Float = saturation.coerceIn(0f, 1f)
    val clampedLightness: Float = lightness.coerceIn(0f, 1f)

    /**
     * Converts this [HslColor] into a Compose [Color].
     */
    fun toColor(alpha: Float = 1f): Color = hslToColor(
        hue = normalizedHue,
        saturation = clampedSaturation,
        lightness = clampedLightness,
        alpha = alpha.coerceIn(0f, 1f),
    )

    /**
     * Converts this [HslColor] to a standard 6-digit uppercase hex string (e.g. `"#3E5C76"`).
     */
    fun toHex(includePrefix: Boolean = true): String = toColor().toHex(includePrefix = includePrefix)

    companion object {
        val Black = HslColor(hue = 0f, saturation = 0f, lightness = 0f)
        val White = HslColor(hue = 0f, saturation = 0f, lightness = 1f)

        /**
         * Creates an [HslColor] from a Compose [Color].
         */
        fun fromColor(color: Color): HslColor = colorToHsl(color)

        /**
         * Parses a hex color string into an [HslColor], or returns null if invalid.
         */
        fun fromHex(hex: String): HslColor? {
            val clean = hex.trim().removePrefix("#")
            if (clean.length != 3 && clean.length != 6 && clean.length != 8) return null
            return try {
                val parsedColor = ColorUtils.parseColorHex(hex)
                colorToHsl(parsedColor)
            } catch (_: Exception) {
                null
            }
        }
    }
}

/**
 * Converts this Compose [Color] into an [HslColor].
 */
fun Color.toHsl(): HslColor = colorToHsl(this)

/**
 * Converts this Compose [Color] into a 6-digit uppercase hex string (e.g. `"#3E5C76"`).
 */
fun Color.toHex(includePrefix: Boolean = true): String {
    val r = (red * 255f).roundToInt().coerceIn(0, 255)
    val g = (green * 255f).roundToInt().coerceIn(0, 255)
    val b = (blue * 255f).roundToInt().coerceIn(0, 255)
    val hex = String.format(Locale.ROOT, "%02X%02X%02X", r, g, b)
    return if (includePrefix) "#$hex" else hex
}

private fun colorToHsl(color: Color): HslColor {
    val r = color.red.coerceIn(0f, 1f)
    val g = color.green.coerceIn(0f, 1f)
    val b = color.blue.coerceIn(0f, 1f)

    val maxVal = max(r, max(g, b))
    val minVal = min(r, min(g, b))
    val delta = maxVal - minVal

    val lightness = (maxVal + minVal) / 2f

    if (delta <= 1e-5f) {
        return HslColor(hue = 0f, saturation = 0f, lightness = lightness)
    }

    val saturation = if (lightness > 0.5f) {
        delta / (2f - maxVal - minVal)
    } else {
        delta / (maxVal + minVal)
    }

    val rawHue = when (maxVal) {
        r -> ((g - b) / delta + (if (g < b) 6f else 0f)) * 60f
        g -> ((b - r) / delta + 2f) * 60f
        else -> ((r - g) / delta + 4f) * 60f
    }

    val normalizedHue = ((rawHue % 360f) + 360f) % 360f
    return HslColor(
        hue = normalizedHue,
        saturation = saturation.coerceIn(0f, 1f),
        lightness = lightness.coerceIn(0f, 1f),
    )
}

private fun hslToColor(hue: Float, saturation: Float, lightness: Float, alpha: Float): Color {
    val h = ((hue % 360f) + 360f) % 360f
    val s = saturation.coerceIn(0f, 1f)
    val l = lightness.coerceIn(0f, 1f)

    if (s <= 1e-5f) {
        return Color(red = l, green = l, blue = l, alpha = alpha)
    }

    val q = if (l < 0.5f) l * (1f + s) else l + s - (l * s)
    val p = 2f * l - q
    val hk = h / 360f

    val tr = hk + (1f / 3f)
    val tg = hk
    val tb = hk - (1f / 3f)

    val r = hueToRgb(p, q, tr)
    val g = hueToRgb(p, q, tg)
    val b = hueToRgb(p, q, tb)

    return Color(red = r.coerceIn(0f, 1f), green = g.coerceIn(0f, 1f), blue = b.coerceIn(0f, 1f), alpha = alpha)
}

private fun hueToRgb(p: Float, q: Float, t: Float): Float {
    var tc = t
    if (tc < 0f) tc += 1f
    if (tc > 1f) tc -= 1f
    return when {
        tc < 1f / 6f -> p + (q - p) * 6f * tc
        tc < 1f / 2f -> q
        tc < 2f / 3f -> p + (q - p) * (2f / 3f - tc) * 6f
        else -> p
    }
}
