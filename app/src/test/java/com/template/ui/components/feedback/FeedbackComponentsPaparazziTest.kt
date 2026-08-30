package com.template.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class FeedbackComponentsPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppSkeletonLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                SkeletonSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSkeletonDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                SkeletonSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppEmptyStateLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                EmptyStateSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppEmptyStateDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                EmptyStateSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppBannerLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                BannerSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppBannerDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                BannerSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppHintCardLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                HintCardSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppHintCardDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                HintCardSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppDoneConfirmationLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                DoneConfirmationSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppDoneConfirmationDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                DoneConfirmationSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSnackbarLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                SnackbarSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSnackbarDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                SnackbarSpecimen()
            }
        }
    }
}

@Composable
private fun SkeletonSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppSkeleton (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppSkeleton(
                    modifier = Modifier.size(width = 100.dp, height = 140.dp),
                )
                AppSkeleton(
                    modifier = Modifier.size(width = 100.dp, height = 140.dp),
                )
                AppSkeleton(
                    modifier = Modifier.size(width = 100.dp, height = 140.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyStateSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "AppEmptyState (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppEmptyState(
                title = "No wallpapers yet",
                description = "Create your first one from the Create tab.",
            )

            AppEmptyState(
                title = "No search matches",
                description = "Try adjusting your query or filter criteria.",
                action = {
                    AppButton(
                        text = "Reset Filters",
                        onClick = {},
                        variant = AppButtonVariant.Secondary,
                    )
                },
            )
        }
    }
}

@Composable
private fun BannerSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val sampleText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.ink)) {
            append("Offline catalog. ")
        }
        append("Photos are cached on-device, so browsing works without a connection.")
    }

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppBanner (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppBanner(text = sampleText)
            AppBanner(text = "High-resolution rendering active for AMOLED displays.")
        }
    }
}

@Composable
private fun HintCardSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val sampleText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.ink)) {
            append("Drag the pin ")
        }
        append("to set which part of the photo stays visible on lock screen.")
    }

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppHintCard (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppHintCard(text = sampleText)
            AppHintCard(text = "Tap any color swatch below to replace active stop hue.")
        }
    }
}

@Composable
private fun DoneConfirmationSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "AppDoneConfirmation (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppDoneConfirmation(
                title = "Wallpaper applied",
                description = "Home and lock screen updated.",
            )

            AppDoneConfirmation(
                title = "Export Complete",
                description = "4K image saved to Pictures/Mural.",
                action = {
                    AppButton(
                        text = "Done",
                        onClick = {},
                        variant = AppButtonVariant.Secondary,
                    )
                },
            )
        }
    }
}

@Composable
private fun SnackbarSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppSnackbar (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppSnackbar(
                message = "Wallpaper saved to Library",
                actionLabel = "Undo",
                onActionClick = {},
            )

            AppSnackbar(
                message = "Daily wallpaper rotation scheduled",
            )
        }
    }
}
