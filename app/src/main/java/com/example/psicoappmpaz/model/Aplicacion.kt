package com.example.psicoappmpaz.model

data class RespuestaDetalle(
    val id: Long? = null,
    val opcionSeleccionada: String,
    val valorPuntaje: Int? = null,
    val fechaRespuesta: String? = null,
    val pregunta: Pregunta? = null
)

data class Aplicacion(
    val id: Long = 0L,
    val codigoTemporal: String,
    val estado: String = "PENDIENTE", // "PENDIENTE", "COMPLETADA", "REVISADA"
    val fechaInicio: String? = null,
    val fechaFinalizacion: String? = null,
    val fechaRevision: String? = null,
    val puntajeTotal: Int? = null,
    val interpretacionPuntaje: String? = null,
    val observacionesPsicologo: String? = null,
    val indicacionesParaProfesor: String? = null,
    val estudiante: Estudiante? = null,
    val cuestionario: Cuestionario? = null,
    val usuarioResponsable: Usuario? = null,
    val respuestas: List<RespuestaDetalle> = emptyList()
)