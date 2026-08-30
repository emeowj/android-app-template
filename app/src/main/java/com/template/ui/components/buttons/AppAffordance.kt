package com.template.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.appFocusRing
import com.template.ui.theme.floatingPillShadow

enum class AffordanceStyle {
    Tinted,
    OnScrim,
}

object AppAffordanceDefaults {
    val Height: Dp = 32.dp
    val OverlayHeight: Dp = 36.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val IconSize: Dp = 16.dp

    @Composable
    fun containerColor(style: AffordanceStyle, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when (style) {
            AffordanceStyle.Tinted -> if (enabled) colors.surface else colors.surface.copy(alpha = 0.5f)
            AffordanceStyle.OnScrim -> if (enabled) colors.ink.copy(alpha = 0.50f) else colors.ink.copy(alpha = 0.25f)
        }
    }

    @Composable
    fun contentColor(style: AffordanceStyle, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when (style) {
            AffordanceStyle.Tinted -> if (enabled) colors.accent else colors.inkMuted
            AffordanceStyle.OnScrim -> if (enabled) colors.surfaceFixed else colors.surfaceFixed.copy(alpha = 0.5f)
        }
    }
}

@Composable
fun AppAffordance(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    style: AffordanceStyle = AffordanceStyle.Tinted,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = AppAffordanceDefaults.Shape,
) {
    val containerColor = AppAffordanceDefaults.containerColor(style = style, enabled = enabled)
    val contentColor = AppAffordanceDefaults.contentColor(style = style, enabled = enabled)
    val colors = AppTheme.colors

    val border = when (style) {
        AffordanceStyle.Tinted -> BorderStroke(1.dp, if (enabled) colors.border else colors.hairline)
        AffordanceStyle.OnScrim -> BorderStroke(1.dp, colors.hairline)
    }

    val shadowModifier = if (style == AffordanceStyle.OnScrim) {
        Modifier.floatingPillShadow(shape = shape)
    } else {
        Modifier
    }

    val height = when (style) {
        AffordanceStyle.Tinted -> AppAffordanceDefaults.Height
        AffordanceStyle.OnScrim -> if (label != null) AppAffordanceDefaults.OverlayHeight else AppAffordanceDefaults.Height
    }

    val horizontalPadding = if (label != null) 10.dp else 0.dp
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        interactionSource = interactionSource,
        modifier = modifier
            .then(shadowModifier)
            .then(
                if (label == null) {
                    Modifier.size(height)
                } else {
                    Modifier.height(height).defaultMinSize(minWidth = height)
                },
            )
            .appFocusRing(visible = false, shape = shape, ringColor = colors.accent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            if (label != null) {
                Text(
                    text = label,
                    style = AppTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                )
            }
            trailingIcon?.invoke()
        }
    }
}

@ThemePreviews
@Composable
private fun AppAffordancePreview() {
    AppPreview {
        Row(
            modifier = Modifier.padding(Padding.md),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // chip-add
            AppAffordance(
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Add",
                        modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                    )
                },
            )

            // see-all
            AppAffordance(
                label = "See all",
                onClick = {},
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                    )
                },
            )

            // reroll-all
            AppAffordance(
                label = "Reroll all",
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_auto_awesome),
                        contentDescription = null,
                        modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                    )
                },
            )

            // more on scrim
            AppAffordance(
                onClick = {},
                style = AffordanceStyle.OnScrim,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = "Share",
                        modifier = Modifier.size(AppAffordanceDefaults.IconSize),
                    )
                },
            )
        }
    }
}
