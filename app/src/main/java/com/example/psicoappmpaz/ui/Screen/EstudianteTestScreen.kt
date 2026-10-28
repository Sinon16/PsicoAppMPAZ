package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    var opcionSeleccionada by remember { mutableStateOf(opciones[0]) }
    val progreso = (indicePregunta + 1).toFloat() / preguntas.size.toFloat()

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
                            text = "Sesión supervisada • Código temporal",
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
            Text(
                text = "Pregunta ${indicePregunta + 1} de ${preguntas.size}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = preguntas[indicePregunta],
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            opciones.forEach { opcion ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { opcionSeleccionada = opcion }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (opcionSeleccionada == opcion),
                            onClick = { opcionSeleccionada = opcion }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = opcion,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (indicePregunta < preguntas.size - 1) {
                        indicePregunta++
                        opcionSeleccionada = opciones[0]
                    } else {
                        onFinalizar()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (indicePregunta < preguntas.size - 1) "Siguiente Pregunta" else "Finalizar y Enviar"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EstudianteTestPreview() {
    EstudianteTestScreen()
}