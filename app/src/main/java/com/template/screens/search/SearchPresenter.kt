package com.template.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.template.data.itunes.ITunesClient
import com.template.data.itunes.ITunesResult
import com.template.ui.rememberRetainedCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SEARCH_DEBOUNCE_MS = 600L

@AssistedInject
class SearchPresenter(
    @Assisted private val screen: SearchScreen,
    private val iTunesClient: ITunesClient,
) : Presenter<SearchScreen.State> {
    @CircuitInject(screen = SearchScreen::class, scope = AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: SearchScreen): SearchPresenter
    }

    @Composable
    override fun present(): SearchScreen.State {
        val scope = rememberRetainedCoroutineScope()

        val provider =
            rememberRetained(screen, scope, iTunesClient) {
                SearchStateProvider(
                    initialQuery = screen.initialQuery,
                    iTunesClient = iTunesClient,
                    scope = scope,
                )
            }

        return provider.state
    }
}

@Stable
private class SearchStateProvider(
    initialQuery: String,
    private val iTunesClient: ITunesClient,
    private val scope: CoroutineScope,
) {
    private var query by mutableStateOf(initialQuery)
    private val results = mutableStateListOf<ITunesResult>()
    private var isSearching by mutableStateOf(false)
    private var searchJob: Job? = null

    init {
        if (initialQuery.isNotBlank()) {
            performSearch()
        }
    }

    val state: SearchScreen.State
        get() {
            return if (query.isBlank() && results.isEmpty()) {
                SearchScreen.State.Empty(query = query, eventSink = ::handleEvent)
            } else {
                SearchScreen.State.Loaded(
                    query = query,
                    results = results.toList(),
                    isSearching = isSearching,
                    eventSink = ::handleEvent,
                )
            }
        }

    private fun handleEvent(event: SearchScreen.Event) {
        when (event) {
            is SearchScreen.Event.UpdateQuery -> {
                query = event.query
                debouncedSearch()
            }

            SearchScreen.Event.Search -> performSearch()
            SearchScreen.Event.ClearQuery -> {
                query = ""
                results.clear()
            }

            is SearchScreen.Event.ClickResult -> {
                // Handle result click if needed
            }
        }
    }

    private fun debouncedSearch() {
        searchJob?.cancel()
        searchJob =
            scope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                performSearch()
            }
    }

    private fun performSearch() {
        if (query.isBlank()) {
            searchJob?.cancel()
            results.clear()
            return
        }

        searchJob?.cancel()
        searchJob =
            scope.launch {
                isSearching = true
                Timber.d("Searching for: $query")
                iTunesClient
                    .search(query)
                    .onSuccess { response ->
                        Timber.d("Found ${response.resultCount} results")
                        results.clear()
                        results.addAll(response.results)
                    }
                    .onFailure { error ->
                        Timber.e(error, "Search failed for: $query")
                        // Still clear results on failure to be safe?
                        // Or keep old results. Let's keep old ones but maybe show toast.
                    }
                isSearching = false
            }
    }
}
