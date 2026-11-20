package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.LoginRequest
import com.example.psicoappmpaz.data.models.RegisterRequest
import com.example.psicoappmpaz.model.Aplicacion
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var tabSeleccionada by mutableIntStateOf(0)
    var usuarioText by mutableStateOf("")
    var passwordText by mutableStateOf("")
    var codigoEstudiante by mutableStateOf("")

    var mensajeError by mutableStateOf("")
    var cargando by mutableStateOf(false)

    // Guarda la evaluación cargada si el estudiante inicia sesión con código
    var aplicacionActivaEstudiante by mutableStateOf<Aplicacion?>(null)

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

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(
                        username = usuarioText.trim(),
                        password = passwordText.trim()
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val auth = response.body()!!
                    RetrofitClient.authToken = auth.token

                    val nombre = auth.nombreCompleto ?: usuarioText.trim()
                    val rol = auth.rol ?: "PSICOLOGO"

                    RetrofitClient.currentUserNombre = nombre
                    RetrofitClient.currentUserRol = rol

                    when (rol.uppercase()) {
                        "PSICOLOGO" -> onSuccessPsico(nombre)
                        "PROFESOR" -> onSuccessProfe(nombre)
                        else -> onSuccessPsico(nombre)
                    }
                } else {
                    mensajeError = "Usuario o contraseña incorrectos (Error ${response.code()})."
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión con el servidor: ${e.localizedMessage}"
            } finally {
                cargando = false
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

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAplicacionPorCodigo(
                    codigoTemporal = codigoEstudiante.trim()
                )

                if (response.isSuccessful && response.body() != null) {
                    aplicacionActivaEstudiante = response.body()
                    onSuccessEstudiante()
                } else {
                    mensajeError = "Código inválido o expirado. Prueba con EST-101"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    fun registrarUsuario(
        username: String,
        password: String,
        nombreCompleto: String,
        rol: String,
        onSuccess: () -> Unit
    ) {
        cargando = true
        mensajeError = ""

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    username = username.trim(),
                    password = password.trim(),
                    nombreCompleto = nombreCompleto.trim(),
                    rol = rol
                )
                val response = RetrofitClient.apiService.register(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    mensajeError = if (!errorBody.isNullOrBlank()) errorBody else "Error al registrar (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }
}