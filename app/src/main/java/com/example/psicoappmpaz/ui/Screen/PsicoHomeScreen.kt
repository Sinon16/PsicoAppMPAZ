package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsicoHomeScreen(
    onCrearAplicacion: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onIrAsistencia: () -> Unit
) {
    var mostrarDialogoSincronizacion by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Psicología Escolar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Escuela Escritora Marcela Paz",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                actions = {
                    // Botón para simular sincronización con servidor remoto
                    IconButton(onClick = { mostrarDialogoSincronizacion = true }) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Sincronizar Cloud",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    // Botón para ir al módulo de Asistencia
                    IconButton(onClick = onIrAsistencia) {
                        Icon(
                            imageVector = Icons.Default.HowToReg,
                            contentDescription = "Asistencia",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera de Bienvenida
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "TU ESPACIO DE ACOMPAÑAMIENTO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Hola, Carolina 👋",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cada evaluación, un paso hacia su bienestar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 2. Tarjetas de Resumen
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "En curso",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "12",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "evaluaciones",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Completadas",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "28",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "evaluaciones",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // 3. Botón de Acción Principal
            item {
                Button(
                    onClick = onCrearAplicacion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Asignar nueva evaluación",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // 4. Sección de Evaluaciones Recientes
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Evaluaciones recientes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Lista estática de prueba
            items(listaEvaluacionesDemo) { eval ->
                ElevatedCard(
                    onClick = { onVerDetalle(eval.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = eval.estudiante,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                // Tag de estado
                                Surface(
                                    color = if (eval.estado == "Completada")
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = eval.estado,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = eval.curso,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = eval.test,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Ver detalles",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    // Modal de Advertencia de Sincronización Remota (Cumple con requisito de módulo en construcción)
    if (mostrarDialogoSincronizacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoSincronizacion = false },
            icon = { Icon(Icons.Default.CloudOff, contentDescription = null) },
            title = { Text("Sincronización Cloud") },
            text = {
                Text("Actualmente la aplicación opera en Modo Local (Offline). Las respuestas están respaldadas de forma segura en la base de datos Room del dispositivo.")
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoSincronizacion = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}

// Datos ficticios
private data class EvalDemo(
    val id: Int,
    val estudiante: String,
    val curso: String,
    val test: String,
    val estado: String
)

private val listaEvaluacionesDemo = listOf(
    EvalDemo(118, "Estudiante #118", "4° Básico • Sección B", "Cuestionario Educativo A", "Completada"),
    EvalDemo(124, "Estudiante #124", "3° Básico • Sección A", "Escala de Bienestar Escolar", "En curso"),
    EvalDemo(130, "Estudiante #130", "5° Básico • Sección A", "Cuestionario Educativo A", "Completada")
)