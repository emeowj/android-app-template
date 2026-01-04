package com.template.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.screen.StaticScreen
import com.template.R
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.rememberEnumPreference
import com.template.ui.components.ChoiceOption
import com.template.ui.components.SectionHeader
import com.template.ui.components.SelectionResult
import com.template.ui.components.SettingsNavigationRow
import com.template.ui.components.selectionSheetOverlay
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
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
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()

    val darkModeOptions = listOf(
        ChoiceOption(DarkMode.SYSTEM, stringResource(R.string.settings_dark_mode_system)),
        ChoiceOption(DarkMode.LIGHT, stringResource(R.string.settings_dark_mode_light)),
        ChoiceOption(DarkMode.DARK, stringResource(R.string.settings_dark_mode_dark))
    )
    val darkModeLabel = darkModeOptions.find { it.value == darkMode }?.label ?: ""

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SectionHeader(title = stringResource(R.string.settings_appearance_section))
            }

            item {
                SettingsNavigationRow(
                    title = stringResource(R.string.settings_dark_mode_title),
                    description = darkModeLabel,
                    icon = painterResource(R.drawable.ic_palette),
                    onClick = {
                        scope.launch {
                            val result = overlayHost.show(
                                selectionSheetOverlay(
                                    titleRes = R.string.settings_dark_mode_title,
                                    options = darkModeOptions,
                                    selected = darkMode
                                )
                            )
                            if (result is SelectionResult.Selected) {
                                darkMode = result.value
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = Padding.medium),
                    shape = MaterialTheme.shapes.medium,
                    showChevron = false
                )
            }
        }
    }
}

@Composable
@ThemePreviews
private fun SettingsPreview() {
    AppPreview {
        SettingsUi()
    }
}
