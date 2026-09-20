package com.template.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    style: Style = Style,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource)

    Box(
        modifier = modifier
            .styleable(styleState, AppTheme.styles.card.elevated, style)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        CompositionLocalProvider(LocalContentColor provides AppTheme.colors.ink) {
            content()
        }
    }
}

@ThemePreviews
@Composable
private fun AppCardPreview() {
    AppPreview {
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.md),
        ) {
            Column(modifier = Modifier.padding(Padding.md)) {
                Text(
                    text = "Card title",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Supporting detail",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
