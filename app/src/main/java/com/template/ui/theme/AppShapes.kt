package com.template.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val chip: Shape = RoundedCornerShape(AppShapes.ChipRadius),
    val swatch: Shape = RoundedCornerShape(AppShapes.SwatchRadius),
    val input: Shape = RoundedCornerShape(AppShapes.InputRadius),
    val batchTile: Shape = RoundedCornerShape(AppShapes.BatchTileRadius),
    val heroThumb: Shape = RoundedCornerShape(AppShapes.HeroThumbRadius),
    val card: Shape = RoundedCornerShape(AppShapes.CardRadius),
    val previewCorner: Shape = RoundedCornerShape(AppShapes.PreviewCornerRadius),
    val dialog: Shape = RoundedCornerShape(AppShapes.DialogRadius),
    val pill: Shape = RoundedCornerShape(AppShapes.PillRadius),
) {
    companion object {
        val ChipRadius: Dp = 6.dp
        val SwatchRadius: Dp = 8.dp
        val InputRadius: Dp = 10.dp
        val BatchTileRadius: Dp = 10.dp
        val HeroThumbRadius: Dp = 9.dp
        val CardRadius: Dp = 16.dp
        val PreviewCornerRadius: Dp = 18.dp
        val DialogRadius: Dp = 24.dp
        val PillRadius: Dp = 999.dp

        val ListCapRadius: Dp = 24.dp
        val ListJointRadius: Dp = 6.dp

        fun listItemShape(index: Int, count: Int): Shape = when {
            count <= 1 -> RoundedCornerShape(ListCapRadius)

            index == 0 -> RoundedCornerShape(
                topStart = ListCapRadius,
                topEnd = ListCapRadius,
                bottomStart = ListJointRadius,
                bottomEnd = ListJointRadius,
            )

            index == count - 1 -> RoundedCornerShape(
                topStart = ListJointRadius,
                topEnd = ListJointRadius,
                bottomStart = ListCapRadius,
                bottomEnd = ListCapRadius,
            )

            else -> RoundedCornerShape(ListJointRadius)
        }
    }
}

val LocalAppShapes = staticCompositionLocalOf { AppShapes() }

// Legacy alias / backward-compatibility helper
object AppShape {
    val extraSmallRadius = AppShapes.ChipRadius
    val smallRadius = AppShapes.InputRadius
    val mediumRadius = AppShapes.CardRadius
    val largeRadius = AppShapes.DialogRadius
    val extraLargeRadius = 32.dp
    val pillRadius = AppShapes.PillRadius

    val chip = RoundedCornerShape(AppShapes.ChipRadius)
    val swatch = RoundedCornerShape(AppShapes.SwatchRadius)
    val input = RoundedCornerShape(AppShapes.InputRadius)
    val card = RoundedCornerShape(AppShapes.CardRadius)
    val dialog = RoundedCornerShape(AppShapes.DialogRadius)
    val pill = RoundedCornerShape(AppShapes.PillRadius)

    val extraSmall: Shape = chip
    val small: Shape = input
    val medium: Shape = card
    val large: Shape = dialog
    val extraLarge: Shape = RoundedCornerShape(extraLargeRadius)

    val button: Shape = small
    val cardInner: Shape = chip
    val sheet: Shape = RoundedCornerShape(
        topStart = AppShapes.DialogRadius,
        topEnd = AppShapes.DialogRadius,
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
    )

    val listFull: Shape = RoundedCornerShape(AppShapes.ListCapRadius)
    val listMiddle: Shape = RoundedCornerShape(AppShapes.ListJointRadius)
    val listTop: Shape = RoundedCornerShape(
        topStart = AppShapes.ListCapRadius,
        topEnd = AppShapes.ListCapRadius,
        bottomStart = AppShapes.ListJointRadius,
        bottomEnd = AppShapes.ListJointRadius,
    )
    val listBottom: Shape = RoundedCornerShape(
        topStart = AppShapes.ListJointRadius,
        topEnd = AppShapes.ListJointRadius,
        bottomStart = AppShapes.ListCapRadius,
        bottomEnd = AppShapes.ListCapRadius,
    )

    fun listItemShape(index: Int, total: Int): Shape = AppShapes.listItemShape(index, total)
    fun calculateListShape(index: Int, size: Int): Shape = listItemShape(index = index, total = size)
}

fun appMaterialShapes(): Shapes = Shapes(
    extraSmall = AppShape.extraSmall as CornerBasedShape,
    small = AppShape.small as CornerBasedShape,
    medium = AppShape.medium as CornerBasedShape,
    large = AppShape.large as CornerBasedShape,
    extraLarge = AppShape.extraLarge as CornerBasedShape,
)
