package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun EvaluacionesScreen(
    onVolver: () -> Unit
) {
    EnConstruccionScreen(
        nombrePantalla = "Módulo de Evaluaciones",
        onVolver = onVolver
    )
}