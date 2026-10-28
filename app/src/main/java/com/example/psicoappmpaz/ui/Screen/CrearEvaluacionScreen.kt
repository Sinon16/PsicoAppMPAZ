package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEvaluacionScreen(
    onVolver: () -> Unit
) {
    var fueGuardado by remember { mutableStateOf(false) }

    if (fueGuardado) {
        EnConstruccionScreen(
            nombrePantalla = "Procesando Nueva Evaluación",
            onVolver = onVolver
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear Nueva Evaluación") },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var tituloEvaluacion by remember { mutableStateOf("") }
                var descripcionEvaluacion by remember { mutableStateOf("") }

                Text(
                    text = "Detalles de la Evaluación",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = tituloEvaluacion,
                    onValueChange = { tituloEvaluacion = it },
                    label = { Text("Nombre de la Evaluación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descripcionEvaluacion,
                    onValueChange = { descripcionEvaluacion = it },
                    label = { Text("Descripción u Objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { fueGuardado = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = tituloEvaluacion.isNotBlank()
                ) {
                    Text("Guardar Evaluación", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}