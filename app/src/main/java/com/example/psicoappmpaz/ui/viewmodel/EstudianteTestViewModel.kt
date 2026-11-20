package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.RespuestaDetalleRequest
import com.example.psicoappmpaz.data.models.IdRef
import com.example.psicoappmpaz.model.Aplicacion // Ajusta esta importación a tu modelo real
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

class EstudianteTestViewModel : ViewModel() {

    // Guardamos la aplicación completa para tener acceso a todo (estudiante, preguntas, etc.)
    var aplicacionActual by mutableStateOf<Aplicacion?>(null)

    var respuestasSeleccionadas = mutableStateMapOf<Long, String>()

    var cargando by mutableStateOf(false)
    var enviando by mutableStateOf(false)
    var envioExitoso by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    // CORRECCIÓN: El estudiante accede con el código temporal, cargamos la aplicación completa
    fun cargarAplicacionPorCodigo(codigoTemporal: String) {
        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                // CORRECCIÓN: Llamamos al endpoint libre getAplicacionPorCodigo
                val response = RetrofitClient.apiService.getAplicacionPorCodigo(codigoTemporal)
                if (response.isSuccessful && response.body() != null) {
                    aplicacionActual = response.body()
                } else {
                    mensajeError = "Código no encontrado o error (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de red: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    fun enviarRespuestas(codigoTemporal: String) {
        enviando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                var puntajeTotal = 0

                val detalles = respuestasSeleccionadas.map { (idPregunta, opcion) ->
                    val puntajeOpcion = calcularPuntaje(opcion)
                    puntajeTotal += puntajeOpcion

                    RespuestaDetalleRequest(
                        opcionSeleccionada = opcion,
                        valorPuntaje = puntajeOpcion,
                        pregunta = IdRef(idPregunta)
                    )
                }

                // CORRECCIÓN: Usar responderYFinalizar que es el nombre en tu ApiService
                val response = RetrofitClient.apiService.responderYFinalizar(
                    codigoTemporal = codigoTemporal,
                    puntajeCalculado = puntajeTotal, // Tu API acepta este campo opcional
                    respuestas = detalles
                )

                if (response.isSuccessful) {
                    envioExitoso = true
                } else {
                    mensajeError = "No se pudieron guardar las respuestas (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                enviando = false
            }
        }
    }

    private fun calcularPuntaje(opcion: String): Int {
        return when (opcion) {
            "Muy bien 😄" -> 4
            "Bien 🙂" -> 3
            "A veces difícil 😐" -> 2
            "Difícil 😟" -> 1
            else -> 0
        }
    }
}