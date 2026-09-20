package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.appFocusRing

object AppButtonDefaults {
    val Height: Dp = 56.dp
    val TextButtonHeight: Dp = 44.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val IconSize: Dp = 20.dp
    val HorizontalPadding: Dp = 24.dp
    val TextButtonHorizontalPadding: Dp = 12.dp

    @Composable
    fun contentColor(style: Style, enabled: Boolean): Color {
        val colors = AppTheme.colors
        return when (style) {
            AppTheme.styles.button.secondary -> if (enabled) colors.ink else colors.inkMuted
            AppTheme.styles.button.text -> if (enabled) colors.ink else colors.inkMuted
            AppTheme.styles.button.textDanger -> if (enabled) colors.danger else colors.danger.copy(alpha = 0.4f)
            else -> if (enabled) colors.background else colors.inkMuted
        }
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentColor: Color? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    block: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val effectivelyEnabled = enabled && !isLoading
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = effectivelyEnabled
    }
    val resolvedContentColor = contentColor ?: AppButtonDefaults.contentColor(style = style, enabled = effectivelyEnabled)
    val isTextButton = style == AppTheme.styles.button.text || style == AppTheme.styles.button.textDanger
    val horizontalPadding = if (isTextButton) {
        AppButtonDefaults.TextButtonHorizontalPadding
    } else {
        AppButtonDefaults.HorizontalPadding
    }

    Box(
        modifier = modifier
            .then(if (block) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .styleable(styleState, AppTheme.styles.button.primary, style)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = effectivelyEnabled,
                onClick = onClick,
            )
            .appFocusRing(visible = false, shape = AppButtonDefaults.Shape, ringColor = AppTheme.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides resolvedContentColor) {
            Row(
                modifier = Modifier
                    .then(if (block) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(AppButtonDefaults.IconSize),
                    )
                } else {
                    leadingIcon?.invoke()
                    Text(
                        text = text,
                        style = AppTheme.typography.bodyLg,
                        fontWeight = FontWeight.Medium,
                        color = LocalContentColor.current,
                        maxLines = 1,
                    )
                    trailingIcon?.invoke()
                }
            }
        }
    }
}

/**
 * Convenience overload accepting drawable icon resource and optional label alias.
 */
@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentColor: Color? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    @DrawableRes iconRes: Int? = null,
    block: Boolean = false,
) {
    AppButton(
        text = label,
        onClick = onClick,
        modifier = modifier,
        style = style,
        contentColor = contentColor,
        enabled = enabled,
        isLoading = isLoading,
        block = block,
        leadingIcon = iconRes?.let { res ->
            {
                Icon(
                    painter = painterResource(res),
                    contentDescription = null,
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(AppButtonDefaults.IconSize),
                )
            }
        },
    )
}

@ThemePreviews
@Composable
private fun AppButtonPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            AppButton(text = "Primary Button", onClick = {})
            AppButton(text = "Secondary Button", onClick = {}, style = AppTheme.styles.button.secondary)
            AppButton(text = "Text Button", onClick = {}, style = AppTheme.styles.button.text)
            AppButton(text = "Text Danger Button", onClick = {}, style = AppTheme.styles.button.textDanger)
            AppButton(text = "Disabled Primary", onClick = {}, enabled = false)
            AppButton(text = "Loading State", onClick = {}, isLoading = true)
        }
    }
}
