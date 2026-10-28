package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginExitosoPsico: (String) -> Unit,
    onLoginExitosoProfe: (String) -> Unit,
    onIngresoEstudiante: () -> Unit,
    onIrARegistro: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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

            TabRow(selectedTabIndex = viewModel.tabSeleccionada) {
                Tab(
                    selected = viewModel.tabSeleccionada == 0,
                    onClick = { viewModel.tabSeleccionada = 0; viewModel.mensajeError = "" },
                    text = { Text("Profesional (BD)") }
                )
                Tab(
                    selected = viewModel.tabSeleccionada == 1,
                    onClick = { viewModel.tabSeleccionada = 1; viewModel.mensajeError = "" },
                    text = { Text("Estudiante (Código)") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.tabSeleccionada == 0) {
                OutlinedTextField(
                    value = viewModel.usuarioText,
                    onValueChange = { viewModel.usuarioText = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !viewModel.cargando
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = viewModel.passwordText,
                    onValueChange = { viewModel.passwordText = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !viewModel.cargando
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.loginProfesional(
                            onSuccessPsico = { nombreUsuario -> onLoginExitosoPsico(nombreUsuario) },
                            onSuccessProfe = { nombreUsuario -> onLoginExitosoProfe(nombreUsuario) }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.cargando
                ) {
                    if (viewModel.cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Ingresar al Sistema PIE")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // enlace para registrar cuenta nueva
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿No tienes cuenta registrada?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(
                            onClick = onIrARegistro,
                    contentPadding = PaddingValues(start = 4.dp, end = 4.dp)
                    ){
                        Text(
                            text = "Regístrate aquí",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                OutlinedTextField(
                    value = viewModel.codigoEstudiante,
                    onValueChange = { viewModel.codigoEstudiante = it },
                    label = { Text("Código Temporal (Ej: EST-101)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !viewModel.cargando
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
                        viewModel.loginEstudiante(
                            onSuccessEstudiante = onIngresoEstudiante
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.cargando
                ) {
                    if (viewModel.cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Entrar como Estudiante")
                    }
                }
            }

            if (viewModel.mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}