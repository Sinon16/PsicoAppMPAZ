package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.model.Aplicacion
import com.example.psicoappmpaz.model.Asistencia
import com.example.psicoappmpaz.network.RetrofitClient
import com.example.psicoappmpaz.ui.components.AppBottomBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesorHomeScreen(
    nombreUsuario: String = "Docente",
    onIrAsistencia: () -> Unit = {},
    onIrEvaluaciones: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    var aplicaciones by remember { mutableStateOf<List<Aplicacion>>(emptyList()) }
    var asistenciasHoy by remember { mutableStateOf<List<Asistencia>>(emptyList()) }
    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Evaluación seleccionada para ver las indicaciones del psicólogo
    var aplicacionSeleccionada by remember { mutableStateOf<Aplicacion?>(null) }

    val fechaHoy = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun cargarResumenAula() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        scope.launch {
            cargando = true
            mensajeError = null
            try {
                val respApps = RetrofitClient.apiService.getAplicaciones(token)
                if (respApps.isSuccessful && respApps.body() != null) {
                    aplicaciones = respApps.body()!!
                } else {
                    mensajeError = "Error al cargar evaluaciones (${respApps.code()})"
                }

                val respAsis = RetrofitClient.apiService.getAsistencias(token, fechaHoy)
                if (respAsis.isSuccessful && respAsis.body() != null) {
                    asistenciasHoy = respAsis.body()!!
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarResumenAula()
    }

    // Cálculos dinámicos desde BD
    val totalApps = aplicaciones.size
    val totalFinalizadas = aplicaciones.count {
        it.estado.equals("COMPLETADA", ignoreCase = true) ||
                it.estado.equals("REVISADA", ignoreCase = true)
    }
    val progresoApps = if (totalApps > 0) totalFinalizadas.toFloat() / totalApps.toFloat() else 0f

    val ultimasAsistenciasPorAlumno = asistenciasHoy
        .groupBy { it.estudiante?.id }
        .mapValues { it.value.last() }
        .values
    val totalAsistenciaReg = ultimasAsistenciasPorAlumno.size
    val totalPresentes = ultimasAsistenciasPorAlumno.count { it.presente }
    val porcentajeAsistencia = if (totalAsistenciaReg > 0) {
        (totalPresentes * 100) / totalAsistenciaReg
    } else null

    val cursoPrincipal = aplicaciones.lastOrNull()?.estudiante?.curso
        ?: ultimasAsistenciasPorAlumno.firstOrNull()?.estudiante?.curso
        ?: "Sin curso asignado"
    val instrumentoActivo = aplicaciones.lastOrNull()?.cuestionario?.titulo
        ?: "Sin instrumento activo"

    // Diálogo con las indicaciones pedagógicas del psicólogo (sin datos clínicos)
    aplicacionSeleccionada?.let { app ->
        val indicaciones = app.indicacionesParaProfesor?.takeIf { it.isNotBlank() }
        val instrucciones = app.cuestionario?.instruccionesProfesor?.takeIf { it.isNotBlank() }

        AlertDialog(
            onDismissRequest = { aplicacionSeleccionada = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(
                    text = app.estudiante?.nombre ?: app.codigoTemporal,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "${app.cuestionario?.titulo ?: "Evaluación"} • ${app.estudiante?.curso ?: "Sin curso"}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Estado técnico: ${app.estado}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    HorizontalDivider()

                    Text(
                        text = "Indicaciones del psicólogo para el aula:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = indicaciones
                                ?: "El psicólogo aún no ha registrado indicaciones pedagógicas para esta evaluación.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    if (instrucciones != null) {
                        Text(
                            text = "Pauta de aplicación en sala:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = instrucciones,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "Nota PIE: Las respuestas individuales y puntajes clínicos son confidenciales.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            },
            confirmButton = {
                Button(onClick = { aplicacionSeleccionada = null }) {
                    Text("Entendido")
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
                            text = "Panel Docente",
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
                    IconButton(onClick = { cargarResumenAula() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onIrAsistencia) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Asistencia",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onVolver) {
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
            AppBottomBar(
                itemSeleccionado = 0,
                rolUsuario = "PROFESOR",
                nombreUsuario = nombreUsuario,
                onNavegarInicio = { /* Ya estamos en inicio */ },
                onNavegarEvaluaciones = onIrEvaluaciones,
                onNavegarAsistencia = onIrAsistencia,
                onNavegarPerfil = onIrPerfil
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
            // 1. Cabecera
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SEGUIMIENTO TÉCNICO DE AULA",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Hola, $nombreUsuario 👋",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Avance general e indicaciones pedagógicas del psicólogo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 2. Tarjetas de Resumen Técnico desde BD
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
                                    text = "Finalizados",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$totalFinalizadas / $totalApps",
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
                                    text = "Asistencia",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = porcentajeAsistencia?.let { "$it%" } ?: "—",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (totalAsistenciaReg > 0) "$totalPresentes de $totalAsistenciaReg hoy" else "Sin registro hoy",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // 3. Tarjeta de Curso e Instrumento Activo
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Curso: $cursoPrincipal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Instrumento activo: $instrumentoActivo",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progresoApps },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Proceso: $totalFinalizadas / $totalApps evaluaciones finalizadas",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 4. Botón directo a Asistencia
            item {
                Button(
                    onClick = onIrAsistencia,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(imageVector = Icons.Default.HowToReg, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pasar Asistencia del Curso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 5. Lista de Estado Técnico e Indicaciones del Psicólogo
            item {
                Column {
                    Text(
                        text = "Estado e indicaciones del psicólogo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Toca un alumno para leer las recomendaciones para el aula",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (cargando && aplicaciones.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (aplicaciones.isEmpty()) {
                item {
                    Text(
                        text = mensajeError ?: "No hay evaluaciones registradas aún.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                items(aplicaciones.reversed(), key = { it.id }) { app ->
                    val nombreAlumno = app.estudiante?.nombre ?: "Estudiante (${app.codigoTemporal})"
                    val curso = app.estudiante?.curso ?: "Sin curso"
                    val tituloTest = app.cuestionario?.titulo ?: "Evaluación #${app.id}"
                    val tieneIndicaciones = !app.indicacionesParaProfesor.isNullOrBlank()
                    val esFinalizado = app.estado.equals("COMPLETADA", ignoreCase = true) ||
                            app.estado.equals("REVISADA", ignoreCase = true)

                    ElevatedCard(
                        onClick = { aplicacionSeleccionada = app },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = nombreAlumno,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "$curso • $tituloTest",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Surface(
                                    color = if (esFinalizado)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = app.estado,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = if (tieneIndicaciones) {
                                    Color(0xFFE8F5E9)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (tieneIndicaciones) Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (tieneIndicaciones) {
                                            "Indicaciones: ${app.indicacionesParaProfesor}"
                                        } else {
                                            "Sin indicaciones del psicólogo todavía"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (tieneIndicaciones) Color(0xFF1B5E20) else MaterialTheme.colorScheme.outline,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}