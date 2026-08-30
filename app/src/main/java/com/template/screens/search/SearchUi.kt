package com.template.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.template.R
import com.template.data.itunes.ITunesResult
import com.template.ui.LocalBottomBarPadding
import com.template.ui.components.feedback.AppEmptyState
import com.template.ui.components.inputs.AppSearchField
import com.template.ui.previews.AppPreview
import com.template.ui.previews.Previews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope

@CircuitInject(SearchScreen::class, AppScope::class)
@Composable
fun SearchUi(state: SearchScreen.State, modifier: Modifier = Modifier) {
    val colors = AppTheme.colors
    val densityTokens = LocalAppDensity.current

    Scaffold(
        modifier = modifier,
        topBar = { SearchTopbar(state = state) },
        containerColor = colors.background,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (state) {
                is SearchScreen.State.Empty -> EmptyResultUi()
                is SearchScreen.State.Loaded -> SearchResultUi(state = state)
            }
        }
    }
}

@Composable
private fun SearchTopbar(state: SearchScreen.State) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = AppTheme.colors
    val densityTokens = LocalAppDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .statusBarsPadding()
            .padding(horizontal = densityTokens.screenPadding)
            .padding(top = Padding.sm, bottom = Padding.sm),
    ) {
        AppSearchField(
            value = state.query,
            onValueChange = { newValue ->
                if (newValue != state.query) {
                    state.eventSink(SearchScreen.Event.UpdateQuery(newValue))
                }
            },
            placeholder = stringResource(R.string.search_hint),
            onClear = { state.eventSink(SearchScreen.Event.ClearQuery) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    state.eventSink(SearchScreen.Event.Search)
                },
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun EmptyResultUi(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AppEmptyState(
            title = stringResource(R.string.search_title),
            description = stringResource(R.string.search_empty_hint),
        )
    }
}

@Composable
private fun SearchResultUi(
    state: SearchScreen.State.Loaded,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val densityTokens = LocalAppDensity.current

    if (state.results.isEmpty() && !state.isSearching) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AppEmptyState(
                title = stringResource(R.string.no_results),
                description = stringResource(R.string.search_empty_hint),
            )
        }
    } else {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = densityTokens.screenPadding),
            contentPadding = PaddingValues(top = Padding.sm, bottom = LocalBottomBarPadding.current + 80.dp),
            verticalArrangement = Arrangement.spacedBy(Padding.hairline),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isSearching) {
                item(key = "loading") {
                    CircularProgressIndicator(
                        color = colors.accent,
                        modifier = Modifier.padding(Padding.md),
                    )
                }
            }

            itemsIndexed(
                items = state.results,
                key = { _, item -> item.trackId ?: item.collectionId ?: item.hashCode() },
            ) { index, result ->
                ResultItem(
                    result = result,
                    shape = AppShapes.listItemShape(index, state.results.size),
                    onClick = { state.eventSink(SearchScreen.Event.ClickResult(result)) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun ResultItem(
    result: ITunesResult,
    shape: Shape,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        onClick = onClick,
        shape = shape,
        color = colors.surface,
        contentColor = colors.ink,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(Padding.md)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = result.artworkUrl100,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(AppShapes.InputRadius))
                    .background(color = colors.ink04),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(Padding.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.trackName ?: result.collectionName ?: "Unknown",
                    style = typography.bodyLg,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = result.artistName,
                    style = typography.bodyMd,
                    color = colors.inkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Previews
@Composable
private fun SearchUiPreview() {
    AppPreview {
        SearchUi(
            state = SearchScreen.State.Loaded(
                query = "Jack Johnson",
                results = listOf(
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
            ),
        )
    }
}
