package com.template.ui.previews

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_9
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.ui.theme.AppDensity
import com.template.ui.theme.AppTypePairing
import org.junit.Rule
import org.junit.Test

class DesignSystemUiTest {
    @get:Rule
    val paparazzi: Paparazzi =
        Paparazzi(
            deviceConfig = PIXEL_9.copy(screenWidth = 1200, screenHeight = 7600),
            theme = "android:Theme.Material.Light.NoActionBar",
            renderingMode = SessionParams.RenderingMode.SHRINK,
        )

    @Test
    fun snapshotDesignSystemShowcaseLightEditorial() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = false,
                initialPairing = AppTypePairing.Editorial,
                initialDensity = AppDensity.Comfortable,
            )
        }
    }

    @Test
    fun snapshotDesignSystemShowcaseDarkEditorial() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = true,
                initialPairing = AppTypePairing.Editorial,
                initialDensity = AppDensity.Comfortable,
            )
        }
    }

    @Test
    fun snapshotDesignSystemShowcaseLiterary() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = false,
                initialPairing = AppTypePairing.Literary,
                initialDensity = AppDensity.Comfortable,
            )
        }
    }

    @Test
    fun snapshotDesignSystemShowcaseModern() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = false,
                initialPairing = AppTypePairing.Modern,
                initialDensity = AppDensity.Comfortable,
            )
        }
    }

    @Test
    fun snapshotDesignSystemShowcaseCompact() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = false,
                initialPairing = AppTypePairing.Editorial,
                initialDensity = AppDensity.Compact,
            )
        }
    }

    @Test
    fun snapshotDesignSystemShowcaseSpacious() {
        paparazzi.snapshot {
            DesignSystemShowcase(
                initialDarkTheme = false,
                initialPairing = AppTypePairing.Editorial,
                initialDensity = AppDensity.Spacious,
            )
        }
    }
}
