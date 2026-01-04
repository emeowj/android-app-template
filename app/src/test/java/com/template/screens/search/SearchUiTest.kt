package com.template.screens.search

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.template.data.itunes.ITunesResult
import com.template.ui.theme.TemplateTheme
import org.junit.Rule
import org.junit.Test

class SearchUiTest {
    @get:Rule
    val paparazzi =
        Paparazzi(deviceConfig = PIXEL_5, theme = "android:Theme.Material.Light.NoActionBar")

    @Test
    fun snapshotSearchEmpty() {
        paparazzi.snapshot {
            TemplateTheme { SearchUi(state = SearchScreen.State.Empty(query = "", eventSink = {})) }
        }
    }

    @Test
    fun snapshotSearchLoaded() {
        paparazzi.snapshot {
            TemplateTheme {
                SearchUi(
                    state =
                        SearchScreen.State.Loaded(
                            query = "Jack Johnson",
                            results =
                                listOf(
                                    ITunesResult(
                                        trackId = 1,
                                        artistName = "Jack Johnson",
                                        trackName = "Better Together",
                                        collectionName = "In Between Dreams",
                                        artworkUrl100 = null,
                                        wrapperType = "track",
                                    ),
                                    ITunesResult(
                                        trackId = 2,
                                        artistName = "Jack Johnson",
                                        trackName = "Banana Pancakes",
                                        collectionName = "In Between Dreams",
                                        artworkUrl100 = null,
                                        wrapperType = "track",
                                    ),
                                ),
                            isSearching = false,
                            eventSink = {},
                        )
                )
            }
        }
    }

    @Test
    fun snapshotSearchSearching() {
        paparazzi.snapshot {
            TemplateTheme {
                SearchUi(
                    state =
                        SearchScreen.State.Loaded(
                            query = "Jack Johnson",
                            results = emptyList(),
                            isSearching = true,
                            eventSink = {},
                        )
                )
            }
        }
    }
}
