package com.template.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.inputs.AppSwitch
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.appFocusRing

enum class AppListRowSurface {
    GroupedCard,
    FlatSheet,
    FlatSettings,
}

sealed interface AppListRowTrailing {
    data class Value(val text: String) : AppListRowTrailing
    data object Checkmark : AppListRowTrailing
    data class Switch(val checked: Boolean, val onCheckedChange: ((Boolean) -> Unit)? = null) : AppListRowTrailing
    data object Chevron : AppListRowTrailing
    data class Custom(val content: @Composable () -> Unit) : AppListRowTrailing
}

object AppListRowDefaults {
    val MinHeight: Dp = 52.dp
    val IconWellSize: Dp = 36.dp
    val IconSize: Dp = 20.dp
    val ItemGap: Dp = 14.dp
    val VerticalPadding: Dp = 14.dp
    val HairlineWidth: Dp = 1.dp
    val CheckmarkSize: Dp = 20.dp
    val ChevronSize: Dp = 16.dp
}

@Composable
fun AppListRow(
    title: String,
    modifier: Modifier = Modifier,
    note: String? = null,
    @DrawableRes iconRes: Int? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    surface: AppListRowSurface = AppListRowSurface.GroupedCard,
    index: Int = 0,
    count: Int = 1,
    showTopHairline: Boolean = false,
    trailing: AppListRowTrailing? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    danger: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val density = LocalDensity.current
    val densityTokens = LocalAppDensity.current

    val rowShape: Shape = when (surface) {
        AppListRowSurface.GroupedCard -> AppShapes.listItemShape(index = index, count = count)
        AppListRowSurface.FlatSheet -> RectangleShape
        AppListRowSurface.FlatSettings -> RectangleShape
    }

    val containerColor = when (surface) {
        AppListRowSurface.GroupedCard -> colors.surface
        AppListRowSurface.FlatSheet -> colors.surface
        AppListRowSurface.FlatSettings -> Color.Transparent
    }

    val iconWellColor = when (surface) {
        AppListRowSurface.GroupedCard -> colors.background
        AppListRowSurface.FlatSheet -> colors.background
        AppListRowSurface.FlatSettings -> colors.surface
    }

    val titleColor = if (danger) colors.danger else colors.ink
    val iconTint = if (danger) colors.danger else colors.ink
    val noteColor = colors.inkMuted
    val rippleColor = if (danger) colors.danger else colors.ink

    val showBottomHairline = (surface == AppListRowSurface.FlatSheet || surface == AppListRowSurface.FlatSettings) && (index < count - 1 || count <= 1)

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(color = rippleColor),
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(containerColor)
            .then(clickableModifier)
            .appFocusRing(visible = false, shape = rowShape)
            .drawWithContent {
                drawContent()
                if (showTopHairline) {
                    val strokeWidthPx = with(density) { AppListRowDefaults.HairlineWidth.toPx() }
                    drawLine(
                        color = colors.hairline,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidthPx,
                    )
                }
                if (showBottomHairline) {
                    val strokeWidthPx = with(density) { AppListRowDefaults.HairlineWidth.toPx() }
                    drawLine(
                        color = colors.hairline,
                        start = Offset(0f, size.height - strokeWidthPx),
                        end = Offset(size.width, size.height - strokeWidthPx),
                        strokeWidth = strokeWidthPx,
                    )
                }
            }
            .padding(
                horizontal = when (surface) {
                    AppListRowSurface.FlatSheet, AppListRowSurface.FlatSettings -> densityTokens.screenPadding
                    AppListRowSurface.GroupedCard -> densityTokens.cardPadding
                },
                vertical = AppListRowDefaults.VerticalPadding,
            )
            .heightIn(min = AppListRowDefaults.MinHeight),
        horizontalArrangement = Arrangement.spacedBy(AppListRowDefaults.ItemGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null || leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(AppListRowDefaults.IconWellSize)
                    .background(
                        color = iconWellColor,
                        shape = RoundedCornerShape(AppShapes.InputRadius),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                } else if (iconRes != null) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(AppListRowDefaults.IconSize),
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = typography.bodyLg,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!note.isNullOrBlank()) {
                Text(
                    text = note,
                    style = typography.bodyMd,
                    color = noteColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (trailingContent != null) {
            trailingContent()
        } else if (trailing != null) {
            when (trailing) {
                is AppListRowTrailing.Value -> {
                    Text(
                        text = trailing.text,
                        style = typography.bodyMd,
                        color = colors.inkSoft,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier.widthIn(max = 160.dp),
                    )
                }

                is AppListRowTrailing.Checkmark -> {
                    Box(
                        modifier = Modifier.size(AppListRowDefaults.CheckmarkSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }

                is AppListRowTrailing.Switch -> {
                    AppSwitch(
                        checked = trailing.checked,
                        onCheckedChange = trailing.onCheckedChange,
                        enabled = enabled,
                    )
                }

                is AppListRowTrailing.Chevron -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = colors.inkMuted,
                        modifier = Modifier.size(AppListRowDefaults.ChevronSize),
                    )
                }

                is AppListRowTrailing.Custom -> {
                    trailing.content()
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppListRowGroupedPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                AppListRow(
                    title = "Appearance",
                    note = "Dark mode, typography, palette",
                    iconRes = R.drawable.ic_palette,
                    surface = AppListRowSurface.GroupedCard,
                    index = 0,
                    count = 3,
                    trailing = AppListRowTrailing.Chevron,
                    onClick = {},
                )
                AppListRow(
                    title = "Daily Wallpaper Rotation",
                    note = "Updates every day at 6:00 AM",
                    iconRes = R.drawable.ic_auto_awesome,
                    surface = AppListRowSurface.GroupedCard,
                    index = 1,
                    count = 3,
                    trailing = AppListRowTrailing.Switch(checked = true, onCheckedChange = {}),
                )
                AppListRow(
                    title = "Clear Cached Wallpapers",
                    note = "Reclaims 142 MB storage",
                    iconRes = R.drawable.ic_close,
                    surface = AppListRowSurface.GroupedCard,
                    index = 2,
                    count = 3,
                    danger = true,
                    onClick = {},
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppListRowFlatSettingsPreview() {
    AppPreview {
        Column {
            AppListRow(
                title = "Font Pairing",
                surface = AppListRowSurface.FlatSettings,
                index = 0,
                count = 2,
                trailing = AppListRowTrailing.Value("Editorial"),
                onClick = {},
                showTopHairline = true,
            )
            AppListRow(
                title = "Strict Action Economy",
                note = "Enforce single primary CTA per screen",
                surface = AppListRowSurface.FlatSettings,
                index = 1,
                count = 2,
                trailing = AppListRowTrailing.Checkmark,
                onClick = {},
            )
        }
    }
}
