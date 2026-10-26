package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.psicoappmpaz.ui.components.AppBottomBar

@Composable
fun PerfilScreen(
    rolUsuario: String = "PSICOLOGO",
    nombreUsuario: String = "",
    onIrInicio: () -> Unit = {},
    onIrEvaluaciones: () -> Unit = {},
    onIrAsistencia: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                itemSeleccionado = 3,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onNavegarInicio = onIrInicio,
                onNavegarEvaluaciones = onIrEvaluaciones,
                onNavegarAsistencia = onIrAsistencia,
                onNavegarPerfil = { /* Ya está aquí */ }
            )
        }
    ) { innerPadding ->
        EnConstruccionScreen(
            nombrePantalla = "Módulo de Perfil",
            onVolver = onVolver,
            modifier = Modifier.padding(innerPadding)
        )
    }
}