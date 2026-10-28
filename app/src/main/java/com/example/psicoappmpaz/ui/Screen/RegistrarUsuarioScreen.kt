package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.psicoappmpaz.data.entity.UsuarioEntity
import com.example.psicoappmpaz.data.local.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarUsuarioScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Campos del formulario ajustados a UsuarioEntity
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombreCompleto by remember { mutableStateOf("") }

    // Opciones de Rol estrictamente limitadas a PROFESOR y PSICOLOGO
    val rolesDisponibles = listOf("PROFESOR", "PSICOLOGO")
    var rolSeleccionado by remember { mutableStateOf(rolesDisponibles[0]) }
    var menuRolDesplegado by remember { mutableStateOf(false) }

    // Estados de interfaz
    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var registroExitoso by remember { mutableStateOf(false) }

    if (registroExitoso) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Usuario Registrado") },
            text = { Text("El usuario '$nombreCompleto' con el rol '$rolSeleccionado' ha sido guardado exitosamente en la base de datos.") },
            confirmButton = {
                Button(onClick = onVolver) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Usuario") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Datos de la Cuenta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario (Username)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 2. Nombre Completo
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 3. Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (Password)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 4. Selector de Rol (Exclusivo PROFESOR o PSICOLOGO)
            ExposedDropdownMenuBox(
                expanded = menuRolDesplegado,
                onExpandedChange = { menuRolDesplegado = !menuRolDesplegado }
            ) {
                OutlinedTextField(
                    value = rolSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol Asignado") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuRolDesplegado) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuRolDesplegado,
                    onDismissRequest = { menuRolDesplegado = false }
                ) {
                    rolesDisponibles.forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol) },
                            onClick = {
                                rolSeleccionado = rol
                                menuRolDesplegado = false
                            }
                        )
                    }
                }
            }

            mensajeError?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Botón Guardar en SQLite vía Room
            Button(
                onClick = {
                    scope.launch {
                        cargando = true
                        mensajeError = null
                        try {
                            withContext(Dispatchers.IO) {
                                val db = AppDataBase.obtenerBaseDatos(context)

                                // Instanciamos la entidad exacta
                                val nuevoUsuario = UsuarioEntity(
                                    username = username.trim(),
                                    password = password.trim(),
                                    nombreCompleto = nombreCompleto.trim(),
                                    rol = rolSeleccionado
                                )

                                db.usuarioDao().insertarUsuario(nuevoUsuario)
                            }
                            registroExitoso = true
                        } catch (e: Exception) {
                            mensajeError = "Error al guardar en la BD: ${e.localizedMessage}"
                        } finally {
                            cargando = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = username.isNotBlank() && nombreCompleto.isNotBlank() && password.isNotBlank() && !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Guardar Usuario", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}