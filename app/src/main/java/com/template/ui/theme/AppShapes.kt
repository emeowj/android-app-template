package com.template.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object AppShape {
    val listFullRadius = 24.dp
    val listMiddleRadius = 8.dp
    val buttonRadius = 12.dp
    val cardRadius = 16.dp
    val cardInnerRadius = 8.dp
    val dialogRadius = 24.dp
    val chipRadius = 8.dp
    val inputRadius = 12.dp
    val sheetRadius = 24.dp

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

    fun listItemShape(index: Int, total: Int): Shape =
        when {
            total == 1 -> listFull
            index == 0 -> listTop
            index == total - 1 -> listBottom
            else -> listMiddle
        }

    fun calculateListShape(index: Int, size: Int): Shape =
        listItemShape(index = index, total = size)
}

fun appMaterialShapes(): Shapes =
    Shapes(
        extraSmall = AppShape.chip as CornerBasedShape,
        small = AppShape.input as CornerBasedShape,
        medium = AppShape.card as CornerBasedShape,
        large = AppShape.dialog as CornerBasedShape,
        extraLarge = AppShape.sheet as CornerBasedShape,
    )
