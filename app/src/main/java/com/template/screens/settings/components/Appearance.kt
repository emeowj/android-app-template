package com.template.screens.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BottomSheetOverlay
import com.template.R
import com.template.data.settings.BaseSizeKey
import com.template.data.settings.BodyFontFamilyKey
import com.template.data.settings.ColorPresetIdKey
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.DisplayFontFamilyKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference
import com.template.ui.components.sheets.AppSheetHeader
import com.template.ui.components.sheets.AppSheetSurface
import com.template.ui.components.sheets.appBottomSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppFontFamily
import com.template.ui.theme.AppShape
import com.template.ui.theme.BaseSize
import com.template.ui.theme.ColorPreset
import com.template.ui.theme.ColorRoles
import com.template.ui.theme.FontPairing
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding
import com.template.ui.theme.ThemeEngine
import kotlinx.coroutines.launch

fun LazyListScope.appearance() {
    item(key = "palette") {
        var colorPresetId by rememberPreference(ColorPresetIdKey, ColorPreset.DEFAULT.id)
        val selectedPreset =
            ColorPreset.OPTIONS.find { it.id == colorPresetId } ?: ColorPreset.DEFAULT
        SettingsRowItem(
            row =
                SettingsRow(
                    title = stringResource(R.string.settings_palette_title),
                    value = stringResource(selectedPreset.nameRes),
                    onClick = rememberOpenAppearanceSheet(AppearanceSheet.PALETTE),
                ),
            showDivider = true,
        )
    }

    item(key = "mode") {
        var darkMode by rememberEnumPreference(DarkModeKey)
        SettingsRowItem(
            row =
                SettingsRow(
                    title = stringResource(R.string.settings_dark_mode_title),
                    value = darkMode.displayLabel(),
                    onClick = rememberOpenAppearanceSheet(AppearanceSheet.MODE),
                ),
            showDivider = true,
        )
    }

    item(key = "type") {
        var displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
        var bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)
        SettingsRowItem(
            row =
                SettingsRow(
                    title = stringResource(R.string.settings_type_pairing_title),
                    value =
                        stringResource(
                            R.string.settings_type_summary,
                            stringResource(displayFontFamily.displayNameRes),
                            bodyFontFamily.shortDisplayName(),
                        ),
                    onClick = rememberOpenAppearanceSheet(AppearanceSheet.TYPE),
                ),
            showDivider = true,
        )
    }

    item(key = "size") {
        var baseSize by rememberEnumPreference(BaseSizeKey)
        SettingsRowItem(
            row =
                SettingsRow(
                    title = stringResource(R.string.settings_typography_size_label),
                    value = stringResource(baseSize.displayNameRes),
                    onClick = rememberOpenAppearanceSheet(AppearanceSheet.SIZE),
                ),
            showDivider = false,
        )
    }
}

@Composable
private fun rememberOpenAppearanceSheet(sheet: AppearanceSheet): () -> Unit {
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()
    return { scope.launch { overlayHost.show(appearanceSheetOverlay(sheet)) } }
}

internal enum class AppearanceSheet(
    @StringRes val titleRes: Int,
    @StringRes val headingRes: Int,
) {
    PALETTE(R.string.settings_palette_title, R.string.settings_palette_sheet_heading),
    MODE(R.string.settings_dark_mode_title, R.string.settings_dark_mode_sheet_heading),
    TYPE(R.string.settings_type_pairing_title, R.string.settings_type_pairing_heading),
    SIZE(R.string.settings_typography_size_label, R.string.settings_typography_size_sheet_heading),
}

private fun appearanceSheetOverlay(sheet: AppearanceSheet): BottomSheetOverlay<AppearanceSheet, Unit> = appBottomSheetOverlay(
    model = sheet,
    onDismiss = {},
) { model, _ ->
    AppearanceSheetContent(model = model)
}

@Composable
internal fun AppearanceSheetContent(
    model: AppearanceSheet,
    modifier: Modifier = Modifier,
    typeCustomSelected: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(Padding.lg),
    ) {
        AppSheetHeader(
            kicker = stringResource(model.titleRes),
            title = stringResource(model.headingRes),
        )
        Column(modifier = Modifier.weight(1f, fill = false)) {
            AppearanceSheetOptions(
                model = model,
                typeCustomSelected = typeCustomSelected,
            )
        }
    }
}

@Composable
private fun AppearanceSheetOptions(
    model: AppearanceSheet,
    modifier: Modifier = Modifier,
    typeCustomSelected: Boolean = false,
) {
    when (model) {
        AppearanceSheet.PALETTE -> PaletteSheetOptions(modifier = modifier)

        AppearanceSheet.MODE -> ModeSheetOptions(modifier = modifier)

        AppearanceSheet.TYPE ->
            TypePairingSheetContent(
                initialCustomSelected = typeCustomSelected,
                modifier = modifier,
            )

        AppearanceSheet.SIZE -> SizeSheetOptions(modifier = modifier)
    }
}

@Composable
private fun PaletteSheetOptions(modifier: Modifier = Modifier) {
    var colorPresetId by rememberPreference(ColorPresetIdKey, ColorPreset.DEFAULT.id)
    val selectedPreset = ColorPreset.OPTIONS.find { it.id == colorPresetId } ?: ColorPreset.DEFAULT
    val isDark = LocalColorRoles.current == selectedPreset.roles(isDark = true)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(Padding.sm),
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
        contentPadding = PaddingValues(horizontal = Padding.lg, vertical = Padding.sm),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Padding.md),
    ) {
        items(ColorPreset.OPTIONS, key = { it.id }) { preset ->
            PaletteOption(
                preset = preset,
                selected = preset.id == colorPresetId,
                isDark = isDark,
                onClick = { colorPresetId = preset.id },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ModeSheetOptions(modifier: Modifier = Modifier) {
    var darkMode by rememberEnumPreference(DarkModeKey)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
        contentPadding =
            PaddingValues(
                horizontal = Padding.lg,
                vertical = Padding.sm,
            ),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(DarkMode.entries, key = { it.name }) { option ->
            ModeOption(
                mode = option,
                selected = option == darkMode,
                onClick = { darkMode = option },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun TypePairingSheetContent(
    initialCustomSelected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val fontFamilies = rememberFontFamilies()
    var displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    var bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)
    val currentPairing = FontPairing(displayFontFamily, bodyFontFamily)
    val presetSelected = FontPairing.PRESETS.any { it == currentPairing }
    var customSelected by rememberSaveable(initialCustomSelected, presetSelected) {
        mutableStateOf(initialCustomSelected || !presetSelected)
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = Padding.lg),
        verticalArrangement = Arrangement.spacedBy(Padding.lg),
        modifier = modifier.fillMaxWidth(),
    ) {
        item(key = "carousel") {
            TypePairingCarousel(
                currentPairing = currentPairing,
                customSelected = customSelected,
                fontFamilies = fontFamilies,
                onSelectPreset = { pairing ->
                    customSelected = false
                    displayFontFamily = pairing.display
                    bodyFontFamily = pairing.body
                },
                onSelectCustom = { customSelected = true },
            )
        }

        if (customSelected || !presetSelected) {
            item(key = "custom-display") {
                FontFamilyPickerRow(
                    label = stringResource(R.string.settings_display_font_title),
                    selected = displayFontFamily,
                    options = DisplayFontOptions,
                    fontFamilies = fontFamilies,
                    onSelect = {
                        customSelected = true
                        displayFontFamily = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(key = "custom-body") {
                FontFamilyPickerRow(
                    label = stringResource(R.string.settings_body_font_title),
                    selected = bodyFontFamily,
                    options = BodyFontOptions,
                    fontFamilies = fontFamilies,
                    onSelect = {
                        customSelected = true
                        bodyFontFamily = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            item(key = "hint") {
                Text(
                    text = stringResource(R.string.settings_type_pairing_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalColorRoles.current.inkMuted,
                    modifier = Modifier.padding(horizontal = Padding.lg),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TypePairingCarousel(
    currentPairing: FontPairing,
    customSelected: Boolean,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onSelectPreset: (FontPairing) -> Unit,
    onSelectCustom: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        horizontalArrangement = Arrangement.spacedBy(Padding.md),
        contentPadding = PaddingValues(horizontal = Padding.lg),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(FontPairing.PRESETS, key = { "${it.display.name}-${it.body.name}" }) { pairing ->
            TypePairingPreviewCard(
                label = pairing.label(),
                pairing = pairing,
                selected = !customSelected && pairing == currentPairing,
                fontFamilies = fontFamilies,
                onClick = { onSelectPreset(pairing) },
                modifier = Modifier
                    .width(TypePairingCardWidth)
                    .height(TypePairingCardHeight),
            )
        }

        item(key = "custom") {
            TypePairingPreviewCard(
                label = stringResource(R.string.settings_type_pairing_custom_label),
                pairing = currentPairing,
                selected = customSelected || FontPairing.PRESETS.none { it == currentPairing },
                fontFamilies = fontFamilies,
                onClick = onSelectCustom,
                modifier = Modifier
                    .width(TypePairingCardWidth)
                    .height(TypePairingCardHeight),
            )
        }
    }
}

@Composable
private fun TypePairingPreviewCard(
    label: String,
    pairing: FontPairing,
    selected: Boolean,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.card,
        color = if (selected) colors.surfaceAlt else colors.bg,
        border =
            BorderStroke(
                width = if (selected) 2.dp else Padding.hairline,
                color = if (selected) colors.accent else colors.hairline,
            ),
    ) {
        Column(
            modifier = Modifier.padding(Padding.lg),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) colors.accent else colors.inkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(R.string.appearance_preview_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = fontFamilies[pairing.display],
                    fontStyle = FontStyle.Italic,
                    color = colors.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(R.string.appearance_preview_progress),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = fontFamilies[pairing.body],
                    color = colors.inkSoft,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Padding.hairline)
                    .background(colors.hairline),
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.appearance_preview_stat_count),
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = fontFamilies[pairing.display],
                    color = colors.ink,
                )
                Spacer(modifier = Modifier.width(Padding.sm))
                Text(
                    text = stringResource(R.string.appearance_preview_stat_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = fontFamilies[pairing.body],
                    fontWeight = FontWeight.SemiBold,
                    color = colors.inkMuted,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FontFamilyPickerRow(
    label: String,
    selected: AppFontFamily,
    options: List<AppFontFamily>,
    fontFamilies: Map<AppFontFamily, FontFamily>,
    onSelect: (AppFontFamily) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    val listState = rememberLazyListState()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.inkMuted,
            modifier = Modifier.padding(horizontal = Padding.lg),
        )
        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            horizontalArrangement = Arrangement.spacedBy(Padding.md),
            contentPadding = PaddingValues(horizontal = Padding.lg),
        ) {
            items(options, key = { it.name }) { font ->
                FontFamilyOptionCard(
                    font = font,
                    selected = font == selected,
                    fontFamily = fontFamilies[font],
                    onClick = { onSelect(font) },
                    modifier = Modifier
                        .width(FontOptionCardWidth)
                        .height(FontOptionCardHeight),
                )
            }
        }
    }
}

@Composable
private fun FontFamilyOptionCard(
    font: AppFontFamily,
    selected: Boolean,
    fontFamily: FontFamily?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.card,
        color = if (selected) colors.surfaceAlt else colors.surface,
        border =
            BorderStroke(
                width = if (selected) 2.dp else Padding.hairline,
                color = if (selected) colors.accent else colors.hairline,
            ),
    ) {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.settings_base_size_sample),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = fontFamily,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(font.displayNameRes),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.inkSoft,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun FontPairing.label(): String = stringResource(
    R.string.settings_type_summary,
    display.typePairingShortName(),
    body.typePairingShortName(),
).uppercase()

@Composable
private fun AppFontFamily.typePairingShortName(): String = when (this) {
    AppFontFamily.HANKEN_GROTESK -> stringResource(R.string.settings_font_hanken_short)
    AppFontFamily.CORMORANT_GARAMOND -> stringResource(R.string.settings_font_cormorant_short)
    AppFontFamily.IBM_PLEX_SANS -> stringResource(R.string.settings_font_plex_short)
    else -> stringResource(displayNameRes)
}

@Composable
private fun SizeSheetOptions(modifier: Modifier = Modifier) {
    var baseSize by rememberEnumPreference(BaseSizeKey)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
        contentPadding =
            PaddingValues(
                horizontal = Padding.lg,
                vertical = Padding.sm,
            ),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(BaseSize.entries, key = { it.name }) { option ->
            BaseSizeOption(
                size = option,
                selected = option == baseSize,
                onClick = { baseSize = option },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PaletteOption(
    preset: ColorPreset,
    selected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    val roles = preset.roles(isDark)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SelectableCard(
            selected = selected,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
        ) {
            PaletteSwatch(roles = roles)
        }
        Text(
            text = stringResource(preset.nameRes),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) colors.ink else colors.inkSoft,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PaletteSwatch(roles: ColorRoles, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Padding.md),
        verticalArrangement = Arrangement.spacedBy(Padding.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(roles.ink),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.68f)
                    .height(7.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(roles.inkSoft),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Padding.xs)) {
            listOf(roles.accent, roles.surfaceAlt, roles.accentSoft).forEach { color ->
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(color),
                )
            }
        }
    }
}

@Composable
private fun ModeOption(
    mode: DarkMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitle =
        when (mode) {
            DarkMode.SYSTEM -> stringResource(R.string.appearance_mode_system_subtitle)
            DarkMode.LIGHT -> stringResource(R.string.appearance_mode_light_subtitle)
            DarkMode.DARK -> stringResource(R.string.appearance_mode_dark_subtitle)
        }
    SelectableCard(
        selected = selected,
        onClick = onClick,
        modifier = modifier.heightIn(min = 104.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.md),
            horizontalArrangement = Arrangement.spacedBy(Padding.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ModeGlyph(mode = mode)
            Column {
                Text(
                    text = mode.displayLabel(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalColorRoles.current.ink,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalColorRoles.current.inkMuted,
                )
            }
        }
    }
}

@Composable
private fun ModeGlyph(mode: DarkMode, modifier: Modifier = Modifier) {
    val light = Color(0xFFF4F2EE)
    val dark = Color(0xFF11141A)
    Surface(
        modifier = modifier.size(48.dp),
        color = if (mode == DarkMode.LIGHT) light else dark,
        shape = AppShape.medium,
    ) {
        Column(
            modifier = Modifier.padding(Padding.sm),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (mode == DarkMode.LIGHT) dark else light),
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.55f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFA8C0D6)),
            )
        }
    }
}

@Composable
private fun SelectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = LocalColorRoles.current
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.card,
        color = if (selected) colors.surfaceAlt else colors.bg,
        border =
            BorderStroke(
                width = if (selected) 2.dp else Padding.hairline,
                color = if (selected) colors.accent else colors.hairline,
            ),
        content = content,
    )
}

@Composable
private fun DarkMode.displayLabel(): String = when (this) {
    DarkMode.SYSTEM -> stringResource(R.string.appearance_mode_system)
    DarkMode.LIGHT -> stringResource(R.string.settings_dark_mode_light)
    DarkMode.DARK -> stringResource(R.string.settings_dark_mode_dark)
}

@Composable
private fun AppFontFamily.shortDisplayName(): String = when (this) {
    AppFontFamily.AVERIA_SERIF_LIBRE -> stringResource(R.string.settings_font_averia_short)
    AppFontFamily.HANKEN_GROTESK -> stringResource(R.string.settings_font_hanken_short)
    else -> stringResource(displayNameRes)
}

@Composable
internal fun rememberFontFamilies(): Map<AppFontFamily, FontFamily> {
    val context = LocalContext.current
    return remember(context) {
        AppFontFamily.entries.associateWith { ThemeEngine.createFontFamily(it) }
    }
}

private val TypePairingCardWidth = 304.dp
private val TypePairingCardHeight = 232.dp
private val FontOptionCardWidth = 164.dp
private val FontOptionCardHeight = 112.dp

private val DisplayFontOptions =
    listOf(
        AppFontFamily.ROBOTO_SERIF,
        AppFontFamily.FRAUNCES,
        AppFontFamily.CORMORANT_GARAMOND,
        AppFontFamily.LOBSTER,
        AppFontFamily.FASCINATE,
        AppFontFamily.FREDOKA,
        AppFontFamily.GOOGLE_SANS_FLEX,
        AppFontFamily.SPACE_GROTESK,
        AppFontFamily.HANKEN_GROTESK,
        AppFontFamily.INTER,
        AppFontFamily.IBM_PLEX_SANS,
    )

private val BodyFontOptions =
    listOf(
        AppFontFamily.HANKEN_GROTESK,
        AppFontFamily.INTER,
        AppFontFamily.IBM_PLEX_SANS,
        AppFontFamily.GOOGLE_SANS_FLEX,
        AppFontFamily.SPACE_GROTESK,
        AppFontFamily.FREDOKA,
        AppFontFamily.ROBOTO_SERIF,
        AppFontFamily.FRAUNCES,
        AppFontFamily.CORMORANT_GARAMOND,
        AppFontFamily.LOBSTER,
        AppFontFamily.FASCINATE,
    )

@Composable
@ThemePreviews
private fun AppearancePreview() {
    AppPreview {
        LazyColumn {
            appearance()
        }
    }
}

@Composable
@ThemePreviews
private fun AppearanceSheetPreview(
    @PreviewParameter(AppearanceSheetPreviewParameterProvider::class)
    sheet: AppearanceSheet,
) {
    AppPreview {
        AppSheetSurface {
            AppearanceSheetContent(model = sheet)
        }
    }
}

private class AppearanceSheetPreviewParameterProvider :
    CollectionPreviewParameterProvider<AppearanceSheet>(
        collection = AppearanceSheet.entries,
    )
