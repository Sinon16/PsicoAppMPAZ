package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.data.models.CrearAplicacionRequest
import com.example.psicoappmpaz.data.models.CrearEstudianteRequest
import com.example.psicoappmpaz.data.models.IdRef
import com.example.psicoappmpaz.model.Cuestionario
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAplicacionScreen(
    onVolver: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Campos del formulario
    var nombreEstudiante by remember { mutableStateOf("") }
    var cursoEstudiante by remember { mutableStateOf("") }
    var codigoTemporal by remember { mutableStateOf("EST-${(100..999).random()}") }

    // Cuestionarios cargados desde Spring Boot
    var cuestionarios by remember { mutableStateOf<List<Cuestionario>>(emptyList()) }
    var cuestionarioSeleccionado by remember { mutableStateOf<Cuestionario?>(null) }
    var menuEvaluacionDesplegado by remember { mutableStateOf(false) }

    // Días de vigencia (1 a 3 días)
    var diasVigencia by remember { mutableIntStateOf(1) }

    // Estados de red
    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var creacionExitosa by remember { mutableStateOf(false) }

    // Cargar catálogo real al abrir el modal
    LaunchedEffect(Unit) {
        val token = RetrofitClient.getBearerToken()
        if (token != "Bearer ") {
            try {
                val res = RetrofitClient.apiService.getCuestionarios(token)
                if (res.isSuccessful && !res.body().isNullOrEmpty()) {
                    cuestionarios = res.body()!!
                    cuestionarioSeleccionado = cuestionarios.first()
                }
            } catch (_: Exception) { }
        }
    }

    if (creacionExitosa) {
        AlertDialog(
            onDismissRequest = onVolver,
            title = { Text("Evaluación Asignada") },
            text = {
                Text(
                    "Se creó el acceso para '$nombreEstudiante' ($cursoEstudiante).\n\n" +
                            "Código de ingreso: $codigoTemporal\n" +
                            "Vigencia: $diasVigencia ${if (diasVigencia == 1) "día" else "días"}"
                )
            },
            confirmButton = {
                Button(onClick = onVolver) {
                    Text("Listo")
                }
            }
        )
    }

    // Tarjeta Modal Flotante Vertical (Estilo Mockup)
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabecera con ícono "+" y botón "X" de cierre como en el mockup
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar ventana",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column {
                Text(
                    text = "Una nueva oportunidad",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Asigna una evaluación para acompañar a un estudiante.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // 1. Nombre del Estudiante
            OutlinedTextField(
                value = nombreEstudiante,
                onValueChange = { nombreEstudiante = it },
                label = { Text("Nombre o Alias del Estudiante") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando
            )

            // 2. Curso
            OutlinedTextField(
                value = cursoEstudiante,
                onValueChange = { cursoEstudiante = it },
                label = { Text("Curso (Ej: 4° Básico A)") },
                leadingIcon = { Icon(Icons.Default.Class, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando
            )

            // 3. Código Temporal
            OutlinedTextField(
                value = codigoTemporal,
                onValueChange = { codigoTemporal = it },
                label = { Text("Código Temporal") },
                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando
            )

            // 4. Selector Desplegable de Evaluación (Desde BD)
            ExposedDropdownMenuBox(
                expanded = menuEvaluacionDesplegado,
                onExpandedChange = { menuEvaluacionDesplegado = !menuEvaluacionDesplegado }
            ) {
                OutlinedTextField(
                    value = cuestionarioSeleccionado?.titulo ?: "Cargando evaluaciones...",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Evaluación") },
                    leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEvaluacionDesplegado) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuEvaluacionDesplegado,
                    onDismissRequest = { menuEvaluacionDesplegado = false }
                ) {
                    cuestionarios.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.titulo) },
                            onClick = {
                                cuestionarioSeleccionado = item
                                menuEvaluacionDesplegado = false
                            }
                        )
                    }
                }
            }

            // 5. Selección de Días de Vigencia (1 a 3 Días)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Vigencia del Código",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 3).forEach { dia ->
                        FilterChip(
                            selected = diasVigencia == dia,
                            onClick = { diasVigencia = dia },
                            label = { Text("$dia ${if (dia == 1) "Día" else "Días"}") },
                            leadingIcon = if (diasVigencia == dia) {
                                { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            mensajeError?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 6. Botón Asignar Evaluación
            Button(
                onClick = {
                    val cuest = cuestionarioSeleccionado ?: return@Button
                    val token = RetrofitClient.getBearerToken()
                    scope.launch {
                        cargando = true
                        mensajeError = null
                        try {
                            // Paso 1: Crear el estudiante temporal en la BD
                            val resEst = RetrofitClient.apiService.crearEstudiante(
                                token,
                                CrearEstudianteRequest(
                                    nombreCompleto = nombreEstudiante.trim(),
                                    curso = cursoEstudiante.trim()
                                )
                            )

                            if (resEst.isSuccessful && resEst.body() != null) {
                                val estudianteCreado = resEst.body()!!
                                // Paso 2: Crear la aplicación de evaluación con el código
                                val resApp = RetrofitClient.apiService.crearAplicacion(
                                    token,
                                    CrearAplicacionRequest(
                                        codigoTemporal = codigoTemporal.trim(),
                                        estado = "PENDIENTE",
                                        estudiante = IdRef(estudianteCreado.id),
                                        cuestionario = IdRef(cuest.id)
                                    )
                                )
                                if (resApp.isSuccessful) {
                                    creacionExitosa = true
                                } else {
                                    mensajeError = "Error al asignar evaluación (${resApp.code()})"
                                }
                            } else {
                                mensajeError = "Error al crear estudiante (${resEst.code()})"
                            }
                        } catch (e: Exception) {
                            mensajeError = "Error de conexión: ${e.localizedMessage}"
                        } finally {
                            cargando = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = nombreEstudiante.isNotBlank() &&
                        cursoEstudiante.isNotBlank() &&
                        codigoTemporal.isNotBlank() &&
                        cuestionarioSeleccionado != null &&
                        !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Asignar evaluación →", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}