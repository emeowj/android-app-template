package com.template.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

@Composable
fun TemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

object AppShape {
    private val largeRadius = Padding.large
    private val smallRadius = Padding.small

    val listFull = RoundedCornerShape(size = largeRadius)
    val listTop = RoundedCornerShape(
        topStart = largeRadius,
        topEnd = largeRadius,
        bottomStart = smallRadius,
        bottomEnd = smallRadius
    )
    val listMiddle = RoundedCornerShape(size = smallRadius)
    val listBottom = RoundedCornerShape(
        topStart = smallRadius,
        topEnd = smallRadius,
        bottomStart = largeRadius,
        bottomEnd = largeRadius,
    )

    fun calculateListShape(index: Int, size: Int): Shape = when {
        size == 1 -> listFull
        index == 0 -> listTop
        index == size - 1 -> listBottom
        else -> listMiddle
    }
}

object Padding {
    val hairline = 1.dp
    val extraSmall = 2.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 36.dp
}