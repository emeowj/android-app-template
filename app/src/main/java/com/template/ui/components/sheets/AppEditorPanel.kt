package com.template.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.AppSegmentedControl
import com.template.ui.components.AppSegmentedControlOption
import com.template.ui.components.inputs.AppSliderRow
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import com.template.ui.theme.panelShadow

object AppEditorPanelDefaults {
    val TopCornerRadius: Dp = AppShapes.DialogRadius
    val Shape: Shape = RoundedCornerShape(
        topStart = TopCornerRadius,
        topEnd = TopCornerRadius,
    )
    val DefaultMaxHeight: Dp = 520.dp
    val PencilIconSize: Dp = 15.dp
    val NameRadius: Dp = AppShapes.InputRadius
    val BottomPadding: Dp = Padding.md
    val HeaderPaddingTop: Dp = 10.dp
    val HeaderPaddingBottom: Dp = 10.dp
    val BodyPaddingTop: Dp = Padding.xs
    val BodyPaddingBottom: Dp = Padding.md
}

/**
 * Header section for [AppEditorPanel] (`.ed-panel-head`) with optional kicker,
 * editable wallpaper name button/row, and editor tab switcher controls.
 */
@Composable
fun AppEditorPanelHeader(
    wallpaperName: String,
    modifier: Modifier = Modifier,
    kicker: String? = null,
    onNameClick: (() -> Unit)? = null,
    tabs: (@Composable () -> Unit)? = null,
) {
    val colors = AppTheme.colors
    val density = LocalAppDensity.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = density.screenPadding,
                end = density.screenPadding,
                top = AppEditorPanelDefaults.HeaderPaddingTop,
                bottom = AppEditorPanelDefaults.HeaderPaddingBottom,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Padding.xs),
        ) {
            if (!kicker.isNullOrBlank()) {
                Text(
                    text = kicker.uppercase(),
                    style = AppTheme.typography.overline,
                    color = colors.inkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            val nameShape = RoundedCornerShape(AppEditorPanelDefaults.NameRadius)
            val nameModifier = if (onNameClick != null) {
                Modifier
                    .clip(nameShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = colors.ink),
                        role = Role.Button,
                        onClick = onNameClick,
                    )
                    .padding(end = 6.dp, top = 3.dp, bottom = 3.dp)
            } else {
                Modifier.padding(vertical = 3.dp)
            }

            Row(
                modifier = nameModifier,
                horizontalArrangement = Arrangement.spacedBy(Padding.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = wallpaperName,
                    style = AppTheme.typography.titleSm,
                    fontWeight = FontWeight.Normal,
                    color = colors.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit name",
                    tint = colors.inkMuted,
                    modifier = Modifier.size(AppEditorPanelDefaults.PencilIconSize),
                )
            }
        }

        if (tabs != null) {
            tabs()
        }
    }
}

/**
 * Persistent bottom panel for the wallpaper editor with max 66% viewport height,
 * 24dp top corners, panelShadow, editable wallpaper name header, and editor tab switcher.
 *
 * Uses [LazyColumn] for the body to allow parameter controls and slider rows to scroll smoothly.
 */
@Composable
fun AppEditorPanel(
    wallpaperName: String,
    modifier: Modifier = Modifier,
    kicker: String? = null,
    onNameClick: (() -> Unit)? = null,
    tabs: (@Composable () -> Unit)? = null,
    showGrabber: Boolean = true,
    onGrabberClick: (() -> Unit)? = null,
    state: LazyListState = rememberLazyListState(),
    maxHeight: Dp = AppEditorPanelDefaults.DefaultMaxHeight,
    content: LazyListScope.() -> Unit,
) {
    val colors = AppTheme.colors
    val density = LocalAppDensity.current

    Box(modifier = modifier.fillMaxWidth().heightIn(max = maxHeight)) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .panelShadow(shape = AppEditorPanelDefaults.Shape),
            shape = AppEditorPanelDefaults.Shape,
            color = colors.surface,
            contentColor = colors.ink,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppEditorPanelDefaults.BottomPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (showGrabber) {
                    val grabberModifier = if (onGrabberClick != null) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = onGrabberClick,
                        )
                    } else {
                        Modifier
                    }
                    AppSheetGrabber(modifier = grabberModifier)
                }

                AppEditorPanelHeader(
                    wallpaperName = wallpaperName,
                    kicker = kicker,
                    onNameClick = onNameClick,
                    tabs = tabs,
                )

                // Body (.panel-body) - LazyColumn allowing content to scroll smoothly
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    state = state,
                    contentPadding = PaddingValues(
                        start = density.screenPadding,
                        end = density.screenPadding,
                        top = AppEditorPanelDefaults.BodyPaddingTop,
                        bottom = AppEditorPanelDefaults.BodyPaddingBottom,
                    ),
                    verticalArrangement = Arrangement.spacedBy(Padding.md),
                    content = content,
                )
            }
        }
    }
}

private enum class EditorTab {
    COLOR,
    TUNING,
}

@ThemePreviews
@Composable
private fun AppEditorPanelPreview() {
    var intensity by remember { mutableFloatStateOf(0.72f) }
    var selectedTab by remember { mutableStateOf(EditorTab.COLOR) }

    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .fillMaxWidth()
                .padding(Padding.md)
                .wrapContentHeight(),
        ) {
            AppEditorPanel(
                kicker = "Gradient",
                wallpaperName = "Mesh drift",
                onNameClick = {},
                tabs = {
                    AppSegmentedControl(
                        selectedValue = selectedTab,
                        options = listOf(
                            AppSegmentedControlOption(
                                value = EditorTab.COLOR,
                                label = "Color",
                                iconRes = R.drawable.ic_palette,
                            ),
                            AppSegmentedControlOption(
                                value = EditorTab.TUNING,
                                label = "Tuning",
                                iconRes = R.drawable.ic_settings,
                            ),
                        ),
                        onOptionSelected = { selectedTab = it },
                        iconOnly = true,
                    )
                },
            ) {
                item {
                    AppSliderRow(
                        label = "Intensity",
                        value = intensity,
                        onValueChange = { intensity = it },
                    )
                }
            }
        }
    }
}
