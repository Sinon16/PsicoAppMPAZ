package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psicoappmpaz.ui.viewmodel.CrearCuestionarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEvaluacionScreen(
    viewModel: CrearCuestionarioViewModel = viewModel(),
    onVolver: () -> Unit
) {
    val opcionesEscala = listOf("Muy bien", "Bien", "Medio", "Mal", "Muy mal")

    if (viewModel.fueGuardado) {
        AlertDialog(
            onDismissRequest = onVolver,
            title = { Text("Evaluación Guardada") },
            text = { Text("El cuestionario '${viewModel.tituloEvaluacion}' con ${viewModel.listaPreguntas.size} preguntas ya está disponible en el catálogo.") },
            confirmButton = {
                Button(onClick = onVolver) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Evaluación") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            item {
                Text(
                    text = "Detalles Principales",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.tituloEvaluacion,
                    onValueChange = { viewModel.tituloEvaluacion = it },
                    label = { Text("Nombre de la Evaluación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !viewModel.cargando
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.descripcionEvaluacion,
                    onValueChange = { viewModel.descripcionEvaluacion = it },
                    label = { Text("Descripción u Objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    enabled = !viewModel.cargando
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Preguntas (${viewModel.listaPreguntas.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedButton(
                        onClick = { viewModel.agregarPregunta() },
                        enabled = !viewModel.cargando
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar pregunta")
                    }
                }
            }

            itemsIndexed(viewModel.listaPreguntas) { index, pregunta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
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

                            if (viewModel.listaPreguntas.size > 1) {
                                IconButton(
                                    onClick = { viewModel.eliminarPregunta(index) },
                                    enabled = !viewModel.cargando
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = pregunta.enunciado,
                            onValueChange = { nuevoTexto ->
                                viewModel.actualizarEnunciadoPregunta(index, nuevoTexto)
                            },
                            label = { Text("Enunciado de la pregunta") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !viewModel.cargando
                        )

                        Text(
                            text = "Opciones de respuesta precargadas:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            opcionesEscala.forEach { opcion ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected = (pregunta.opcionSeleccionada == opcion),
                                        onClick = { /* Solo demostrativo en creación */ }
                                    )
                                    Text(text = opcion, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }

            item {
                viewModel.mensajeError?.let { err ->
                    Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { viewModel.guardarCuestionario() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = viewModel.tituloEvaluacion.isNotBlank() &&
                            viewModel.listaPreguntas.all { it.enunciado.isNotBlank() } &&
                            !viewModel.cargando
                ) {
                    if (viewModel.cargando) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Guardar Evaluación", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}