package com.template.ui.components.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.R
import com.template.ui.components.AppSegmentedControl
import com.template.ui.components.AppSegmentedControlOption
import com.template.ui.components.feedback.AppDialogContent
import com.template.ui.components.inputs.AppSliderRow
import com.template.ui.components.navigation.AppListRow
import com.template.ui.components.navigation.AppListRowSurface
import com.template.ui.components.navigation.AppListRowTrailing
import com.template.ui.theme.AppTheme
import com.template.ui.theme.Padding
import org.junit.Rule
import org.junit.Test

class ModalSheetComponentsPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppBottomSheetLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                BottomSheetSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppBottomSheetDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                BottomSheetSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppEditorPanelLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                EditorPanelSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppEditorPanelDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                EditorPanelSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppDialogLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                DialogSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppDialogDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                DialogSpecimen()
            }
        }
    }
}

@Composable
private fun BottomSheetSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.md),
        ) {
            Text(
                text = "AppBottomSheet (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppSheetSurface {
                AppBottomSheetContent(
                    kicker = "Theme",
                    title = "Choose a theme",
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        AppListRow(
                            title = "Midnight",
                            surface = AppListRowSurface.FlatSheet,
                            trailing = AppListRowTrailing.Checkmark,
                            onClick = {},
                        )
                        AppListRow(
                            title = "Paper",
                            surface = AppListRowSurface.FlatSheet,
                            onClick = {},
                        )
                        AppListRow(
                            title = "System",
                            surface = AppListRowSurface.FlatSheet,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}

private enum class TestEditorTab {
    COLOR,
    TUNING,
}

@Composable
private fun EditorPanelSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.md),
        ) {
            Text(
                text = "AppEditorPanel (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppEditorPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                kicker = "Gradient",
                wallpaperName = "Mesh drift",
                onNameClick = {},
                tabs = {
                    AppSegmentedControl(
                        selectedValue = TestEditorTab.COLOR,
                        options = listOf(
                            AppSegmentedControlOption(
                                value = TestEditorTab.COLOR,
                                label = "Color",
                                iconRes = R.drawable.ic_palette,
                            ),
                            AppSegmentedControlOption(
                                value = TestEditorTab.TUNING,
                                label = "Tuning",
                                iconRes = R.drawable.ic_settings,
                            ),
                        ),
                        onOptionSelected = {},
                        iconOnly = true,
                    )
                },
            ) {
                item {
                    AppSliderRow(
                        label = "Intensity",
                        value = 0.72f,
                        onValueChange = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(Padding.md),
            verticalArrangement = Arrangement.spacedBy(Padding.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AppDialog (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppDialogContent(
                title = "Discard changes?",
                description = "You've made edits to this wallpaper that haven't been saved yet.",
                dismissText = "Keep editing",
                onDismiss = {},
                confirmText = "Discard",
                onConfirm = {},
                isDestructive = true,
            )
        }
    }
}
