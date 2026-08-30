package com.template.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.slack.circuit.overlay.LocalOverlayHost
import com.template.R
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.DensityKey
import com.template.data.settings.DynamicColorEnabledKey
import com.template.data.settings.TypePairingKey
import com.template.data.settings.rememberEnumPreference
import com.template.data.settings.rememberPreference
import com.template.ui.components.navigation.AppListRow
import com.template.ui.components.navigation.AppListRowSurface
import com.template.ui.components.navigation.AppListRowTrailing
import com.template.ui.components.sheets.AppBottomSheetHeader
import com.template.ui.components.sheets.AppSheetSurface
import com.template.ui.components.sheets.appBottomSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppDensity
import com.template.ui.theme.AppTypePairing
import kotlinx.coroutines.launch

@Composable
fun ThemeSettingRow(
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var darkMode by rememberEnumPreference(DarkModeKey)
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val themeLabel = when (darkMode) {
        DarkMode.SYSTEM -> stringResource(R.string.settings_theme_system)
        DarkMode.LIGHT -> stringResource(R.string.settings_theme_light)
        DarkMode.DARK -> stringResource(R.string.settings_theme_dark)
    }

    AppListRow(
        title = stringResource(R.string.settings_theme_title),
        surface = AppListRowSurface.FlatSettings,
        trailing = AppListRowTrailing.Value(themeLabel),
        modifier = modifier,
        onClick = {
            scope.launch {
                overlayHost.show(
                    appBottomSheetOverlay(
                        model = Unit,
                        onDismiss = {},
                    ) { _, navigator ->
                        ThemeSheetContent(
                            currentMode = darkMode,
                            onSelectMode = { mode, titleRes ->
                                darkMode = mode
                                navigator.finish(Unit)
                                onShowSnackbar(
                                    context.getString(R.string.settings_snack_theme_set, context.getString(titleRes).lowercase()),
                                )
                            },
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun ThemeSheetContent(
    currentMode: DarkMode,
    onSelectMode: (DarkMode, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppBottomSheetHeader(
            kicker = stringResource(R.string.settings_section_appearance),
            title = stringResource(R.string.settings_theme_title),
        )
        val themes = listOf(
            Triple(DarkMode.SYSTEM, R.string.settings_theme_system, R.string.settings_theme_system_desc),
            Triple(DarkMode.LIGHT, R.string.settings_theme_light, R.string.settings_theme_light_desc),
            Triple(DarkMode.DARK, R.string.settings_theme_dark, R.string.settings_theme_dark_desc),
        )
        themes.forEach { (mode, titleRes, descRes) ->
            AppListRow(
                title = stringResource(titleRes),
                note = stringResource(descRes),
                surface = AppListRowSurface.FlatSheet,
                trailing = if (currentMode == mode) AppListRowTrailing.Checkmark else null,
                onClick = { onSelectMode(mode, titleRes) },
            )
        }
    }
}

@Composable
fun TypographySettingRow(
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var typePairing by rememberEnumPreference(TypePairingKey)
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val typePairingLabel = when (typePairing) {
        AppTypePairing.Editorial -> stringResource(R.string.type_pairing_editorial)
        AppTypePairing.Literary -> stringResource(R.string.type_pairing_literary)
        AppTypePairing.Modern -> stringResource(R.string.type_pairing_modern)
    }

    AppListRow(
        title = stringResource(R.string.settings_typography_title),
        surface = AppListRowSurface.FlatSettings,
        trailing = AppListRowTrailing.Value(typePairingLabel),
        modifier = modifier,
        onClick = {
            scope.launch {
                overlayHost.show(
                    appBottomSheetOverlay(
                        model = Unit,
                        onDismiss = {},
                    ) { _, navigator ->
                        TypographySheetContent(
                            currentPairing = typePairing,
                            onSelectPairing = { pairing, labelRes ->
                                typePairing = pairing
                                navigator.finish(Unit)
                                onShowSnackbar(
                                    context.getString(R.string.settings_snack_type_set, context.getString(labelRes).lowercase()),
                                )
                            },
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun TypographySheetContent(
    currentPairing: AppTypePairing,
    onSelectPairing: (AppTypePairing, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppBottomSheetHeader(
            kicker = stringResource(R.string.settings_section_appearance),
            title = stringResource(R.string.settings_typography_title),
        )
        val pairings = listOf(
            AppTypePairing.Editorial to R.string.type_pairing_editorial,
            AppTypePairing.Literary to R.string.type_pairing_literary,
            AppTypePairing.Modern to R.string.type_pairing_modern,
        )
        pairings.forEach { (pairing, labelRes) ->
            TypographySpecimenRow(
                pairing = pairing,
                selected = currentPairing == pairing,
                onClick = { onSelectPairing(pairing, labelRes) },
            )
        }
    }
}

@Composable
fun DensitySettingRow(
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var density by rememberEnumPreference(DensityKey)
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val densityLabel = when (density) {
        AppDensity.Compact -> stringResource(R.string.density_compact)
        AppDensity.Comfortable -> stringResource(R.string.density_comfortable)
        AppDensity.Spacious -> stringResource(R.string.density_spacious)
    }

    AppListRow(
        title = stringResource(R.string.density_comfortable),
        surface = AppListRowSurface.FlatSettings,
        trailing = AppListRowTrailing.Value(densityLabel),
        modifier = modifier,
        onClick = {
            scope.launch {
                overlayHost.show(
                    appBottomSheetOverlay(
                        model = Unit,
                        onDismiss = {},
                    ) { _, navigator ->
                        DensitySheetContent(
                            currentDensity = density,
                            onSelectDensity = { d, titleRes ->
                                density = d
                                navigator.finish(Unit)
                                onShowSnackbar(
                                    context.getString(R.string.settings_snack_density_set, context.getString(titleRes).lowercase()),
                                )
                            },
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun DensitySheetContent(
    currentDensity: AppDensity,
    onSelectDensity: (AppDensity, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppBottomSheetHeader(
            kicker = stringResource(R.string.settings_section_appearance),
            title = stringResource(R.string.density_comfortable),
        )
        val densities = listOf(
            Triple(AppDensity.Compact, R.string.density_compact, R.string.settings_density_compact_desc),
            Triple(AppDensity.Comfortable, R.string.density_comfortable, R.string.settings_density_comfortable_desc),
            Triple(AppDensity.Spacious, R.string.density_spacious, R.string.settings_density_spacious_desc),
        )
        densities.forEach { (d, titleRes, descRes) ->
            AppListRow(
                title = stringResource(titleRes),
                note = stringResource(descRes),
                surface = AppListRowSurface.FlatSheet,
                trailing = if (currentDensity == d) AppListRowTrailing.Checkmark else null,
                onClick = { onSelectDensity(d, titleRes) },
            )
        }
    }
}

@Composable
fun DynamicColorSettingRow(
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dynamicColor by rememberPreference(DynamicColorEnabledKey, true)
    val context = LocalContext.current

    AppListRow(
        title = stringResource(R.string.settings_dynamic_color_title),
        note = stringResource(R.string.settings_dynamic_color_note),
        surface = AppListRowSurface.FlatSettings,
        modifier = modifier,
        trailing = AppListRowTrailing.Switch(
            checked = dynamicColor,
            onCheckedChange = {
                dynamicColor = it
                val msg = if (it) {
                    context.getString(R.string.settings_snack_dynamic_color_on)
                } else {
                    context.getString(R.string.settings_snack_dynamic_color_off)
                }
                onShowSnackbar(msg)
            },
        ),
    )
}

@ThemePreviews
@Composable
private fun AppearanceSettingRowsPreview() {
    AppPreview {
        Column(modifier = Modifier.fillMaxWidth()) {
            ThemeSettingRow(onShowSnackbar = {})
            TypographySettingRow(onShowSnackbar = {})
            DensitySettingRow(onShowSnackbar = {})
            DynamicColorSettingRow(onShowSnackbar = {})
        }
    }
}

@ThemePreviews
@Composable
private fun ThemeSheetContentPreview() {
    AppPreview {
        AppSheetSurface {
            ThemeSheetContent(
                currentMode = DarkMode.SYSTEM,
                onSelectMode = { _, _ -> },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun TypographySheetContentPreview() {
    AppPreview {
        AppSheetSurface {
            TypographySheetContent(
                currentPairing = AppTypePairing.Editorial,
                onSelectPairing = { _, _ -> },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun DensitySheetContentPreview() {
    AppPreview {
        AppSheetSurface {
            DensitySheetContent(
                currentDensity = AppDensity.Comfortable,
                onSelectDensity = { _, _ -> },
            )
        }
    }
}
