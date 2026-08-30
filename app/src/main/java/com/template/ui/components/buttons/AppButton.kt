package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

enum class AppButtonVariant {
    Primary,
    Secondary,
    Text,
    TextDanger,
}

@Immutable
data class AppButtonColors(
    val container: Color,
    val content: Color,
    val border: Color? = null,
    val disabledContainer: Color = Color.Transparent,
    val disabledContent: Color,
    val disabledBorder: Color? = null,
)

object AppButtonDefaults {
    val Height: Dp = 56.dp
    val TextButtonHeight: Dp = 44.dp
    val Shape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val IconSize: Dp = 20.dp
    val HorizontalPadding: Dp = 24.dp
    val TextButtonHorizontalPadding: Dp = 12.dp

    @Composable
    fun colors(variant: AppButtonVariant): AppButtonColors {
        val colors = AppTheme.colors
        return when (variant) {
            AppButtonVariant.Primary -> AppButtonColors(
                container = colors.ink,
                content = colors.background,
                border = null,
                disabledContainer = colors.ink14,
                disabledContent = colors.inkMuted,
            )

            AppButtonVariant.Secondary -> AppButtonColors(
                container = colors.surface,
                content = colors.ink,
                border = colors.border,
                disabledContainer = colors.surface,
                disabledContent = colors.inkMuted,
                disabledBorder = colors.hairline,
            )

            AppButtonVariant.Text -> AppButtonColors(
                container = Color.Transparent,
                content = colors.ink,
                border = null,
                disabledContainer = Color.Transparent,
                disabledContent = colors.inkMuted,
            )

            AppButtonVariant.TextDanger -> AppButtonColors(
                container = Color.Transparent,
                content = colors.danger,
                border = null,
                disabledContainer = Color.Transparent,
                disabledContent = colors.danger.copy(alpha = 0.4f),
            )
        }
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    block: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = AppButtonDefaults.Shape,
    colors: AppButtonColors = AppButtonDefaults.colors(variant),
) {
    val effectivelyEnabled = enabled && !isLoading
    val container = if (effectivelyEnabled) colors.container else colors.disabledContainer
    val content = if (effectivelyEnabled) colors.content else colors.disabledContent
    val borderColor = if (effectivelyEnabled) colors.border else colors.disabledBorder
    val border = borderColor?.let { BorderStroke(1.dp, it) }

    val height = when (variant) {
        AppButtonVariant.Primary, AppButtonVariant.Secondary -> AppButtonDefaults.Height
        AppButtonVariant.Text, AppButtonVariant.TextDanger -> AppButtonDefaults.TextButtonHeight
    }

    val horizontalPadding = when (variant) {
        AppButtonVariant.Primary, AppButtonVariant.Secondary -> AppButtonDefaults.HorizontalPadding
        AppButtonVariant.Text, AppButtonVariant.TextDanger -> AppButtonDefaults.TextButtonHorizontalPadding
    }

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        enabled = effectivelyEnabled,
        shape = shape,
        color = container,
        contentColor = content,
        border = border,
        interactionSource = interactionSource,
        modifier = modifier
            .height(height)
            .then(if (block) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .appFocusRing(visible = false, shape = shape, ringColor = AppTheme.colors.accent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .then(if (block) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = content,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(AppButtonDefaults.IconSize),
                )
            } else {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = AppTheme.typography.bodyLg,
                    fontWeight = FontWeight.Medium,
                    color = content,
                    maxLines = 1,
                )
                trailingIcon?.invoke()
            }
        }
    }
}

/**
 * Convenience overload accepting drawable icon resource and optional label alias for backward compatibility.
 */
@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    @DrawableRes iconRes: Int? = null,
    colors: AppButtonColors = AppButtonDefaults.colors(variant),
    shape: Shape = AppButtonDefaults.Shape,
    block: Boolean = false,
) {
    AppButton(
        text = label,
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        isLoading = isLoading,
        shape = shape,
        colors = colors,
        block = block,
        leadingIcon = iconRes?.let { res ->
            {
                Icon(
                    painter = painterResource(res),
                    contentDescription = null,
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
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            AppButton(text = "Primary Button", onClick = {})
            AppButton(text = "Secondary Button", onClick = {}, variant = AppButtonVariant.Secondary)
            AppButton(text = "Text Button", onClick = {}, variant = AppButtonVariant.Text)
            AppButton(text = "Text Danger Button", onClick = {}, variant = AppButtonVariant.TextDanger)
            AppButton(text = "Disabled Primary", onClick = {}, enabled = false)
            AppButton(text = "Loading State", onClick = {}, isLoading = true)
        }
    }
}
