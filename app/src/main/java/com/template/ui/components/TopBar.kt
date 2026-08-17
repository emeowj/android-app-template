package com.template.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults as MaterialTopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

object TopBarDefaults {
    val ButtonSize = 40.dp
    val ScrollThreshold = 60.dp
    val ContentHorizontalPadding = Padding.sm + Padding.xs
}

@Composable
fun rememberScrolledPast(
    listState: LazyListState,
    threshold: Dp = TopBarDefaults.ScrollThreshold,
): Boolean {
    val thresholdPx = with(LocalDensity.current) { threshold.roundToPx() }
    val scrolledPast: State<Boolean> =
        remember(listState, thresholdPx) {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > thresholdPx
            }
        }
    return scrolledPast.value
}

@Composable
fun rememberScrolledPast(
    gridState: LazyGridState,
    threshold: Dp = TopBarDefaults.ScrollThreshold,
): Boolean {
    val thresholdPx = with(LocalDensity.current) { threshold.roundToPx() }
    val scrolledPast: State<Boolean> =
        remember(gridState, thresholdPx) {
            derivedStateOf {
                gridState.firstVisibleItemIndex > 0 ||
                    gridState.firstVisibleItemScrollOffset > thresholdPx
            }
        }
    return scrolledPast.value
}

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    scrolledPast: Boolean = false,
    alwaysSolid: Boolean = false,
    includeStatusBarPadding: Boolean = true,
    background: Color = LocalColorRoles.current.bg,
    leading: @Composable RowScope.() -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val colors = LocalColorRoles.current
    val solid = alwaysSolid || scrolledPast
    val borderColor by
        animateColorAsState(
            targetValue = if (solid) colors.hairline else Color.Transparent,
            label = "TopBarBorder",
        )
    val containerColor by
        animateColorAsState(
            targetValue = if (solid) background else Color.Transparent,
            label = "TopBarContainer",
        )
    val titleAlpha by
        animateFloatAsState(
            targetValue = if (solid) 1f else 0f,
            label = "TopBarTitleAlpha",
        )
    val titleOffset by
        animateFloatAsState(
            targetValue = if (solid) 0f else 4f,
            label = "TopBarTitleOffset",
        )
    val density = LocalDensity.current
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier =
                    Modifier
                        .graphicsLayer {
                            alpha = titleAlpha
                            translationY = with(density) { titleOffset.dp.toPx() }
                        },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colors.inkMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier =
            modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    val strokeWidth = with(density) { Padding.hairline.toPx() }
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height - strokeWidth),
                        end = Offset(size.width, size.height - strokeWidth),
                        strokeWidth = strokeWidth,
                    )
                },
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Padding.xs, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                content = leading,
            )
        },
        actions = trailing,
        windowInsets =
            if (includeStatusBarPadding) {
                MaterialTopAppBarDefaults.windowInsets
            } else {
                WindowInsets(0)
            },
        colors =
            MaterialTopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                scrolledContainerColor = containerColor,
                navigationIconContentColor = colors.ink,
                titleContentColor = colors.ink,
                actionIconContentColor = colors.ink,
            ),
        contentPadding = PaddingValues(horizontal = TopBarDefaults.ContentHorizontalPadding),
    )
}

@Composable
fun TopBarButton(
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = Padding.sm),
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalColorRoles.current
    val semanticsModifier =
        if (contentDescription == null) {
            Modifier
        } else {
            Modifier.semantics { this.contentDescription = contentDescription }
        }

    Surface(
        onClick = onClick,
        modifier =
            modifier
                .then(semanticsModifier)
                .height(TopBarDefaults.ButtonSize)
                .widthIn(min = TopBarDefaults.ButtonSize),
        shape = AppShape.button,
        color = if (active) colors.surfaceAlt else Color.Transparent,
        contentColor = colors.ink,
    ) {
        Row(
            modifier =
                Modifier
                    .defaultMinSize(minWidth = TopBarDefaults.ButtonSize)
                    .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(Padding.xs, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun TopBarIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false,
) {
    TopBarButton(
        onClick = onClick,
        contentDescription = contentDescription,
        modifier = modifier,
        active = active,
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
    }
}

@ThemePreviews
@Preview(widthDp = 412)
@Composable
private fun AppTopBarPreview() {
    AppPreview {
        Box {
            AppTopBar(
                title = "Home",
                alwaysSolid = true,
                leading = {
                    TopBarIconButton(
                        iconRes = R.drawable.ic_arrow_back,
                        contentDescription = "Back",
                        onClick = {},
                    )
                },
                trailing = {
                    TopBarIconButton(
                        iconRes = R.drawable.ic_search,
                        contentDescription = "Search",
                        onClick = {},
                    )
                    TopBarButton(
                        onClick = {},
                        contentDescription = "Add item",
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(text = "New", style = MaterialTheme.typography.labelLarge)
                    }
                },
            )
        }
    }
}
