package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun EstudianteTestScreen(onFinalizar: () -> Unit) {
    EnConstruccionScreen(nombrePantalla = "Responder Testeo Supervisado", onVolver = onFinalizar)
}