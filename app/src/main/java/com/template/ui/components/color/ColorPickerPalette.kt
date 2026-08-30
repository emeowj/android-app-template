package com.template.ui.components.color

import androidx.compose.ui.graphics.Color

/**
 * Curated 24-color palette for the shared color picker.
 * Organized as 3 rows of 8 swatches spanning darks, warm tones, and cool accent hues.
 */
object ColorPickerPalette {
    val Row1: List<Color> = listOf(
        Color(0xFF14161D),
        Color(0xFF26303F),
        Color(0xFF1F3B47),
        Color(0xFF264653),
        Color(0xFF2B3648),
        Color(0xFF3F4A5C),
        Color(0xFF5A5F6E),
        Color(0xFF7C8896),
    )

    val Row2: List<Color> = listOf(
        Color(0xFFB0664A),
        Color(0xFFD97757),
        Color(0xFFE9A06B),
        Color(0xFFE9C46A),
        Color(0xFFD8B25F),
        Color(0xFF8A7C3F),
        Color(0xFF4A4436),
        Color(0xFF231F26),
    )

    val Row3: List<Color> = listOf(
        Color(0xFF6F8F86),
        Color(0xFF2FB6C4),
        Color(0xFF5FA8D3),
        Color(0xFF7B4FD1),
        Color(0xFFA06B8A),
        Color(0xFFC25F6A),
        Color(0xFFE4DDCF),
        Color(0xFFF4F0E4),
    )

    val All: List<Color> = Row1 + Row2 + Row3

    const val Columns: Int = 8
    const val TotalSwatches: Int = 24
}
