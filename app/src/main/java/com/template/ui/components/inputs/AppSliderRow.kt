package com.template.ui.components.inputs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing

@Immutable
data class AppSliderColors(
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val thumbColor: Color,
    val thumbBorderColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledInactiveTrackColor: Color,
    val disabledThumbColor: Color,
    val disabledThumbBorderColor: Color,
)

object AppSliderDefaults {
    val TrackHeight: Dp = 4.dp
    val ThumbSize: Dp = 22.dp
    val ThumbStrokeWidth: Dp = 1.5.dp
    val ThumbShape: Shape = CircleShape
    val TrackShape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val ThumbHoverScale: Float = 1.08f

    @Composable
    fun colors(): AppSliderColors {
        val colors = AppTheme.colors
        return AppSliderColors(
            activeTrackColor = colors.ink,
            inactiveTrackColor = colors.ink14,
            thumbColor = colors.surface,
            thumbBorderColor = colors.ink,
            disabledActiveTrackColor = colors.ink.copy(alpha = 0.38f),
            disabledInactiveTrackColor = colors.ink14.copy(alpha = 0.38f),
            disabledThumbColor = colors.surface.copy(alpha = 0.72f),
            disabledThumbBorderColor = colors.ink.copy(alpha = 0.38f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val SliderState.fraction: Float
    get() = if (valueRange.endInclusive > valueRange.start) {
        ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
    } else {
        0f
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    colors: AppSliderColors = AppSliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val thumbScale by animateFloatAsState(
        targetValue = if (enabled && (isHovered || isPressed)) AppSliderDefaults.ThumbHoverScale else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "SliderThumbScale",
    )

    val activeTrack = if (enabled) colors.activeTrackColor else colors.disabledActiveTrackColor
    val inactiveTrack = if (enabled) colors.inactiveTrackColor else colors.disabledInactiveTrackColor
    val thumbBg = if (enabled) colors.thumbColor else colors.disabledThumbColor
    val thumbBorder = if (enabled) colors.thumbBorderColor else colors.disabledThumbBorderColor

    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(24.dp)
            .appFocusRing(visible = false, shape = AppSliderDefaults.TrackShape, ringColor = AppTheme.colors.accent),
        valueRange = valueRange,
        steps = steps,
        enabled = enabled,
        interactionSource = interactionSource,
        thumb = {
            Box(
                modifier = Modifier
                    .size(AppSliderDefaults.ThumbSize)
                    .scale(thumbScale)
                    .shadow(
                        elevation = 2.dp,
                        shape = AppSliderDefaults.ThumbShape,
                        clip = false,
                    )
                    .background(color = thumbBg, shape = AppSliderDefaults.ThumbShape)
                    .border(
                        width = AppSliderDefaults.ThumbStrokeWidth,
                        color = thumbBorder,
                        shape = AppSliderDefaults.ThumbShape,
                    ),
            )
        },
        track = { sliderState ->
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSliderDefaults.TrackHeight)
                    .clip(AppSliderDefaults.TrackShape),
            ) {
                val fraction = sliderState.fraction
                val heightPx = size.height
                val cornerRadius = CornerRadius(heightPx / 2f, heightPx / 2f)

                // Inactive track (full background)
                drawRoundRect(
                    color = inactiveTrack,
                    size = size,
                    cornerRadius = cornerRadius,
                )

                // Active track (from left to thumb fraction)
                if (fraction > 0f) {
                    val activeWidth = size.width * fraction
                    drawRoundRect(
                        color = activeTrack,
                        topLeft = Offset.Zero,
                        size = Size(width = activeWidth, height = heightPx),
                        cornerRadius = cornerRadius,
                    )
                }
            }
        },
        colors = SliderDefaults.colors(
            thumbColor = thumbBg,
            activeTrackColor = activeTrack,
            inactiveTrackColor = inactiveTrack,
            disabledThumbColor = colors.disabledThumbColor,
            disabledActiveTrackColor = colors.disabledActiveTrackColor,
            disabledInactiveTrackColor = colors.disabledInactiveTrackColor,
        ),
    )
}

@Composable
fun AppSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    valueFormatter: (Float) -> String = { "${(it * 100).toInt()}%" },
    note: String? = null,
    enabled: Boolean = true,
    colors: AppSliderColors = AppSliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val themeColors = AppTheme.colors
    val typography = AppTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = typography.bodyMd,
                fontWeight = FontWeight.Medium,
                color = if (enabled) themeColors.ink else themeColors.inkMuted,
            )
            Text(
                text = valueFormatter(value),
                style = typography.numeric,
                color = themeColors.inkMuted,
            )
        }

        AppSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
        )

        if (note != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note,
                style = typography.caption,
                color = themeColors.inkMuted,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppSliderRowPreview() {
    var blurValue by remember { mutableFloatStateOf(0.40f) }
    var opacityValue by remember { mutableFloatStateOf(0.75f) }

    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AppSliderRow(
                label = "Blur",
                value = blurValue,
                onValueChange = { blurValue = it },
                note = "Softens edges between gradient stops.",
            )

            AppSliderRow(
                label = "Opacity",
                value = opacityValue,
                onValueChange = { opacityValue = it },
            )

            AppSliderRow(
                label = "Disabled Slider",
                value = 0.5f,
                onValueChange = {},
                enabled = false,
                note = "This control cannot be adjusted.",
            )
        }
    }
}
