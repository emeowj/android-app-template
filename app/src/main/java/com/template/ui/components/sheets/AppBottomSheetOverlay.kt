package com.template.ui.components.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import com.template.ui.theme.sheetShadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppTheme.colors
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = AppBottomSheetDefaults.Shape,
        dragHandle = {
            AppSheetGrabber()
        },
        containerColor = colors.surface,
        contentColor = colors.ink,
        scrimColor = colors.ink56,
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets.statusBars },
    ) {
        AppSheetSurface(content = content)
    }
}

fun <Model : Any, Result : Any> appBottomSheetOverlay(
    model: Model,
    onDismiss: () -> Result,
    showDragHandle: Boolean = true,
    content: @Composable (Model, OverlayNavigator<Result>) -> Unit,
): BottomSheetOverlay<Model, Result> = BottomSheetOverlay(
    model = model,
    onDismiss = onDismiss,
    sheetContainerColor = Color.Transparent,
    tonalElevation = 0.dp,
    sheetShape = AppBottomSheetDefaults.Shape,
    dragHandle = {},
    skipPartiallyExpandedState = true,
    contentWindowInsets = { WindowInsets() },
) { sheetModel, navigator ->
    ContentWithOverlays {
        AppSheetSurface(modifier = Modifier.statusBarsPadding()) {
            if (showDragHandle) {
                AppSheetGrabber(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            content(sheetModel, navigator)
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun AppSheetSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = AppTheme.colors
    Surface(
        color = colors.surface,
        contentColor = colors.ink,
        shape = AppBottomSheetDefaults.Shape,
        modifier = modifier
            .fillMaxWidth()
            .sheetShadow(shape = AppBottomSheetDefaults.Shape),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun AppSheetSurfacePreview() {
    AppPreview {
        AppSheetSurface(modifier = Modifier.padding(vertical = Padding.sm)) {
            AppSheetGrabber(modifier = Modifier.align(Alignment.CenterHorizontally))
            AppSheetHeader(
                kicker = "Preview",
                title = "Shared sheet surface",
            )
            Text(
                text = "Sheet content uses the same rounded surface and app theme.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding.lg),
            )
            AppSheetActionBar(
                secondaryLabel = "Cancel",
                onSecondary = {},
                primaryLabel = "Done",
                onPrimary = {},
            )
        }
    }
}
