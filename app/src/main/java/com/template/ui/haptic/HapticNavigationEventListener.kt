package com.template.ui.haptic

import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.navigation.intercepting.NavigationContext
import com.slack.circuitx.navigation.intercepting.NavigationEventListener

class HapticNavigationEventListener(private val hapticManager: HapticFeedbackManager) :
    NavigationEventListener {
    override fun goTo(screen: Screen, navigationContext: NavigationContext) {
        hapticManager.performHaptic(HapticFeedbackType.Navigation)
    }
}
