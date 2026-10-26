package com.example.psicoappmpaz.ui.screen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.local.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDataBase.obtenerBaseDatos(application)

    var tabSeleccionada by mutableIntStateOf(0)
    var usuarioText by mutableStateOf("")
    var passwordText by mutableStateOf("")
    var codigoEstudiante by mutableStateOf("")

    var mensajeError by mutableStateOf("")
    var cargando by mutableStateOf(false)

    // Pasa el nombreCompleto en los callbacks para que la navegación se lo entregue a PsicoHomeScreen
    fun loginProfesional(
        onSuccessPsico: (String) -> Unit,
        onSuccessProfe: (String) -> Unit
    ) {
        if (usuarioText.isBlank() || passwordText.isBlank()) {
            mensajeError = "Por favor, ingresa usuario y contraseña."
            return
        }

        cargando = true
        mensajeError = ""

        viewModelScope.launch(Dispatchers.IO) {
            val usuarioEncontrado = db.usuarioDao().login(
                username = usuarioText.trim(),
                password = passwordText.trim()
            )

            withContext(Dispatchers.Main) {
                cargando = false
                if (usuarioEncontrado != null) {
                    val nombre = usuarioEncontrado.nombreCompleto
                    when (usuarioEncontrado.rol) {
                        "PSICOLOGO" -> onSuccessPsico(nombre)
                        "PROFESOR" -> onSuccessProfe(nombre)
                        else -> onSuccessPsico(nombre)
                    }
                } else {
                    mensajeError = "Usuario o contraseña incorrectos."
                }
            }
        }
    }

    fun loginEstudiante(onSuccessEstudiante: () -> Unit) {
        if (codigoEstudiante.isBlank()) {
            mensajeError = "Ingresa tu código de acceso."
            return
        }

        cargando = true
        mensajeError = ""

        viewModelScope.launch(Dispatchers.IO) {
            val aplicacion = db.aplicacionDao().buscarPorCodigo(codigoEstudiante.trim())

            withContext(Dispatchers.Main) {
                cargando = false
                // Validación estricta contra la base de datos
                if (aplicacion != null) {
                    onSuccessEstudiante()
                } else {
                    mensajeError = "Código inválido o expirado. Prueba con EST-101"
                }
            }
        }
    }
}