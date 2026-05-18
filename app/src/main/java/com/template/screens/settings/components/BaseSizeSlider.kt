package com.template.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.BaseSize
import com.template.ui.theme.Padding

@Composable
fun BaseSizeSlider(
    selected: BaseSize,
    onSelect: (BaseSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    val entries = BaseSize.entries
    val selectedIndex = entries.indexOf(selected).coerceAtLeast(0)

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = AppShape.card,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.medium, vertical = Padding.small),
            verticalArrangement = Arrangement.spacedBy(Padding.small),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.small),
            ) {
                entries.forEachIndexed { index, size ->
                    val isSelected = index == selectedIndex
                    Text(
                        text = stringResource(size.displayNameRes),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color =
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        textAlign =
                            when (index) {
                                0 -> TextAlign.Start
                                entries.lastIndex -> TextAlign.End
                                else -> TextAlign.Center
                            },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Slider(
                value = selectedIndex.toFloat(),
                onValueChange = { value ->
                    val newIndex = value.toInt().coerceIn(0, entries.lastIndex)
                    if (newIndex != selectedIndex) onSelect(entries[newIndex])
                },
                valueRange = 0f..entries.lastIndex.toFloat(),
                steps = entries.size - 2,
            )
        }
    }
}

@Composable
@ThemePreviews
private fun BaseSizeSliderPreview() {
    AppPreview {
        BaseSizeSlider(
            selected = BaseSize.MEDIUM,
            onSelect = {},
            modifier = Modifier.padding(Padding.medium),
        )
    }
}
