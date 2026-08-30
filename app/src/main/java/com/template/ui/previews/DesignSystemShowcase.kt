package com.template.ui.previews

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.template.R
import com.template.ui.components.AppCard
import com.template.ui.components.AppChip
import com.template.ui.components.AppDropdownMenu
import com.template.ui.components.AppDropdownMenuItem
import com.template.ui.components.AppSegmentedControl
import com.template.ui.components.AppSegmentedControlOption
import com.template.ui.components.buttons.AppAffordance
import com.template.ui.components.buttons.AppButton
import com.template.ui.components.buttons.AppButtonVariant
import com.template.ui.components.buttons.AppIconButton
import com.template.ui.components.color.AppColorField
import com.template.ui.components.color.AppColorPopoverCard
import com.template.ui.components.feedback.AppBanner
import com.template.ui.components.feedback.AppDialogContent
import com.template.ui.components.feedback.AppDoneConfirmation
import com.template.ui.components.feedback.AppEmptyState
import com.template.ui.components.feedback.AppHintCard
import com.template.ui.components.feedback.AppSkeleton
import com.template.ui.components.feedback.AppSnackbar
import com.template.ui.components.inputs.AppSearchField
import com.template.ui.components.inputs.AppSliderRow
import com.template.ui.components.inputs.AppSwitch
import com.template.ui.components.inputs.AppTextField
import com.template.ui.components.navigation.AppActionBar
import com.template.ui.components.navigation.AppBottomNav
import com.template.ui.components.navigation.AppBottomNavItem
import com.template.ui.components.navigation.AppDrillInTopBar
import com.template.ui.components.navigation.AppFloatingPill
import com.template.ui.components.navigation.AppListRow
import com.template.ui.components.navigation.AppListRowSurface
import com.template.ui.components.navigation.AppListRowTrailing
import com.template.ui.components.navigation.AppTopBar
import com.template.ui.components.sheets.AppBottomSheetContent
import com.template.ui.components.sheets.AppSheetSurface
import com.template.ui.theme.AppDensity
import com.template.ui.theme.AppShapes
import com.template.ui.theme.AppTheme
import com.template.ui.theme.AppTypePairing
import com.template.ui.theme.LocalAppDensity
import com.template.ui.theme.Padding
import kotlinx.coroutines.launch

val SampleGradientBrush = Brush.linearGradient(listOf(Color(0xFF1A1440), Color(0xFF3B2168), Color(0xFF7A3B74), Color(0xFFC25F6A)))

@Composable
fun DesignSystemShowcase(
    modifier: Modifier = Modifier,
    initialDarkTheme: Boolean = false,
    initialPairing: AppTypePairing = AppTypePairing.Editorial,
    initialDensity: AppDensity = AppDensity.Comfortable,
    onBackClick: (() -> Unit)? = null,
) {
    var darkTheme by remember { mutableStateOf(initialDarkTheme) }
    var pairing by remember { mutableStateOf(initialPairing) }
    var density by remember { mutableStateOf(initialDensity) }

    AppTheme(
        darkTheme = darkTheme,
        pairing = pairing,
        density = density,
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val colors = AppTheme.colors
        val screenPadding = LocalAppDensity.current.screenPadding

        Surface(
            color = colors.background,
            modifier = modifier.fillMaxWidth(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // ═══ STICKY TOOLBAR ═══
                item(key = "toolbar") {
                    ShowcaseToolbar(
                        darkTheme = darkTheme,
                        onDarkThemeChange = { darkTheme = it },
                        pairing = pairing,
                        onPairingChange = { pairing = it },
                        density = density,
                        onDensityChange = { density = it },
                        onBackClick = onBackClick,
                    )
                }

                // ═══ INTRO & TOC ═══
                item(key = "intro") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenPadding),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Design System Catalog",
                            style = AppTheme.typography.titleLg,
                            color = colors.ink,
                        )
                        Text(
                            text = "Interactive catalog of all reusable UI primitives and components. Toggle Theme, Type Pairing, and Density above to re-theme all components live.",
                            style = AppTheme.typography.bodyMd,
                            color = colors.inkMuted,
                        )

                        // Table of Contents Anchor Chips
                        Text(
                            text = "JUMP TO",
                            style = AppTheme.typography.overline,
                            color = colors.inkMuted,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val sections = listOf(
                                "Actions" to 2,
                                "Selection & Input" to 3,
                                "Navigation & Shells" to 4,
                                "Cards & Containers" to 5,
                                "Overlays & Feedback" to 6,
                            )
                            sections.forEach { (label, index) ->
                                AppChip(
                                    label = label,
                                    selected = false,
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index)
                                        }
                                    },
                                )
                            }
                        }
                    }
                }

                // ═══ SECTION 1: ACTIONS ═══
                item(key = "sec_actions") {
                    ShowcaseSectionCard(title = "Actions") {
                        ActionsSectionContent()
                    }
                }

                // ═══ SECTION 2: SELECTION & INPUT ═══
                item(key = "sec_input") {
                    ShowcaseSectionCard(title = "Selection & input") {
                        SelectionInputSectionContent()
                    }
                }

                // ═══ SECTION 3: NAVIGATION & STRUCTURE ═══
                item(key = "sec_nav") {
                    ShowcaseSectionCard(title = "Navigation & structure") {
                        NavigationStructureSectionContent()
                    }
                }

                // ═══ SECTION 4: CARDS & STRUCTURE ═══
                item(key = "sec_cards") {
                    ShowcaseSectionCard(title = "Cards & containers") {
                        CardsSectionContent()
                    }
                }

                // ═══ SECTION 5: OVERLAYS & FEEDBACK ═══
                item(key = "sec_overlay") {
                    ShowcaseSectionCard(title = "Overlays & feedback") {
                        OverlaysFeedbackSectionContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowcaseToolbar(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    pairing: AppTypePairing,
    onPairingChange: (AppTypePairing) -> Unit,
    density: AppDensity,
    onDensityChange: (AppDensity) -> Unit,
    onBackClick: (() -> Unit)? = null,
) {
    val colors = AppTheme.colors
    Surface(
        color = colors.surface.copy(alpha = 0.96f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.hairline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (onBackClick != null) {
                        AppIconButton(
                            onClick = onBackClick,
                            iconRes = R.drawable.ic_arrow_back,
                            contentDescription = "Back",
                        )
                    }
                    Text(
                        text = "Design System",
                        style = AppTheme.typography.titleSm,
                        color = colors.ink,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = if (darkTheme) "Dark" else "Light",
                        style = AppTheme.typography.caption,
                        fontWeight = FontWeight.Medium,
                        color = colors.inkMuted,
                    )
                    AppSwitch(
                        checked = darkTheme,
                        onCheckedChange = onDarkThemeChange,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "TYPE",
                        style = AppTheme.typography.overline,
                        color = colors.inkMuted,
                    )
                    AppSegmentedControl(
                        selectedValue = pairing,
                        options = listOf(
                            AppSegmentedControlOption(AppTypePairing.Editorial, "Editorial"),
                            AppSegmentedControlOption(AppTypePairing.Literary, "Literary"),
                            AppSegmentedControlOption(AppTypePairing.Modern, "Modern"),
                        ),
                        onOptionSelected = onPairingChange,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "DENSITY",
                        style = AppTheme.typography.overline,
                        color = colors.inkMuted,
                    )
                    AppSegmentedControl(
                        selectedValue = density,
                        options = listOf(
                            AppSegmentedControlOption(AppDensity.Compact, "Compact"),
                            AppSegmentedControlOption(AppDensity.Comfortable, "Comfortable"),
                            AppSegmentedControlOption(AppDensity.Spacious, "Spacious"),
                        ),
                        onOptionSelected = onDensityChange,
                    )
                }
            }
        }
    }
}

@Composable
fun ShowcaseSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = AppTheme.colors
    val screenPadding = LocalAppDensity.current.screenPadding
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = AppTheme.typography.titleLg,
                color = colors.ink,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.hairline),
            )
        }
        content()
    }
}

@Composable
fun ShowcaseBlock(
    title: String,
    source: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    val colors = AppTheme.colors
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleSm,
                    color = colors.ink,
                )
                Text(
                    text = source,
                    style = AppTheme.typography.caption,
                    color = colors.inkMuted,
                )
            }
            if (description != null) {
                Text(
                    text = description,
                    style = AppTheme.typography.caption,
                    color = colors.inkMuted,
                )
            }
        }
        content()
    }
}

@Composable
fun ActionsSectionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShowcaseBlock(
            title = "AppButton",
            source = "Primary, Secondary, Text, TextDanger",
            description = "One Primary per screen. Strict action economy.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    AppButton(
                        text = "Primary action",
                        onClick = {},
                        variant = AppButtonVariant.Primary,
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        text = "Disabled",
                        onClick = {},
                        variant = AppButtonVariant.Primary,
                        enabled = false,
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppButton(
                        text = "Secondary",
                        onClick = {},
                        variant = AppButtonVariant.Secondary,
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        text = "Text",
                        onClick = {},
                        variant = AppButtonVariant.Text,
                    )
                    AppButton(
                        text = "Delete",
                        onClick = {},
                        variant = AppButtonVariant.TextDanger,
                    )
                }
            }
        }

        ShowcaseBlock(
            title = "AppIconButton",
            source = "AppIconButton",
            description = "44dp touch target, transparent at rest with overlay-on-scrim support.",
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIconButton(onClick = {}, iconRes = R.drawable.ic_arrow_back, contentDescription = "Back")
                AppIconButton(onClick = {}, iconRes = R.drawable.ic_settings, contentDescription = "Settings")
                AppIconButton(onClick = {}, iconRes = R.drawable.ic_search, contentDescription = "Search")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SampleGradientBrush)
                        .padding(8.dp),
                ) {
                    AppIconButton(onClick = {}, overlay = true, iconRes = R.drawable.ic_arrow_back, contentDescription = "Back overlay")
                }
            }
        }

        ShowcaseBlock(
            title = "AppAffordance",
            source = "AppAffordance",
            description = "Small accent-tinted inline action pills.",
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppAffordance(
                    onClick = {},
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_add), contentDescription = "Add") },
                )
                AppAffordance(
                    onClick = {},
                    label = "See all",
                    trailingIcon = { Icon(painter = painterResource(R.drawable.ic_chevron_right), contentDescription = null) },
                )
                AppAffordance(
                    onClick = {},
                    label = "Auto generate",
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_auto_awesome), contentDescription = null) },
                )
            }
        }
    }
}

@Composable
fun SelectionInputSectionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShowcaseBlock(
            title = "AppChip",
            source = "AppChip",
            description = "Selected state with accent12 container and tabular count badge.",
        ) {
            var selectedChip by remember { mutableStateOf("All") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppChip(
                    label = "All",
                    count = 42,
                    selected = selectedChip == "All",
                    onClick = { selectedChip = "All" },
                )
                AppChip(
                    label = "Tracks",
                    count = 12,
                    selected = selectedChip == "Tracks",
                    onClick = { selectedChip = "Tracks" },
                )
                AppChip(
                    label = "Albums",
                    selected = selectedChip == "Albums",
                    onClick = { selectedChip = "Albums" },
                )
            }
        }

        ShowcaseBlock(
            title = "AppSegmentedControl & AppSwitch",
            source = "AppSegmentedControl · AppSwitch",
            description = "Sliding thumb segmented control and custom 48×28dp toggle switch.",
        ) {
            var selectedTab by remember { mutableStateOf("Name") }
            var switchVal by remember { mutableStateOf(true) }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppSegmentedControl(
                    selectedValue = selectedTab,
                    options = listOf(
                        AppSegmentedControlOption("Name", "Name"),
                        AppSegmentedControlOption("Recent", "Recent"),
                        AppSegmentedControlOption("Color", "Color"),
                    ),
                    onOptionSelected = { selectedTab = it },
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppSwitch(checked = switchVal, onCheckedChange = { switchVal = it })
                    Text(
                        text = if (switchVal) "Enabled" else "Disabled",
                        style = AppTheme.typography.bodyMd,
                        color = AppTheme.colors.ink,
                    )
                }
            }
        }

        ShowcaseBlock(
            title = "AppTextField & AppSearchField",
            source = "AppTextField · AppSearchField",
            description = "Text fields with accent focus border and glow ring.",
        ) {
            var query by remember { mutableStateOf("Search term") }
            var text by remember { mutableStateOf("") }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppSearchField(
                    value = query,
                    onValueChange = { query = it },
                    onClear = { query = "" },
                )
                AppTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "Item name",
                    placeholder = "e.g. Design token",
                )
            }
        }

        ShowcaseBlock(
            title = "AppSliderRow",
            source = "AppSliderRow",
            description = "Slider with numerical tabular readout and optional note.",
        ) {
            var sliderVal by remember { mutableFloatStateOf(0.65f) }
            AppSliderRow(
                label = "Intensity",
                value = sliderVal,
                onValueChange = { sliderVal = it },
                note = "Controls level and effect amount.",
            )
        }

        ShowcaseBlock(
            title = "AppColorField & AppColorPopover",
            source = "AppColorField · AppColorPopoverCard",
            description = "Color picker field with floating popover palette.",
        ) {
            var color by remember { mutableStateOf(Color(0xFF3E5C76)) }
            var expanded by remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    color = AppTheme.colors.surface,
                    shape = AppTheme.shapes.card,
                ) {
                    AppColorField(
                        label = "Primary accent",
                        color = color,
                        onClick = { expanded = !expanded },
                        expanded = expanded,
                    )
                }

                if (expanded) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        AppColorPopoverCard(
                            color = color,
                            onColorChange = { color = it },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationStructureSectionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShowcaseBlock(
            title = "AppTopBar & AppDrillInTopBar",
            source = "AppTopBar · AppDrillInTopBar",
            description = "Standard 62dp bar and drill-in subscreen top bar.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTopBar(
                    title = "Library",
                    subtitle = "All items",
                    includeStatusBarPadding = false,
                    trailing = {
                        AppIconButton(
                            onClick = {},
                            iconRes = R.drawable.ic_settings,
                            contentDescription = "Settings",
                        )
                    },
                )

                AppDrillInTopBar(
                    title = "Item details",
                    onBackClick = {},
                    includeStatusBarPadding = false,
                )
            }
        }

        ShowcaseBlock(
            title = "AppBottomNav",
            source = "AppBottomNav · AppBottomNavItem",
            description = "Floating pill navigation capsule.",
        ) {
            var selectedIndex by remember { mutableIntStateOf(0) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.colors.background),
                contentAlignment = Alignment.Center,
            ) {
                AppBottomNav {
                    AppBottomNavItem(
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0 },
                        iconRes = R.drawable.ic_home,
                        contentDescription = "Home",
                    )
                    AppBottomNavItem(
                        selected = selectedIndex == 1,
                        onClick = { selectedIndex = 1 },
                        iconRes = R.drawable.ic_search,
                        contentDescription = "Search",
                    )
                }
            }
        }

        ShowcaseBlock(
            title = "AppListRow",
            source = "AppListRow · Grouped, Flat, and Danger",
            description = "List rows with adaptive joint radii and trailing switches/values.",
        ) {
            var toggleChecked by remember { mutableStateOf(true) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppShapes.CardRadius))
                    .background(AppTheme.colors.surface)
                    .border(1.dp, AppTheme.colors.hairline, RoundedCornerShape(AppShapes.CardRadius)),
            ) {
                AppListRow(
                    title = "Account preferences",
                    note = "Manage settings",
                    iconRes = R.drawable.ic_settings,
                    trailing = AppListRowTrailing.Chevron,
                    surface = AppListRowSurface.GroupedCard,
                    index = 0,
                    count = 3,
                    onClick = {},
                )
                AppListRow(
                    title = "Theme mode",
                    iconRes = R.drawable.ic_palette,
                    trailing = AppListRowTrailing.Value("Midnight"),
                    surface = AppListRowSurface.GroupedCard,
                    index = 1,
                    count = 3,
                    onClick = {},
                )
                AppListRow(
                    title = "Notifications",
                    iconRes = R.drawable.ic_schedule,
                    trailing = AppListRowTrailing.Switch(
                        checked = toggleChecked,
                        onCheckedChange = { toggleChecked = it },
                    ),
                    surface = AppListRowSurface.GroupedCard,
                    index = 2,
                    count = 3,
                )
            }
        }

        ShowcaseBlock(
            title = "AppActionBar & FloatingPill",
            source = "AppActionBar · AppFloatingPill",
            description = "Sticky bottom action bar and floating action pill.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppActionBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AppButton(
                            text = "Cancel",
                            onClick = {},
                            variant = AppButtonVariant.Secondary,
                            modifier = Modifier.weight(1f),
                        )
                        AppButton(
                            text = "Save changes",
                            onClick = {},
                            variant = AppButtonVariant.Primary,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SampleGradientBrush),
                    contentAlignment = Alignment.Center,
                ) {
                    AppFloatingPill(
                        onClick = {},
                        text = "Quick actions",
                        iconRes = R.drawable.ic_tune,
                    )
                }
            }
        }
    }
}

@Composable
fun CardsSectionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShowcaseBlock(
            title = "AppCard",
            source = "AppCard",
            description = "Standard 24dp rounded container with hairline border.",
        ) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Container card",
                        style = AppTheme.typography.titleMd,
                        color = AppTheme.colors.ink,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Surfaces group related elements with consistent border and background tokens.",
                        style = AppTheme.typography.bodyMd,
                        color = AppTheme.colors.inkMuted,
                    )
                }
            }
        }

        ShowcaseBlock(
            title = "AppDropdownMenu",
            source = "AppDropdownMenu",
            description = "Custom-styled popup menu.",
        ) {
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                AppButton(
                    text = "Open menu",
                    onClick = { menuExpanded = true },
                    variant = AppButtonVariant.Secondary,
                )
                AppDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    AppDropdownMenuItem(
                        text = "Edit item",
                        iconRes = R.drawable.ic_tune,
                        onClick = { menuExpanded = false },
                    )
                    AppDropdownMenuItem(
                        text = "Delete item",
                        iconRes = R.drawable.ic_close,
                        onClick = { menuExpanded = false },
                        danger = true,
                    )
                }
            }
        }
    }
}

@Composable
fun OverlaysFeedbackSectionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShowcaseBlock(
            title = "AppBottomSheet",
            source = "AppBottomSheetContent · AppSheetSurface",
            description = "Framed bottom sheet surface with 24dp corners and header grabber.",
        ) {
            AppSheetSurface {
                AppBottomSheetContent(
                    title = "Theme mode",
                    kicker = "Appearance",
                ) {
                    AppListRow(
                        title = "System default",
                        trailing = AppListRowTrailing.Checkmark,
                        surface = AppListRowSurface.FlatSheet,
                        index = 0,
                        count = 3,
                    )
                    AppListRow(
                        title = "Light mode",
                        surface = AppListRowSurface.FlatSheet,
                        index = 1,
                        count = 3,
                    )
                    AppListRow(
                        title = "Dark mode",
                        surface = AppListRowSurface.FlatSheet,
                        index = 2,
                        count = 3,
                    )
                }
            }
        }

        ShowcaseBlock(
            title = "AppDialogContent",
            source = "AppDialogContent",
            description = "Modal dialog content with title, description, and action buttons.",
        ) {
            Surface(
                shape = RoundedCornerShape(AppShapes.DialogRadius),
                color = AppTheme.colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.hairline),
                modifier = Modifier.fillMaxWidth(),
            ) {
                AppDialogContent(
                    title = "Confirm action?",
                    description = "Are you sure you want to proceed with this action?",
                    actions = {
                        AppButton(text = "Cancel", onClick = {}, variant = AppButtonVariant.Text)
                        AppButton(text = "Confirm", onClick = {}, variant = AppButtonVariant.Primary)
                    },
                )
            }
        }

        ShowcaseBlock(
            title = "AppSnackbar",
            source = "AppSnackbar",
            description = "Floating toast notification pill.",
        ) {
            AppSnackbar(
                message = "Settings updated successfully",
                actionLabel = "Undo",
                onActionClick = {},
            )
        }

        ShowcaseBlock(
            title = "Status & Inline Feedback",
            source = "AppEmptyState, AppBanner, AppHintCard, AppSkeleton, AppDoneConfirmation",
            description = "5 feedback and placeholder components.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppEmptyState(
                    title = "No items yet",
                    description = "Add your first item to get started.",
                )

                AppBanner(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = AppTheme.colors.ink)) {
                            append("Tip: ")
                        }
                        append("You can customize typography and density anytime in Settings.")
                    },
                )

                AppHintCard(
                    text = "Tap on any card to view detailed specifications.",
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.ic_info), contentDescription = null)
                    },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppSkeleton(
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp),
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        AppDoneConfirmation(
                            title = "Completed",
                            description = "All changes saved.",
                        )
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun DesignSystemShowcasePreview() {
    DesignSystemShowcase()
}
