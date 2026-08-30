package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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

enum class AppIconButtonTone {
    Default,
    Muted,
    Accent,
}

object AppIconButtonDefaults {
    val Size: Dp = 44.dp
    val IconSize: Dp = 21.dp
    val Shape: Shape = CircleShape

    @Composable
    fun containerColor(overlay: Boolean, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when {
            overlay -> colors.ink.copy(alpha = 0.34f)
            else -> Color.Transparent
        }
    }

    @Composable
    fun contentColor(overlay: Boolean, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when {
            overlay -> if (enabled) colors.surfaceFixed else colors.surfaceFixed.copy(alpha = 0.4f)
            !enabled -> colors.inkMuted
            else -> colors.ink
        }
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = AppIconButtonDefaults.Shape,
    content: @Composable () -> Unit,
) {
    val containerColor = AppIconButtonDefaults.containerColor(overlay = overlay, enabled = enabled)
    val contentColor = AppIconButtonDefaults.contentColor(overlay = overlay, enabled = enabled)
    val border = if (overlay) BorderStroke(1.dp, AppTheme.colors.hairline) else null
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
            .size(AppIconButtonDefaults.Size)
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
    ) {
        Box(
            modifier = Modifier.size(AppIconButtonDefaults.Size),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

/**
 * Convenience overload accepting drawable icon resource and legacy tone parameters.
 */
@Composable
fun AppIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tone: AppIconButtonTone = AppIconButtonTone.Default,
    overlay: Boolean = false,
    enabled: Boolean = true,
    size: Dp = AppIconButtonDefaults.Size,
    iconSize: Dp = AppIconButtonDefaults.IconSize,
) {
    val colors = AppTheme.colors
    val explicitTint = when (tone) {
        AppIconButtonTone.Default -> null
        AppIconButtonTone.Muted -> colors.inkMuted
        AppIconButtonTone.Accent -> colors.accent
    }

    AppIconButton(
        onClick = onClick,
        modifier = modifier.size(size),
        overlay = overlay,
        enabled = enabled,
    ) {
        val tint = explicitTint ?: AppIconButtonDefaults.contentColor(overlay = overlay, enabled = enabled)
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = tint,
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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_share,
                onClick = {},
                contentDescription = "Share",
            )
            AppIconButton(
                iconRes = R.drawable.ic_close,
                onClick = {},
                contentDescription = "Close",
                tone = AppIconButtonTone.Muted,
            )
            AppIconButton(
                iconRes = R.drawable.ic_star,
                onClick = {},
                contentDescription = "Star",
                tone = AppIconButtonTone.Accent,
            )
            AppIconButton(
                iconRes = R.drawable.ic_share,
                onClick = {},
                contentDescription = "Overlay Share",
                overlay = true,
            )
            AppIconButton(
                iconRes = R.drawable.ic_close,
                onClick = {},
                contentDescription = "Disabled",
                enabled = false,
            )
        }
    }
}
