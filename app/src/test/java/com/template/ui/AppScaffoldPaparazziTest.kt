package com.template.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.slack.circuit.foundation.Circuit
import com.template.screens.home.HomeScreen
import com.template.screens.home.HomeUi
import com.template.screens.search.SearchScreen
import com.template.screens.search.SearchUi
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class AppScaffoldPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.NORMAL,
    )

    private val testCircuit = Circuit.Builder()
        .addUi<HomeScreen, HomeScreen.State> { _, modifier ->
            HomeUi(state = HomeScreen.State {}, modifier = modifier)
        }
        .addUi<SearchScreen, SearchScreen.State> { _, modifier ->
            SearchUi(state = SearchScreen.State.Empty(query = "", eventSink = {}), modifier = modifier)
        }
        .setOnUnavailableContent { screen, modifier ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Preview placeholder for ${screen::class.simpleName}")
            }
        }
        .build()

    @Test
    fun snapshotAppScaffoldLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                AppScaffold(circuit = testCircuit)
            }
        }
    }

    @Test
    fun snapshotAppScaffoldDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                AppScaffold(circuit = testCircuit)
            }
        }
    }
}
