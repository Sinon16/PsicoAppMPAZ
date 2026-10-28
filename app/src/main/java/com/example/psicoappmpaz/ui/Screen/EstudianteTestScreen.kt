package com.example.psicoappmpaz.ui.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteTestScreen(
    onFinalizar: () -> Unit = {}
) {
    val preguntas = listOf(
        "¿Cómo te sientes cuando llegas a la escuela?",
        "¿Cómo te sientes al aprender algo nuevo?",
        "¿Cómo te sientes cuando realizas trabajos en grupo?",
        "¿Cómo te sientes cuando participas en clase?",
        "¿Cómo te sientes al terminar tu día en la escuela?"
    )

    val opciones = listOf(
        "Muy bien 😄",
        "Bien 🙂",
        "A veces difícil 😐",
        "Difícil 😟"
    )

    var indicePregunta by remember { mutableIntStateOf(0) }
    var opcionSeleccionada by remember { mutableStateOf("") }

    var enviandoRespuestas by remember { mutableStateOf(false) }
    var envioExitoso by remember { mutableStateOf(false) }

    val progresoAnimado = (indicePregunta + 1).toFloat() / preguntas.size.toFloat()

    // Temporizador: simula envio y luego cierra la sesion temporal
    LaunchedEffect(enviandoRespuestas) {
        if (enviandoRespuestas) {
            delay(1500)
            enviandoRespuestas = false
            envioExitoso = true
            delay(2500)
            if (envioExitoso) {
                envioExitoso = false
                onFinalizar()
            }
        }
    }

    // Ventana emergente de confirmacion de envio
    if (enviandoRespuestas || envioExitoso) {
        AlertDialog(
            onDismissRequest = { /* Bloqueado mientras cierra sesion */ },
            icon = {
                if (enviandoRespuestas) {
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
                    text = if (enviandoRespuestas) "Enviando respuestas..." else "¡Respuestas enviadas!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = if (enviandoRespuestas) {
                        "Guardando tu cuestionario de forma confidencial..."
                    } else {
                        "Tu participación fue registrada correctamente (Almacenamiento BD: Para una futura actualización).\n\nPor seguridad, tu código temporal ha expirado. Volviendo al inicio..."
                    },
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                if (envioExitoso) {
                    TextButton(
                        onClick = {
                            envioExitoso = false
                            onFinalizar()
                        }
                    ) {
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
                            text = "Cuestionario Escolar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sesión supervisada - Código temporal",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        text = preguntas[indicePregunta],
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
                        .clickable { opcionSeleccionada = opcion },
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
                            onClick = { opcionSeleccionada = opcion }
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

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (indicePregunta > 0) {
                    OutlinedButton(
                        onClick = {
                            indicePregunta--
                            opcionSeleccionada = ""
                        },
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
                            opcionSeleccionada = ""
                        } else {
                            enviandoRespuestas = true
                        }
                    },
                    enabled = opcionSeleccionada.isNotEmpty() && !enviandoRespuestas,
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

@Preview(showBackground = true)
@Composable
fun EstudianteTestPreview() {
    EstudianteTestScreen()
}