package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.ui.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionesScreen(
    rolUsuario: String = "PSICOLOGO",
    nombreUsuario: String = "",
    onIrCrearEvaluacion: () -> Unit = {},
    onIrInicio: () -> Unit = {},
    onIrAsistencia: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Evaluaciones", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                itemSeleccionado = 1,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onNavegarInicio = onIrInicio,
                onNavegarEvaluaciones = { /* Ya está aquí */ },
                onNavegarAsistencia = onIrAsistencia,
                onNavegarPerfil = onIrPerfil
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
            // Cabecera con título y botón 'Crear Evaluación' alineado a la derecha
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Catálogo Disponible",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pruebas y cuestionarios activos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Button(
                        onClick = onIrCrearEvaluacion,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Crear Evaluación")
                    }
                }
            }

            // Lista del catálogo simulado
            items(listaCatalogoEvaluaciones) { eval ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = eval.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = eval.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Preguntas: ${eval.cantidadPreguntas}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = "Duración: ${eval.duracionEstimada}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class EvaluacionCatalogo(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val cantidadPreguntas: Int,
    val duracionEstimada: String
)

private val listaCatalogoEvaluaciones = listOf(
    EvaluacionCatalogo(
        id = 1,
        nombre = "Cuestionario Educativo A",
        descripcion = "Evaluación de competencias académicas y hábitos de estudio.",
        cantidadPreguntas = 15,
        duracionEstimada = "20 min"
    ),
    EvaluacionCatalogo(
        id = 2,
        nombre = "Escala de Bienestar Escolar",
        descripcion = "Medición del clima aula y percepción de apoyo escolar.",
        cantidadPreguntas = 10,
        duracionEstimada = "15 min"
    ),
    EvaluacionCatalogo(
        id = 3,
        nombre = "Cuestionario de Convivencia",
        descripcion = "Detección de dinámicas relacionales e integración grupal.",
        cantidadPreguntas = 20,
        duracionEstimada = "25 min"
    )
)