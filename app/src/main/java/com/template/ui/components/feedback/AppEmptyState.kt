package com.template.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme

object AppEmptyStateDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.CardRadius)
    val BorderWidth: Dp = 1.dp
    val DashOnInterval: Dp = 6.dp
    val DashOffInterval: Dp = 6.dp
    val VerticalPadding: Dp = 40.dp
    val HorizontalPadding: Dp = 24.dp
    val ContentSpacing: Dp = 6.dp
    val ActionSpacing: Dp = 16.dp
}

/**
 * Centered empty state container with dashed hairline border, serif heading,
 * caption body text, and optional action/icon slots.
 */
@Composable
fun AppEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    borderColor: Color = AppTheme.colors.hairline,
    shapeRadius: Dp = AppShapes.CardRadius,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidthPx = AppEmptyStateDefaults.BorderWidth.toPx()
                val radiusPx = shapeRadius.toPx()
                val halfStroke = strokeWidthPx / 2f
                val dashOn = AppEmptyStateDefaults.DashOnInterval.toPx()
                val dashOff = AppEmptyStateDefaults.DashOffInterval.toPx()

                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(halfStroke, halfStroke),
                    size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                    cornerRadius = CornerRadius(radiusPx - halfStroke, radiusPx - halfStroke),
                    style = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashOn, dashOff), 0f),
                    ),
                )
            }
            .padding(
                horizontal = AppEmptyStateDefaults.HorizontalPadding,
                vertical = AppEmptyStateDefaults.VerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = title,
                style = typography.titleSm,
                color = colors.ink,
                textAlign = TextAlign.Center,
            )

            if (description != null) {
                Spacer(modifier = Modifier.height(AppEmptyStateDefaults.ContentSpacing))
                Text(
                    text = description,
                    style = typography.caption,
                    color = colors.inkMuted,
                    textAlign = TextAlign.Center,
                )
            }

            if (action != null) {
                Spacer(modifier = Modifier.height(AppEmptyStateDefaults.ActionSpacing))
                action()
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppEmptyStatePreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppEmptyState(
                title = "No wallpapers yet",
                description = "Create your first one from the Create tab.",
            )

            AppEmptyState(
                title = "No favorites saved",
                description = "Tap the heart icon on any wallpaper to add it to your library favorites.",
                action = {
                    AppButton(
                        text = "Browse Wallpapers",
                        onClick = {},
                        variant = AppButtonVariant.Secondary,
                    )
                },
            )
        }
    }
}
