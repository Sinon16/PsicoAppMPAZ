package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.data.models.FeedbackRequest
import com.example.psicoappmpaz.model.Aplicacion
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

class EvaluacionesViewModel : ViewModel() {

    var aplicaciones by mutableStateOf<List<Aplicacion>>(emptyList())
    var aplicacionSeleccionada by mutableStateOf<Aplicacion?>(null)

    // Filtro de pestañas en el listado: 0 = Todas, 1 = En curso, 2 = Completadas
    var filtroEstadoIndex by mutableIntStateOf(0)

    // Pestaña activa dentro de DetalleResultadoScreen: 0 = Respuestas, 1 = Interpretación, 2 = Informe técnico
    var tabDetalleIndex by mutableIntStateOf(0)

    // Campos editables de las pestañas de revisión
    var textoInterpretacionClinica by mutableStateOf("")
    var textoInformeTecnicoProfesor by mutableStateOf("")

    var cargando by mutableStateOf(false)
    var guardandoNotas by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var mensajeExito by mutableStateOf<String?>(null)

    // Lista filtrada automáticamente según la pestaña elegida en el Home/Listado
    val aplicacionesFiltradas: List<Aplicacion>
        get() = when (filtroEstadoIndex) {
            1 -> aplicaciones.filter {
                it.estado.equals("PENDIENTE", ignoreCase = true) ||
                        it.estado.equals("EN_CURSO", ignoreCase = true)
            }
            2 -> aplicaciones.filter {
                it.estado.equals("COMPLETADA", ignoreCase = true) ||
                        it.estado.equals("REVISADA", ignoreCase = true)
            }
            else -> aplicaciones
        }

    fun cargarAplicaciones() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        cargando = true
        mensajeError = null

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAplicaciones(token)
                if (response.isSuccessful && response.body() != null) {
                    aplicaciones = response.body()!!
                } else {
                    mensajeError = "Error al cargar evaluaciones (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    // Busca la aplicación por ID para mostrarla en DetalleResultadoScreen
    fun cargarDetallePorId(idApp: Long) {
        val encontrada = aplicaciones.find { it.id == idApp }
        if (encontrada != null) {
            seleccionarAplicacion(encontrada)
        } else {
            // Si se abrió directo desde navegación y aún no está en memoria, la consulta al backend
            val token = RetrofitClient.getBearerToken()
            if (token == "Bearer ") return

            cargando = true
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.apiService.getAplicaciones(token)
                    if (response.isSuccessful && response.body() != null) {
                        aplicaciones = response.body()!!
                        aplicaciones.find { it.id == idApp }?.let { seleccionarAplicacion(it) }
                    }
                } catch (e: Exception) {
                    mensajeError = "Error al cargar detalle: ${e.localizedMessage}"
                } finally {
                    cargando = false
                }
            }
        }
    }

    private fun seleccionarAplicacion(app: Aplicacion) {
        aplicacionSeleccionada = app
        textoInterpretacionClinica = app.observacionesPsicologo ?: ""
        textoInformeTecnicoProfesor = app.indicacionesParaProfesor ?: ""
        mensajeExito = null
        mensajeError = null
    }

    // Guarda ambas notas profesionales (Interpretación clínica + Indicaciones para el profesor)
    fun guardarNotasProfesionales(onSuccess: () -> Unit = {}) {
        val app = aplicacionSeleccionada ?: return
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return

        guardandoNotas = true
        mensajeError = null
        mensajeExito = null

        viewModelScope.launch {
            try {
                val request = FeedbackRequest(
                    observaciones = textoInterpretacionClinica.trim(),
                    indicaciones = textoInformeTecnicoProfesor.trim()
                )
                val response = RetrofitClient.apiService.agregarFeedback(token, app.id, request)
                if (response.isSuccessful && response.body() != null) {
                    val actualizada = response.body()!!
                    aplicacionSeleccionada = actualizada
                    // Actualiza la lista en memoria para que se refleje de inmediato al volver
                    aplicaciones = aplicaciones.map { if (it.id == actualizada.id) actualizada else it }
                    mensajeExito = "Notas profesionales guardadas correctamente."
                    onSuccess()
                } else {
                    mensajeError = "Error al guardar notas (${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            } finally {
                guardandoNotas = false
            }
        }
    }
}