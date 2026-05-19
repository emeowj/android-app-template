package com.template.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.screen.StaticScreen
import com.template.R
import com.template.data.settings.HapticFeedbackEnabledKey
import com.template.data.settings.rememberPreference
import com.template.screens.settings.components.SettingsRow
import com.template.screens.settings.components.SettingsRowItem
import com.template.screens.settings.components.SettingsSectionHeader
import com.template.screens.settings.components.appearance
import com.template.ui.LocalBottomBarPadding
import com.template.ui.components.AppTopBar
import com.template.ui.components.rememberScrolledPast
import com.template.ui.previews.AppPreview
import com.template.ui.previews.ThemePreviews
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.Padding
import dev.zacsweers.metro.AppScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : StaticScreen

@CircuitInject(SettingsScreen::class, AppScope::class)
@Composable
fun SettingsUi(modifier: Modifier = Modifier) {
    var hapticFeedbackEnabled by rememberPreference(HapticFeedbackEnabledKey, true)
    val behaviorRows =
        listOf(
            SettingsRow(
                title = stringResource(R.string.settings_haptic_feedback_title),
                value =
                    stringResource(
                        if (hapticFeedbackEnabled) R.string.settings_on else R.string.settings_off,
                    ),
                onClick = { hapticFeedbackEnabled = !hapticFeedbackEnabled },
            ),
        )

    SettingsContent(behaviorRows = behaviorRows, modifier = modifier)
}

@Composable
private fun SettingsContent(
    behaviorRows: List<SettingsRow>,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColorRoles.current
    val lookTitle = stringResource(R.string.settings_look_section)
    val behaviorTitle = stringResource(R.string.settings_behavior_section)
    val screenTitle = stringResource(R.string.settings_title)
    val listState = rememberLazyListState()
    val scrolledPast = rememberScrolledPast(listState)
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        modifier = modifier,
        containerColor = colors.bg,
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        bottom =
                            padding.calculateBottomPadding() +
                                LocalBottomBarPadding.current +
                                Padding.xl,
                    ),
            ) {
                item(key = "top-spacer") {
                    Spacer(
                        modifier =
                            Modifier.height(
                                padding.calculateTopPadding() +
                                    topInset +
                                    Padding.lg,
                            ),
                    )
                }
                item(key = "settings-title") {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.displayMedium,
                        color = colors.ink,
                        modifier =
                            Modifier
                                .padding(horizontal = Padding.lg)
                                .padding(bottom = Padding.xl),
                    )
                }
                settingsSection(title = lookTitle) {
                    appearance()
                }
                settingsSection(
                    title = behaviorTitle,
                    rows = behaviorRows,
                    topPadding = Padding.lg,
                )
            }
            AppTopBar(
                title = screenTitle,
                scrolledPast = scrolledPast,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

private fun LazyListScope.settingsSection(
    title: String,
    topPadding: Dp = 0.dp,
    rows: LazyListScope.() -> Unit,
) {
    item(key = "$title-header") {
        Column {
            if (topPadding > 0.dp) {
                Spacer(modifier = Modifier.height(topPadding))
            }
            SettingsSectionHeader(title = title)
        }
    }
    rows()
}

private fun LazyListScope.settingsSection(
    title: String,
    rows: List<SettingsRow>,
    topPadding: Dp = 0.dp,
) {
    item(key = "$title-header") {
        Column {
            if (topPadding > 0.dp) {
                Spacer(modifier = Modifier.height(topPadding))
            }
            SettingsSectionHeader(title = title)
        }
    }
    itemsIndexed(rows, key = { index, _ -> "$title-$index" }) { index, row ->
        SettingsRowItem(
            row = row,
            showDivider = index < rows.lastIndex,
        )
    }
}

@Composable
@ThemePreviews
private fun SettingsPreview() {
    AppPreview { SettingsUi() }
}
