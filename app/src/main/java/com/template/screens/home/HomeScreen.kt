package com.template.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.template.R
import com.template.screens.settings.SettingsScreen
import com.template.ui.components.AppCard
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.components.navigation.AppTopBar
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(val eventSink: (Event) -> Unit) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object ClickSettings : Event
    }
}

@AssistedInject
class HomePresenter(@Assisted private val navigator: Navigator) : Presenter<HomeScreen.State> {
    @CircuitInject(HomeScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }

    @Composable
    override fun present(): HomeScreen.State = HomeScreen.State { event ->
        when (event) {
            HomeScreen.Event.ClickSettings -> navigator.goTo(SettingsScreen)
        }
    }
}

@CircuitInject(HomeScreen::class, AppScope::class)
@Composable
fun HomeUi(state: HomeScreen.State, modifier: Modifier = Modifier) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.app_name),
                subtitle = stringResource(R.string.home_title),
                trailing = {
                    AppIconButton(
                        onClick = { state.eventSink(HomeScreen.Event.ClickSettings) },
                        iconRes = R.drawable.ic_settings,
                        contentDescription = stringResource(R.string.nav_settings),
                    )
                },
            )
        },
        containerColor = colors.background,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Padding.lg),
            contentAlignment = Alignment.Center,
        ) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(Padding.lg),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.home_screen_text),
                        style = typography.titleMd,
                        color = colors.ink,
                    )
                    Spacer(modifier = Modifier.height(Padding.sm))
                    Text(
                        text = stringResource(R.string.appearance_type_sample),
                        style = typography.bodyMd,
                        color = colors.inkMuted,
                    )
                }
            }
        }
    }
}

@Composable
@ThemePreviews
private fun HomePreview() {
    AppPreview { HomeUi(state = HomeScreen.State {}) }
}
