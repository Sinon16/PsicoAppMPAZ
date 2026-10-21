package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun PsicoHomeScreen(
    onCrearAplicacion: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onIrAsistencia: () -> Unit
) {
    EnConstruccionScreen(nombrePantalla = "Panel Psicólogo PIE")
}