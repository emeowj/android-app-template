package com.template.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.navigation.intercepting.rememberInterceptingNavigator
import com.template.R
import com.template.data.settings.HapticFeedbackEnabledKey
import com.template.data.settings.rememberPreference
import com.template.screens.home.HomeScreen
import com.template.screens.search.SearchScreen
import com.template.ui.haptic.HapticFeedbackType
import com.template.ui.haptic.HapticNavigationEventListener
import com.template.ui.haptic.LocalHapticFeedbackManager
import com.template.ui.haptic.WithHapticFeedbackManager
import com.template.ui.previews.AppPreview
import com.template.ui.previews.PreviewCircuit
import com.template.ui.previews.Previews
import kotlinx.collections.immutable.persistentListOf

enum class NavigationTab(
    val labelRes: Int,
    val icon: Int,
    val iconFilled: Int,
    val screen: Screen,
) {
    Home(
        labelRes = R.string.nav_home,
        icon = R.drawable.ic_home,
        iconFilled = R.drawable.ic_home_filled,
        screen = HomeScreen,
    ),
    Search(
        labelRes = R.string.nav_search,
        icon = R.drawable.ic_search,
        iconFilled = R.drawable.ic_search_filled,
        screen = SearchScreen(),
    ),
}

@Composable
fun AppScaffold(circuit: Circuit, modifier: Modifier = Modifier) {
    CircuitCompositionLocals(circuit) {
        val hapticFeedbackEnabled by rememberPreference(HapticFeedbackEnabledKey, true)
        WithHapticFeedbackManager(enabled = hapticFeedbackEnabled) {
            val backStack = rememberSaveableBackStack(root = HomeScreen)
            val baseNavigator = rememberCircuitNavigator(backStack, enableBackHandler = true)
            val hapticManager = LocalHapticFeedbackManager.current
            val eventListeners =
                remember(hapticManager) {
                    persistentListOf(HapticNavigationEventListener(hapticManager))
                }
            val navigator =
                rememberInterceptingNavigator(
                    navigator = baseNavigator,
                    eventListeners = eventListeners,
                )

            val selectedTab by
                remember(backStack) {
                    derivedStateOf {
                        val top = backStack.topRecord?.screen
                        NavigationTab.entries.firstOrNull { it.screen == top }
                    }
                }

            ContentWithOverlays {
                Scaffold(
                    modifier = modifier,
                    bottomBar = {
                        AnimatedVisibility(
                            visible = selectedTab != null,
                            enter = slideInVertically { it } + fadeIn(),
                            exit = slideOutVertically { it } + fadeOut(),
                        ) {
                            BottomNavBar(selectedTab = selectedTab, navigator = navigator)
                        }
                    },
                    contentWindowInsets = WindowInsets(0),
                ) { padding ->
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = Modifier.padding(padding),
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    selectedTab: NavigationTab?,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val hapticManager = LocalHapticFeedbackManager.current
    ShortNavigationBar(modifier = modifier) {
        NavigationTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            ShortNavigationBarItem(
                selected = selected,
                onClick = {
                    hapticManager.performHaptic(HapticFeedbackType.TabClick)
                    if (!selected) {
                        navigator.resetRoot(tab.screen)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(if (selected) tab.iconFilled else tab.icon),
                        contentDescription = stringResource(tab.labelRes),
                    )
                },
                label = null,
            )
        }
    }
}

@Previews
@Composable
private fun AppScaffoldPreview() {
    AppPreview { AppScaffold(circuit = PreviewCircuit) }
}
