package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.AsistenciaRequest
import com.example.psicoappmpaz.data.models.IdRef
import com.example.psicoappmpaz.model.Asistencia
import com.example.psicoappmpaz.model.Estudiante
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AsistenciaViewModel : ViewModel() {

    var cursosDisponibles by mutableStateOf<List<String>>(emptyList())
    var cursoSeleccionado by mutableStateOf("")

    var estudiantes by mutableStateOf<List<Estudiante>>(emptyList())
    var registrosDelDia by mutableStateOf<List<Asistencia>>(emptyList())
    var asistenciaMap = mutableStateMapOf<Long, Boolean>()

    var cargando by mutableStateOf(false)
    var guardando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var mensajeExito by mutableStateOf<String?>(null)

    private var todosLosEstudiantes: List<Estudiante> = emptyList()

    val fechaHoy: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun cargarDatosIniciales() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null
        mensajeExito = null

        viewModelScope.launch {
            try {
                val respEst = RetrofitClient.apiService.getEstudiantes(token)
                if (respEst.isSuccessful && respEst.body() != null) {
                    val lista = respEst.body()!!
                    // Priorizamos estudiantes regulares; si no hay, mostramos todos
                    val regulares = lista.filter { !it.esTemporal }
                    todosLosEstudiantes = if (regulares.isNotEmpty()) regulares else lista

                    val cursos = todosLosEstudiantes
                        .map { it.curso }
                        .filter { it.isNotBlank() }
                        .distinct()
                        .sorted()

                    cursosDisponibles = cursos
                    if (cursoSeleccionado.isBlank() || cursoSeleccionado !in cursos) {
                        cursoSeleccionado = cursos.firstOrNull() ?: ""
                    }

                    if (cursoSeleccionado.isNotBlank()) {
                        cargarAsistenciaDelCurso(cursoSeleccionado)
                    } else {
                        estudiantes = emptyList()
                        registrosDelDia = emptyList()
                        cargando = false
                    }
                } else {
                    mensajeError = "Error al cargar estudiantes (${respEst.code()})"
                    cargando = false
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
                cargando = false
            }
        }
    }

    fun seleccionarCurso(curso: String) {
        cursoSeleccionado = curso
        mensajeExito = null
        mensajeError = null
        cargarAsistenciaDelCurso(curso)
    }

    fun cargarAsistenciaDelCurso(curso: String) {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                val estCurso = todosLosEstudiantes.filter { it.curso == curso }
                estudiantes = estCurso

                asistenciaMap.clear()
                // Por defecto todos presentes hasta contrastar con la BD
                estCurso.forEach { asistenciaMap[it.id] = true }

                val respAsis = RetrofitClient.apiService.getAsistencias(
                    token = token,
                    fecha = fechaHoy,
                    curso = curso
                )
                if (respAsis.isSuccessful && respAsis.body() != null) {
                    val registros = respAsis.body()!!
                    registrosDelDia = registros
                    // Sobreescribimos con el último estado guardado hoy en BD
                    registros.forEach { reg ->
                        reg.estudiante?.id?.let { idEst ->
                            asistenciaMap[idEst] = reg.presente
                        }
                    }
                } else {
                    registrosDelDia = emptyList()
                }
            } catch (e: Exception) {
                mensajeError = "Error al consultar asistencia: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    fun guardarAsistencia() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer " || estudiantes.isEmpty()) return

        guardando = true
        mensajeError = null
        mensajeExito = null

        viewModelScope.launch {
            try {
                val listaAsistencias = estudiantes.map { est ->
                    AsistenciaRequest(
                        fecha = fechaHoy,
                        presente = asistenciaMap[est.id] ?: false,
                        estudiante = IdRef(est.id),
                        registradoPor = null
                    )
                }

                val response = RetrofitClient.apiService.registrarAsistencias(token, listaAsistencias)
                if (response.isSuccessful && response.body() != null) {
                    registrosDelDia = response.body()!!
                    mensajeExito = "Asistencia de $cursoSeleccionado guardada ($fechaHoy)."
                } else {
                    mensajeError = "Error al guardar asistencia (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al guardar: ${e.localizedMessage}"
            } finally {
                guardando = false
            }
        }
    }
}