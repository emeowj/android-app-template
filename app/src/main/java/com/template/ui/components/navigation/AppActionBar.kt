package com.template.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity

object AppActionBarDefaults {
    val VerticalPadding: Dp = 14.dp
    val ItemGap: Dp = 10.dp
    val HairlineWidth: Dp = 1.dp
}

@Composable
fun AppActionBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AppTheme.colors
    val density = LocalDensity.current
    val densityTokens = LocalAppDensity.current

    val effectivePadding = contentPadding ?: PaddingValues(
        horizontal = densityTokens.screenPadding,
        vertical = AppActionBarDefaults.VerticalPadding,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .drawWithContent {
                drawContent()
                val strokeWidthPx = with(density) { AppActionBarDefaults.HairlineWidth.toPx() }
                drawLine(
                    color = colors.hairline,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidthPx,
                )
            }
            .padding(effectivePadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppActionBarDefaults.ItemGap),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun AppActionBarDualPreview() {
    AppPreview {
        AppActionBar {
            AppButton(
                text = "Cancel",
                onClick = {},
                variant = AppButtonVariant.Secondary,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                text = "Apply Wallpaper",
                onClick = {},
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppActionBarSinglePreview() {
    AppPreview {
        AppActionBar {
            AppButton(
                text = "Save Changes",
                onClick = {},
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
