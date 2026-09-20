package com.template.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme

object AppHintCardDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.CardRadius)
    val BorderWidth: Dp = 1.dp
    val IconSize: Dp = 18.dp
    val ItemSpacing: Dp = 10.dp
    val VerticalPadding: Dp = 12.dp
    val HorizontalPadding: Dp = 14.dp
}

/**
 * Background-filled hint card (sits a shade back from surface) for contextual tips.
 */
@Composable
fun AppHintCard(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: Style = Style,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
) {
    AppHintCard(
        modifier = modifier,
        style = style,
        leadingIcon = leadingIcon,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.caption,
            color = LocalContentColor.current,
        )
    }
}

@Composable
fun AppHintCard(
    text: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
) {
    AppHintCard(
        text = AnnotatedString(text),
        modifier = modifier,
        style = style,
        leadingIcon = leadingIcon,
    )
}

@Composable
fun AppHintCard(
    modifier: Modifier = Modifier,
    style: Style = Style,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .styleable(null, AppTheme.styles.feedback.hintCard, style)
            .padding(
                horizontal = AppHintCardDefaults.HorizontalPadding,
                vertical = AppHintCardDefaults.VerticalPadding,
            ),
    ) {
        CompositionLocalProvider(LocalContentColor provides AppTheme.colors.ink) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppHintCardDefaults.ItemSpacing),
                verticalAlignment = Alignment.Top,
            ) {
                if (leadingIcon != null) {
                    Box(modifier = Modifier.padding(top = 1.dp)) {
                        leadingIcon()
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun DefaultHintIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_info),
        contentDescription = null,
        tint = LocalContentColor.current,
        modifier = Modifier.size(AppHintCardDefaults.IconSize),
    )
}

@ThemePreviews
@Composable
private fun AppHintCardPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppHintCard(
                text = "Tap any token chip to copy its value to the clipboard.",
            )
            AppHintCard(
                text = buildAnnotatedString {
                    append("Looking for more control? Head to ")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Settings → Type pairing")
                    }
                    append(" to adjust your typography scales.")
                },
            )
        }
    }
}
