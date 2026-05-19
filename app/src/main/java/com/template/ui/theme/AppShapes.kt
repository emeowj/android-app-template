package com.template.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object AppShape {
    val extraSmallRadius = 6.dp
    val smallRadius = 10.dp
    val mediumRadius = 16.dp
    val largeRadius = 24.dp
    val extraLargeRadius = 32.dp
    val pillRadius = 999.dp

    val listFullRadius = largeRadius
    val listMiddleRadius = extraSmallRadius
    val buttonRadius = smallRadius
    val cardRadius = mediumRadius
    val cardInnerRadius = extraSmallRadius
    val dialogRadius = largeRadius
    val chipRadius = extraSmallRadius
    val inputRadius = smallRadius
    val sheetRadius = largeRadius

    val extraSmall: Shape = RoundedCornerShape(extraSmallRadius)
    val small: Shape = RoundedCornerShape(smallRadius)
    val medium: Shape = RoundedCornerShape(mediumRadius)
    val large: Shape = RoundedCornerShape(largeRadius)
    val extraLarge: Shape = RoundedCornerShape(extraLargeRadius)
    val pill: Shape = RoundedCornerShape(pillRadius)
    val listFull: Shape = RoundedCornerShape(listFullRadius)
    val listMiddle: Shape = RoundedCornerShape(listMiddleRadius)
    val listTop: Shape =
        RoundedCornerShape(
            topStart = listFullRadius,
            topEnd = listFullRadius,
            bottomStart = listMiddleRadius,
            bottomEnd = listMiddleRadius,
        )
    val listBottom: Shape =
        RoundedCornerShape(
            topStart = listMiddleRadius,
            topEnd = listMiddleRadius,
            bottomStart = listFullRadius,
            bottomEnd = listFullRadius,
        )
    val button: Shape = RoundedCornerShape(buttonRadius)
    val card: Shape = RoundedCornerShape(cardRadius)
    val cardInner: Shape = RoundedCornerShape(cardInnerRadius)
    val dialog: Shape = RoundedCornerShape(dialogRadius)
    val chip: Shape = RoundedCornerShape(chipRadius)
    val input: Shape = RoundedCornerShape(inputRadius)
    val sheet: Shape =
        RoundedCornerShape(
            topStart = sheetRadius,
            topEnd = sheetRadius,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )

    fun listItemShape(index: Int, total: Int): Shape = when {
        total == 1 -> listFull
        index == 0 -> listTop
        index == total - 1 -> listBottom
        else -> listMiddle
    }

    fun calculateListShape(index: Int, size: Int): Shape = listItemShape(index = index, total = size)
}

fun appMaterialShapes(): Shapes = Shapes(
    extraSmall = AppShape.extraSmall as CornerBasedShape,
    small = AppShape.small as CornerBasedShape,
    medium = AppShape.medium as CornerBasedShape,
    large = AppShape.large as CornerBasedShape,
    extraLarge = AppShape.extraLarge as CornerBasedShape,
)
