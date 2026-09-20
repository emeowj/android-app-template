package com.template.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing

object AppChipDefaults {
    val MinHeight: Dp = 32.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.ChipRadius)
    val BorderWidth: Dp = 0.5.dp
    val HorizontalPadding: Dp = 12.dp
    val IconSize: Dp = 13.dp
    val ItemSpacing: Dp = 6.dp

    @Composable
    fun contentColor(selected: Boolean, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when {
            !enabled -> colors.inkMuted.copy(alpha = 0.44f)
            selected -> colors.accent
            else -> colors.inkMuted
        }
    }
}

@Composable
fun AppChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentColor: Color? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    count: Int? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = AppChipDefaults.Shape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = enabled
        it.isSelected = selected
    }
    val baseStyle = if (selected) AppTheme.styles.chip.selected else AppTheme.styles.chip.filter
    val resolvedContentColor = contentColor ?: AppChipDefaults.contentColor(selected = selected, enabled = enabled)

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = AppChipDefaults.MinHeight)
            .styleable(styleState, baseStyle, style)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                onClick = onClick,
            )
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides resolvedContentColor) {
            Row(
                modifier = Modifier.padding(horizontal = AppChipDefaults.HorizontalPadding, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(AppChipDefaults.ItemSpacing, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                Text(
                    text = label,
                    style = AppTheme.typography.bodySm,
                    fontWeight = FontWeight.Medium,
                    color = LocalContentColor.current,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                count?.let {
                    Text(
                        text = it.toString(),
                        style = AppTheme.typography.numeric,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalContentColor.current.copy(alpha = 0.70f),
                        maxLines = 1,
                    )
                }
                trailingIcon?.invoke()
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppChipPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppChip(
                    label = "All Wallpapers",
                    onClick = {},
                    selected = true,
                    count = 42,
                )
                AppChip(
                    label = "Favorites",
                    onClick = {},
                    selected = false,
                    count = 12,
                )
                AppChip(
                    label = "Generated",
                    onClick = {},
                    selected = false,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppChip(
                    label = "With Icon",
                    onClick = {},
                    selected = false,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            modifier = Modifier.size(AppChipDefaults.IconSize),
                        )
                    },
                )
                AppChip(
                    label = "Selected With Icon",
                    onClick = {},
                    selected = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            modifier = Modifier.size(AppChipDefaults.IconSize),
                        )
                    },
                    count = 5,
                )
                AppChip(
                    label = "Disabled",
                    onClick = {},
                    enabled = false,
                    count = 0,
                )
            }
        }
    }
}
