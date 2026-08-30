package com.template.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.plateShadow(
    shape: Shape = RoundedCornerShape(AppShapes.CardRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 2.dp, shape = shape, clip = clip)
    .shadow(elevation = 6.dp, shape = shape, clip = clip)

fun Modifier.plateShadowHover(
    shape: Shape = RoundedCornerShape(AppShapes.CardRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 4.dp, shape = shape, clip = clip)
    .shadow(elevation = 10.dp, shape = shape, clip = clip)

fun Modifier.floatingPillShadow(
    shape: Shape = RoundedCornerShape(AppShapes.PillRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 6.dp, shape = shape, clip = clip)

fun Modifier.overlaySurfaceShadow(
    shape: Shape = RoundedCornerShape(AppShapes.PillRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 8.dp, shape = shape, clip = clip)

fun Modifier.sheetShadow(
    shape: Shape = RoundedCornerShape(
        topStart = AppShapes.DialogRadius,
        topEnd = AppShapes.DialogRadius,
    ),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 12.dp, shape = shape, clip = clip)

fun Modifier.panelShadow(
    shape: Shape = RoundedCornerShape(
        topStart = AppShapes.DialogRadius,
        topEnd = AppShapes.DialogRadius,
    ),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 14.dp, shape = shape, clip = clip)

fun Modifier.dialogShadow(
    shape: Shape = RoundedCornerShape(AppShapes.DialogRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 16.dp, shape = shape, clip = clip)

fun Modifier.railShadow(
    shape: Shape = RoundedCornerShape(AppShapes.PillRadius),
    clip: Boolean = false,
): Modifier = this
    .shadow(elevation = 6.dp, shape = shape, clip = clip)

fun Modifier.appFocusRing(
    visible: Boolean,
    shape: Shape = RoundedCornerShape(AppShapes.InputRadius),
    ringColor: Color = Color(0xFF3E5C76),
    ringWidth: Dp = 2.dp,
): Modifier = if (visible) {
    this.border(width = ringWidth, color = ringColor, shape = shape)
} else {
    this
}
