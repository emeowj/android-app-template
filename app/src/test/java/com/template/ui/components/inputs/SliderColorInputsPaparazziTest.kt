package com.template.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SliderColorInputsPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppSliderLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                SliderSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSliderDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                SliderSpecimen()
            }
        }
    }
}

@Composable
private fun SliderSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "AppSliderRow (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppSliderRow(
                label = "Blur",
                value = 0.40f,
                onValueChange = {},
                note = "Softens edges between gradient stops.",
                modifier = Modifier.fillMaxWidth(),
            )

            AppSliderRow(
                label = "Intensity",
                value = 0.72f,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
            )

            AppSliderRow(
                label = "Disabled Property",
                value = 0.50f,
                onValueChange = {},
                enabled = false,
                note = "This parameter is locked by current preset.",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
