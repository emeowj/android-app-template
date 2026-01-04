package com.template

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.template.data.settings.DarkMode
import com.template.data.settings.DarkModeKey
import com.template.data.settings.rememberEnumPreference
import com.template.ui.AppScaffold
import com.template.ui.theme.TemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appGraph = (application as TemplateApplication).appGraph

        setContent {
            val darkMode by rememberEnumPreference<DarkMode>(DarkModeKey)
            val isDarkTheme = when (darkMode) {
                DarkMode.SYSTEM -> isSystemInDarkTheme()
                DarkMode.LIGHT -> false
                DarkMode.DARK -> true
            }

            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        detectDarkMode = { isDarkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        DefaultLightScrim,
                        DefaultDarkScrim,
                        detectDarkMode = { isDarkTheme }
                    ),
                )
            }

            TemplateTheme(darkTheme = isDarkTheme) {
                AppScaffold(circuit = appGraph.circuit)
            }
        }
    }

    companion object {
        private val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
        private val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}
