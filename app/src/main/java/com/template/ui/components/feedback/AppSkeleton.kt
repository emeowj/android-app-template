package com.template.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppColors
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme

@Immutable
data class AppSkeletonColors(
    val containerColor: Color,
    val borderColor: Color,
)

object AppSkeletonDefaults {
    val Shape: Shape = RoundedCornerShape(AppShapes.CardRadius)
    val BorderWidth: Dp = 1.dp

    @Composable
    fun colors(): AppSkeletonColors {
        val colors = AppTheme.colors
        return AppSkeletonColors(
            containerColor = AppColors.mixOklab(colors.background, colors.surface, 0.72f),
            borderColor = colors.hairline,
        )
    }
}

/**
 * Static placeholder container for loading states before content transitions in.
 *
 * Adheres strictly to the Bound design rule: zero shimmer animations.
 */
@Composable
fun AppSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = AppSkeletonDefaults.Shape,
    colors: AppSkeletonColors = AppSkeletonDefaults.colors(),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor)
            .border(width = AppSkeletonDefaults.BorderWidth, color = colors.borderColor, shape = shape),
    )
}

@ThemePreviews
@Composable
private fun AppSkeletonPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppSkeleton(
                    modifier = Modifier.size(width = 100.dp, height = 178.dp),
                )
                AppSkeleton(
                    modifier = Modifier.size(width = 100.dp, height = 178.dp),
                )
            }
        }
    }
}
