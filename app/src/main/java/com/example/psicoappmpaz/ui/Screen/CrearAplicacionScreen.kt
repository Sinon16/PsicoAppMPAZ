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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.psicoappmpaz.ui.viewmodel.CrearAplicacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAplicacionScreen(
    viewModel: CrearAplicacionViewModel = viewModel(),
    onVolver: () -> Unit
) {
    var menuEvaluacionDesplegado by remember { mutableStateOf(false) }

    // Cargar catálogo al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarCuestionarios()
    }

    if (viewModel.creacionExitosa) {
        AlertDialog(
            onDismissRequest = onVolver,
            title = { Text("Evaluación Asignada") },
            text = {
                Text(
                    "Se creó el acceso para '${viewModel.nombreEstudiante}' (${viewModel.cursoEstudiante}).\n\n" +
                            "Código de ingreso: ${viewModel.codigoTemporal}\n" +
                            "Vigencia: ${viewModel.diasVigencia} ${if (viewModel.diasVigencia == 1) "día" else "días"}"
                )
            },
            confirmButton = {
                Button(onClick = onVolver) {
                    Text("Listo")
                }
            }
        )
    }

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

            OutlinedTextField(
                value = viewModel.nombreEstudiante,
                onValueChange = { viewModel.nombreEstudiante = it },
                label = { Text("Nombre o Alias del Estudiante") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !viewModel.cargando
            )

            OutlinedTextField(
                value = viewModel.cursoEstudiante,
                onValueChange = { viewModel.cursoEstudiante = it },
                label = { Text("Curso (Ej: 4° Básico A)") },
                leadingIcon = { Icon(Icons.Default.Class, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !viewModel.cargando
            )

            OutlinedTextField(
                value = viewModel.codigoTemporal,
                onValueChange = { viewModel.codigoTemporal = it },
                label = { Text("Código Temporal") },
                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !viewModel.cargando
            )

            ExposedDropdownMenuBox(
                expanded = menuEvaluacionDesplegado,
                onExpandedChange = { menuEvaluacionDesplegado = !menuEvaluacionDesplegado }
            ) {
                OutlinedTextField(
                    value = viewModel.cuestionarioSeleccionado?.titulo ?: "Cargando evaluaciones...",
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
                    viewModel.cuestionarios.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.titulo) },
                            onClick = {
                                viewModel.cuestionarioSeleccionado = item
                                menuEvaluacionDesplegado = false
                            }
                        )
                    }
                }
            }

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
                            selected = viewModel.diasVigencia == dia,
                            onClick = { viewModel.diasVigencia = dia },
                            label = { Text("$dia ${if (dia == 1) "Día" else "Días"}") },
                            leadingIcon = if (viewModel.diasVigencia == dia) {
                                { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            viewModel.mensajeError?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    // Puedes pasar aquí el ID real del psicólogo si lo tienes guardado en preferencias
                    viewModel.crearEstudianteYAplicacion(idUsuarioResponsable = 1L)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.nombreEstudiante.isNotBlank() &&
                        viewModel.cursoEstudiante.isNotBlank() &&
                        viewModel.codigoTemporal.isNotBlank() &&
                        viewModel.cuestionarioSeleccionado != null &&
                        !viewModel.cargando
            ) {
                if (viewModel.cargando) {
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