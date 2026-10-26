package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EnConstruccionScreen(
    nombrePantalla: String,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier // <--- Agrega este parámetro
) {
    Box(
        modifier = modifier.fillMaxSize(), // <--- Aplica el modifier aquí
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nombrePantalla,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pantalla en construcción 🚧",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onVolver) {
                Text("Volver")
            }
        }
    }
}