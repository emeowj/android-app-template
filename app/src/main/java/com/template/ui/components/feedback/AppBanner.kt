package com.template.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme

@Immutable
data class AppBannerColors(
    val containerColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val boldTextColor: Color,
)

object AppBannerDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.CardRadius)
    val BorderWidth: Dp = 1.dp
    val VerticalPadding: Dp = 12.dp
    val HorizontalPadding: Dp = 14.dp

    @Composable
    fun colors(): AppBannerColors {
        val colors = AppTheme.colors
        return AppBannerColors(
            containerColor = colors.surface,
            borderColor = colors.hairline,
            textColor = colors.inkMuted,
            boldTextColor = colors.ink,
        )
    }
}

/**
 * Surface banner card for inline informational notes.
 */
@Composable
fun AppBanner(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    shape: Shape = AppBannerDefaults.Shape,
    colors: AppBannerColors = AppBannerDefaults.colors(),
) {
    AppBanner(
        modifier = modifier,
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
fun AppBanner(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = AppBannerDefaults.Shape,
    colors: AppBannerColors = AppBannerDefaults.colors(),
) {
    AppBanner(
        text = AnnotatedString(text),
        modifier = modifier,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun AppBanner(
    modifier: Modifier = Modifier,
    shape: Shape = AppBannerDefaults.Shape,
    colors: AppBannerColors = AppBannerDefaults.colors(),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.containerColor)
            .border(width = AppBannerDefaults.BorderWidth, color = colors.borderColor, shape = shape)
            .padding(
                horizontal = AppBannerDefaults.HorizontalPadding,
                vertical = AppBannerDefaults.VerticalPadding,
            ),
    ) {
        content()
    }
}

@ThemePreviews
@Composable
private fun AppBannerPreview() {
    val sampleText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = AppTheme.colors.ink)) {
            append("Offline catalog. ")
        }
        append("Photos are cached on-device, so browsing works without a connection.")
    }

    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppBanner(text = sampleText)
            AppBanner(text = "Unsplash API rate limit refreshed. High-resolution downloads active.")
        }
    }
}
