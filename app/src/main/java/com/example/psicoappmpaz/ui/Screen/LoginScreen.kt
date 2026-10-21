package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onLoginExitosoPsico: () -> Unit,
    onLoginExitosoProfe: () -> Unit,
    onIngresoEstudiante: () -> Unit
) {
    EnConstruccionScreen(nombrePantalla = "Iniciar Sesión")
}