package com.template.ui.components.color

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.roundToInt

/**
 * Calculates anchored positioning for [AppColorPopover], ensuring it floats near the anchor,
 * auto-flips above/below when screen space is tight, and clamps within screen margins.
 */
class ColorPopoverPositionProvider(
    private val density: Density,
    private val margin: Dp = 10.dp,
    private val verticalGap: Dp = 6.dp,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val marginPx = with(density) { margin.toPx() }
        val gapPx = with(density) { verticalGap.toPx() }

        // Horizontal positioning: center on anchor, clamped to window margins
        val anchorCenterX = anchorBounds.left + (anchorBounds.width / 2f)
        val desiredX = (anchorCenterX - (popupContentSize.width / 2f)).roundToInt()
        val minX = marginPx.roundToInt()
        val maxX = (windowSize.width - popupContentSize.width - marginPx).roundToInt().coerceAtLeast(minX)
        val clampedX = desiredX.coerceIn(minX, maxX)

        // Vertical positioning: prefer below anchor; flip above if not enough space below
        val spaceBelow = windowSize.height - anchorBounds.bottom - marginPx
        val spaceAbove = anchorBounds.top - marginPx
        val neededHeight = popupContentSize.height + gapPx

        val openBelow = spaceBelow >= neededHeight || spaceBelow >= spaceAbove

        val y = if (openBelow) {
            (anchorBounds.bottom + gapPx).roundToInt()
        } else {
            (anchorBounds.top - popupContentSize.height - gapPx).roundToInt()
        }

        val minY = marginPx.roundToInt()
        val maxY = (windowSize.height - popupContentSize.height - marginPx).roundToInt().coerceAtLeast(minY)
        val clampedY = y.coerceIn(minY, maxY)

        return IntOffset(clampedX, clampedY)
    }
}
