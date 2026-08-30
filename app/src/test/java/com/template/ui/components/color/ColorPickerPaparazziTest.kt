package com.template.ui.components.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ColorPickerPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppColorFieldLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ColorFieldSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppColorFieldDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ColorFieldSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppColorPopoverCardLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ColorPopoverSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppColorPopoverCardDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ColorPopoverSpecimen()
            }
        }
    }
}

@Composable
private fun ColorFieldSpecimen() {
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
                text = "AppColorField (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Surface(
                color = colors.surface,
                shape = AppTheme.shapes.card,
            ) {
                Column {
                    AppColorField(
                        label = "Symbol colour 1",
                        color = Color(0xFFD97757),
                        onClick = {},
                        expanded = false,
                        showTopDivider = false,
                    )
                    AppColorField(
                        label = "Symbol colour 2 (Active/Open)",
                        color = Color(0xFF5FA8D3),
                        onClick = {},
                        expanded = true,
                        showTopDivider = true,
                    )
                    AppColorField(
                        label = "Symbol colour 3",
                        color = Color(0xFF7B4FD1),
                        onClick = {},
                        expanded = false,
                        showTopDivider = true,
                    )
                    AppColorField(
                        label = "Field colour",
                        color = Color(0xFF13141A),
                        onClick = {},
                        expanded = false,
                        showTopDivider = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorPopoverSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AppColorPopoverCard (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                AppColorPopoverCard(
                    color = Color(0xFF5FA8D3),
                    onColorChange = {},
                )
            }
        }
    }
}
