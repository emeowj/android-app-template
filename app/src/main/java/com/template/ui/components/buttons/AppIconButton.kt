package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.appFocusRing

object AppIconButtonDefaults {
    val Size: Dp = 44.dp
    val IconSize: Dp = 21.dp
    val Shape: Shape = CircleShape

    @Composable
    fun contentColor(style: Style, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when (style) {
            AppTheme.styles.iconButton.overlay -> if (enabled) colors.surfaceFixed else colors.surfaceFixed.copy(alpha = 0.4f)
            AppTheme.styles.iconButton.filled -> if (enabled) colors.background else colors.inkMuted
            else -> if (enabled) colors.ink else colors.inkMuted
        }
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentColor: Color? = null,
    enabled: Boolean = true,
    shape: Shape = AppIconButtonDefaults.Shape,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = enabled
    }
    val resolvedContentColor = contentColor ?: AppIconButtonDefaults.contentColor(style = style, enabled = enabled)

    Box(
        modifier = modifier
            .size(AppIconButtonDefaults.Size)
            .styleable(styleState, AppTheme.styles.iconButton.standard, style)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = AppIconButtonDefaults.Size / 2),
                enabled = enabled,
                onClick = onClick,
            )
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides resolvedContentColor) {
            content()
        }
    }
}

/**
 * Convenience overload accepting drawable icon resource.
 */
@Composable
fun AppIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: Style = Style,
    tint: Color? = null,
    enabled: Boolean = true,
    size: Dp = AppIconButtonDefaults.Size,
    iconSize: Dp = AppIconButtonDefaults.IconSize,
) {
    AppIconButton(
        onClick = onClick,
        modifier = modifier.size(size),
        style = style,
        contentColor = tint,
        enabled = enabled,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = LocalContentColor.current,
            modifier = Modifier.size(iconSize),
        )
    }
}

@ThemePreviews
@Composable
private fun AppIconButtonPreview() {
    AppPreview {
        Row(
            modifier = Modifier.padding(Padding.md),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_search,
                onClick = {},
                contentDescription = "Standard",
                style = AppTheme.styles.iconButton.standard,
            )
            AppIconButton(
                iconRes = R.drawable.ic_search,
                onClick = {},
                contentDescription = "Filled",
                style = AppTheme.styles.iconButton.filled,
            )
            AppIconButton(
                iconRes = R.drawable.ic_search,
                onClick = {},
                contentDescription = "Tonal",
                style = AppTheme.styles.iconButton.tonal,
            )
            AppIconButton(
                iconRes = R.drawable.ic_search,
                onClick = {},
                contentDescription = "Outlined",
                style = AppTheme.styles.iconButton.outlined,
            )
            AppIconButton(
                iconRes = R.drawable.ic_search,
                onClick = {},
                contentDescription = "Overlay",
                style = AppTheme.styles.iconButton.overlay,
            )
        }
    }
}
