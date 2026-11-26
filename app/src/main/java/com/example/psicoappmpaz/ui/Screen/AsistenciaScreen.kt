package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psicoappmpaz.model.Estudiante
import com.example.psicoappmpaz.ui.components.AppBottomBar
import com.example.psicoappmpaz.ui.viewmodel.AsistenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsistenciaScreen(
    rolUsuario: String = "Defecto",
    nombreUsuario: String = "",
    onIrInicio: () -> Unit = {},
    onIrEvaluaciones: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onVolver: () -> Unit = {},
    viewModel: AsistenciaViewModel = viewModel()
) {
    var menuCursoAbierto by remember { mutableStateOf(false) }
    var mostrarDetalleCurso by remember { mutableStateOf(false) }
    var estudianteDetalle by remember { mutableStateOf<Estudiante?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarDatosIniciales()
    }

    // Diálogo: Detalle de asistencia del curso en BD
    if (mostrarDetalleCurso) {
        val presentes = viewModel.estudiantes.count { viewModel.asistenciaMap[it.id] == true }
        val ausentes = viewModel.estudiantes.size - presentes

        AlertDialog(
            onDismissRequest = { mostrarDetalleCurso = false },
            title = { Text("Detalle de Asistencia", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Curso: ${viewModel.cursoSeleccionado}", fontWeight = FontWeight.SemiBold)
                    Text("Fecha: ${viewModel.fechaHoy}")
                    Text("Presentes: $presentes  |  Ausentes: $ausentes")
                    HorizontalDivider()
                    if (viewModel.registrosDelDia.isEmpty()) {
                        Text(
                            text = "Aún no hay registros guardados hoy en la base de datos para este curso.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Text(
                            text = "Registros en BD hoy (${viewModel.registrosDelDia.size}):",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        // Muestra el último registro por estudiante
                        val ultimosPorAlumno = viewModel.registrosDelDia
                            .groupBy { it.estudiante?.id }
                            .mapValues { it.value.last() }
                            .values

                        ultimosPorAlumno.forEach { reg ->
                            val nombreEst = reg.estudiante?.nombre ?: "Estudiante"
                            val estado = if (reg.presente) "Presente" else "Ausente"
                            val registrador = reg.registradoPor?.nombreCompleto?.let { " (por $it)" } ?: ""
                            Text(
                                text = "• $nombreEst: $estado$registrador",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { mostrarDetalleCurso = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo: Detalle individual del estudiante
    estudianteDetalle?.let { est ->
        val presenteActual = viewModel.asistenciaMap[est.id] ?: false
        val ultimoRegistroBD = viewModel.registrosDelDia.lastOrNull { it.estudiante?.id == est.id }

        AlertDialog(
            onDismissRequest = { estudianteDetalle = null },
            title = { Text(est.nombre, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("ID Estudiante: #${est.id}")
                    Text("Curso: ${est.curso}")
                    Text("Tipo: ${if (est.esTemporal) "Temporal" else "Regular"}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Estado marcado hoy (${viewModel.fechaHoy}): ${if (presenteActual) "Presente" else "Ausente"}",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (ultimoRegistroBD != null) {
                            "Guardado en BD: ${if (ultimoRegistroBD.presente) "Presente" else "Ausente"}"
                        } else {
                            "Guardado en BD: Sin registro hoy todavía"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            },
            confirmButton = {
                Button(onClick = { estudianteDetalle = null }) {
                    Text("Cerrar")
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
                actions = {
                    IconButton(onClick = { viewModel.cargarDatosIniciales() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.White
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Selector de curso desde BD
            if (viewModel.cursosDisponibles.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = menuCursoAbierto,
                    onExpandedChange = { menuCursoAbierto = !menuCursoAbierto }
                ) {
                    OutlinedTextField(
                        value = viewModel.cursoSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccionar Curso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCursoAbierto) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = menuCursoAbierto,
                        onDismissRequest = { menuCursoAbierto = false }
                    ) {
                        viewModel.cursosDisponibles.forEach { curso ->
                            DropdownMenuItem(
                                text = { Text(curso) },
                                onClick = {
                                    menuCursoAbierto = false
                                    viewModel.seleccionarCurso(curso)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Cabecera resumen del curso
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (viewModel.cursoSeleccionado.isNotBlank()) {
                                    "Curso: ${viewModel.cursoSeleccionado}"
                                } else {
                                    "Sin curso seleccionado"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Presentes: ${viewModel.estudiantes.count { viewModel.asistenciaMap[it.id] == true }} de ${viewModel.estudiantes.size} • ${viewModel.fechaHoy}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    TextButton(onClick = { mostrarDetalleCurso = true }) {
                        Text("Ver detalles")
                    }
                }
            }

            // Mensajes de feedback
            viewModel.mensajeExito?.let { msg ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(msg, color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            viewModel.mensajeError?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                viewModel.cargando -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                viewModel.estudiantes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay estudiantes registrados en este curso.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(viewModel.estudiantes, key = { it.id }) { est ->
                            val presente = viewModel.asistenciaMap[est.id] ?: false
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.asistenciaMap[est.id] = !presente }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(14.dp)
                                        .fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = presente,
                                        onCheckedChange = { viewModel.asistenciaMap[est.id] = it }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = est.nombre,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (presente) "Estado: Presente" else "Estado: Ausente",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (presente) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                                        )
                                    }
                                    TextButton(onClick = { estudianteDetalle = est }) {
                                        Text("Ver detalles")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.guardarAsistencia() },
                enabled = viewModel.estudiantes.isNotEmpty() && !viewModel.guardando && !viewModel.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (viewModel.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text(text = "Guardar Asistencia")
                }
            }
        }
    }
}