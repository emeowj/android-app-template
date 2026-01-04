package com.template.ui.previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.template.screens.home.HomeScreen
import com.template.ui.theme.TemplateTheme

@Composable
fun AppPreview(content: @Composable () -> Unit) {
    TemplateTheme {
        val backStack = rememberSaveableBackStack(root = HomeScreen)
        val navigator = rememberCircuitNavigator(backStack = backStack)

        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            CircuitCompositionLocals(circuit = PreviewCircuit) { ContentWithOverlays { content() } }
        }
    }
}

val PreviewCircuit =
    Circuit.Builder()
        .setOnUnavailableContent { screen, modifier ->
            Box(
                modifier = modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Preview placeholder for ${screen::class.simpleName}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        .build()
