package com.template.ui.components.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing

@Immutable
data class AppSwitchColors(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,
    val disabledCheckedTrackColor: Color,
    val disabledUncheckedTrackColor: Color,
    val disabledThumbColor: Color,
)

object AppSwitchDefaults {
    val TrackWidth: Dp = 48.dp
    val TrackHeight: Dp = 28.dp
    val ThumbSize: Dp = 22.dp
    val ThumbInset: Dp = 3.dp
    val ThumbTravelDistance: Dp = 20.dp
    val TrackShape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val ThumbShape: Shape = CircleShape

    @Composable
    fun colors(): AppSwitchColors {
        val colors = AppTheme.colors
        return AppSwitchColors(
            checkedTrackColor = colors.accent,
            uncheckedTrackColor = colors.ink14,
            checkedThumbColor = colors.surface,
            uncheckedThumbColor = colors.surface,
            disabledCheckedTrackColor = colors.accent.copy(alpha = 0.44f),
            disabledUncheckedTrackColor = colors.ink14.copy(alpha = 0.44f),
            disabledThumbColor = colors.surface.copy(alpha = 0.72f),
        )
    }
}

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: AppSwitchColors = AppSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.disabledCheckedTrackColor
            !enabled && !checked -> colors.disabledUncheckedTrackColor
            checked -> colors.checkedTrackColor
            else -> colors.uncheckedTrackColor
        },
        label = "SwitchTrackColor",
    )

    val thumbColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledThumbColor
            checked -> colors.checkedThumbColor
            else -> colors.uncheckedThumbColor
        },
        label = "SwitchThumbColor",
    )

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) {
            AppSwitchDefaults.ThumbInset + AppSwitchDefaults.ThumbTravelDistance
        } else {
            AppSwitchDefaults.ThumbInset
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "SwitchThumbOffset",
    )

    val toggleableModifier = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            onValueChange = onCheckedChange,
            enabled = enabled,
            role = Role.Switch,
            interactionSource = interactionSource,
            indication = null,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(width = AppSwitchDefaults.TrackWidth, height = AppSwitchDefaults.TrackHeight)
            .appFocusRing(visible = false, shape = AppSwitchDefaults.TrackShape, ringColor = AppTheme.colors.accent)
            .clip(AppSwitchDefaults.TrackShape)
            .background(color = trackColor)
            .then(toggleableModifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(AppSwitchDefaults.ThumbSize)
                .shadow(
                    elevation = 2.dp,
                    shape = AppSwitchDefaults.ThumbShape,
                    clip = false,
                )
                .background(
                    color = thumbColor,
                    shape = AppSwitchDefaults.ThumbShape,
                ),
        )
    }
}

@ThemePreviews
@Composable
private fun AppSwitchPreview() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }

    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = checked1, onCheckedChange = { checked1 = it })
                Text(text = if (checked1) "Enabled (Checked)" else "Disabled (Unchecked)")
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = checked2, onCheckedChange = { checked2 = it })
                Text(text = if (checked2) "Enabled (Checked)" else "Disabled (Unchecked)")
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = true, onCheckedChange = null, enabled = false)
                Text(text = "Disabled Checked")
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = false, onCheckedChange = null, enabled = false)
                Text(text = "Disabled Unchecked")
            }
        }
    }
}
