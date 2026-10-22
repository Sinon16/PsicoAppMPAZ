package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(
    onLoginExitosoPsico: () -> Unit,
    onLoginExitosoProfe: () -> Unit,
    onIngresoEstudiante: () -> Unit
) {
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    var usuarioText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var codigoEstudiante by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.
        fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Psicología Escolar",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Escuela Escritora Marcela Paz",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(32.dp))


            // Seleccionar modo de ingreso

            TabRow(selectedTabIndex = tabSeleccionada) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = {tabSeleccionada = 0; mensajeError =""},
                    text = {Text("Profesional (BD)")}
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = {tabSeleccionada = 1; mensajeError =""},
                    text = {Text("Estudiante (Código)")}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (tabSeleccionada == 0) {
                // --- INGRESO PROFESIONAL CON CREDENCIALES DE BD ---
                OutlinedTextField(
                    value = usuarioText,
                    onValueChange = { usuarioText = it },
                    label = { Text("Usuario (psico1 / profe1)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { passwordText = it },
                    label = { Text("Contraseña (123)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // Simulación de login con BD local Room
                        if (usuarioText == "psico1" && passwordText == "123") {
                            onLoginExitosoPsico()
                        } else if (usuarioText == "profe1" && passwordText == "123") {
                            onLoginExitosoProfe()
                        } else {
                            mensajeError = "Credenciales inválidas. Usa psico1/123 o profe1/123"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar al Sistema PIE")
                }
            } else {
                // --- INGRESO ESTUDIANTE CON CÓDIGO TEMPORAL ---
                OutlinedTextField(
                    value = codigoEstudiante,
                    onValueChange = { codigoEstudiante = it },
                    label = { Text("Código Temporal (Ej: EST-101)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Acceso de un solo uso entregado por el psicólogo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (codigoEstudiante.isNotBlank()) {
                            onIngresoEstudiante()
                        } else {
                            mensajeError = "Ingresa tu código de 6 dígitos"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar como Estudiante")
                }
            }

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
            }
        }
    }


}
