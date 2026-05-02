package com.template.screens.search

import android.app.Application
import com.slack.circuit.test.test
import com.template.MainDispatcherRule
import com.template.data.itunes.ITunesResult
import com.template.data.itunes.ITunesSearchResponse
import com.template.di.BaseTestGraph
import com.template.testApplication
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchPresenterTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val json = Json { ignoreUnknownKeys = true }

    private val engine = MockEngine.Queue()
    private lateinit var presenterFactory: SearchPresenter.Factory

    @Before
    fun setUp() {
        presenterFactory = createGraphFactory<SearchPresenterTestGraph.Factory>()
            .create(testApplication(), engine)
            .presenterFactory
    }

    @Test
    fun `initial state is empty`() = runTest {
        enqueueResponse(ITunesSearchResponse(0, emptyList()))
        val presenter = presenterFactory.create(SearchScreen())

        presenter.test {
            val state = awaitItem()
            assertTrue(state is SearchScreen.State.Empty)
            assertEquals("", state.query)
        }
    }

    @Test
    fun `search updates results`() = runTest {
        val results =
            listOf(
                ITunesResult(
                    trackId = 1,
                    artistName = "Artist",
                    trackName = "Track",
                    wrapperType = "track",
                )
            )
        enqueueResponse(ITunesSearchResponse(1, results))
        val presenter = presenterFactory.create(SearchScreen())

        presenter.test {
            val initialState = awaitItem()
            initialState.eventSink(SearchScreen.Event.UpdateQuery("test"))

            val stateAfterQueryUpdate = awaitItem()
            assertEquals("test", stateAfterQueryUpdate.query)

            val searchingState = awaitItem()
            assertTrue(searchingState is SearchScreen.State.Loaded && searchingState.isSearching)

            val loadedState = awaitItem()
            assertTrue(loadedState is SearchScreen.State.Loaded)
            val loaded = loadedState as SearchScreen.State.Loaded
            assertEquals(1, loaded.results.size)
            assertEquals("Artist", loaded.results[0].artistName)
            assertTrue(!loaded.isSearching)
        }
    }

    @Test
    fun `should correctly parse real iTunes API response`() = runTest {
        val jsonString = javaClass.classLoader!!.getResource("search_result.json")!!.readText()
        engine.enqueue {
            respond(
                content = jsonString,
                headers =
                    headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val presenter = presenterFactory.create(SearchScreen())

        presenter.test {
            val initialState = awaitItem()
            initialState.eventSink(SearchScreen.Event.UpdateQuery("raye"))

            val stateAfterQueryUpdate = awaitItem()
            assertEquals("raye", stateAfterQueryUpdate.query)

            val searchingState = awaitItem()
            assertTrue(searchingState is SearchScreen.State.Loaded && searchingState.isSearching)

            val loadedState = awaitItem()
            assertTrue(loadedState is SearchScreen.State.Loaded)
            val loaded = loadedState as SearchScreen.State.Loaded
            assertEquals(53, loaded.results.size)
            assertEquals("RAYE", loaded.results[0].artistName)
            assertEquals("WHERE IS MY HUSBAND!", loaded.results[0].trackName)
            assertTrue(!loaded.isSearching)
        }
    }

    private fun enqueueResponse(response: ITunesSearchResponse) {
        engine.enqueue {
            respond(
                content = json.encodeToString(response),
                headers =
                    headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
    }
}

@DependencyGraph(AppScope::class)
interface SearchPresenterTestGraph : BaseTestGraph {
    val presenterFactory: SearchPresenter.Factory

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides application: Application,
            @Provides engine: MockEngine,
        ): SearchPresenterTestGraph
    }
}
