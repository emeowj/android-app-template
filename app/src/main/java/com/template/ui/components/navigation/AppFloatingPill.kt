package com.template.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing
import com.template.ui.theme.overlaySurfaceShadow

object AppFloatingPillDefaults {
    val Height: Dp = 44.dp
    val HorizontalPadding: Dp = 18.dp
    val ItemGap: Dp = 8.dp
    val LabeledIconSize: Dp = 17.dp
    val IconOnlySize: Dp = 44.dp
    val IconOnlyGlyphSize: Dp = 20.dp
}

@Composable
fun AppFloatingPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconOnly: Boolean = false,
    text: String = "Show controls",
    @DrawableRes iconRes: Int? = null,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = AppTheme.colors
    val shape = if (iconOnly) CircleShape else RoundedCornerShape(AppShapes.PillRadius)

    val contentModifier = if (iconOnly) {
        Modifier
            .size(AppFloatingPillDefaults.IconOnlySize)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = colors.ink),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
    } else {
        Modifier
            .height(AppFloatingPillDefaults.Height)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = colors.ink),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = AppFloatingPillDefaults.HorizontalPadding)
    }

    Box(
        modifier = modifier
            .overlaySurfaceShadow(shape = shape)
            .background(color = colors.surface, shape = shape)
            .appFocusRing(visible = false, shape = shape)
            .then(contentModifier),
        contentAlignment = Alignment.Center,
    ) {
        if (iconOnly) {
            if (icon != null) {
                icon()
            } else {
                Icon(
                    painter = painterResource(iconRes ?: R.drawable.ic_settings),
                    contentDescription = text,
                    tint = colors.ink,
                    modifier = Modifier.size(AppFloatingPillDefaults.IconOnlyGlyphSize),
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppFloatingPillDefaults.ItemGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (icon != null) {
                    icon()
                } else {
                    Icon(
                        painter = painterResource(iconRes ?: R.drawable.ic_settings),
                        contentDescription = null,
                        tint = colors.ink,
                        modifier = Modifier.size(AppFloatingPillDefaults.LabeledIconSize),
                    )
                }
                Text(
                    text = text,
                    style = AppTheme.typography.bodyMd,
                    color = colors.ink,
                    maxLines = 1,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppFloatingPillLabeledPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            AppFloatingPill(
                text = "Show controls",
                iconRes = R.drawable.ic_settings,
                onClick = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppFloatingPillIconOnlyPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            AppFloatingPill(
                iconOnly = true,
                iconRes = R.drawable.ic_settings,
                onClick = {},
            )
        }
    }
}
