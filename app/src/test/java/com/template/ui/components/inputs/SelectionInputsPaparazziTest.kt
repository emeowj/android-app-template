package com.template.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.R
import com.template.ui.components.AppChip
import com.template.ui.components.AppChipDefaults
import com.template.ui.components.AppSegmentedControl
import com.template.ui.components.AppSegmentedControlOption
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SelectionInputsPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppChipVariantsLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ChipSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppChipVariantsDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ChipSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSegmentedControlLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                SegmentedControlSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSegmentedControlDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                SegmentedControlSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSwitchLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                SwitchSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppSwitchDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                SwitchSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppTextFieldLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                TextFieldSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppTextFieldDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                TextFieldSpecimen()
            }
        }
    }
}

@Composable
private fun ChipSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppChip (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppChip(
                    label = "All Wallpapers",
                    onClick = {},
                    selected = true,
                    count = 42,
                )
                AppChip(
                    label = "Favorites",
                    onClick = {},
                    selected = false,
                    count = 12,
                )
                AppChip(
                    label = "Draft",
                    onClick = {},
                    selected = false,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppChip(
                    label = "Featured",
                    onClick = {},
                    selected = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            modifier = Modifier.size(AppChipDefaults.IconSize),
                        )
                    },
                    count = 7,
                )
                AppChip(
                    label = "Disabled",
                    onClick = {},
                    enabled = false,
                    count = 0,
                )
            }
        }
    }
}

@Composable
private fun SegmentedControlSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppSegmentedControl (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Text(
                text = "Standard Labeled",
                style = typography.overline,
                color = colors.inkMuted,
            )

            AppSegmentedControl(
                selectedValue = "grid",
                options = listOf(
                    AppSegmentedControlOption("stack", "Stack", R.drawable.ic_view_list),
                    AppSegmentedControlOption("grid", "Grid", R.drawable.ic_grid_view),
                    AppSegmentedControlOption("hero", "Hero", R.drawable.ic_category_photo),
                ),
                onOptionSelected = {},
            )

            Text(
                text = "Icon-Only Mode",
                style = typography.overline,
                color = colors.inkMuted,
            )

            AppSegmentedControl(
                selectedValue = "color",
                options = listOf(
                    AppSegmentedControlOption("palette", "Palette", R.drawable.ic_palette),
                    AppSegmentedControlOption("color", "Color", R.drawable.ic_auto_awesome),
                    AppSegmentedControlOption("pattern", "Pattern", R.drawable.ic_grid_view),
                ),
                onOptionSelected = {},
                iconOnly = true,
            )
        }
    }
}

@Composable
private fun SwitchSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppSwitch (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = true, onCheckedChange = {})
                Text(text = "Checked State (On)", style = typography.bodyMd, color = colors.ink)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = false, onCheckedChange = {})
                Text(text = "Unchecked State (Off)", style = typography.bodyMd, color = colors.ink)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = true, onCheckedChange = null, enabled = false)
                Text(text = "Disabled Checked", style = typography.bodyMd, color = colors.inkMuted)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppSwitch(checked = false, onCheckedChange = null, enabled = false)
                Text(text = "Disabled Unchecked", style = typography.bodyMd, color = colors.inkMuted)
            }
        }
    }
}

@Composable
private fun TextFieldSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "AppTextField (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppTextField(
                value = "Aura Sunset",
                onValueChange = {},
                label = "Wallpaper Title",
                modifier = Modifier.fillMaxWidth(),
            )

            AppTextField(
                value = "",
                onValueChange = {},
                label = "Description",
                placeholder = "Add optional notes",
                modifier = Modifier.fillMaxWidth(),
            )

            AppTextField(
                value = "invalid-seed-format",
                onValueChange = {},
                label = "Seed",
                isError = true,
                supportingText = "Seed format must be numeric hex",
                modifier = Modifier.fillMaxWidth(),
            )

            AppTextField(
                value = "System Default",
                onValueChange = {},
                label = "Preset",
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
