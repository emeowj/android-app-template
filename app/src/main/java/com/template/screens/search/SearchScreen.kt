package com.template.screens.search

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.template.data.itunes.ITunesResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchScreen(
    val initialQuery: String = ""
) : Screen {

    sealed interface State : CircuitUiState {
        val query: String
        val eventSink: (Event) -> Unit

        data class Empty(
            override val query: String = "",
            override val eventSink: (Event) -> Unit
        ) : State

        data class Loaded(
            override val query: String,
            val results: List<ITunesResult>,
            val isSearching: Boolean,
            override val eventSink: (Event) -> Unit
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data class UpdateQuery(val query: String) : Event
        data object Search : Event
        data object ClearQuery : Event
        data class ClickResult(val result: ITunesResult) : Event
    }
}
