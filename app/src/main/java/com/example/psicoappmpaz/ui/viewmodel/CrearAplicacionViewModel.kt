package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.CrearAplicacionRequest
import com.example.psicoappmpaz.data.models.CrearEstudianteRequest
import com.example.psicoappmpaz.data.models.IdRef
import com.example.psicoappmpaz.model.Cuestionario
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

class CrearAplicacionViewModel : ViewModel() {

    // Estados de red y UI
    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var creacionExitosa by mutableStateOf(false)

    // Formulario de creación
    var nombreEstudiante by mutableStateOf("")
    var cursoEstudiante by mutableStateOf("")
    var codigoTemporal by mutableStateOf("EST-${(100..999).random()}")
    var diasVigencia by mutableIntStateOf(1)

    // Catálogo de cuestionarios (para el Dropdown)
    var cuestionarios by mutableStateOf<List<Cuestionario>>(emptyList())
    var cuestionarioSeleccionado by mutableStateOf<Cuestionario?>(null)

    fun cargarCuestionarios() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getCuestionarios(token)
                if (res.isSuccessful && !res.body().isNullOrEmpty()) {
                    cuestionarios = res.body()!!
                    cuestionarioSeleccionado = cuestionarios.first()
                }
            } catch (e: Exception) {
                mensajeError = "Error al cargar evaluaciones de la base de datos"
            }
        }
    }

    fun crearEstudianteYAplicacion(idUsuarioResponsable: Long = 1L) {
        if (nombreEstudiante.isBlank() || cursoEstudiante.isBlank() || codigoTemporal.isBlank() || cuestionarioSeleccionado == null) {
            mensajeError = "Completa todos los campos"
            return
        }

        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                // 1. Crear al estudiante temporal
                val estudianteReq = CrearEstudianteRequest(
                    nombre = nombreEstudiante.trim(), // Asegúrate de que coincida con tu data class (nombre o nombreCompleto)
                    curso = cursoEstudiante.trim(),
                    esTemporal = true
                )
                val responseEst = RetrofitClient.apiService.crearEstudiante(token, estudianteReq)

                if (!responseEst.isSuccessful || responseEst.body() == null) {
                    mensajeError = "Error al crear el estudiante (${responseEst.code()})"
                    return@launch
                }

                val idEstudianteCreado = responseEst.body()!!.id

                // 2. Crear la aplicación (Evaluación)
                val appReq = CrearAplicacionRequest(
                    codigoTemporal = codigoTemporal.trim(),
                    estado = "PENDIENTE",
                    estudiante = IdRef(idEstudianteCreado),
                    cuestionario = IdRef(cuestionarioSeleccionado!!.id),
                    usuarioResponsable = IdRef(idUsuarioResponsable)
                )

                val responseApp = RetrofitClient.apiService.crearAplicacion(token, appReq)

                if (responseApp.isSuccessful) {
                    creacionExitosa = true
                } else {
                    mensajeError = "Error al asignar la evaluación (${responseApp.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de red: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }
}