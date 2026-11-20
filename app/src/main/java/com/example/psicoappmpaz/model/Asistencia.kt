package com.example.psicoappmpaz.model

data class Asistencia(
    val id: Long = 0L,
    val fecha: String, // Formato "YYYY-MM-DD" que devuelve LocalDate en JSON
    val presente: Boolean = true,
    val estudiante: Estudiante? = null,
    val registradoPor: Usuario? = null
)