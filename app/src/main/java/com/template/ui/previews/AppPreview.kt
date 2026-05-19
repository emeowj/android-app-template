package com.template.ui.previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.template.screens.home.HomeScreen
import com.template.ui.theme.ColorRoles
import com.template.ui.theme.LocalColorRoles
import com.template.ui.theme.TemplateTheme

@Composable
fun AppPreview(
    typography: Typography? = null,
    darkTheme: Boolean? = null,
    colorScheme: ColorScheme? = null,
    colorRoles: ColorRoles? = null,
    content: @Composable () -> Unit,
) {
    TemplateTheme(
        typography = typography,
        darkTheme = darkTheme,
        colorScheme = colorScheme,
        colorRoles = colorRoles,
    ) {
        val backStack = rememberSaveableBackStack(root = HomeScreen)
        val navigator = rememberCircuitNavigator(backStack = backStack)

        Surface(color = LocalColorRoles.current.surface) {
            CircuitCompositionLocals(circuit = PreviewCircuit) { ContentWithOverlays { content() } }
        }
    }
}

val PreviewCircuit =
    Circuit.Builder()
        .setOnUnavailableContent { screen, modifier ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Preview placeholder for ${screen::class.simpleName}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        .build()
