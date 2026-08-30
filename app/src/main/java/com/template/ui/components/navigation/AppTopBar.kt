package com.template.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme

object AppTopBarDefaults {
    val Height: Dp = 62.dp
    val ImmersiveHeight: Dp = 98.dp
    val ButtonSize: Dp = 44.dp
    val ContentHorizontalPadding: Dp = 10.dp
    val ScrollThreshold: Dp = 40.dp
    val HairlineWidth: Dp = 1.dp
}

@Composable
fun rememberScrolledPast(
    listState: LazyListState,
    threshold: Dp = AppTopBarDefaults.ScrollThreshold,
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
    threshold: Dp = AppTopBarDefaults.ScrollThreshold,
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
fun rememberTopBarScrolledPast(
    listState: LazyListState,
    threshold: Dp = AppTopBarDefaults.ScrollThreshold,
): Boolean = rememberScrolledPast(listState, threshold)

@Composable
fun rememberTopBarScrolledPast(
    gridState: LazyGridState,
    threshold: Dp = AppTopBarDefaults.ScrollThreshold,
): Boolean = rememberScrolledPast(gridState, threshold)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    immersive: Boolean = false,
    scrolledPast: Boolean = false,
    alwaysSolid: Boolean = false,
    includeStatusBarPadding: Boolean = true,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val isSolid = alwaysSolid || scrolledPast || !immersive

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSolid -> colors.background
            else -> Color.Transparent
        },
        label = "AppTopBarBg",
    )

    val hairlineColor by animateColorAsState(
        targetValue = when {
            isSolid -> colors.hairline
            else -> Color.Transparent
        },
        label = "AppTopBarHairline",
    )

    val titleColor by animateColorAsState(
        targetValue = when {
            !isSolid && immersive -> Color.White
            else -> colors.ink
        },
        label = "AppTopBarTitleColor",
    )

    val subtitleColor by animateColorAsState(
        targetValue = when {
            !isSolid && immersive -> Color.White.copy(alpha = 0.80f)
            else -> colors.inkMuted
        },
        label = "AppTopBarSubtitleColor",
    )

    val density = LocalDensity.current

    val backgroundModifier = if (immersive && !isSolid) {
        Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x9E13141A),
                    Color(0x0013141A),
                ),
            ),
        )
    } else {
        Modifier
    }

    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Text(
                    text = title,
                    style = typography.titleSm,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = typography.bodySm,
                        color = subtitleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        navigationIcon = {
            if (leading != null) {
                Row(
                    modifier = Modifier.height(AppTopBarDefaults.ButtonSize),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = leading,
                )
            }
        },
        actions = {
            if (trailing != null) {
                Row(
                    modifier = Modifier.height(AppTopBarDefaults.ButtonSize),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = trailing,
                )
            }
        },
        windowInsets = if (includeStatusBarPadding) {
            TopAppBarDefaults.windowInsets
        } else {
            WindowInsets(0)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor,
            navigationIconContentColor = colors.ink,
            titleContentColor = titleColor,
            actionIconContentColor = colors.ink,
        ),
        modifier = modifier
            .fillMaxWidth()
            .then(backgroundModifier)
            .drawWithContent {
                drawContent()
                if (hairlineColor != Color.Transparent) {
                    val strokeWidthPx = with(density) { AppTopBarDefaults.HairlineWidth.toPx() }
                    drawLine(
                        color = hairlineColor,
                        start = Offset(0f, size.height - strokeWidthPx),
                        end = Offset(size.width, size.height - strokeWidthPx),
                        strokeWidth = strokeWidthPx,
                    )
                }
            },
    )
}

@Composable
fun AppDrillInTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrolledPast: Boolean = false,
    includeStatusBarPadding: Boolean = true,
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val density = LocalDensity.current

    val hairlineColor by animateColorAsState(
        targetValue = if (scrolledPast) colors.hairline else Color.Transparent,
        label = "AppDrillInTopBarHairline",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (includeStatusBarPadding) Modifier.statusBarsPadding() else Modifier)
            .height(AppTopBarDefaults.Height)
            .background(colors.background)
            .drawWithContent {
                drawContent()
                if (hairlineColor != Color.Transparent) {
                    val strokeWidthPx = with(density) { AppTopBarDefaults.HairlineWidth.toPx() }
                    drawLine(
                        color = hairlineColor,
                        start = Offset(0f, size.height - strokeWidthPx),
                        end = Offset(size.width, size.height - strokeWidthPx),
                        strokeWidth = strokeWidthPx,
                    )
                }
            }
            .padding(horizontal = AppTopBarDefaults.ContentHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppIconButton(
            onClick = onBackClick,
            iconRes = R.drawable.ic_arrow_back,
            contentDescription = stringResource(R.string.nav_back),
        )
        Text(
            text = title,
            style = typography.titleSm,
            color = colors.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ThemePreviews
@Composable
private fun AppTopBarStandardPreview() {
    AppPreview {
        Column {
            AppTopBar(
                title = "Mural",
                subtitle = "Library",
                includeStatusBarPadding = false,
                leading = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_settings,
                        contentDescription = "Settings",
                    )
                },
                trailing = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_search,
                        contentDescription = "Search",
                    )
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_add,
                        contentDescription = "Add",
                    )
                },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppTopBarImmersivePreview() {
    AppPreview {
        Box(modifier = Modifier.background(Color(0xFF2C3E50))) {
            AppTopBar(
                title = "Explore",
                subtitle = "Curated collections",
                immersive = true,
                scrolledPast = false,
                includeStatusBarPadding = false,
                leading = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_arrow_back,
                        contentDescription = "Back",
                        overlay = true,
                    )
                },
                trailing = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_share,
                        contentDescription = "Share",
                        overlay = true,
                    )
                },
            )
        }
    }
}
