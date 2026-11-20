package com.example.psicoappmpaz.model

data class Pregunta(
    val id: Long = 0L,
    val enunciado: String,
    val orden: Int = 0
)

data class Cuestionario(
    val id: Long = 0L,
    val titulo: String,
    val descripcion: String = "",
    val instruccionesProfesor: String = "",
    val especificacionesTecnicasPsicologo: String = "",
    val rangoEdad: String = "",
    val tiempoEstimadoMinutos: Int = 0,
    val preguntas: List<Pregunta> = emptyList()
)