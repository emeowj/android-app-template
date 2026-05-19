package com.template.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

@Immutable
data class AppSegmentedControlOption<T>(
    val value: T,
    val label: String,
    @DrawableRes val iconRes: Int? = null,
    val enabled: Boolean = true,
)

enum class SegmentLabelDisplay {
    VISIBLE,
    HIDDEN,
    ONLY_SELECTED_VISIBLE,
}

@Composable
fun <T> AppSegmentedControl(
    selectedValue: T,
    options: List<AppSegmentedControlOption<T>>,
    onOptionSelected: ((T) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelDisplay: SegmentLabelDisplay = SegmentLabelDisplay.VISIBLE,
) {
    if (options.isEmpty()) return

    val colors = LocalColorRoles.current
    Surface(
        modifier = modifier,
        shape = AppShape.pill,
        color = colors.surfaceAlt,
        border = BorderStroke(Padding.hairline, colors.hairline),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .selectableGroup()
                    .padding(Padding.xs),
            horizontalArrangement = Arrangement.spacedBy(Padding.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { option ->
                val selected = option.value == selectedValue
                val optionEnabled = enabled && option.enabled
                AppSegmentedControlItem(
                    option = option,
                    selected = selected,
                    enabled = optionEnabled,
                    labelDisplay = labelDisplay,
                    modifier = when (labelDisplay) {
                        SegmentLabelDisplay.VISIBLE -> Modifier.weight(1f)

                        SegmentLabelDisplay.HIDDEN -> Modifier.weight(1f)

                        SegmentLabelDisplay.ONLY_SELECTED_VISIBLE -> {
                            if (selected) {
                                Modifier.weight(2f)
                            } else {
                                Modifier.weight(1.2f)
                            }
                        }
                    },
                    onClick = if (onOptionSelected == null) {
                        null
                    } else {
                        {
                            if (!selected) onOptionSelected(option.value)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun <T> AppSegmentedControlItem(
    option: AppSegmentedControlOption<T>,
    selected: Boolean,
    enabled: Boolean,
    labelDisplay: SegmentLabelDisplay,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    val containerColor by
        animateColorAsState(
            targetValue = if (selected) colors.surface else Color.Transparent,
            label = "SegmentContainerColor",
        )
    val contentColor by
        animateColorAsState(
            targetValue =
                when {
                    !enabled -> colors.inkMuted.copy(alpha = 0.44f)
                    selected -> colors.ink
                    else -> colors.inkMuted
                },
            label = "SegmentContentColor",
        )

    Row(
        modifier =
            modifier
                .heightIn(min = 40.dp)
                .clip(AppShape.pill)
                .background(containerColor)
                .then(
                    if (onClick != null) {
                        Modifier
                            .selectable(
                                selected = selected,
                                enabled = enabled,
                                role = Role.RadioButton,
                                onClick = onClick,
                            )
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = Padding.md, vertical = Padding.sm),
        horizontalArrangement = Arrangement.spacedBy(
            Padding.sm,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        option.iconRes?.let { iconRes ->
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp),
            )
        }
        val showLabel = when (labelDisplay) {
            SegmentLabelDisplay.VISIBLE -> true
            SegmentLabelDisplay.HIDDEN -> false
            SegmentLabelDisplay.ONLY_SELECTED_VISIBLE -> selected
        }
        if (option.iconRes == null || showLabel) {
            Text(
                text = option.label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private enum class SegmentPreviewMode {
    STACK,
    GRID,
    HERO,
}

@ThemePreviews
@Composable
private fun AppSegmentedControlPreview() {
    var selected by remember { mutableStateOf(SegmentPreviewMode.STACK) }
    AppPreview {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            for (labelDisplay in SegmentLabelDisplay.entries) {
                AppSegmentedControl(
                    selectedValue = selected,
                    options =
                        listOf(
                            AppSegmentedControlOption(
                                value = SegmentPreviewMode.STACK,
                                label = "STACK",
                                iconRes = R.drawable.ic_view_list,
                            ),
                            AppSegmentedControlOption(
                                value = SegmentPreviewMode.GRID,
                                label = "GRID",
                                iconRes = R.drawable.ic_grid_view,
                            ),
                            AppSegmentedControlOption(
                                value = SegmentPreviewMode.HERO,
                                label = "HERO",
                                iconRes = R.drawable.ic_library,
                            ),
                        ),
                    onOptionSelected = { selected = it },
                    labelDisplay = labelDisplay,
                )
            }
        }
    }
}
