package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.psicoappmpaz.ui.components.AppBottomBar

@Composable
fun EvaluacionesScreen(
    rolUsuario: String = "PSICOLOGO",
    nombreUsuario: String = "",
    onIrInicio: () -> Unit = {},
    onIrAsistencia: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                itemSeleccionado = 1,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onNavegarInicio = onIrInicio,
                onNavegarEvaluaciones = { /* Ya está aquí */ },
                onNavegarAsistencia = onIrAsistencia,
                onNavegarPerfil = onIrPerfil
            )
        }
    ) { innerPadding ->
        EnConstruccionScreen(
            nombrePantalla = "Módulo de Evaluaciones",
            onVolver = onVolver,
            modifier = Modifier.padding(innerPadding)
        )
    }
}