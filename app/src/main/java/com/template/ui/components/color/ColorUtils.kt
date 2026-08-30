package com.template.ui.components.color

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun parseColorHex(hex: String, fallback: Color = Color.Black): Color = try {
        val clean = hex.trim().removePrefix("#")
        when (clean.length) {
            3 -> {
                val r = clean[0].toString().repeat(2).toInt(16)
                val g = clean[1].toString().repeat(2).toInt(16)
                val b = clean[2].toString().repeat(2).toInt(16)
                Color(r, g, b)
            }

            6 -> {
                val r = clean.substring(0, 2).toInt(16)
                val g = clean.substring(2, 4).toInt(16)
                val b = clean.substring(4, 6).toInt(16)
                Color(r, g, b)
            }

            8 -> {
                val a = clean.substring(0, 2).toInt(16)
                val r = clean.substring(2, 4).toInt(16)
                val g = clean.substring(4, 6).toInt(16)
                val b = clean.substring(6, 8).toInt(16)
                Color(r, g, b, a)
            }

            else -> fallback
        }
    } catch (_: Exception) {
        fallback
    }
}

fun String.toComposeColor(): Color = ColorUtils.parseColorHex(this)
