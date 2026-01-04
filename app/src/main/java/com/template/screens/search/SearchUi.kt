package com.template.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.template.R
import com.template.data.itunes.ITunesResult
import com.template.ui.previews.AppPreview
import com.template.ui.previews.Previews
import com.template.ui.theme.AppShape
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SearchScreen::class, AppScope::class)
@Composable
fun SearchUi(state: SearchScreen.State, modifier: Modifier = Modifier) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val textFieldValue = remember(state.query) {
                        mutableStateOf(
                            TextFieldValue(
                                text = state.query,
                                selection = TextRange(state.query.length)
                            )
                        )
                    }
                    SearchTextField(
                        value = textFieldValue.value,
                        onValueChange = { newValue ->
                            textFieldValue.value = newValue
                            if (newValue.text != state.query) {
                                state.eventSink(SearchScreen.Event.UpdateQuery(newValue.text))
                            }
                        },
                        onSearch = {
                            keyboardController?.hide()
                            state.eventSink(SearchScreen.Event.Search)
                        },
                        onClear = {
                            textFieldValue.value = TextFieldValue()
                            state.eventSink(SearchScreen.Event.ClearQuery)
                        },
                        focusRequester = focusRequester,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                contentPadding = PaddingValues(horizontal = Padding.small)
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (state) {
                is SearchScreen.State.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_empty_hint),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is SearchScreen.State.Loaded -> {
                    if (state.results.isEmpty() && !state.isSearching) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_results),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = Padding.small),
                            contentPadding = paddingValues,
                            verticalArrangement = Arrangement.spacedBy(Padding.hairline)
                        ) {
                            itemsIndexed(
                                items = state.results,
                                key = { _, item ->
                                    item.trackId ?: item.collectionId ?: item.hashCode()
                                }
                            ) { index, result ->
                                ResultItem(
                                    result = result,
                                    shape = AppShape.calculateListShape(index, state.results.size),
                                    onClick = {
                                        state.eventSink(
                                            SearchScreen.Event.ClickResult(
                                                result
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (state.isSearching) {
                        LoadingIndicator(
                            modifier = Modifier
                                .padding(paddingValues)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(horizontal = Padding.small)
            .padding(top = Padding.extraSmall)
            .focusRequester(focusRequester),
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = onClear) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = CircleShape,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun ResultItem(
    result: ITunesResult,
    shape: Shape,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(Padding.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = result.artworkUrl600 ?: result.artworkUrl100,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(AppShape.largeRadius - Padding.medium))
                    .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(Padding.small))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.trackName ?: result.collectionName ?: "Unknown",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                        wrapperType = "track"
                    ),
                    ITunesResult(
                        trackId = 2,
                        artistName = "Jack Johnson",
                        trackName = "Banana Pancakes",
                        collectionName = "In Between Dreams",
                        artworkUrl100 = null,
                        wrapperType = "track"
                    )
                ),
                isSearching = false,
                eventSink = {}
            )
        )
    }
}
