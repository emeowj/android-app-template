package com.template.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding
import com.template.ui.theme.TemplateTheme

@Composable
fun AppModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = AppShape.sheet,
        dragHandle = {
            DragHandle()
        },
        containerColor = LocalColorRoles.current.bg,
        tonalElevation = 4.dp,
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
    sheetShape = AppShape.sheet,
    dragHandle = {},
    skipPartiallyExpandedState = true,
    contentWindowInsets = { WindowInsets() },
) { sheetModel, navigator ->
    ContentWithOverlays {
        AppSheetSurface(modifier = Modifier.statusBarsPadding()) {
            if (showDragHandle) {
                DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            content(sheetModel, navigator)
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun DragHandle(modifier: Modifier = Modifier) {
    val colors = LocalColorRoles.current
    Box(
        modifier = modifier
            .padding(top = Padding.md, bottom = Padding.sm)
            .size(width = 80.dp, height = 5.dp)
            .clip(AppShape.pill)
            .background(colors.hairline),
    )
}

@Composable
fun AppSheetSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    TemplateTheme {
        val colors = LocalColorRoles.current
        Surface(
            color = colors.surface,
            shape = AppShape.sheet,
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding(),
                content = content,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AppSheetSurfacePreview() {
    AppPreview {
        AppSheetSurface(modifier = Modifier.padding(vertical = Padding.sm)) {
            DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))
            AppSheetHeader(
                kicker = "Preview",
                title = "Shared sheet surface",
            )
            Text(
                text = "Sheet content uses the same rounded surface and app theme.",
                modifier =
                    Modifier
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
