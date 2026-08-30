package com.template.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import com.template.ui.theme.dialogShadow

object AppDialogDefaults {
    val Radius: Dp = AppShapes.DialogRadius
    val Shape: Shape = RoundedCornerShape(Radius)
    val ActionGap: Dp = Padding.xs
    val ActionMarginTop: Dp = 20.dp
    val TitleMarginBottom: Dp = Padding.sm
    val MaxWidth: Dp = 400.dp
    const val MotionDurationMs: Int = 180
    const val InitialScale: Float = 0.96f
}

/**
 * Header section for [AppDialog] with titleLg headline and bodyMd muted message.
 */
@Composable
fun AppDialogHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    val colors = AppTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = AppTheme.typography.titleLg,
            fontWeight = FontWeight.Normal,
            color = colors.ink,
        )

        if (!description.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(AppDialogDefaults.TitleMarginBottom))
            Text(
                text = description,
                style = AppTheme.typography.bodyMd,
                color = colors.inkMuted,
            )
        }
    }
}

/**
 * Right-aligned actions row (`.dialog-actions`) for [AppDialog].
 */
@Composable
fun AppDialogActions(
    confirmText: String = "Confirm",
    onConfirm: () -> Unit = {},
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    isDestructive: Boolean = true,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            AppDialogDefaults.ActionGap,
            Alignment.End,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (actions != null) {
            actions()
        } else {
            if (!dismissText.isNullOrBlank() && onDismiss != null) {
                AppButton(
                    text = dismissText,
                    onClick = onDismiss,
                    variant = AppButtonVariant.Text,
                )
            }
            AppButton(
                text = confirmText,
                onClick = onConfirm,
                variant = if (isDestructive) AppButtonVariant.TextDanger else AppButtonVariant.Text,
            )
        }
    }
}

/**
 * Self-contained Dialog content card with 24dp radius, dialogShadow, titleLg headline,
 * bodyMd muted message, and right-aligned text action buttons.
 */
@Composable
fun AppDialogContent(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    confirmText: String = "Confirm",
    onConfirm: () -> Unit = {},
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    isDestructive: Boolean = true,
    content: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val colors = AppTheme.colors
    val density = LocalAppDensity.current

    Surface(
        modifier = modifier
            .widthIn(max = AppDialogDefaults.MaxWidth)
            .fillMaxWidth()
            .dialogShadow(shape = AppDialogDefaults.Shape),
        shape = AppDialogDefaults.Shape,
        color = colors.surface,
        contentColor = colors.ink,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(density.screenPadding),
        ) {
            AppDialogHeader(
                title = title,
                description = description,
            )

            if (content != null) {
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }

            Spacer(modifier = Modifier.height(AppDialogDefaults.ActionMarginTop))

            AppDialogActions(
                confirmText = confirmText,
                onConfirm = onConfirm,
                dismissText = dismissText,
                onDismiss = onDismiss,
                isDestructive = isDestructive,
                actions = actions,
            )
        }
    }
}

/**
 * Centered modal dialog overlay wrapping [AppDialogContent] with ink56 backdrop scrim
 * and 180ms scale+fade entrance animation.
 */
@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    confirmText: String = "Confirm",
    onConfirm: () -> Unit = {},
    dismissText: String? = "Cancel",
    onDismiss: (() -> Unit)? = onDismissRequest,
    isDestructive: Boolean = true,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val density = LocalAppDensity.current
    val colors = AppTheme.colors

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.ink56)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest,
                )
                .padding(density.screenPadding),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                ),
            ) {
                AppDialogContent(
                    title = title,
                    modifier = modifier,
                    description = description,
                    confirmText = confirmText,
                    onConfirm = onConfirm,
                    dismissText = dismissText,
                    onDismiss = onDismiss,
                    isDestructive = isDestructive,
                    content = content,
                    actions = actions,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppDialogContentPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(Padding.lg),
            contentAlignment = Alignment.Center,
        ) {
            AppDialogContent(
                title = "Discard changes?",
                description = "You've made edits to this wallpaper that haven't been saved yet.",
                dismissText = "Keep editing",
                onDismiss = {},
                confirmText = "Discard",
                onConfirm = {},
                isDestructive = true,
            )
        }
    }
}
