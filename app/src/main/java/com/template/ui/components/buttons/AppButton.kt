package com.template.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

enum class AppButtonVariant {
    Primary,
    Secondary,
    Ghost,
}

@Immutable
data class AppButtonColors(
    val container: Color,
    val content: Color,
    val border: Color?,
    val disabledContainer: Color,
    val disabledContent: Color,
)

object AppButtonDefaults {
    val Height: Dp = 56.dp
    val Shape: Shape = AppShape.extraLarge
    val IconSize: Dp = 20.dp

    @Composable
    fun colors(variant: AppButtonVariant): AppButtonColors {
        val roles = LocalColorRoles.current
        return when (variant) {
            AppButtonVariant.Primary ->
                AppButtonColors(
                    container = roles.ink,
                    content = roles.bg,
                    border = null,
                    disabledContainer = roles.surfaceAlt,
                    disabledContent = roles.inkMuted,
                )

            AppButtonVariant.Secondary ->
                AppButtonColors(
                    container = roles.surface,
                    content = roles.ink,
                    border = roles.hairline,
                    disabledContainer = roles.surfaceAlt,
                    disabledContent = roles.inkMuted,
                )

            AppButtonVariant.Ghost ->
                AppButtonColors(
                    container = Color.Transparent,
                    content = roles.ink,
                    border = null,
                    disabledContainer = Color.Transparent,
                    disabledContent = roles.inkMuted,
                )
        }
    }
}

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
    height: Dp = AppButtonDefaults.Height,
) {
    val effectivelyEnabled = enabled && !isLoading
    val container = if (effectivelyEnabled) colors.container else colors.disabledContainer
    val content = if (effectivelyEnabled) colors.content else colors.disabledContent
    val border = colors.border?.let { BorderStroke(Padding.hairline, it) }
    Surface(
        onClick = onClick,
        enabled = effectivelyEnabled,
        shape = shape,
        color = container,
        contentColor = content,
        border = border,
        modifier = modifier.height(height),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = Padding.md),
            horizontalArrangement =
                Arrangement.spacedBy(
                    Padding.sm,
                    alignment = Alignment.CenterHorizontally,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = colors.content,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(AppButtonDefaults.IconSize),
                )
            } else {
                iconRes?.let {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        tint = content,
                        modifier = Modifier.size(AppButtonDefaults.IconSize),
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = content,
                    maxLines = 1,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AppButtonPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            AppButton(label = "Primary", onClick = {})
            AppButton(label = "Secondary", onClick = {}, variant = AppButtonVariant.Secondary)
            AppButton(label = "Ghost", onClick = {}, variant = AppButtonVariant.Ghost)
            AppButton(
                label = "Disabled",
                onClick = {},
                variant = AppButtonVariant.Primary,
                enabled = false,
            )
            AppButton(
                label = "With icon",
                onClick = {},
                variant = AppButtonVariant.Primary,
                iconRes = R.drawable.ic_share,
            )
        }
    }
}
