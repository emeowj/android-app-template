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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
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
import com.template.ui.LocalBottomBarPadding
import com.template.ui.previews.AppPreview
import com.template.ui.previews.Previews
import com.template.ui.theme.AppShape
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope

@CircuitInject(SearchScreen::class, AppScope::class)
@Composable
fun SearchUi(state: SearchScreen.State, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchTopbar(state = state)
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is SearchScreen.State.Empty ->
                    EmptyResultUi(paddingValues = paddingValues)

                is SearchScreen.State.Loaded ->
                    SearchResultUi(state = state, paddingValues = paddingValues)
            }
        }
    }
}

@Composable
private fun SearchTopbar(state: SearchScreen.State) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val background = MaterialTheme.colorScheme.surfaceContainer
    CenterAlignedTopAppBar(
        title = {
            val textFieldValue =
                remember(state.query) {
                    mutableStateOf(
                        TextFieldValue(
                            text = state.query,
                            selection = TextRange(state.query.length),
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = Padding.small),
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to background,
                        1f to Color.Transparent
                    )
                )
            }
            .statusBarsPadding()
    )
}

@Composable
private fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .padding(horizontal = Padding.small)
                .padding(top = Padding.extraSmall)
                .focusRequester(focusRequester)
                .dropShadow(
                    shape = RoundedCornerShape(50),
                    shadow = Shadow(
                        radius = 4.dp,
                        spread = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
                ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(onClick = onClear) {
                    Icon(painter = painterResource(R.drawable.ic_close), contentDescription = null)
                }
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = CircleShape,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
    )
}

@Composable
private fun EmptyResultUi(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.search_empty_hint),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchResultUi(
    state: SearchScreen.State.Loaded,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    if (state.results.isEmpty() && !state.isSearching) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.no_results),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Padding.small),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(Padding.hairline),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isSearching) {
                item(key = "loading") {
                    LoadingIndicator(modifier = Modifier.padding(Padding.medium))
                }
            }

            itemsIndexed(
                items = state.results,
                key = { _, item ->
                    item.trackId ?: item.collectionId ?: item.hashCode()
                },
            ) { index, result ->
                ResultItem(
                    result = result,
                    shape = AppShape.calculateListShape(index, state.results.size),
                    onClick = {
                        state.eventSink(SearchScreen.Event.ClickResult(result))
                    },
                    modifier = Modifier
                        .animateItem()
                        .padding(top = if (index == 0) Padding.small else 0.dp)
                )
            }

            if (state.results.isNotEmpty()) {
                item(key = "bottom-spacer") {
                    Spacer(modifier = Modifier.height(LocalBottomBarPadding.current))
                }
            }
        }
    }

}

@Composable
private fun ResultItem(
    result: ITunesResult,
    shape: Shape,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(Padding.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = result.artworkUrl100,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(AppShape.largeRadius - Padding.medium))
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(Padding.small))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.trackName ?: result.collectionName ?: "Unknown",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = result.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
