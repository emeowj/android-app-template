package com.template.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.template.R
import com.template.data.settings.HapticFeedbackEnabledKey
import com.template.data.settings.rememberPreference
import com.template.screens.settings.components.DensitySettingRow
import com.template.screens.settings.components.DynamicColorSettingRow
import com.template.screens.settings.components.SettingsSectionKicker
import com.template.screens.settings.components.ThemeSettingRow
import com.template.screens.settings.components.TypographySettingRow
import com.template.screens.showcase.ShowcaseScreen
import com.template.ui.LocalBottomBarPadding
import com.template.ui.components.navigation.AppDrillInTopBar
import com.template.ui.components.navigation.AppListRow
import com.template.ui.components.navigation.AppListRowSurface
import com.template.ui.components.navigation.AppListRowTrailing
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.AppTheme
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object BackClicked : Event
        data object OpenShowcase : Event
    }
}

class SettingsPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<SettingsScreen.State> {
    @CircuitInject(SettingsScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPresenter
    }

    @Composable
    override fun present(): SettingsScreen.State = SettingsScreen.State { event ->
        when (event) {
            SettingsScreen.Event.BackClicked -> navigator.pop()
            SettingsScreen.Event.OpenShowcase -> navigator.goTo(ShowcaseScreen)
        }
    }
}

@CircuitInject(SettingsScreen::class, AppScope::class)
@Composable
fun SettingsUi(state: SettingsScreen.State, modifier: Modifier = Modifier) {
    val colors = AppTheme.colors
    val densityTokens = LocalAppDensity.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var hapticFeedbackEnabled by rememberPreference(HapticFeedbackEnabledKey, true)
    val listState = rememberLazyListState()

    val showSnackbar: (String) -> Unit = { msg ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppDrillInTopBar(
                title = stringResource(R.string.settings_title),
                onBackClick = { state.eventSink(SettingsScreen.Event.BackClicked) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.background,
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                horizontal = densityTokens.screenPadding,
                vertical = Padding.md,
            ),
        ) {
            item(key = "appearance_kicker") {
                SettingsSectionKicker(
                    title = stringResource(R.string.settings_section_appearance),
                    modifier = Modifier.padding(top = Padding.sm, bottom = Padding.xs),
                )
            }
            item(key = "theme_row") {
                ThemeSettingRow(onShowSnackbar = showSnackbar)
            }
            item(key = "typography_row") {
                TypographySettingRow(onShowSnackbar = showSnackbar)
            }
            item(key = "density_row") {
                DensitySettingRow(onShowSnackbar = showSnackbar)
            }
            item(key = "dynamic_color_row") {
                DynamicColorSettingRow(onShowSnackbar = showSnackbar)
            }

            item(key = "behavior_kicker") {
                SettingsSectionKicker(
                    title = stringResource(R.string.settings_section_behavior),
                    modifier = Modifier.padding(top = Padding.xl, bottom = Padding.xs),
                )
            }
            item(key = "haptics_row") {
                AppListRow(
                    title = stringResource(R.string.settings_haptic_feedback_title),
                    surface = AppListRowSurface.FlatSettings,
                    trailing = AppListRowTrailing.Switch(
                        checked = hapticFeedbackEnabled,
                        onCheckedChange = { hapticFeedbackEnabled = it },
                    ),
                )
            }

            item(key = "dev_kicker") {
                SettingsSectionKicker(
                    title = stringResource(R.string.settings_section_developer),
                    modifier = Modifier.padding(top = Padding.xl, bottom = Padding.xs),
                )
            }
            item(key = "showcase_row") {
                AppListRow(
                    title = stringResource(R.string.showcase_title),
                    note = stringResource(R.string.showcase_subtitle),
                    surface = AppListRowSurface.FlatSettings,
                    trailing = AppListRowTrailing.Chevron,
                    onClick = { state.eventSink(SettingsScreen.Event.OpenShowcase) },
                )
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(LocalBottomBarPadding.current + 48.dp))
            }
        }
    }
}

@Composable
@ThemePreviews
private fun SettingsPreview() {
    AppPreview { SettingsUi(state = SettingsScreen.State {}) }
}
