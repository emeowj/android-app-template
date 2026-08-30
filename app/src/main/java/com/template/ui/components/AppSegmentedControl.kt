package com.template.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.appFocusRing

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

object AppSegmentedControlDefaults {
    val TrackRadius: Dp = AppShapes.InputRadius
    val SegmentRadius: Dp = AppShapes.SwatchRadius
    val TrackShape: Shape = RoundedCornerShape(TrackRadius)
    val SegmentShape: Shape = RoundedCornerShape(SegmentRadius)
    val TrackPadding: Dp = 3.dp
    val SegmentGap: Dp = 2.dp
    val MinSegmentHeight: Dp = 38.dp
    val IconOnlySegmentSize: Dp = 38.dp
    val IconSize: Dp = 18.dp
}

@Composable
fun <T> AppSegmentedControl(
    selectedValue: T,
    options: List<AppSegmentedControlOption<T>>,
    onOptionSelected: ((T) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconOnly: Boolean = false,
    labelDisplay: SegmentLabelDisplay = if (iconOnly) SegmentLabelDisplay.HIDDEN else SegmentLabelDisplay.VISIBLE,
    role: Role = Role.RadioButton,
) {
    if (options.isEmpty()) return

    val colors = AppTheme.colors

    Surface(
        modifier = modifier
            .appFocusRing(visible = false, shape = AppSegmentedControlDefaults.TrackShape, ringColor = colors.accent),
        shape = AppSegmentedControlDefaults.TrackShape,
        color = colors.ink08,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier.padding(AppSegmentedControlDefaults.TrackPadding),
        ) {
            // Items Row
            Row(
                modifier = (if (iconOnly) Modifier.wrapContentWidth() else Modifier.fillMaxWidth())
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(AppSegmentedControlDefaults.SegmentGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                options.forEach { option ->
                    val selected = option.value == selectedValue
                    val optionEnabled = enabled && option.enabled

                    val contentColor by animateColorAsState(
                        targetValue = when {
                            !optionEnabled -> colors.inkMuted.copy(alpha = 0.44f)
                            selected -> colors.ink
                            else -> colors.inkMuted
                        },
                        label = "SegmentContentColor",
                    )

                    val itemModifier = if (iconOnly) {
                        Modifier.size(AppSegmentedControlDefaults.IconOnlySegmentSize)
                    } else {
                        when (labelDisplay) {
                            SegmentLabelDisplay.VISIBLE -> Modifier.weight(1f)

                            SegmentLabelDisplay.HIDDEN -> Modifier.weight(1f)

                            SegmentLabelDisplay.ONLY_SELECTED_VISIBLE -> {
                                if (selected) Modifier.weight(2f) else Modifier.weight(1.2f)
                            }
                        }
                    }

                    val selectedBgModifier = if (selected) {
                        Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = AppSegmentedControlDefaults.SegmentShape,
                                clip = false,
                            )
                            .background(
                                color = colors.surface,
                                shape = AppSegmentedControlDefaults.SegmentShape,
                            )
                    } else {
                        Modifier
                    }

                    Box(
                        modifier = itemModifier
                            .heightIn(min = AppSegmentedControlDefaults.MinSegmentHeight)
                            .then(selectedBgModifier)
                            .clip(AppSegmentedControlDefaults.SegmentShape)
                            .selectable(
                                selected = selected,
                                enabled = optionEnabled,
                                role = role,
                                onClick = {
                                    if (!selected) {
                                        onOptionSelected?.invoke(option.value)
                                    }
                                },
                            )
                            .padding(
                                horizontal = if (iconOnly) 0.dp else 12.dp,
                                vertical = 6.dp,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                6.dp,
                                Alignment.CenterHorizontally,
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            option.iconRes?.let { iconRes ->
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(AppSegmentedControlDefaults.IconSize),
                                )
                            }
                            val showLabel = when (labelDisplay) {
                                SegmentLabelDisplay.VISIBLE -> !iconOnly
                                SegmentLabelDisplay.HIDDEN -> false
                                SegmentLabelDisplay.ONLY_SELECTED_VISIBLE -> selected
                            }
                            if ((option.iconRes == null || showLabel) && !iconOnly) {
                                Text(
                                    text = option.label,
                                    style = AppTheme.typography.bodySm,
                                    color = contentColor,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Standard labeled
            AppSegmentedControl(
                selectedValue = selected,
                options = listOf(
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.STACK,
                        label = "Stack",
                        iconRes = R.drawable.ic_view_list,
                    ),
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.GRID,
                        label = "Grid",
                        iconRes = R.drawable.ic_grid_view,
                    ),
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.HERO,
                        label = "Hero",
                        iconRes = R.drawable.ic_category_photo,
                    ),
                ),
                onOptionSelected = { selected = it },
            )

            // Icon only
            AppSegmentedControl(
                selectedValue = selected,
                options = listOf(
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.STACK,
                        label = "Stack",
                        iconRes = R.drawable.ic_view_list,
                    ),
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.GRID,
                        label = "Grid",
                        iconRes = R.drawable.ic_grid_view,
                    ),
                    AppSegmentedControlOption(
                        value = SegmentPreviewMode.HERO,
                        label = "Hero",
                        iconRes = R.drawable.ic_category_photo,
                    ),
                ),
                onOptionSelected = { selected = it },
                iconOnly = true,
            )
        }
    }
}
