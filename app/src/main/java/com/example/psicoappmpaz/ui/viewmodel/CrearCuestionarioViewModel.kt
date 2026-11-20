package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.CrearCuestionarioRequest
import com.example.psicoappmpaz.data.models.CrearPreguntaRequest
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

// Representa el estado de una pregunta en la interfaz
data class PreguntaFormulario(
    var enunciado: String = "",
    var opcionSeleccionada: String = "Medio"
)

class CrearCuestionarioViewModel : ViewModel() {

    var tituloEvaluacion by mutableStateOf("")
    var descripcionEvaluacion by mutableStateOf("")

    // Lista observable para la UI
    var listaPreguntas = mutableStateListOf(PreguntaFormulario())

    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var fueGuardado by mutableStateOf(false)

    fun agregarPregunta() {
        listaPreguntas.add(PreguntaFormulario())
    }

    fun eliminarPregunta(index: Int) {
        if (listaPreguntas.size > 1) {
            listaPreguntas.removeAt(index)
        }
    }

    fun actualizarEnunciadoPregunta(index: Int, nuevoEnunciado: String) {
        listaPreguntas[index] = listaPreguntas[index].copy(enunciado = nuevoEnunciado)
    }

    fun guardarCuestionario() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                // Convertimos las preguntas de la UI al modelo del Backend que corregimos antes
                val preguntasDto = listaPreguntas.mapIndexed { idx, p ->
                    CrearPreguntaRequest(
                        enunciado = p.enunciado.trim(), // Usamos 'enunciado' como en el Models.kt corregido
                        orden = idx + 1
                    )
                }

                val request = CrearCuestionarioRequest(
                    titulo = tituloEvaluacion.trim(),
                    descripcion = descripcionEvaluacion.trim(),
                    preguntas = preguntasDto
                )

                val response = RetrofitClient.apiService.crearCuestionario(token, request)

                if (response.isSuccessful) {
                    fueGuardado = true
                } else {
                    mensajeError = "Error al guardar cuestionario (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }
}