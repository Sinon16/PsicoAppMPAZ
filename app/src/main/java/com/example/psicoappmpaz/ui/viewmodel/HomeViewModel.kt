package com.example.psicoappmpaz.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psicoappmpaz.model.Aplicacion
import com.example.psicoappmpaz.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var aplicaciones by mutableStateOf<List<Aplicacion>>(emptyList())
    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    // Contadores dinámicos calculados desde la BD externa
    val totalEnCurso: Int
        get() = aplicaciones.count { it.estado.equals("PENDIENTE", ignoreCase = true) || it.estado.equals("EN_CURSO", ignoreCase = true) }

    val totalCompletadas: Int
        get() = aplicaciones.count { it.estado.equals("COMPLETADA", ignoreCase = true) || it.estado.equals("REVISADA", ignoreCase = true) }

    fun cargarAplicaciones() {
        val token = RetrofitClient.getBearerToken()
        if (token == "Bearer ") return
        cargando = true
        mensajeError = null

        viewModelScope.launch{
            try {
                val response = RetrofitClient.apiService.getAplicaciones(token)
                if (response.isSuccessful && response.body() != null){
                    aplicaciones = response.body()!!
                }else {
                    mensajeError = "No se pudieron cargar las evaluaciones (${response.code()})"
                }

            }catch (e: Exception){
                mensajeError = "Error de conexión: ${e.localizedMessage}"
            }finally {
                cargando = false
            }
        }
    }
}