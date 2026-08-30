package com.template.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.template.R
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class NavigationComponentsPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshotAppTopBarStandardLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                TopBarStandardSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppTopBarStandardDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                TopBarStandardSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppTopBarImmersiveLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                TopBarImmersiveSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppTopBarImmersiveDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                TopBarImmersiveSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppBottomNavLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                BottomNavSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppBottomNavDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                BottomNavSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppActionBarLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ActionBarSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppActionBarDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ActionBarSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppListRowGroupedLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ListRowGroupedSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppListRowGroupedDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ListRowGroupedSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppListRowFlatLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                ListRowFlatSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppListRowFlatDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                ListRowFlatSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppQuickJumpRailLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                QuickJumpRailSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppQuickJumpRailDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                QuickJumpRailSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppFloatingPillLight() {
        paparazzi.snapshot {
            AppTheme(darkTheme = false) {
                FloatingPillSpecimen()
            }
        }
    }

    @Test
    fun snapshotAppFloatingPillDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) {
                FloatingPillSpecimen()
            }
        }
    }
}

@Composable
private fun TopBarStandardSpecimen() {
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
                text = "AppTopBar Standard (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppTopBar(
                title = "Mural",
                subtitle = "Library",
                includeStatusBarPadding = false,
                leading = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_settings,
                        contentDescription = "Settings",
                    )
                },
                trailing = {
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_search,
                        contentDescription = "Search",
                    )
                    AppIconButton(
                        onClick = {},
                        iconRes = R.drawable.ic_add,
                        contentDescription = "Add",
                    )
                },
            )
        }
    }
}

@Composable
private fun TopBarImmersiveSpecimen() {
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
                text = "AppTopBar Immersive (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Box(modifier = Modifier.background(Color(0xFF2C3E50))) {
                AppTopBar(
                    title = "Explore",
                    subtitle = "Curated collections",
                    immersive = true,
                    scrolledPast = false,
                    includeStatusBarPadding = false,
                    leading = {
                        AppIconButton(
                            onClick = {},
                            iconRes = R.drawable.ic_arrow_back,
                            contentDescription = "Back",
                            overlay = true,
                        )
                    },
                    trailing = {
                        AppIconButton(
                            onClick = {},
                            iconRes = R.drawable.ic_share,
                            contentDescription = "Share",
                            overlay = true,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun BottomNavSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AppBottomNav (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppBottomNav {
                AppBottomNavItem(
                    selected = true,
                    onClick = {},
                    iconRes = R.drawable.ic_grid_view,
                    contentDescription = "Library",
                )
                AppBottomNavItem(
                    selected = false,
                    onClick = {},
                    iconRes = R.drawable.ic_add,
                    contentDescription = "Create",
                )
                AppBottomNavItem(
                    selected = false,
                    onClick = {},
                    iconRes = R.drawable.ic_explore,
                    contentDescription = "Explore",
                )
            }
        }
    }
}

@Composable
private fun ActionBarSpecimen() {
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
                text = "AppActionBar (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            AppActionBar {
                AppButton(
                    text = "Cancel",
                    onClick = {},
                    variant = AppButtonVariant.Secondary,
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    text = "Apply Wallpaper",
                    onClick = {},
                    variant = AppButtonVariant.Primary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ListRowGroupedSpecimen() {
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
                text = "AppListRow GroupedCard (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                AppListRow(
                    title = "Appearance",
                    note = "Dark mode, typography, palette",
                    iconRes = R.drawable.ic_palette,
                    surface = AppListRowSurface.GroupedCard,
                    index = 0,
                    count = 3,
                    trailing = AppListRowTrailing.Chevron,
                    onClick = {},
                )
                AppListRow(
                    title = "Daily Wallpaper Rotation",
                    note = "Updates every day at 6:00 AM",
                    iconRes = R.drawable.ic_auto_awesome,
                    surface = AppListRowSurface.GroupedCard,
                    index = 1,
                    count = 3,
                    trailing = AppListRowTrailing.Switch(checked = true, onCheckedChange = {}),
                )
                AppListRow(
                    title = "Clear Cached Wallpapers",
                    note = "Reclaims 142 MB storage",
                    iconRes = R.drawable.ic_close,
                    surface = AppListRowSurface.GroupedCard,
                    index = 2,
                    count = 3,
                    danger = true,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun ListRowFlatSpecimen() {
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
                text = "AppListRow FlatSettings (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Column {
                AppListRow(
                    title = "Font Pairing",
                    surface = AppListRowSurface.FlatSettings,
                    index = 0,
                    count = 2,
                    trailing = AppListRowTrailing.Value("Editorial"),
                    onClick = {},
                )
                AppListRow(
                    title = "Strict Action Economy",
                    note = "Enforce single primary CTA per screen",
                    surface = AppListRowSurface.FlatSettings,
                    index = 1,
                    count = 2,
                    trailing = AppListRowTrailing.Checkmark,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun QuickJumpRailSpecimen() {
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
                text = "AppQuickJumpRail (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                val letters = listOf("A", "B", "C", "D", "E", "F", "G").map {
                    QuickJumpItem.Letter(it)
                }
                AppQuickJumpRail(
                    items = letters,
                    selectedIndex = 2,
                    onItemSelected = { _, _ -> },
                )

                val swatches = listOf(
                    QuickJumpItem.Swatch(Color(0xFFE57373), "Red"),
                    QuickJumpItem.Swatch(Color(0xFFFFB74D), "Orange"),
                    QuickJumpItem.Swatch(Color(0xFF81C784), "Green"),
                    QuickJumpItem.Swatch(Color(0xFF64B5F6), "Blue"),
                    QuickJumpItem.Swatch(Color(0xFFBA68C8), "Purple"),
                )
                AppQuickJumpRail(
                    items = swatches,
                    selectedIndex = 1,
                    onItemSelected = { _, _ -> },
                )
            }
        }
    }
}

@Composable
private fun FloatingPillSpecimen() {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Surface(
        color = colors.background,
        modifier = Modifier.width(360.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AppFloatingPill (${if (colors.isDark) "Dark" else "Light"})",
                style = typography.titleSm,
                color = colors.ink,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppFloatingPill(
                    text = "Show controls",
                    iconRes = R.drawable.ic_settings,
                    onClick = {},
                )
                AppFloatingPill(
                    iconOnly = true,
                    iconRes = R.drawable.ic_settings,
                    onClick = {},
                )
            }
        }
    }
}
