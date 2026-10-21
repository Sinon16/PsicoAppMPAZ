package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun CrearAplicacionScreen(onVolver: () -> Unit) {
    EnConstruccionScreen(nombrePantalla = "Crear Aplicación de Test", onVolver = onVolver)
}