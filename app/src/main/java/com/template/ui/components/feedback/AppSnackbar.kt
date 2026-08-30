package com.template.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme

enum class AppSnackbarAnchor {
    Bottom,
    Top,
}

@Immutable
data class AppSnackbarColors(
    val containerColor: Color,
    val textColor: Color,
    val actionTextColor: Color,
    val actionPressedOverlayColor: Color,
)

object AppSnackbarDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.InputRadius)
    val MinHeight: Dp = 48.dp
    val ActionMinHeight: Dp = 36.dp
    val ActionPaddingHorizontal: Dp = 12.dp
    val ActionShape: Shape = CircleShape
    val HorizontalPadding: Dp = 16.dp
    val VerticalPadding: Dp = 10.dp
    val ItemSpacing: Dp = 12.dp

    /** Standard auto-dismiss duration for transient snackbars (3.6s). */
    const val AutoDismissDurationMillis: Long = 3_600L

    @Composable
    fun colors(): AppSnackbarColors {
        val colors = AppTheme.colors
        return AppSnackbarColors(
            containerColor = colors.ink,
            textColor = colors.background,
            actionTextColor = colors.background,
            actionPressedOverlayColor = colors.surface.copy(alpha = 0.16f),
        )
    }
}

/**
 * Floating toast feedback bar with ink container, background-colored text,
 * and an optional trailing pill action button.
 */
@Composable
fun AppSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    shape: Shape = AppSnackbarDefaults.Shape,
    colors: AppSnackbarColors = AppSnackbarDefaults.colors(),
) {
    val typography = AppTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = AppSnackbarDefaults.MinHeight)
            .clip(shape)
            .background(colors.containerColor)
            .padding(
                horizontal = AppSnackbarDefaults.HorizontalPadding,
                vertical = AppSnackbarDefaults.VerticalPadding,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = message,
                style = typography.bodyMd,
                color = colors.textColor,
                modifier = Modifier.weight(1f, fill = false),
            )

            if (actionLabel != null && onActionClick != null) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minHeight = AppSnackbarDefaults.ActionMinHeight)
                        .clip(AppSnackbarDefaults.ActionShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = colors.actionPressedOverlayColor),
                            role = Role.Button,
                            onClick = onActionClick,
                        )
                        .padding(horizontal = AppSnackbarDefaults.ActionPaddingHorizontal),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = actionLabel,
                        style = typography.bodySm,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.actionTextColor,
                    )
                }
            }
        }
    }
}

/**
 * AppSnackbar overload accepting Material 3 [SnackbarData].
 */
@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    shape: Shape = AppSnackbarDefaults.Shape,
    colors: AppSnackbarColors = AppSnackbarDefaults.colors(),
) {
    AppSnackbar(
        message = snackbarData.visuals.message,
        modifier = modifier,
        actionLabel = snackbarData.visuals.actionLabel,
        onActionClick = { snackbarData.performAction() },
        shape = shape,
        colors = colors,
    )
}

/**
 * Animated snackbar host providing slide and fade transitions for bottom or top anchored bars.
 */
@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    anchor: AppSnackbarAnchor = AppSnackbarAnchor.Bottom,
    snackbar: @Composable (SnackbarData) -> Unit = { AppSnackbar(it) },
) {
    val currentSnackbarData = hostState.currentSnackbarData
    AnimatedVisibility(
        visible = currentSnackbarData != null,
        enter = slideInVertically(
            initialOffsetY = { if (anchor == AppSnackbarAnchor.Bottom) it else -it },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { if (anchor == AppSnackbarAnchor.Bottom) it else -it },
        ) + fadeOut(),
        modifier = modifier,
    ) {
        currentSnackbarData?.let { snackbar(it) }
    }
}

@ThemePreviews
@Composable
private fun AppSnackbarPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppSnackbar(
                message = "Wallpaper saved to Library",
                actionLabel = "Undo",
                onActionClick = {},
            )
            AppSnackbar(
                message = "Preset applied successfully",
            )
        }
    }
}
