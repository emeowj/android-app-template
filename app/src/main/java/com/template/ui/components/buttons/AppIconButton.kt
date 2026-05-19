package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

enum class AppIconButtonTone {
    Default,
    Muted,
    Accent,
}

object AppIconButtonDefaults {
    val Size: Dp = 40.dp
    val IconSize: Dp = 24.dp

    @Composable
    fun tint(tone: AppIconButtonTone): Color {
        val roles = LocalColorRoles.current
        return when (tone) {
            AppIconButtonTone.Default -> roles.ink
            AppIconButtonTone.Muted -> roles.inkMuted
            AppIconButtonTone.Accent -> roles.accent
        }
    }
}

@Composable
fun AppIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tone: AppIconButtonTone = AppIconButtonTone.Default,
    enabled: Boolean = true,
    tint: Color = AppIconButtonDefaults.tint(tone),
    size: Dp = AppIconButtonDefaults.Size,
    iconSize: Dp = AppIconButtonDefaults.IconSize,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(size),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = if (enabled) tint else LocalColorRoles.current.inkMuted,
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
                iconRes = R.drawable.ic_share,
                onClick = {},
                contentDescription = null,
            )
            AppIconButton(
                iconRes = R.drawable.ic_close,
                onClick = {},
                contentDescription = null,
                tone = AppIconButtonTone.Muted,
            )
            AppIconButton(
                iconRes = R.drawable.ic_star,
                onClick = {},
                contentDescription = null,
                tone = AppIconButtonTone.Accent,
            )
        }
    }
}
