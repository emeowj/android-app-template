package com.template.screens.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.R
import com.template.data.settings.BaseSizeKey
import com.template.data.settings.BodyFontFamilyKey
import com.template.data.settings.DisplayFontFamilyKey
import com.template.data.settings.rememberEnumPreference
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppFontFamily
import com.template.ui.theme.AppShape
import com.template.ui.theme.FontPairing
import com.template.ui.theme.Padding
import com.template.ui.theme.ThemeEngine
import kotlinx.coroutines.launch

fun typographySheetOverlay(): BottomSheetOverlay<Unit, SelectionResult<Unit>> =
    BottomSheetOverlay(
        model = Unit,
        onDismiss = { SelectionResult.Cancelled },
        skipPartiallyExpandedState = true,
        dragHandle = {},
        contentWindowInsets = { WindowInsets(0) }
    ) { _, _ ->
        SelectionSheetContent(
            title = stringResource(R.string.settings_typography_title),
        ) {
            ContentWithOverlays { TypographySheetBody() }
        }
    }

@Composable
private fun TypographySheetBody(modifier: Modifier = Modifier) {
    var baseSize by rememberEnumPreference(BaseSizeKey)
    var displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    var bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)

    val fontFamilies = rememberFontFamilies()
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = Padding.medium)
            .padding(bottom = Padding.medium),
        verticalArrangement = Arrangement.spacedBy(Padding.medium),
    ) {
        item {
            SectionHeader(title = R.string.settings_typography_size_label)
            BaseSizeSlider(
                selected = baseSize,
                onSelect = { baseSize = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            SectionHeader(title = R.string.settings_typography_fonts_label)
            PairingPreviewRow(
                currentDisplay = displayFontFamily,
                currentBody = bodyFontFamily,
                fontFamilies = fontFamilies,
                onSelectPairing = { pairing ->
                    displayFontFamily = pairing.display
                    bodyFontFamily = pairing.body
                },
            )
        }

        item {
            AddPairingCard(
                onClick = {
                    scope.launch { overlayHost.show(pairingBuilderOverlay()) }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SectionHeader(@StringRes title: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = Padding.medium, bottom = Padding.small),
    )
}

@Composable
private fun PairingPreviewRow(
    currentDisplay: AppFontFamily,
    currentBody: AppFontFamily,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onSelectPairing: (FontPairing) -> Unit,
    modifier: Modifier = Modifier,
) {
    val current = FontPairing(currentDisplay, currentBody)

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .height(280.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = Padding.extraSmall),
        verticalArrangement = Arrangement.spacedBy(Padding.small),
        horizontalArrangement = Arrangement.spacedBy(Padding.small),
    ) {
        itemsIndexed(
            items = FontPairing.PRESETS,
            key = { _, p -> "${p.display.name}-${p.body.name}" },
        ) { _, pairing ->
            PairingCard(
                pairing = pairing,
                selected = pairing == current,
                fontFamilies = fontFamilies,
                onClick = { onSelectPairing(pairing) },
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .aspectRatio(ratio = 1.4f),
            )
        }
    }
}

@Composable
private fun PairingCard(
    pairing: FontPairing,
    selected: Boolean,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = stringResource(pairing.display.displayNameRes)
    val bodyName = stringResource(pairing.body.displayNameRes)

    Surface(
        onClick = onClick,
        shape = AppShape.card,
        color =
            if (selected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface,
        border =
            if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            else null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(Padding.medium),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = fontFamilies[pairing.display],
                ),
                color =
                    if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = bodyName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = fontFamilies[pairing.body],
                ),
                color =
                    if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AddPairingCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cd = stringResource(R.string.settings_typography_add_pairing_cd)
    Surface(
        onClick = onClick,
        shape = AppShape.listFull,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .semantics { contentDescription = cd },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Padding.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

private fun pairingBuilderOverlay(): BottomSheetOverlay<Unit, SelectionResult<Unit>> =
    BottomSheetOverlay(
        model = Unit,
        onDismiss = { SelectionResult.Cancelled },
        skipPartiallyExpandedState = true,
        dragHandle = {},
    ) { _, _ ->
        SelectionSheetContent(
            title = stringResource(R.string.settings_typography_pairing_builder_title),
        ) {
            PairingBuilderBody()
        }
    }

@Composable
private fun PairingBuilderBody(modifier: Modifier = Modifier) {
    var displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    var bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)
    val fontFamilies = rememberFontFamilies()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = Padding.medium)
            .padding(bottom = Padding.medium),
        verticalArrangement = Arrangement.spacedBy(Padding.medium),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PairingCard(
                pairing = FontPairing(displayFontFamily, bodyFontFamily),
                selected = false,
                fontFamilies = fontFamilies,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
            )
        }

        FontChipRow(
            label = stringResource(R.string.settings_display_font_title),
            selected = displayFontFamily,
            fontFamilies = fontFamilies,
            onSelect = { displayFontFamily = it },
        )

        FontChipRow(
            label = stringResource(R.string.settings_body_font_title),
            selected = bodyFontFamily,
            fontFamilies = fontFamilies,
            onSelect = { bodyFontFamily = it },
        )
    }
}

@Composable
private fun FontChipRow(
    label: String,
    selected: AppFontFamily,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onSelect: (AppFontFamily) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Padding.small),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Padding.small),
        ) {
            for (font in AppFontFamily.entries) {
                FontSuggestionChip(
                    label = stringResource(font.displayNameRes),
                    fontFamily = fontFamilies[font],
                    selected = font == selected,
                    onClick = { onSelect(font) },
                )
            }
        }
    }
}

@Composable
private fun FontSuggestionChip(
    label: String,
    fontFamily: FontFamily?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = AppShape.listFull,
        color =
            if (selected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface,
        border =
            if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 40.dp)
                .padding(horizontal = Padding.medium, vertical = Padding.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily),
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color =
                    if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
internal fun rememberFontFamilies(): Map<AppFontFamily, FontFamily> {
    val context = LocalContext.current
    return remember(context) {
        AppFontFamily.entries.associateWith { ThemeEngine.createFontFamily(it) }
    }
}

@Composable
@ThemePreviews
private fun TypographySheetPreview() {
    AppPreview {
        SelectionSheetContent(title = "Typography") {
            TypographySheetBody()
        }
    }
}

@Composable
@ThemePreviews
private fun PairingBuilderPreview() {
    AppPreview {
        SelectionSheetContent(title = "Custom Pairing") {
            PairingBuilderBody()
        }
    }
}
