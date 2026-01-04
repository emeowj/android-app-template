package com.template.ui.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.Padding

data class ChoiceOption<T>(val value: T, val label: String)

sealed class SelectionResult<out T> {
    data class Selected<T>(val value: T) : SelectionResult<T>()

    data object Cancelled : SelectionResult<Nothing>()
}

data class SelectionModel<T>(
    @StringRes val titleRes: Int,
    val options: List<ChoiceOption<T>>,
    val selected: T,
    val confirmationMode: Boolean,
)

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

@Composable
fun SelectionSheetFooter(
    onCancel: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier,
    applyEnabled: Boolean = true,
    cancelEnabled: Boolean = true,
    cancelText: String = stringResource(R.string.overlay_cancel),
    applyText: String = stringResource(R.string.overlay_apply),
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(all = Padding.medium).navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(Padding.small),
    ) {
        OutlinedButton(
            onClick = onCancel,
            enabled = cancelEnabled,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = cancelText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
        }

        Button(onClick = onApply, enabled = applyEnabled, modifier = Modifier.weight(1f)) {
            Text(
                text = applyText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

fun <T> selectionSheetOverlay(
    @StringRes titleRes: Int,
    options: List<ChoiceOption<T>>,
    selected: T,
    confirmationMode: Boolean = false,
): BottomSheetOverlay<SelectionModel<T>, SelectionResult<T>> =
    BottomSheetOverlay(
        model = SelectionModel(titleRes, options, selected, confirmationMode),
        onDismiss = { SelectionResult.Cancelled },
        skipPartiallyExpandedState = true,
        dragHandle = {},
    ) { model, navigator ->
        SelectionSheet(
            title = stringResource(model.titleRes),
            options = model.options,
            initialSelected = model.selected,
            confirmationMode = model.confirmationMode,
            onSelect = { navigator.finish(SelectionResult.Selected(it)) },
            onCancel = { navigator.finish(SelectionResult.Cancelled) },
        )
    }

@Composable
internal fun <T> SelectionSheet(
    title: String,
    options: List<ChoiceOption<T>>,
    initialSelected: T,
    confirmationMode: Boolean,
    onSelect: (T) -> Unit,
    onCancel: () -> Unit,
) {
    var selectedValue by remember { mutableStateOf(initialSelected) }

    SelectionSheetContent(
        title = title,
        footer =
            if (confirmationMode) {
                {
                    SelectionSheetFooter(
                        onCancel = onCancel,
                        onApply = { onSelect(selectedValue) },
                        applyEnabled = selectedValue != initialSelected,
                    )
                }
            } else {
                null
            },
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = Padding.small).navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(Padding.hairline),
        ) {
            itemsIndexed(items = options) { index, option ->
                val isSelected = option.value == selectedValue
                Surface(
                    onClick = {
                        if (confirmationMode) {
                            selectedValue = option.value
                        } else {
                            onSelect(option.value)
                        }
                    },
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
                            Modifier.fillMaxWidth()
                                .heightIn(min = 56.dp)
                                .padding(horizontal = Padding.medium),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = isSelected, onClick = null)
                        Spacer(modifier = Modifier.width(Padding.small))
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.bodyLarge,
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
                Spacer(modifier = Modifier.navigationBarsPadding().padding(bottom = Padding.medium))
            }
        }
    }
}

@Composable
@ThemePreviews
private fun SelectionSheetPreview() {
    AppPreview {
        SelectionSheet(
            title = "Selection Title",
            options =
                listOf(
                    ChoiceOption(1, "Option 1"),
                    ChoiceOption(2, "Option 2"),
                    ChoiceOption(3, "Option 3"),
                ),
            initialSelected = 1,
            confirmationMode = true,
            onSelect = {},
            onCancel = {},
        )
    }
}
