package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun DetalleResultadoScreen(
    idAplicacion: Long = 0L,
    onVolver: () -> Unit
) {
    // Tu compañero implementará aquí las 3 pestañas (Respuestas, Interpretación, Informe técnico)
    // usando EvaluacionesViewModel.cargarDetallePorId(idAplicacion)
    EnConstruccionScreen(
        nombrePantalla = "Detalle de Resultados (#$idAplicacion)",
        onVolver = onVolver
    )
}