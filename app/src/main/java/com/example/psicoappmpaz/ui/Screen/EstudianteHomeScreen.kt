package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun EstudianteHomeScreen(onIniciarTest: () -> Unit) {
    EnConstruccionScreen(nombrePantalla = "Inicio Estudiante", onVolver = onIniciarTest)
}