package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class PreguntaFormulario(
    var textoPregunta: String = "",
    var opcionSeleccionada: String = "Medio" // Ponderación/Escala por defecto
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEvaluacionScreen(
    onVolver: () -> Unit
) {
    // 5 Opciones de escala precargadas
    val opcionesEscala = listOf("Muy bien", "Bien", "Medio", "Mal", "Muy mal")

    var tituloEvaluacion by remember { mutableStateOf("") }
    var descripcionEvaluacion by remember { mutableStateOf("") }

    // Lista dinámica de preguntas
    val listaPreguntas = remember { mutableStateListOf(PreguntaFormulario()) }

    var fueGuardado by remember { mutableStateOf(false) }

    if (fueGuardado) {
        EnConstruccionScreen(
            nombrePantalla = "Procesando Evaluación: $tituloEvaluacion",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Datos generales
                item {
                    Text(
                        text = "Detalles Principales",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = tituloEvaluacion,
                        onValueChange = { tituloEvaluacion = it },
                        label = { Text("Nombre de la Evaluación") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = descripcionEvaluacion,
                        onValueChange = { descripcionEvaluacion = it },
                        label = { Text("Descripción u Objetivo") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Preguntas (${listaPreguntas.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedButton(
                            onClick = { listaPreguntas.add(PreguntaFormulario()) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar pregunta")
                        }
                    }
                }

                // 2. Tarjetas de preguntas con opciones precargadas visibles
                itemsIndexed(listaPreguntas) { index, pregunta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pregunta N° ${index + 1}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (listaPreguntas.size > 1) {
                                    IconButton(
                                        onClick = { listaPreguntas.removeAt(index) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            // Escribir el enunciado
                            OutlinedTextField(
                                value = pregunta.textoPregunta,
                                onValueChange = { nuevoTexto ->
                                    listaPreguntas[index] = pregunta.copy(textoPregunta = nuevoTexto)
                                },
                                label = { Text("Enunciado de la pregunta") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Text(
                                text = "Opciones de respuesta precargadas:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )

                            // 5 Opciones precargadas visibles con selección tipo Likert
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                opcionesEscala.forEach { opcion ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        RadioButton(
                                            selected = (pregunta.opcionSeleccionada == opcion),
                                            onClick = {
                                                listaPreguntas[index] = pregunta.copy(opcionSeleccionada = opcion)
                                            }
                                        )
                                        Text(
                                            text = opcion,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Botón para redirigir
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { fueGuardado = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = tituloEvaluacion.isNotBlank() &&
                                listaPreguntas.all { it.textoPregunta.isNotBlank() }
                    ) {
                        Text("Guardar Evaluación", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}