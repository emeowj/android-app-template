package com.template.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
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
import com.template.ui.theme.appFocusRing
import com.template.ui.theme.floatingPillShadow

object AppBottomNavDefaults {
    val ContainerPadding: Dp = 6.dp
    val ItemGap: Dp = 4.dp
    val ItemTouchSize: Dp = 50.dp
    val ItemVisualSize: Dp = 46.dp
    val IconSize: Dp = 22.dp
    val HairlineWidth: Dp = 1.dp
}

@Composable
fun AppBottomNav(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .floatingPillShadow(shape = CircleShape)
                .background(color = colors.surface, shape = CircleShape)
                .border(
                    width = AppBottomNavDefaults.HairlineWidth,
                    color = colors.hairline,
                    shape = CircleShape,
                )
                .padding(AppBottomNavDefaults.ContainerPadding),
            horizontalArrangement = Arrangement.spacedBy(AppBottomNavDefaults.ItemGap),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun RowScope.AppBottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AppBottomNavItem(
        selected = selected,
        onClick = onClick,
        contentDescription = contentDescription,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(AppBottomNavDefaults.IconSize),
            )
        },
    )
}

@Composable
fun RowScope.AppBottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: @Composable () -> Unit,
) {
    val colors = AppTheme.colors

    val containerColor by animateColorAsState(
        targetValue = when {
            selected -> colors.accent
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 160),
        label = "AppBottomNavItemBg",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            selected -> colors.background
            else -> colors.inkMuted
        },
        animationSpec = tween(durationMillis = 160),
        label = "AppBottomNavItemContent",
    )

    Box(
        modifier = modifier
            .size(AppBottomNavDefaults.ItemTouchSize)
            .clip(CircleShape)
            .appFocusRing(visible = false, shape = CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = AppBottomNavDefaults.ItemVisualSize / 2),
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick,
            )
            .semantics {
                this.role = Role.Tab
                this.selected = selected
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(AppBottomNavDefaults.ItemVisualSize)
                .background(color = containerColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides contentColor,
            ) {
                icon()
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppBottomNavPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            AppBottomNav {
                AppBottomNavItem(
                    selected = true,
                    onClick = {},
                    iconRes = R.drawable.ic_grid_view,
                    contentDescription = "Library",
                )
                AppBottomNavItem(
                    selected = false,
                    onClick = {},
                    iconRes = R.drawable.ic_add,
                    contentDescription = "Create",
                )
                AppBottomNavItem(
                    selected = false,
                    onClick = {},
                    iconRes = R.drawable.ic_explore,
                    contentDescription = "Explore",
                )
            }
        }
    }
}
