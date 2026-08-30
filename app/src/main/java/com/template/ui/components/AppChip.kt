package com.template.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

@Immutable
data class AppChipColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val selectedContainer: Color,
    val selectedContent: Color,
    val selectedBorder: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledBorder: Color,
)

object AppChipDefaults {
    val MinHeight: Dp = 32.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.ChipRadius)
    val BorderWidth: Dp = 0.5.dp
    val HorizontalPadding: Dp = 12.dp
    val IconSize: Dp = 13.dp
    val ItemSpacing: Dp = 6.dp

    @Composable
    fun colors(): AppChipColors {
        val colors = AppTheme.colors
        return AppChipColors(
            container = colors.surface,
            content = colors.inkMuted,
            border = colors.border,
            selectedContainer = colors.accent12,
            selectedContent = colors.accent,
            selectedBorder = colors.accent,
            disabledContainer = colors.surface,
            disabledContent = colors.inkMuted.copy(alpha = 0.44f),
            disabledBorder = colors.hairline,
        )
    }
}

@Composable
fun AppChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    count: Int? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = AppChipDefaults.Shape,
    colors: AppChipColors = AppChipDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val containerColor = when {
        !enabled -> colors.disabledContainer
        selected -> colors.selectedContainer
        else -> colors.container
    }

    val contentColor = when {
        !enabled -> colors.disabledContent
        selected -> colors.selectedContent
        else -> colors.content
    }

    val borderColor = when {
        !enabled -> colors.disabledBorder
        selected -> colors.selectedBorder
        else -> colors.border
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = BorderStroke(AppChipDefaults.BorderWidth, borderColor),
        interactionSource = interactionSource,
        modifier = modifier
            .defaultMinSize(minHeight = AppChipDefaults.MinHeight)
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
    ) {
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
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            count?.let {
                Text(
                    text = it.toString(),
                    style = AppTheme.typography.numeric,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor.copy(alpha = 0.70f),
                    maxLines = 1,
                )
            }
            trailingIcon?.invoke()
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
