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
import com.example.psicoappmpaz.data.models.CrearCuestionarioRequest
import com.example.psicoappmpaz.data.models.CrearPreguntaRequest
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

data class PreguntaFormulario(
    var textoPregunta: String = "",
    var opcionSeleccionada: String = "Medio"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEvaluacionScreen(
    onVolver: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val opcionesEscala = listOf("Muy bien", "Bien", "Medio", "Mal", "Muy mal")

    var tituloEvaluacion by remember { mutableStateOf("") }
    var descripcionEvaluacion by remember { mutableStateOf("") }
    val listaPreguntas = remember { mutableStateListOf(PreguntaFormulario()) }

    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var fueGuardado by remember { mutableStateOf(false) }

    if (fueGuardado) {
        AlertDialog(
            onDismissRequest = onVolver,
            title = { Text("Evaluación Guardada") },
            text = { Text("El cuestionario '$tituloEvaluacion' con ${listaPreguntas.size} preguntas ya está disponible en el catálogo.") },
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
                    value = tituloEvaluacion,
                    onValueChange = { tituloEvaluacion = it },
                    label = { Text("Nombre de la Evaluación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcionEvaluacion,
                    onValueChange = { descripcionEvaluacion = it },
                    label = { Text("Descripción u Objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    enabled = !cargando
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
                        onClick = { listaPreguntas.add(PreguntaFormulario()) },
                        enabled = !cargando
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
                                    onClick = { listaPreguntas.removeAt(index) },
                                    enabled = !cargando
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = pregunta.textoPregunta,
                            onValueChange = { nuevoTexto ->
                                listaPreguntas[index] = pregunta.copy(textoPregunta = nuevoTexto)
                            },
                            label = { Text("Enunciado de la pregunta") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !cargando
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

            item {
                mensajeError?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val token = RetrofitClient.getBearerToken()
                        scope.launch {
                            cargando = true
                            mensajeError = null
                            try {
                                val preguntasDto = listaPreguntas.mapIndexed { idx, p ->
                                    CrearPreguntaRequest(
                                        textoPregunta = p.textoPregunta.trim(),
                                        opcionesRespuesta = opcionesEscala.joinToString("|"),
                                        orden = idx + 1
                                    )
                                }
                                val request = CrearCuestionarioRequest(
                                    titulo = tituloEvaluacion.trim(),
                                    descripcion = descripcionEvaluacion.trim(),
                                    preguntas = preguntasDto
                                )
                                val response = RetrofitClient.apiService.crearCuestionario(token, request)
                                if (response.isSuccessful) {
                                    fueGuardado = true
                                } else {
                                    mensajeError = "Error al guardar cuestionario (${response.code()})"
                                }
                            } catch (e: Exception) {
                                mensajeError = "Error de conexión: ${e.localizedMessage}"
                            } finally {
                                cargando = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = tituloEvaluacion.isNotBlank() &&
                            listaPreguntas.all { it.textoPregunta.isNotBlank() } &&
                            !cargando
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Guardar Evaluación", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}