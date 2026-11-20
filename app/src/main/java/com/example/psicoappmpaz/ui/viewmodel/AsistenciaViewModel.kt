package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.AsistenciaRequest
import com.example.psicoappmpaz.data.models.IdRef
import com.example.psicoappmpaz.model.Estudiante // Ajusta esta importación a tu modelo real
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AsistenciaViewModel : ViewModel() {

    var estudiantes by mutableStateOf<List<Estudiante>>(emptyList())
    var asistenciaMap = mutableStateMapOf<Long, Boolean>()

    var cargando by mutableStateOf(false)
    var guardando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var mensajeExito by mutableStateOf<String?>(null)

    fun cargarEstudiantesPorCurso(curso: String) {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                // CORRECCIÓN: Usar getEstudiantesPorCurso y pasarle la variable curso
                val response = RetrofitClient.apiService.getEstudiantesPorCurso(token, curso)
                if (response.isSuccessful && response.body() != null) {
                    estudiantes = response.body()!!
                    // (Opcional) Inicializar a todos como presentes
                    // estudiantes.forEach { asistenciaMap[it.id] = true }
                } else {
                    mensajeError = "Error al cargar el curso (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    fun guardarAsistencia(idRegistrador: Long) {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer " || asistenciaMap.isEmpty()) return

        guardando = true
        mensajeError = null
        mensajeExito = null

        // CORRECCIÓN: Usar SimpleDateFormat para evitar el error de API 26
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaHoy = sdf.format(Date())

        viewModelScope.launch {
            try {
                // CORRECCIÓN: Armar la lista completa primero
                val listaAsistencias = asistenciaMap.map { (idEstudiante, presente) ->
                    AsistenciaRequest(
                        fecha = fechaHoy,
                        presente = presente,
                        estudiante = IdRef(idEstudiante),
                        registradoPor = IdRef(idRegistrador)
                    )
                }

                // Enviar la lista de una sola vez
                RetrofitClient.apiService.registrarAsistencias(token, listaAsistencias)

                mensajeExito = "Asistencia guardada correctamente"
            } catch (e: Exception) {
                mensajeError = "Error al guardar asistencia: ${e.localizedMessage}"
            } finally {
                guardando = false
            }
        }
    }
}