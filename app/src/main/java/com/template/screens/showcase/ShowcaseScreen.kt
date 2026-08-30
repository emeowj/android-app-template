package com.template.screens.showcase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.template.ui.previews.DesignSystemShowcase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.parcelize.Parcelize

@Parcelize
data object ShowcaseScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object BackClicked : Event
    }
}

class ShowcasePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<ShowcaseScreen.State> {
    @CircuitInject(ShowcaseScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): ShowcasePresenter
    }

    @Composable
    override fun present(): ShowcaseScreen.State = ShowcaseScreen.State { event ->
        when (event) {
            ShowcaseScreen.Event.BackClicked -> navigator.pop()
        }
    }
}

@CircuitInject(ShowcaseScreen::class, AppScope::class)
@Composable
fun ShowcaseUi(state: ShowcaseScreen.State, modifier: Modifier = Modifier) {
    DesignSystemShowcase(
        modifier = modifier,
        onBackClick = { state.eventSink(ShowcaseScreen.Event.BackClicked) },
    )
}
