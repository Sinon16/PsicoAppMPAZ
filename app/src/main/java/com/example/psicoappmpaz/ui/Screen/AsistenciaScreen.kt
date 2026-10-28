package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.ui.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsistenciaScreen(
    rolUsuario: String = "Defecto",
    nombreUsuario: String = "",
    onIrInicio: () -> Unit = {},
    onIrEvaluaciones: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    // Estado para ir a EnConstruccionScreen al presionar "Ver detalles"
    var verDetallesEnConstruccion by remember { mutableStateOf(false) }

    val estudiantes = listOf(
        "Estudiante EST-001",
        "Estudiante EST-002",
        "Estudiante EST-003",
        "Estudiante EST-004"
    )

    val asistenciaMap = remember {
        mutableStateMapOf(
            "Estudiante EST-001" to true,
            "Estudiante EST-002" to true,
            "Estudiante EST-003" to true,
            "Estudiante EST-004" to false
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Registro de Asistencia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Escuela Escritora Marcela Paz",
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
        },
        bottomBar = {
            AppBottomBar(
                itemSeleccionado = 2,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onNavegarInicio = onIrInicio,
                onNavegarEvaluaciones = onIrEvaluaciones,
                onNavegarAsistencia = { /* Ya está aquí */ },
                onNavegarPerfil = onIrPerfil
            )
        }
    ) { innerPadding ->
        if (verDetallesEnConstruccion) {
            EnConstruccionScreen(
                nombrePantalla = "Detalle de Asistencia en Base de Datos",
                onVolver = { verDetallesEnConstruccion = false },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Cabecera del curso
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Curso: 4° Básico A",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Presentes: ${asistenciaMap.values.count { it }} de ${estudiantes.size}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        TextButton(onClick = { verDetallesEnConstruccion = true }) {
                            Text("Ver detalles")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(estudiantes) { codigoEstudiante ->
                        val presente = asistenciaMap[codigoEstudiante] ?: false
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { asistenciaMap[codigoEstudiante] = !presente }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = presente,
                                    onCheckedChange = { asistenciaMap[codigoEstudiante] = it }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = codigoEstudiante,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (presente) "Estado: Presente" else "Estado: Ausente",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                TextButton(onClick = { verDetallesEnConstruccion = true }) {
                                    Text("Ver detalles")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { verDetallesEnConstruccion = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Guardar Asistencia")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AsistenciaPreview() {
    AsistenciaScreen()
}