package com.template.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme

@Immutable
data class AppDoneConfirmationColors(
    val badgeColor: Color,
    val checkmarkColor: Color,
    val titleColor: Color,
    val descriptionColor: Color,
)

object AppDoneConfirmationDefaults {
    val BadgeSize: Dp = 46.dp
    val CheckmarkIconSize: Dp = 24.dp
    val VerticalPadding: Dp = 20.dp
    val BottomPadding: Dp = 8.dp
    val HeadingSpacing: Dp = 14.dp
    val DescriptionSpacing: Dp = 6.dp
    val ActionSpacing: Dp = 16.dp

    @Composable
    fun colors(): AppDoneConfirmationColors {
        val colors = AppTheme.colors
        return AppDoneConfirmationColors(
            badgeColor = colors.accent,
            checkmarkColor = colors.surface,
            titleColor = colors.ink,
            descriptionColor = colors.inkMuted,
        )
    }
}

/**
 * Centered flow-completion status layout featuring an accent checkmark badge,
 * display title, and confirmation metadata.
 */
@Composable
fun AppDoneConfirmation(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    action: (@Composable () -> Unit)? = null,
    colors: AppDoneConfirmationColors = AppDoneConfirmationDefaults.colors(),
) {
    val typography = AppTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppDoneConfirmationDefaults.VerticalPadding,
                bottom = AppDoneConfirmationDefaults.BottomPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(AppDoneConfirmationDefaults.BadgeSize)
                .background(color = colors.badgeColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(AppDoneConfirmationDefaults.CheckmarkIconSize),
                tint = colors.checkmarkColor,
            )
        }

        Spacer(modifier = Modifier.height(AppDoneConfirmationDefaults.HeadingSpacing))

        Text(
            text = title,
            style = typography.titleLg,
            color = colors.titleColor,
            textAlign = TextAlign.Center,
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(AppDoneConfirmationDefaults.DescriptionSpacing))
            Text(
                text = description,
                style = typography.caption,
                color = colors.descriptionColor,
                textAlign = TextAlign.Center,
            )
        }

        if (action != null) {
            Spacer(modifier = Modifier.height(AppDoneConfirmationDefaults.ActionSpacing))
            action()
        }
    }
}

@ThemePreviews
@Composable
private fun AppDoneConfirmationPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppDoneConfirmation(
                title = "Wallpaper applied",
                description = "Home and lock screen updated.",
            )

            AppDoneConfirmation(
                title = "Collection created",
                description = "Neon Minimalist added to Library.",
                action = {
                    AppButton(
                        text = "View Collection",
                        onClick = {},
                        variant = AppButtonVariant.Secondary,
                    )
                },
            )
        }
    }
}
