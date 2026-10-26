package com.example.psicoappmpaz.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppBottomBar(
    itemSeleccionado: Int,
    rolUsuario: String,
    nombreUsuario: String,
    onNavegarInicio: () -> Unit,
    onNavegarEvaluaciones: () -> Unit,
    onNavegarAsistencia: () -> Unit,
    onNavegarPerfil: () -> Unit
){
    NavigationBar(
        modifier = Modifier.height(60.dp),
        windowInsets = WindowInsets(0.dp)
    ) {
        // 1. Botón Inicio (Inteligente según el Rol)
        NavigationBarItem(
            selected = itemSeleccionado == 0,
            onClick = {
                if (itemSeleccionado != 0) {
                    onNavegarInicio()
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )

        // 2. Botón Evaluaciones
        NavigationBarItem(
            selected = itemSeleccionado == 1,
            onClick = {
                if (itemSeleccionado != 1) {
                    onNavegarEvaluaciones()
                }
            },
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Evaluaciones") },
            label = { Text("Evaluaciones") }
        )

        // 3. Botón Asistencia
        NavigationBarItem(
            selected = itemSeleccionado == 2,
            onClick = {
                if (itemSeleccionado != 2) {
                    onNavegarAsistencia()
                }
            },
            icon = { Icon(Icons.Default.Group, contentDescription = "Asistencia") },
            label = { Text("Asistencia") }
        )

        // 4. Botón Perfil
        NavigationBarItem(
            selected = itemSeleccionado == 3,
            onClick = {
                if (itemSeleccionado != 3) {
                    onNavegarPerfil()
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }

}