package com.template

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.template.screens.home.HomeScreen
import com.template.screens.home.HomeUi
import org.junit.Rule
import org.junit.Test

class ExamplePaparazziTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun snapshotHomeScreen() {
        paparazzi.snapshot {
            HomeUi(state = HomeScreen.State {})
        }
    }
}
