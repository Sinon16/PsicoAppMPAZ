package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.ui.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    rolUsuario: String = "PSICOLOGO",
    nombreUsuario: String = "",
    onIrInicio: () -> Unit = {},
    onIrEvaluaciones: () -> Unit = {},
    onIrAsistencia: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    var activarNotificaciones by remember { mutableStateOf(true) }
    val esProfesor = rolUsuario.uppercase() == "PROFESOR"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Perfil de Usuario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Escuela Escritora Marcela Paz",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Foto predeterminada, Nombre y Rol
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(68.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = nombreUsuario.ifBlank { "Usuario PIE" },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (esProfesor) "Cuenta Docente (@profe1)" else "Especialista PIE (@psico1)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Rol activo: $rolUsuario",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Información de Permisos y Limitaciones según el Caso
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Alcance Técnico del Perfil",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (esProfesor) {
                            "Perfil Docente: Habilitado para control de asistencia y monitoreo técnico de sesiones en aula. Por normativa de confidencialidad PIE, este perfil no visualiza respuestas individuales ni asigna cuestionarios."
                        } else {
                            "Perfil Psicólogo: Habilitado para crear códigos temporales, asignar cuestionarios, revisar resultados clínicos y gestionar asistencia."
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 3. Ticket (Checkbox) de Activar Notificaciones
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activarNotificaciones = !activarNotificaciones }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = activarNotificaciones,
                        onCheckedChange = { activarNotificaciones = it }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Activar notificaciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Recibir avisos de evaluaciones y asistencia en curso",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Botón para volver al Inicio
            Button(
                onClick = onIrInicio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Volver al Inicio", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilPreview() {
    PerfilScreen(rolUsuario = "PROFESOR", nombreUsuario = "Docente MPAZ")
}