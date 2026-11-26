package com.example.psicoappmpaz.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psicoappmpaz.ui.viewmodel.EstudianteTestViewModel
import com.example.psicoappmpaz.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteTestScreen(
    onFinalizar: () -> Unit = {},
    testViewModel: EstudianteTestViewModel = viewModel()
) {
    val activity = LocalContext.current as? ComponentActivity
    val loginViewModel: LoginViewModel = if (activity != null) {
        viewModel(viewModelStoreOwner = activity)
    } else {
        viewModel()
    }

    val codigo = loginViewModel.codigoEstudiante.ifBlank {
        loginViewModel.aplicacionActivaEstudiante?.codigoTemporal ?: ""
    }

    // Carga la aplicación desde memoria o la pide al backend por código
    LaunchedEffect(codigo) {
        val appMemoria = loginViewModel.aplicacionActivaEstudiante
        if (appMemoria != null && !appMemoria.cuestionario?.preguntas.isNullOrEmpty()) {
            testViewModel.aplicacionActual = appMemoria
        } else if (codigo.isNotBlank()) {
            testViewModel.cargarAplicacionPorCodigo(codigo)
        }
    }

    val opciones = listOf(
        "Muy bien 😄",
        "Bien 🙂",
        "A veces difícil 😐",
        "Difícil 😟"
    )

    val preguntas = remember(testViewModel.aplicacionActual) {
        testViewModel.aplicacionActual?.cuestionario?.preguntas
            ?.sortedBy { it.orden }
            ?: emptyList()
    }

    var indicePregunta by remember { mutableIntStateOf(0) }

    // Cierre automático tras envío exitoso
    LaunchedEffect(testViewModel.envioExitoso) {
        if (testViewModel.envioExitoso) {
            delay(2500)
            onFinalizar()
        }
    }

    // Diálogo de envío / confirmación real
    if (testViewModel.enviando || testViewModel.envioExitoso) {
        AlertDialog(
            onDismissRequest = { /* Bloqueado mientras cierra sesión */ },
            icon = {
                if (testViewModel.enviando) {
                    CircularProgressIndicator(modifier = Modifier.size(44.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(52.dp)
                    )
                }
            },
            title = {
                Text(
                    text = if (testViewModel.enviando) "Enviando respuestas..." else "¡Respuestas enviadas!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = if (testViewModel.enviando) {
                        "Guardando tu cuestionario de forma confidencial en el servidor..."
                    } else {
                        "Tu participación fue registrada correctamente.\n\nPor seguridad, tu sesión temporal ha finalizado. Volviendo al inicio..."
                    },
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                if (testViewModel.envioExitoso) {
                    TextButton(onClick = onFinalizar) {
                        Text("Salir ahora")
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = testViewModel.aplicacionActual?.cuestionario?.titulo ?: "Cuestionario Escolar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (codigo.isNotBlank()) "Sesión supervisada • Código $codigo" else "Sesión supervisada",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                testViewModel.cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                preguntas.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = testViewModel.mensajeError
                                ?: "No se encontraron preguntas para este cuestionario.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onFinalizar) {
                            Text("Volver al inicio")
                        }
                    }
                }

                else -> {
                    val preguntaActual = preguntas[indicePregunta]
                    val opcionSeleccionada = testViewModel.respuestasSeleccionadas[preguntaActual.id] ?: ""
                    val progresoAnimado = (indicePregunta + 1).toFloat() / preguntas.size.toFloat()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pregunta ${indicePregunta + 1} de ${preguntas.size}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockClock,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${(progresoAnimado * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progresoAnimado },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.QuestionAnswer,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Lee con atención y elige una opción:",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = preguntaActual.enunciado,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        opciones.forEach { opcion ->
                            val estaSeleccionada = (opcionSeleccionada == opcion)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        testViewModel.respuestasSeleccionadas[preguntaActual.id] = opcion
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (estaSeleccionada)
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                border = BorderStroke(
                                    width = if (estaSeleccionada) 2.dp else 1.dp,
                                    color = if (estaSeleccionada)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outlineVariant
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected = estaSeleccionada,
                                        onClick = {
                                            testViewModel.respuestasSeleccionadas[preguntaActual.id] = opcion
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = opcion,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        testViewModel.mensajeError?.let { err ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = err,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (indicePregunta > 0) {
                                OutlinedButton(
                                    onClick = { indicePregunta-- },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                ) {
                                    Text("Anterior")
                                }
                            }

                            Button(
                                onClick = {
                                    if (indicePregunta < preguntas.size - 1) {
                                        indicePregunta++
                                    } else {
                                        testViewModel.enviarRespuestas(codigo)
                                    }
                                },
                                enabled = opcionSeleccionada.isNotEmpty() && !testViewModel.enviando,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = if (indicePregunta < preguntas.size - 1)
                                        "Siguiente"
                                    else
                                        "Finalizar y Enviar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}