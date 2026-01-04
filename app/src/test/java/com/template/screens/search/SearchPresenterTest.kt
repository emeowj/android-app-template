package com.template.screens.search

import com.slack.circuit.test.test
import com.template.data.itunes.ITunesClient
import com.template.data.itunes.ITunesResult
import com.template.data.itunes.ITunesSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainDispatcherRule(val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class SearchPresenterTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `initial state is empty`() = runTest {
        val client = createMockClient(ITunesSearchResponse(0, emptyList()))
        val presenter = SearchPresenter(SearchScreen(), client)

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
        val client = createMockClient(ITunesSearchResponse(1, results))
        val presenter = SearchPresenter(SearchScreen(), client)

        presenter.test {
            val initialState = awaitItem()
            initialState.eventSink(SearchScreen.Event.UpdateQuery("test"))

            // 1. Query update state
            val stateAfterQueryUpdate = awaitItem()
            assertEquals("test", stateAfterQueryUpdate.query)

            // 2. Searching state (after debounce)
            val searchingState = awaitItem()
            assertTrue(searchingState is SearchScreen.State.Loaded && searchingState.isSearching)

            // 3. Results loaded state
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
        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonString,
                headers =
                    headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val httpClient =
            HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                            coerceInputValues = true
                        }
                    )
                }
            }
        val client = ITunesClient(httpClient)
        val presenter = SearchPresenter(SearchScreen(), client)

        presenter.test {
            val initialState = awaitItem()
            initialState.eventSink(SearchScreen.Event.UpdateQuery("raye"))

            // 1. Query update state
            val stateAfterQueryUpdate = awaitItem()
            assertEquals("raye", stateAfterQueryUpdate.query)

            // 2. Searching state
            val searchingState = awaitItem()
            assertTrue(searchingState is SearchScreen.State.Loaded && searchingState.isSearching)

            // 3. Results loaded state
            val loadedState = awaitItem()
            assertTrue(loadedState is SearchScreen.State.Loaded)
            val loaded = loadedState as SearchScreen.State.Loaded
            assertEquals(53, loaded.results.size)
            assertEquals("RAYE", loaded.results[0].artistName)
            assertEquals("WHERE IS MY HUSBAND!", loaded.results[0].trackName)
            assertTrue(!loaded.isSearching)
        }
    }

    private fun createMockClient(response: ITunesSearchResponse): ITunesClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = json.encodeToString(response),
                headers =
                    headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val httpClient = HttpClient(mockEngine) { install(ContentNegotiation) { json(json) } }
        return ITunesClient(httpClient)
    }
}
