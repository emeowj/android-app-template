package com.template.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.navigation.AppListRow
import com.template.ui.components.navigation.AppListRowSurface
import com.template.ui.components.navigation.AppListRowTrailing
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import com.template.ui.theme.sheetShadow

object AppBottomSheetDefaults {
    val TopCornerRadius: Dp = AppShapes.DialogRadius
    val Shape: Shape = RoundedCornerShape(
        topStart = TopCornerRadius,
        topEnd = TopCornerRadius,
    )
    val GrabberWidth: Dp = 34.dp
    val GrabberHeight: Dp = Padding.xs
    val GrabberShape: Shape = RoundedCornerShape(AppShapes.PillRadius)
    val GrabberMarginTop: Dp = 10.dp
    val GrabberMarginBottom: Dp = Padding.xs
    val HeaderPaddingTop: Dp = Padding.xs
    val HeaderPaddingBottom: Dp = Padding.md
    val BodyPaddingTop: Dp = 12.dp
    val HairlineWidth: Dp = Padding.hairline
}

/**
 * 34×4dp pill grabber centered at the top of bottom sheets.
 */
@Composable
fun AppSheetGrabber(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.ink14,
) {
    Box(
        modifier = modifier
            .padding(
                top = AppBottomSheetDefaults.GrabberMarginTop,
                bottom = AppBottomSheetDefaults.GrabberMarginBottom,
            )
            .size(
                width = AppBottomSheetDefaults.GrabberWidth,
                height = AppBottomSheetDefaults.GrabberHeight,
            )
            .clip(AppBottomSheetDefaults.GrabberShape)
            .background(color),
    )
}

/**
 * Header section of [AppBottomSheet] (`.sheet-head`) with uppercase overline kicker,
 * titleMd heading, optional subtitle, optional trailing actions, and bottom hairline divider.
 */
@Composable
fun AppBottomSheetHeader(
    modifier: Modifier = Modifier,
    kicker: String? = null,
    title: String? = null,
    subtitle: String? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    if (kicker.isNullOrBlank() && title.isNullOrBlank() && subtitle.isNullOrBlank()) return

    val colors = AppTheme.colors
    val density = LocalAppDensity.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = density.screenPadding,
                    vertical = AppBottomSheetDefaults.HeaderPaddingTop,
                )
                .padding(bottom = AppBottomSheetDefaults.HeaderPaddingBottom),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Padding.xxs),
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
                if (!title.isNullOrBlank()) {
                    Text(
                        text = title,
                        style = AppTheme.typography.titleMd,
                        fontWeight = FontWeight.Normal,
                        color = colors.ink,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.bodySm,
                        color = colors.inkMuted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (actions != null) {
                actions()
            }
        }
        HorizontalDivider(
            thickness = AppBottomSheetDefaults.HairlineWidth,
            color = colors.hairline,
        )
    }
}

/**
 * Self-contained Bottom Sheet content surface with standardized 24dp top corners,
 * grabber pill, sheet-head (kicker + title + subtitle + bottom hairline), and full-width sheet-body.
 */
@Composable
fun AppBottomSheetContent(
    modifier: Modifier = Modifier,
    kicker: String? = null,
    title: String? = null,
    subtitle: String? = null,
    showGrabber: Boolean = true,
    headerActions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalAppDensity.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showGrabber) {
            AppSheetGrabber()
        }

        AppBottomSheetHeader(
            kicker = kicker,
            title = title,
            subtitle = subtitle,
            actions = headerActions,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppBottomSheetDefaults.BodyPaddingTop,
                    bottom = density.screenPadding,
                ),
            content = content,
        )
    }
}

/**
 * Standard modal bottom sheet wrapping Material 3 [ModalBottomSheet] with
 * custom styling (24dp top corners, sheetShadow, ink56 scrim, 34×4dp grabber).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    ),
    kicker: String? = null,
    title: String? = null,
    subtitle: String? = null,
    showGrabber: Boolean = true,
    headerActions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppTheme.colors

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = AppBottomSheetDefaults.Shape,
        containerColor = colors.surface,
        contentColor = colors.ink,
        scrimColor = colors.ink56,
        tonalElevation = 0.dp,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        AppBottomSheetContent(
            kicker = kicker,
            title = title,
            subtitle = subtitle,
            showGrabber = showGrabber,
            headerActions = headerActions,
            content = {
                content()
                Spacer(modifier = Modifier.navigationBarsPadding())
            },
        )
    }
}

@ThemePreviews
@Composable
private fun AppBottomSheetContentPreview() {
    AppPreview {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(Padding.md),
        ) {
            AppSheetSurface {
                AppBottomSheetContent(
                    kicker = "Theme",
                    title = "Choose a theme",
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        AppListRow(
                            title = "Midnight",
                            surface = AppListRowSurface.FlatSheet,
                            trailing = AppListRowTrailing.Checkmark,
                            onClick = {},
                        )
                        AppListRow(
                            title = "Paper",
                            surface = AppListRowSurface.FlatSheet,
                            onClick = {},
                        )
                        AppListRow(
                            title = "System",
                            surface = AppListRowSurface.FlatSheet,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}
