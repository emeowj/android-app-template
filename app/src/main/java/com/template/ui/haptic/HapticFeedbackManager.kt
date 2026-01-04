package com.template.ui.haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType as ComposeHapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

interface HapticFeedbackManager {
    fun performHaptic(type: HapticFeedbackType)

    fun setEnabled(enabled: Boolean)

    fun isEnabled(): Boolean
}

val LocalHapticFeedbackManager =
    staticCompositionLocalOf<HapticFeedbackManager> { DisabledHapticFeedbackManager }

@Composable
fun WithHapticFeedbackManager(enabled: Boolean = true, content: @Composable () -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val manager =
        remember(hapticFeedback, context) {
            RealHapticFeedbackManager(context, hapticFeedback).apply { setEnabled(enabled) }
        }
    CompositionLocalProvider(LocalHapticFeedbackManager provides manager) { content() }
}

private object DisabledHapticFeedbackManager : HapticFeedbackManager {
    override fun performHaptic(type: HapticFeedbackType) {}

    override fun setEnabled(enabled: Boolean) {}

    override fun isEnabled(): Boolean = false
}

private class RealHapticFeedbackManager(
    context: Context,
    private val hapticFeedback: HapticFeedback,
) : HapticFeedbackManager {

    private var enabled: Boolean = true

    private val vibrator: Vibrator? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(Vibrator::class.java)
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

    override fun performHaptic(type: HapticFeedbackType) {
        if (!enabled) return

        when (type) {
            HapticFeedbackType.TabClick -> playThud()
            HapticFeedbackType.Navigation -> playTick()
            HapticFeedbackType.ScrollToTop -> playClick()
            HapticFeedbackType.Action -> playClick()
            HapticFeedbackType.RefreshThreshold -> playHeavyClick()
            HapticFeedbackType.Selection -> playTick()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun isEnabled(): Boolean = enabled

    private fun playThud() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            playPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD)
        } else {
            hapticFeedback.performHapticFeedback(ComposeHapticFeedbackType.LongPress)
        }
    }

    private fun playTick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            playPredefined(VibrationEffect.EFFECT_TICK)
        } else {
            hapticFeedback.performHapticFeedback(ComposeHapticFeedbackType.TextHandleMove)
        }
    }

    private fun playClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            playPredefined(VibrationEffect.EFFECT_CLICK)
        } else {
            hapticFeedback.performHapticFeedback(ComposeHapticFeedbackType.LongPress)
        }
    }

    private fun playHeavyClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            playPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
        } else {
            hapticFeedback.performHapticFeedback(ComposeHapticFeedbackType.LongPress)
        }
    }

    private fun playPredefined(effectId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && vibrator != null) {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
        }
    }

    private fun playPrimitive(primitive: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && vibrator != null) {
            if (vibrator.areAllPrimitivesSupported(primitive)) {
                vibrator.vibrate(
                    VibrationEffect.startComposition().addPrimitive(primitive).compose()
                )
                return
            }
        }
        hapticFeedback.performHapticFeedback(ComposeHapticFeedbackType.LongPress)
    }
}
