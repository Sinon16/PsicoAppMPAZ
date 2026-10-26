package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAplicacionScreen(
    onVolver: () -> Unit
) {
    // Estado para saber si ya se presionado "Crear Estudiante"
    var fueCreado by remember { mutableStateOf(false) }

    // Campos del formulario
    var nombreEstudiante by remember { mutableStateOf("") }
    var cursoEstudiante by remember { mutableStateOf("") }
    var codigoTemporal by remember { mutableStateOf("EST-") }

    // Lista y selección de Evaluaciones
    val listaEvaluaciones = listOf(
        "Cuestionario Educativo A",
        "Escala de Bienestar Escolar",
        "Cuestionario de Convivencia"
    )
    var evaluacionSeleccionada by remember { mutableStateOf(listaEvaluaciones[0]) }
    var menuEvaluacionDesplegado by remember { mutableStateOf(false) }

    // Días de vigencia (1 a 3 días)
    var diasVigencia by remember { mutableIntStateOf(1) }

    // Si ya presionó "Crear Estudiante", se muestra la pantalla en construcción
    if (fueCreado) {
        EnConstruccionScreen(
            nombrePantalla = "Procesando Aplicación Creada",
            onVolver = onVolver
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear Aplicación de Test") },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Asignación de Evaluación",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // 1. Nombre del Estudiante
                OutlinedTextField(
                    value = nombreEstudiante,
                    onValueChange = { nombreEstudiante = it },
                    label = { Text("Nombre del Estudiante") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 2. Curso
                OutlinedTextField(
                    value = cursoEstudiante,
                    onValueChange = { cursoEstudiante = it },
                    label = { Text("Curso (Ej: 5° Básico A)") },
                    leadingIcon = { Icon(Icons.Default.Class, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 3. Código Temporal
                OutlinedTextField(
                    value = codigoTemporal,
                    onValueChange = { codigoTemporal = it },
                    label = { Text("Código Temporal") },
                    leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 4. Selector Desplegable de Evaluación
                ExposedDropdownMenuBox(
                    expanded = menuEvaluacionDesplegado,
                    onExpandedChange = { menuEvaluacionDesplegado = !menuEvaluacionDesplegado }
                ) {
                    OutlinedTextField(
                        value = evaluacionSeleccionada,
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
                        listaEvaluaciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    evaluacionSeleccionada = opcion
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
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                                    { Icon(Icons.Default.DateRange, contentDescription = null) }
                                } else null,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 6. Botón Crear Estudiante
                Button(
                    onClick = { fueCreado = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = nombreEstudiante.isNotBlank() && cursoEstudiante.isNotBlank() && codigoTemporal.isNotBlank()
                ) {
                    Text("Crear Estudiante", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}