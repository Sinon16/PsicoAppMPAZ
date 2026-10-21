package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun ProfesorHomeScreen(onVolver: () -> Unit) {
    EnConstruccionScreen(nombrePantalla = "Panel Docente", onVolver = onVolver)
}