package com.template.screens.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.Padding

data class ChoiceOption<T>(
    val value: T,
    val label: String,
    val labelStyle: TextStyle? = null,
)

sealed class SelectionResult<out T> {
    data class Selected<T>(val value: T) : SelectionResult<T>()

    data object Cancelled : SelectionResult<Nothing>()
}

@Composable
fun SelectionSheetContent(
    title: String,
    modifier: Modifier = Modifier,
    footer: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Padding.small),
        ) {
            CenterAlignedTopAppBar(
                title = { Text(text = title, fontWeight = FontWeight.Bold) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
            )

            content()

            if (footer != null) {
                footer()
            }
        }
    }
}

/**
 * Slot-based selection bottom sheet.
 *
 * The overlay owns the title bar; the [content] slot renders whatever option layout the
 * caller wants — radio rows, color chips, future swatches/sliders/etc. — and reports user
 * picks via the provided `onSelect` callback. `onSelect(value)` finishes the overlay
 * immediately with `Selected(value)`; dismissing the sheet resolves to `Cancelled`.
 */
fun <T> selectionSheetOverlay(
    @StringRes titleRes: Int,
    selected: T,
    content: @Composable (selected: T, onSelect: (T) -> Unit) -> Unit,
): BottomSheetOverlay<Unit, SelectionResult<T>> =
    BottomSheetOverlay(
        model = Unit,
        onDismiss = { SelectionResult.Cancelled },
        skipPartiallyExpandedState = true,
        dragHandle = {},
    ) { _, navigator ->
        SelectionSheetContent(title = stringResource(titleRes)) {
            content(selected) { navigator.finish(SelectionResult.Selected(it)) }
        }
    }

/**
 * Default radio-list rendering for [selectionSheetOverlay].
 *
 * Renders [options] as a `LazyColumn` of selectable rows with a leading [RadioButton], the
 * label styled per [ChoiceOption.labelStyle], and grouped corner shapes via
 * [AppShape.calculateListShape].
 */
@Composable
fun <T> RadioListSelectionContent(
    options: List<ChoiceOption<T>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = Padding.small)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(Padding.hairline),
    ) {
        itemsIndexed(items = options) { index, option ->
            val isSelected = option.value == selected
            Surface(
                onClick = { onSelect(option.value) },
                shape = AppShape.calculateListShape(index = index, size = options.size),
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .padding(horizontal = Padding.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = isSelected, onClick = null)
                    Spacer(modifier = Modifier.width(Padding.small))
                    Text(
                        text = option.label,
                        style = option.labelStyle ?: MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        color =
                            if (isSelected) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                }
            }
        }

        item(key = "spacer") {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = Padding.medium)
            )
        }
    }
}

@Composable
@ThemePreviews
private fun SelectionSheetPreview() {
    AppPreview {
        SelectionSheetContent(title = "Selection Title") {
            RadioListSelectionContent(
                options =
                    listOf(
                        ChoiceOption(1, "Option 1"),
                        ChoiceOption(2, "Option 2"),
                        ChoiceOption(3, "Option 3"),
                    ),
                selected = 1,
                onSelect = {},
            )
        }
    }
}
