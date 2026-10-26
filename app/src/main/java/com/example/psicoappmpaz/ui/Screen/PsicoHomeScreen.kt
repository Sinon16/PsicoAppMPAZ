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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.model.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsicoHomeScreen(
    nombreUsuario: String = "Defecto",
    onCrearAplicacion: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onIrAsistencia: () -> Unit,
    onIrEvaluaciones: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    var itemNavegacionSeleccionado by remember { mutableIntStateOf(0) }

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
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    // Botón para Ir a Asistencia
                    IconButton(onClick = onIrAsistencia) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Asistencia",
                            tint = Color.White
                        )
                    }
                    // Botón Reemplazado: Cerrar Sesión
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(60.dp),
                windowInsets = WindowInsets(0.dp)
            ) {
                NavigationBarItem(
                    selected = itemNavegacionSeleccionado == 0,
                    onClick = { itemNavegacionSeleccionado = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = itemNavegacionSeleccionado == 1,
                    onClick = {
                        itemNavegacionSeleccionado = 1
                        onIrEvaluaciones()
                    },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Evaluaciones") },
                    label = { Text("Evaluaciones") }
                )
                NavigationBarItem(
                    selected = itemNavegacionSeleccionado == 2,
                    onClick = {
                        itemNavegacionSeleccionado = 2
                        onIrAsistencia()
                    },
                    icon = { Icon(Icons.Default.Group, contentDescription = "Asistencia") },
                    label = { Text("Asistencia") }
                )
                NavigationBarItem(
                    selected = itemNavegacionSeleccionado == 3,
                    onClick = { itemNavegacionSeleccionado = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "TU ESPACIO DE ACOMPAÑAMIENTO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Hola, $nombreUsuario 👋",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cada evaluación, un paso hacia su bienestar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 2. Tarjetas de Resumen (En curso / Completadas)
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
                            Text(text = "evaluaciones", style = MaterialTheme.typography.bodySmall)
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
                            Text(text = "evaluaciones", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // 3. crear usuario temporal
            item {
                Button(
                    onClick = onCrearAplicacion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Crear Alumno Temporal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 4. Últimas Evaluaciones
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Últimas evaluaciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { onVerDetalle(118) }) {
                        Text("Ver todas")
                    }
                }
            }

            // Lista de elementos recientes
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
                            Text(
                                text = eval.test,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${eval.curso} • ${eval.fecha}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Surface(
                            color = if (eval.estado == "Completada")
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = eval.estado,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class EvalDemo(
    val id: Int,
    val test: String,
    val curso: String,
    val fecha: String,
    val estado: String
)

private val listaEvaluacionesDemo = listOf(
    EvalDemo(118, "Evaluación de Lenguaje", "4° Básico A", "12 may. 2025", "En curso"),
    EvalDemo(124, "Evaluación Socioemocional", "5° Básico B", "10 may. 2025", "Completada"),
    EvalDemo(130, "Seguimiento Individual", "3° Básico A", "8 may. 2025", "En curso")
)