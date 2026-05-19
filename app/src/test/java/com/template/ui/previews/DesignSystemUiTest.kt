package com.template.ui.previews

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_9
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.ui.theme.ColorPreset
import org.junit.Rule
import org.junit.Test

class DesignSystemUiTest {
    @get:Rule
    val paparazzi: Paparazzi =
        Paparazzi(
            deviceConfig = PIXEL_9.copy(screenWidth = 2400, screenHeight = 3600),
            theme = "android:Theme.Material.Light.NoActionBar",
            renderingMode = SessionParams.RenderingMode.NORMAL,
        )

    @Test
    fun snapshotDesignSystemMidnightLight() {
        snapshotPreset(presetId = "midnight", isDark = false)
    }

    @Test
    fun snapshotDesignSystemMidnightDark() {
        snapshotPreset(presetId = "midnight", isDark = true)
    }

    @Test
    fun snapshotDesignSystemSageLight() {
        snapshotPreset(presetId = "sage", isDark = false)
    }

    @Test
    fun snapshotDesignSystemSageDark() {
        snapshotPreset(presetId = "sage", isDark = true)
    }

    @Test
    fun snapshotDesignSystemInkLight() {
        snapshotPreset(presetId = "ink", isDark = false)
    }

    @Test
    fun snapshotDesignSystemInkDark() {
        snapshotPreset(presetId = "ink", isDark = true)
    }

    @Test
    fun snapshotDesignSystemClayLight() {
        snapshotPreset(presetId = "clay", isDark = false)
    }

    @Test
    fun snapshotDesignSystemClayDark() {
        snapshotPreset(presetId = "clay", isDark = true)
    }

    private fun snapshotPreset(presetId: String, isDark: Boolean) {
        val preset = ColorPreset.OPTIONS.first { it.id == presetId }
        paparazzi.snapshot {
            AppPreview(
                darkTheme = isDark,
                colorRoles = preset.roles(isDark = isDark),
            ) {
                DesignSystemShowcase(
                    presetName = presetId,
                    modeLabel = if (isDark) "dark" else "light",
                )
            }
        }
    }
}
