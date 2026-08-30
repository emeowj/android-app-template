package com.template.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Immutable
data class AppHintCardColors(
    val containerColor: Color,
    val borderColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val boldTextColor: Color,
)

object AppHintCardDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.CardRadius)
    val BorderWidth: Dp = 1.dp
    val IconSize: Dp = 18.dp
    val ItemSpacing: Dp = 10.dp
    val VerticalPadding: Dp = 12.dp
    val HorizontalPadding: Dp = 14.dp

    @Composable
    fun colors(): AppHintCardColors {
        val colors = AppTheme.colors
        return AppHintCardColors(
            containerColor = colors.background,
            borderColor = colors.hairline,
            iconColor = colors.inkMuted,
            textColor = colors.inkMuted,
            boldTextColor = colors.ink,
        )
    }
}

/**
 * Background-filled hint card (sits a shade back from surface) for contextual tips.
 */
@Composable
fun AppHintCard(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
    shape: Shape = AppHintCardDefaults.Shape,
    colors: AppHintCardColors = AppHintCardDefaults.colors(),
) {
    AppHintCard(
        modifier = modifier,
        leadingIcon = leadingIcon,
        shape = shape,
        colors = colors,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.caption,
            color = colors.textColor,
        )
    }
}

@Composable
fun AppHintCard(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
    shape: Shape = AppHintCardDefaults.Shape,
    colors: AppHintCardColors = AppHintCardDefaults.colors(),
) {
    AppHintCard(
        text = AnnotatedString(text),
        modifier = modifier,
        leadingIcon = leadingIcon,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun AppHintCard(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = { DefaultHintIcon() },
    shape: Shape = AppHintCardDefaults.Shape,
    colors: AppHintCardColors = AppHintCardDefaults.colors(),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.containerColor)
            .border(width = AppHintCardDefaults.BorderWidth, color = colors.borderColor, shape = shape)
            .padding(
                horizontal = AppHintCardDefaults.HorizontalPadding,
                vertical = AppHintCardDefaults.VerticalPadding,
            ),
    ) {
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

@Composable
private fun DefaultHintIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_info),
        contentDescription = null,
        modifier = Modifier.size(AppHintCardDefaults.IconSize),
        tint = AppTheme.colors.inkMuted,
    )
}

@ThemePreviews
@Composable
private fun AppHintCardPreview() {
    val sampleText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = AppTheme.colors.ink)) {
            append("Drag the pin ")
        }
        append("to set which part of the photo stays visible on lock screen.")
    }

    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppHintCard(text = sampleText)
            AppHintCard(text = "Gradient stops can be adjusted by dragging handles left or right.")
        }
    }
}
