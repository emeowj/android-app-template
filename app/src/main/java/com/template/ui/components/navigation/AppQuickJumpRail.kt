package com.template.ui.components.navigation

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.template.ui.haptic.HapticFeedbackManager
import com.template.ui.haptic.HapticFeedbackType
import com.template.ui.haptic.LocalHapticFeedbackManager
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.overlaySurfaceShadow
import com.template.ui.theme.railShadow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Immutable
sealed interface QuickJumpItem {
    val key: String
    val label: String

    @Immutable
    data class Letter(val letter: String, override val label: String = letter, val id: String = letter) : QuickJumpItem {
        override val key: String get() = id
    }

    @Immutable
    data class Swatch(val color: Color, override val label: String, val id: String = label) : QuickJumpItem {
        override val key: String get() = id
    }

    @Immutable
    data class LinearGradientSwatch(
        val colors: List<Color>,
        override val label: String,
        val id: String = label,
    ) : QuickJumpItem {
        override val key: String get() = id
    }

    @Immutable
    data class PatternGlyphItem(
        override val label: String,
        val id: String = label,
    ) : QuickJumpItem {
        override val key: String get() = id
    }

    @Immutable
    data class IconItem(@DrawableRes val iconRes: Int, override val label: String, val id: String = label) : QuickJumpItem {
        override val key: String get() = id
    }
}

object AppQuickJumpRailDefaults {
    val ItemSize: Dp = 20.dp
    val ItemGap: Dp = 7.dp
    val PaddingVertical: Dp = 8.dp
    val PaddingHorizontal: Dp = 5.dp
    val BubbleOffset: Dp = 40.dp
    val RestingAlpha: Float = 0.34f
    val ActiveAlpha: Float = 1.0f
    val HairlineWidth: Dp = 1.dp
    val ActiveScale: Float = 1.28f
    const val ReleaseHoldMillis: Long = 550L
}

/**
 * Pure calculation mapping a vertical Y touch coordinate within the rail to a valid item index.
 */
internal fun calculateQuickJumpIndex(y: Float, railHeightPx: Int, itemCount: Int): Int {
    if (itemCount <= 0 || railHeightPx <= 0) return 0
    val fraction = (y / railHeightPx).coerceIn(0f, 0.999f)
    return (fraction * itemCount).toInt().coerceIn(0, itemCount - 1)
}

@Composable
fun AppQuickJumpRail(
    items: List<QuickJumpItem>,
    selectedIndex: Int,
    onItemSelected: (Int, QuickJumpItem) -> Unit,
    modifier: Modifier = Modifier,
    activeItemScale: Float = AppQuickJumpRailDefaults.ActiveScale,
    forceDragging: Boolean = false,
    hapticFeedbackManager: HapticFeedbackManager = LocalHapticFeedbackManager.current,
) {
    if (items.isEmpty()) return

    val colors = AppTheme.colors
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }
    var releaseJob by remember { mutableStateOf<Job?>(null) }
    var railHeightPx by remember { mutableIntStateOf(1) }
    var internalSelectedIndex by remember(selectedIndex) { mutableIntStateOf(selectedIndex.coerceIn(0, items.size - 1)) }

    val isEffectivelyDragging = forceDragging || isDragging

    val railAlpha by animateFloatAsState(
        targetValue = if (isEffectivelyDragging) AppQuickJumpRailDefaults.ActiveAlpha else AppQuickJumpRailDefaults.RestingAlpha,
        label = "AppQuickJumpRailAlpha",
    )

    fun startDragging(index: Int) {
        releaseJob?.cancel()
        isDragging = true
        internalSelectedIndex = index
        hapticFeedbackManager.performHaptic(HapticFeedbackType.Selection)
        onItemSelected(index, items[index])
    }

    fun endDragging() {
        hapticFeedbackManager.performHaptic(HapticFeedbackType.Action)
        releaseJob?.cancel()
        releaseJob = coroutineScope.launch {
            delay(AppQuickJumpRailDefaults.ReleaseHoldMillis)
            isDragging = false
        }
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .clearAndSetSemantics {}
            .onGloballyPositioned { coordinates ->
                railHeightPx = coordinates.size.height
            }
            .pointerInput(items) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val initialIndex = calculateQuickJumpIndex(down.position.y, railHeightPx, items.size)
                    startDragging(initialIndex)

                    try {
                        drag(down.id) { change ->
                            change.consume()
                            val index = calculateQuickJumpIndex(change.position.y, railHeightPx, items.size)
                            if (index != internalSelectedIndex) {
                                internalSelectedIndex = index
                                hapticFeedbackManager.performHaptic(HapticFeedbackType.Selection)
                                onItemSelected(index, items[index])
                            }
                        }
                    } finally {
                        endDragging()
                    }
                }
            },
        contentAlignment = Alignment.CenterEnd,
    ) {
        if (isEffectivelyDragging && internalSelectedIndex in items.indices) {
            val activeItem = items[internalSelectedIndex]
            val bubbleOffsetX = with(density) { -AppQuickJumpRailDefaults.BubbleOffset.roundToPx() }

            QuickJumpBubble(
                label = activeItem.label,
                bubbleOffsetX = bubbleOffsetX,
                bubbleOffsetY = {
                    val itemHeightApprox = if (items.isNotEmpty()) railHeightPx.toFloat() / items.size else 0f
                    ((internalSelectedIndex + 0.5f) * itemHeightApprox - (railHeightPx / 2f)).toInt()
                },
            )
        }

        Column(
            modifier = Modifier
                .graphicsLayer { alpha = railAlpha }
                .railShadow(shape = CircleShape)
                .background(color = colors.surface, shape = CircleShape)
                .border(
                    width = AppQuickJumpRailDefaults.HairlineWidth,
                    color = colors.hairline,
                    shape = CircleShape,
                )
                .padding(
                    horizontal = AppQuickJumpRailDefaults.PaddingHorizontal,
                    vertical = AppQuickJumpRailDefaults.PaddingVertical,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppQuickJumpRailDefaults.ItemGap),
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == internalSelectedIndex
                QuickJumpRailItem(
                    item = item,
                    isSelected = isSelected,
                    isDragging = isEffectivelyDragging,
                    activeItemScale = activeItemScale,
                )
            }
        }
    }
}

@Composable
private fun QuickJumpBubble(
    label: String,
    bubbleOffsetX: Int,
    bubbleOffsetY: () -> Int,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Box(
        modifier = modifier
            .offset { IntOffset(x = bubbleOffsetX, y = bubbleOffsetY()) }
            .overlaySurfaceShadow(shape = RoundedCornerShape(AppShapes.ChipRadius))
            .background(colors.ink, shape = RoundedCornerShape(AppShapes.ChipRadius))
            .padding(horizontal = 13.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = typography.caption,
            color = colors.background,
            maxLines = 1,
        )
    }
}

@Composable
private fun QuickJumpRailItem(
    item: QuickJumpItem,
    isSelected: Boolean,
    isDragging: Boolean,
    activeItemScale: Float,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val scale = if (isSelected && isDragging) activeItemScale else 1.0f
    val contentColor = if (isSelected && isDragging) colors.ink else colors.inkSoft

    Box(
        modifier = modifier
            .size(AppQuickJumpRailDefaults.ItemSize)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center,
    ) {
        when (item) {
            is QuickJumpItem.Letter -> {
                Text(
                    text = item.letter,
                    style = typography.numeric,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                )
            }

            is QuickJumpItem.Swatch -> {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(item.color)
                        .border(
                            width = 0.5.dp,
                            color = if (isSelected) colors.ink else colors.hairline,
                            shape = CircleShape,
                        ),
                )
            }

            is QuickJumpItem.LinearGradientSwatch -> {
                val gradientBrush = remember(item.colors) {
                    val safeColors = if (item.colors.size >= 2) {
                        item.colors
                    } else if (item.colors.size == 1) {
                        listOf(item.colors[0], item.colors[0])
                    } else {
                        listOf(Color(0xFFE0533C), Color(0xFFE8B931), Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFF9C27B0))
                    }
                    Brush.linearGradient(
                        colors = safeColors,
                        start = Offset.Zero,
                        end = Offset.Infinite,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(gradientBrush)
                        .border(
                            width = 0.5.dp,
                            color = if (isSelected) colors.ink else colors.hairline,
                            shape = CircleShape,
                        ),
                )
            }

            is QuickJumpItem.PatternGlyphItem -> {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(
                            width = 0.5.dp,
                            color = if (isSelected) colors.ink else colors.hairline,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val half = size.width / 2f
                        drawRect(
                            color = contentColor,
                            topLeft = Offset(0f, 0f),
                            size = Size(half, half),
                        )
                        drawRect(
                            color = contentColor,
                            topLeft = Offset(half, half),
                            size = Size(half, half),
                        )
                    }
                }
            }

            is QuickJumpItem.IconItem -> {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.label,
                    tint = contentColor,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppQuickJumpRailLettersPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            val letters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M").map {
                QuickJumpItem.Letter(it)
            }
            AppQuickJumpRail(
                items = letters,
                selectedIndex = 2,
                onItemSelected = { _, _ -> },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppQuickJumpRailSwatchesPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            val swatches = listOf(
                QuickJumpItem.Swatch(Color(0xFFE57373), "Red"),
                QuickJumpItem.Swatch(Color(0xFFFFB74D), "Orange"),
                QuickJumpItem.Swatch(Color(0xFF81C784), "Green"),
                QuickJumpItem.Swatch(Color(0xFF64B5F6), "Blue"),
                QuickJumpItem.LinearGradientSwatch(
                    listOf(Color(0xFFE0533C), Color(0xFFE8B931), Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFF9C27B0)),
                    "Multicolour",
                ),
                QuickJumpItem.PatternGlyphItem("Pattern"),
            )
            AppQuickJumpRail(
                items = swatches,
                selectedIndex = 1,
                onItemSelected = { _, _ -> },
            )
        }
    }
}
