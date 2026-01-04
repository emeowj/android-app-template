package com.template.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.template.R
import com.template.screens.settings.SettingsScreen
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import dev.zacsweers.metro.AppScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object ClickSettings : Event
    }
}

@CircuitInject(HomeScreen::class, AppScope::class)
class HomePresenter(
    private val navigator: Navigator
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State { event ->
            when (event) {
                HomeScreen.Event.ClickSettings -> navigator.goTo(SettingsScreen)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(HomeScreen::class, AppScope::class)
@Composable
fun HomeUi(state: HomeScreen.State, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                actions = {
                    IconButton(onClick = { state.eventSink(HomeScreen.Event.ClickSettings) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.nav_settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.home_screen_text))
        }
    }
}

@Composable
@ThemePreviews
private fun HomePreview() {
    AppPreview {
        HomeUi(state = HomeScreen.State { })
    }
}
