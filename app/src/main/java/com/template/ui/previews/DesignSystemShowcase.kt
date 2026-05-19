package com.template.ui.previews

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.AppCard
import com.template.ui.components.AppChip
import com.template.ui.components.AppChipStyle
import com.template.ui.components.AppSegmentedControl
import com.template.ui.components.AppSegmentedControlOption
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.components.buttons.AppIconButtonTone
import com.template.ui.components.inputs.AppSearchField
import com.template.ui.components.inputs.AppTextField
import com.template.ui.components.sheets.AppSheetActionRow
import com.template.ui.components.sheets.AppSheetSectionLabel
import com.template.ui.theme.AppShape
import com.template.ui.theme.ColorPreset
import com.template.ui.theme.ColorRoles
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding

private val FullWidth = GridItemSpan(2)
private val HalfWidth = GridItemSpan(1)

@Composable
fun DesignSystemShowcase(
    presetName: String,
    modeLabel: String,
    modifier: Modifier = Modifier,
) {
    val roles = LocalColorRoles.current
    Surface(color = roles.bg, modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Padding.lg),
            verticalArrangement = Arrangement.spacedBy(Padding.lg),
            horizontalArrangement = Arrangement.spacedBy(Padding.lg),
        ) {
            item(span = { FullWidth }) {
                ShowcaseHeader(presetName = presetName, modeLabel = modeLabel)
            }
            item(span = { FullWidth }) { ColorSwatchSection(roles = roles) }
            item(span = { FullWidth }) { TypographySection() }
            item(span = { FullWidth }) { ShapesSection() }
            item(span = { FullWidth }) { ButtonsSection() }
            item(span = { HalfWidth }) { IconButtonsSection() }
            item(span = { HalfWidth }) { ChipsSection() }
            item(span = { HalfWidth }) { SegmentedControlSection() }
            item(span = { HalfWidth }) { StatusLabelsSection() }
            item(span = { FullWidth }) { InputsSection() }
            item(span = { HalfWidth }) { SampleCardSection() }
            item(span = { HalfWidth }) { SheetRowsSection() }
        }
    }
}

@Composable
private fun ShowcaseHeader(presetName: String, modeLabel: String) {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.xs)) {
        Text(
            text = "Template design system",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "$presetName · $modeLabel",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private data class Swatch(val name: String, val color: Color, val onColor: Color)

@Composable
private fun ColorSwatchSection(roles: ColorRoles) {
    val ink = roles.ink
    val onAccent = roles.onAccent
    val swatches = listOf(
        Swatch("bg", roles.bg, ink),
        Swatch("surface", roles.surface, ink),
        Swatch("surfaceAlt", roles.surfaceAlt, ink),
        Swatch("ink", roles.ink, roles.bg),
        Swatch("inkSoft", roles.inkSoft, roles.bg),
        Swatch("inkMuted", roles.inkMuted, roles.bg),
        Swatch("accent", roles.accent, onAccent),
        Swatch("accentSoft", roles.accentSoft, onAccent),
        Swatch("onAccent", roles.onAccent, roles.accent),
        Swatch("good", roles.good, onAccent),
        Swatch("warn", roles.warn, onAccent),
        Swatch("hairline", roles.hairline, ink),
    )
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "COLOR ROLES")
        swatches.chunked(6).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            ) {
                row.forEach { swatch ->
                    SwatchTile(
                        swatch = swatch,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(6 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun SwatchTile(swatch: Swatch, modifier: Modifier = Modifier) {
    val roles = LocalColorRoles.current
    Column(
        modifier = modifier
            .clip(AppShape.medium)
            .background(swatch.color)
            .border(1.dp, roles.hairline, AppShape.medium)
            .padding(horizontal = Padding.sm, vertical = Padding.sm),
        verticalArrangement = Arrangement.spacedBy(Padding.xxs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = swatch.name,
            style = MaterialTheme.typography.labelMedium,
            color = swatch.onColor,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = swatch.color.toHex(),
            style = MaterialTheme.typography.labelSmall,
            color = swatch.onColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun Color.toHex(): String = "#%08X".format(toArgb())

@Composable
private fun TypographySection() {
    val ink = MaterialTheme.colorScheme.onBackground
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Column(verticalArrangement = Arrangement.spacedBy(Padding.xs)) {
        SectionHeading(text = "TYPOGRAPHY")
        TypeRow(
            label = "Display L",
            sample = "Template",
            style = MaterialTheme.typography.displaySmall,
            ink = ink,
            muted = muted,
        )
        TypeRow(
            label = "Headline",
            sample = "Focused interface system",
            style = MaterialTheme.typography.headlineSmall,
            ink = ink,
            muted = muted,
        )
        TypeRow(
            label = "Title",
            sample = "Project overview",
            style = MaterialTheme.typography.titleMedium,
            ink = ink,
            muted = muted,
        )
        TypeRow(
            label = "Body",
            sample = "Reusable primitives keep layout, color, and type consistent across screens.",
            style = MaterialTheme.typography.bodyMedium,
            ink = ink,
            muted = muted,
        )
        TypeRow(
            label = "Label",
            sample = "RECENT ACTIVITY",
            style = MaterialTheme.typography.labelMedium,
            ink = ink,
            muted = muted,
        )
    }
}

@Composable
private fun TypeRow(
    label: String,
    sample: String,
    style: TextStyle,
    ink: Color,
    muted: Color,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(Padding.sm)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = muted,
            modifier = Modifier
                .width(64.dp)
                .alignBy(FirstBaseline),
        )
        Text(
            text = sample,
            style = style,
            color = ink,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .alignBy(FirstBaseline),
        )
    }
}

@Composable
private fun ShapesSection() {
    val roles = LocalColorRoles.current
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "SHAPES & SURFACES")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
        ) {
            ShapeTile(label = "card", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.card)
                        .background(roles.surfaceAlt)
                        .border(1.dp, roles.hairline, AppShape.card),
                )
            }
            ShapeTile(label = "button", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.button)
                        .background(roles.accent),
                )
            }
            ShapeTile(label = "pill", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.pill)
                        .background(roles.accentSoft),
                )
            }
            ShapeTile(label = "list top", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.listTop)
                        .background(roles.surfaceAlt),
                )
            }
            ShapeTile(label = "list mid", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.listMiddle)
                        .background(roles.surfaceAlt),
                )
            }
            ShapeTile(label = "list bot", modifier = Modifier.weight(1f), background = roles.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(AppShape.listBottom)
                        .background(roles.surfaceAlt),
                )
            }
        }
    }
}

@Composable
private fun ShapeTile(
    label: String,
    background: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val roles = LocalColorRoles.current
    Column(
        modifier = modifier
            .clip(AppShape.card)
            .background(background)
            .border(1.dp, roles.hairline, AppShape.card)
            .padding(Padding.sm),
        verticalArrangement = Arrangement.spacedBy(Padding.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "BUTTONS")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppButton(
                label = "Primary",
                onClick = {},
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = "Secondary",
                onClick = {},
                variant = AppButtonVariant.Secondary,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = "Ghost",
                onClick = {},
                variant = AppButtonVariant.Ghost,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = "With icon",
                onClick = {},
                variant = AppButtonVariant.Primary,
                iconRes = R.drawable.ic_share,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = "Disabled",
                onClick = {},
                enabled = false,
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = "Loading",
                onClick = {},
                isLoading = true,
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun IconButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "ICON BUTTONS & TOGGLES")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_share,
                onClick = {},
                contentDescription = null,
                tone = AppIconButtonTone.Default,
            )
            AppIconButton(
                iconRes = R.drawable.ic_close,
                onClick = {},
                contentDescription = null,
                tone = AppIconButtonTone.Muted,
            )
            AppIconButton(
                iconRes = R.drawable.ic_star,
                onClick = {},
                contentDescription = null,
                tone = AppIconButtonTone.Accent,
            )
            Switch(checked = true, onCheckedChange = {})
            Switch(checked = false, onCheckedChange = {})
        }
    }
}

@Composable
private fun ChipsSection() {
    val roles = LocalColorRoles.current
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "CHIPS")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppChip(
                label = "All",
                onClick = {},
                style = AppChipStyle.Filter,
                selected = true,
                trailingText = "42",
            )
            AppChip(
                label = "Active",
                onClick = {},
                style = AppChipStyle.Filter,
                selected = false,
                trailingText = "3",
            )
            AppChip(label = "Draft", style = AppChipStyle.Pill)
            AppChip(
                label = "Updated",
                style = AppChipStyle.Pill,
                tint = roles.accent,
            )
        }
    }
}

@Composable
private fun StatusLabelsSection() {
    val roles = LocalColorRoles.current
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "STATUS LABELS")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppChip(label = "Queued", style = AppChipStyle.Status, tint = roles.inkMuted)
            AppChip(label = "Active", style = AppChipStyle.Status, tint = roles.good)
            AppChip(label = "Needs review", style = AppChipStyle.Status, tint = roles.warn)
        }
    }
}

@Composable
private fun InputsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "INPUTS")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppTextField(
                value = "Template project",
                onValueChange = {},
                label = "Name",
                modifier = Modifier.weight(1f),
            )
            AppTextField(
                value = "",
                onValueChange = {},
                label = "Description",
                placeholder = "Short summary",
                modifier = Modifier.weight(1f),
            )
            AppSearchField(
                value = "settings",
                onValueChange = {},
                onClear = {},
                label = "Search",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SegmentedControlSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "SEGMENTED CONTROL")
        AppSegmentedControl(
            selectedValue = ShowcaseSegmentMode.STACK,
            options =
                listOf(
                    AppSegmentedControlOption(
                        value = ShowcaseSegmentMode.STACK,
                        label = "STACK",
                        iconRes = R.drawable.ic_view_list,
                    ),
                    AppSegmentedControlOption(
                        value = ShowcaseSegmentMode.GRID,
                        label = "GRID",
                        iconRes = R.drawable.ic_grid_view,
                    ),
                    AppSegmentedControlOption(
                        value = ShowcaseSegmentMode.HERO,
                        label = "HERO",
                        iconRes = R.drawable.ic_library,
                    ),
                ),
            onOptionSelected = {},
        )
    }
}

@Composable
private fun SheetRowsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "SHEET ROWS")
        SheetRowsSample()
    }
}

@Composable
private fun SampleCardSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.sm)) {
        SectionHeading(text = "CARD")
        SampleCard()
    }
}

@Composable
private fun SheetRowsSample() {
    val roles = LocalColorRoles.current
    Surface(
        modifier = Modifier.width(280.dp),
        shape = AppShape.large,
        color = roles.surface,
        border = BorderStroke(Padding.hairline, roles.hairline),
    ) {
        Column(
            modifier = Modifier.padding(Padding.sm),
            verticalArrangement = Arrangement.spacedBy(Padding.xxs),
        ) {
            AppSheetSectionLabel(text = "Sort by")
            listOf("Date added", "Name", "Status").forEachIndexed { index, label ->
                val selected = index == 0
                AppSheetActionRow(label = label, selected = selected, onClick = {})
            }
            AppSheetActionRow(
                label = "More filters",
                iconRes = R.drawable.ic_search,
                onClick = {},
            )
        }
    }
}

private enum class ShowcaseSegmentMode {
    STACK,
    GRID,
    HERO,
}

@Composable
private fun SampleCard() {
    val roles = LocalColorRoles.current
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.xs),
        ) {
            Text(
                text = "Template project",
                style = MaterialTheme.typography.titleMedium,
                color = roles.ink,
            )
            Text(
                text = "Design system · 12 components",
                style = MaterialTheme.typography.bodySmall,
                color = roles.inkSoft,
            )
            HorizontalDivider(
                color = roles.hairline,
                modifier = Modifier.padding(vertical = Padding.md),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Padding.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Ready for reuse",
                    style = MaterialTheme.typography.labelMedium,
                    color = roles.inkMuted,
                )
                AppButton(
                    label = "Open",
                    onClick = {},
                    variant = AppButtonVariant.Primary,
                    shape = AppShape.button,
                )
            }
        }
    }
}

private class ColorPresetParameterProvider : PreviewParameterProvider<ColorPreset> {
    override val values: Sequence<ColorPreset> = ColorPreset.OPTIONS.asSequence()
}

@Preview(name = "Light", widthDp = 1200, heightDp = 1600)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 1200, heightDp = 1600)
@Composable
private fun DesignSystemShowcasePreview(
    @PreviewParameter(ColorPresetParameterProvider::class) preset: ColorPreset,
) {
    AppPreview(colorRoles = preset.roles(isDark = false)) {
        DesignSystemShowcase(presetName = preset.id, modeLabel = "light")
    }
}
