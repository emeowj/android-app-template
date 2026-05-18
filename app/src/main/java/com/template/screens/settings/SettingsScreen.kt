package com.template.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.screen.StaticScreen
import com.template.R
import com.template.data.settings.BaseSizeKey
import com.template.data.settings.BodyFontFamilyKey
import com.template.data.settings.ColorPresetIdKey
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.DisplayFontFamilyKey
import com.template.data.settings.HapticFeedbackEnabledKey
import com.template.data.settings.UseDynamicColorKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference
import com.template.screens.settings.components.ChoiceOption
import com.template.screens.settings.components.ColorChipGridContent
import com.template.screens.settings.components.ColorChoice
import com.template.screens.settings.components.RadioListSelectionContent
import com.template.screens.settings.components.SectionHeader
import com.template.screens.settings.components.SelectionResult
import com.template.screens.settings.components.SettingsNavigationRow
import com.template.screens.settings.components.SettingsToggleRow
import com.template.screens.settings.components.selectionSheetOverlay
import com.template.screens.settings.components.typographySheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppShape
import com.template.ui.theme.ColorPreset
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : StaticScreen

@CircuitInject(SettingsScreen::class, AppScope::class)
@Composable
fun SettingsUi(modifier: Modifier = Modifier) {
    var darkMode by rememberEnumPreference(DarkModeKey)
    var hapticFeedbackEnabled by rememberPreference(HapticFeedbackEnabledKey, true)
    var useDynamicColor by rememberPreference(UseDynamicColorKey, true)
    var colorPresetId by rememberPreference(ColorPresetIdKey, ColorPreset.DEFAULT.id)
    val baseSize by rememberEnumPreference(BaseSizeKey)
    val displayFontFamily by rememberEnumPreference(DisplayFontFamilyKey)
    val bodyFontFamily by rememberEnumPreference(BodyFontFamilyKey)

    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()

    val darkModeOptions =
        listOf(
            ChoiceOption(DarkMode.SYSTEM, stringResource(R.string.settings_dark_mode_system)),
            ChoiceOption(DarkMode.LIGHT, stringResource(R.string.settings_dark_mode_light)),
            ChoiceOption(DarkMode.DARK, stringResource(R.string.settings_dark_mode_dark)),
        )
    val darkModeLabel = darkModeOptions.find { it.value == darkMode }?.label ?: ""

    val colorPresetLabel =
        ColorPreset.OPTIONS.firstOrNull { it.id == colorPresetId }?.let {
            stringResource(it.nameRes)
        } ?: ""
    val currentColorChoice: ColorChoice =
        if (useDynamicColor) ColorChoice.Dynamic else ColorChoice.Preset(colorPresetId)
    val colorsLabel =
        if (useDynamicColor) {
            stringResource(R.string.settings_dynamic_color_title)
        } else {
            colorPresetLabel
        }

    val baseSizeLabel = stringResource(baseSize.displayNameRes)
    val displayFontLabel = stringResource(displayFontFamily.displayNameRes)
    val bodyFontLabel = stringResource(bodyFontFamily.displayNameRes)
    val typographySummary = "$displayFontLabel · $bodyFontLabel · $baseSizeLabel"

    val appearanceRows =
        buildList<@Composable (Shape) -> Unit> {
            add { shape ->
                SettingsNavigationRow(
                    title = stringResource(R.string.settings_dark_mode_title),
                    description = darkModeLabel,
                    icon = painterResource(R.drawable.ic_dark_mode),
                    onClick = {
                        scope.launch {
                            val result =
                                overlayHost.show(
                                    selectionSheetOverlay(
                                        titleRes = R.string.settings_dark_mode_title,
                                        selected = darkMode,
                                    ) { sel, onSel ->
                                        RadioListSelectionContent(
                                            options = darkModeOptions,
                                            selected = sel,
                                            onSelect = onSel,
                                        )
                                    },
                                )
                            if (result is SelectionResult.Selected) darkMode = result.value
                        }
                    },
                    modifier = Modifier.padding(horizontal = Padding.medium),
                    shape = shape,
                    showChevron = false,
                )
            }
            add { shape ->
                SettingsNavigationRow(
                    title = stringResource(R.string.settings_colors_title),
                    description = colorsLabel,
                    icon = painterResource(R.drawable.ic_palette),
                    onClick = {
                        scope.launch {
                            val result =
                                overlayHost.show(
                                    selectionSheetOverlay(
                                        titleRes = R.string.settings_colors_title,
                                        selected = currentColorChoice,
                                    ) { sel, onSel ->
                                        ColorChipGridContent(selected = sel, onSelect = onSel)
                                    },
                                )
                            if (result is SelectionResult.Selected) {
                                when (val choice = result.value) {
                                    is ColorChoice.Dynamic -> useDynamicColor = true

                                    is ColorChoice.Preset -> {
                                        useDynamicColor = false
                                        colorPresetId = choice.id
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = Padding.medium),
                    shape = shape,
                    showChevron = false,
                )
            }
            add { shape ->
                SettingsNavigationRow(
                    title = stringResource(R.string.settings_typography_title),
                    description = typographySummary,
                    icon = painterResource(R.drawable.ic_format_size),
                    onClick = {
                        scope.launch { overlayHost.show(typographySheetOverlay()) }
                    },
                    modifier = Modifier.padding(horizontal = Padding.medium),
                    shape = shape,
                    showChevron = false,
                )
            }
        }

    val behaviorRows =
        buildList<@Composable (Shape) -> Unit> {
            add { shape ->
                SettingsToggleRow(
                    title = stringResource(R.string.settings_haptic_feedback_title),
                    checked = hapticFeedbackEnabled,
                    onCheckedChange = { hapticFeedbackEnabled = it },
                    icon = painterResource(R.drawable.ic_vibration),
                    modifier = Modifier.padding(horizontal = Padding.medium),
                    shape = shape,
                )
            }
        }

    val appearanceTitle = stringResource(R.string.settings_appearance_section)
    val behaviorTitle = stringResource(R.string.settings_behavior_section)

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(Padding.hairline),
        ) {
            settingsSection(title = appearanceTitle, rows = appearanceRows)
            settingsSection(
                title = behaviorTitle,
                rows = behaviorRows,
                topPadding = Padding.medium,
            )
        }
    }
}

private fun LazyListScope.settingsSection(
    title: String,
    rows: List<@Composable (Shape) -> Unit>,
    topPadding: Dp = 0.dp,
) {
    item(key = "$title-header") {
        SectionHeader(title = title, modifier = Modifier.padding(top = topPadding))
    }
    itemsIndexed(rows, key = { index, _ -> "$title-$index" }) { index, row ->
        row(AppShape.listItemShape(index, rows.size))
    }
}

@Composable
@ThemePreviews
private fun SettingsPreview() {
    AppPreview { SettingsUi() }
}
