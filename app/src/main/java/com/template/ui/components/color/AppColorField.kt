package com.template.ui.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.appFocusRing

object AppColorFieldDefaults {
    val SwatchSize: Dp = 26.dp
    val ChevronSize: Dp = 13.dp
    val InternalGap: Dp = 10.dp
    val VerticalPadding: Dp = 14.dp
    val InsetHairlineWidth: Dp = 1.dp
    const val HoverPressScale: Float = 1.08f
}

/**
 * AppColorField is a collapsed trigger row that displays a circular color swatch,
 * a two-line label (field name over current hex string), and an animated chevron.
 * Tapping this row toggles the shared color popover.
 *
 * @param label The descriptive name of the color field (e.g. "Symbol colour 1", "Field colour").
 * @param color The current [Color] value.
 * @param onClick Called when the row is tapped.
 * @param expanded Whether the associated color popover is currently open.
 * @param modifier Modifier applied to the row button.
 * @param enabled Whether the control is interactive.
 * @param showTopDivider Whether to render a 1px hairline border at the top of this row.
 */
@Composable
fun AppColorField(
    label: String,
    color: Color,
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showTopDivider: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val swatchScale by animateFloatAsState(
        targetValue = if (enabled && (isHovered || isPressed)) AppColorFieldDefaults.HoverPressScale else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "SwatchScale",
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 140),
        label = "ChevronRotation",
    )

    val colors = AppTheme.colors
    val chevronColor by animateColorAsState(
        targetValue = if (expanded) colors.accent else colors.inkMuted,
        animationSpec = tween(durationMillis = 140),
        label = "ChevronColor",
    )

    val hexString = remember(color) { color.toHex() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (showTopDivider) {
                    val hairlinePx = AppColorFieldDefaults.InsetHairlineWidth.toPx()
                    drawLine(
                        color = colors.hairline,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = hairlinePx,
                    )
                }
            }
            .appFocusRing(
                visible = false,
                shape = RoundedCornerShape(AppShapes.InputRadius),
                ringColor = colors.accent,
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .semantics {
                role = Role.Button
                selected = expanded
            }
            .padding(
                horizontal = Padding.medium,
                vertical = AppColorFieldDefaults.VerticalPadding,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppColorFieldDefaults.InternalGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppColorFieldDefaults.SwatchSize)
                .scale(swatchScale)
                .drawWithContent {
                    drawContent()
                    val strokePx = AppColorFieldDefaults.InsetHairlineWidth.toPx()
                    drawCircle(
                        color = colors.hairline,
                        radius = (size.minDimension - strokePx) / 2f,
                        style = Stroke(width = strokePx),
                    )
                }
                .clip(CircleShape)
                .background(color = color),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Padding.xxs),
        ) {
            Text(
                text = label,
                style = AppTheme.typography.bodyMd,
                color = colors.ink,
            )
            Text(
                text = hexString,
                style = AppTheme.typography.numeric,
                color = colors.inkMuted,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = chevronColor,
            modifier = Modifier
                .size(AppColorFieldDefaults.ChevronSize)
                .rotate(chevronRotation),
        )
    }
}

/**
 * Convenient overload of [AppColorField] accepting a hex string.
 */
@Composable
fun AppColorField(
    label: String,
    hex: String,
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showTopDivider: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val resolvedColor = remember(hex) {
        ColorUtils.parseColorHex(hex, fallback = Color.Black)
    }
    AppColorField(
        label = label,
        color = resolvedColor,
        onClick = onClick,
        expanded = expanded,
        modifier = modifier,
        enabled = enabled,
        showTopDivider = showTopDivider,
        interactionSource = interactionSource,
    )
}

@ThemePreviews
@Composable
private fun AppColorFieldPreview() {
    var expanded by remember { mutableStateOf(false) }
    var expandedSecond by remember { mutableStateOf(true) }

    AppPreview {
        Column(
            modifier = Modifier
                .background(AppTheme.colors.surface)
                .padding(Padding.medium),
        ) {
            AppColorField(
                label = "Symbol colour 1",
                color = Color(0xFFD97757),
                onClick = { expanded = !expanded },
                expanded = expanded,
                showTopDivider = false,
            )
            AppColorField(
                label = "Symbol colour 2",
                color = Color(0xFF5FA8D3),
                onClick = { expandedSecond = !expandedSecond },
                expanded = expandedSecond,
                showTopDivider = true,
            )
            AppColorField(
                label = "Field colour",
                color = Color(0xFF13141A),
                onClick = {},
                expanded = false,
                showTopDivider = true,
            )
        }
    }
}
