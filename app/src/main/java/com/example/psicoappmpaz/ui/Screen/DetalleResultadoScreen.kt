package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psicoappmpaz.model.Aplicacion
import com.example.psicoappmpaz.model.RespuestaDetalle
import com.example.psicoappmpaz.network.RetrofitClient
import com.example.psicoappmpaz.ui.viewmodel.EvaluacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleResultadoScreen(
    idAplicacion: Long = 0L,
    onVolver: () -> Unit,
    viewModel: EvaluacionesViewModel = viewModel()
) {
    val app = viewModel.aplicacionSeleccionada
    val esPsicologo = RetrofitClient.currentUserRol.uppercase() != "PROFESOR"
    val pestanas = listOf("Respuestas", "Interpretación", "Informe técnico")

    LaunchedEffect(idAplicacion) {
        viewModel.tabDetalleIndex = 0
        viewModel.cargarDetallePorId(idAplicacion)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Resultados", fontWeight = FontWeight.Bold) },
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
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
                viewModel.cargando && app == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                app == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.mensajeError ?: "No se encontró la evaluación #$idAplicacion.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarDetallePorId(idAplicacion) }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Resumen superior del estudiante y la evaluación
                        ResumenAplicacionCard(app = app)

                        // Pestañas: Respuestas | Interpretación | Informe técnico
                        TabRow(selectedTabIndex = viewModel.tabDetalleIndex) {
                            pestanas.forEachIndexed { index, titulo ->
                                Tab(
                                    selected = viewModel.tabDetalleIndex == index,
                                    onClick = { viewModel.tabDetalleIndex = index },
                                    text = {
                                        Text(
                                            text = titulo,
                                            fontWeight = if (viewModel.tabDetalleIndex == index) {
                                                FontWeight.Bold
                                            } else {
                                                FontWeight.Normal
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        // Mensajes de estado al guardar
                        viewModel.mensajeExito?.let { msg ->
                            Surface(
                                color = Color(0xFFE8F5E9),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = msg,
                                        color = Color(0xFF2E7D32),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        viewModel.mensajeError?.let { msg ->
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Contenido dinámico por pestaña
                        when (viewModel.tabDetalleIndex) {
                            0 -> TabRespuestas(respuestas = app.respuestas)
                            1 -> TabNotasProfesionales(
                                titulo = "Interpretación Clínica",
                                descripcion = "Registro reservado para el análisis profesional del psicólogo.",
                                interpretacionAutomatica = app.interpretacionPuntaje,
                                valorTexto = viewModel.textoInterpretacionClinica,
                                onValueChange = { viewModel.textoInterpretacionClinica = it },
                                editable = esPsicologo,
                                guardando = viewModel.guardandoNotas,
                                onGuardar = { viewModel.guardarNotasProfesionales() }
                            )
                            2 -> TabNotasProfesionales(
                                titulo = "Informe Técnico para el Docente",
                                descripcion = "Indicaciones, sugerencias de apoyo y adaptaciones para el aula.",
                                interpretacionAutomatica = null,
                                valorTexto = viewModel.textoInformeTecnicoProfesor,
                                onValueChange = { viewModel.textoInformeTecnicoProfesor = it },
                                editable = esPsicologo,
                                guardando = viewModel.guardandoNotas,
                                onGuardar = { viewModel.guardarNotasProfesionales() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenAplicacionCard(app: Aplicacion) {
    val colorEstado = when (app.estado.uppercase()) {
        "REVISADA" -> Color(0xFF2E7D32)
        "COMPLETADA" -> Color(0xFF1565C0)
        else -> Color(0xFFEF6C00)
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = app.estudiante?.nombre ?: "Estudiante sin asignar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        app.estudiante?.curso?.let { curso ->
                            Text(
                                text = "Curso: $curso",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = app.estado.uppercase(),
                        color = colorEstado,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = app.cuestionario?.titulo ?: "Evaluación #${app.id}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "Puntaje: ${app.puntajeTotal?.toString() ?: "—"}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Código: ${app.codigoTemporal}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun TabRespuestas(respuestas: List<RespuestaDetalle>) {
    if (respuestas.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "El estudiante aún no ha enviado respuestas para esta evaluación.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
        return
    }

    val ordenadas = respuestas.sortedBy { it.pregunta?.orden ?: Int.MAX_VALUE }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(ordenadas) { index, resp ->
            val numero = resp.pregunta?.orden ?: (index + 1)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "$numero. ${resp.pregunta?.enunciado ?: "Pregunta #$numero"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Respuesta: ${resp.opcionSeleccionada}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        resp.valorPuntaje?.let { pts ->
                            Text(
                                text = "$pts pts",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabNotasProfesionales(
    titulo: String,
    descripcion: String,
    interpretacionAutomatica: String?,
    valorTexto: String,
    onValueChange: (String) -> Unit,
    editable: Boolean,
    guardando: Boolean,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        if (!interpretacionAutomatica.isNullOrBlank()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Interpretación automática del puntaje",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = interpretacionAutomatica,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        OutlinedTextField(
            value = valorTexto,
            onValueChange = onValueChange,
            readOnly = !editable,
            label = { Text(if (editable) "Escribe aquí tus notas..." else "Notas registradas") },
            placeholder = {
                Text(
                    if (editable) "Ingresa observaciones clínicas o indicaciones..."
                    else "Sin registro todavía."
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp),
            maxLines = 12
        )

        if (editable) {
            Button(
                onClick = onGuardar,
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar notas profesionales")
                }
            }
        }
    }
}