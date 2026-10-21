package com.example.psicoappmpaz.model

data class Cuestionario(
    val idCuestionario: Int = 0,
    val titulo: String,
    val tipo: String, // "Educativo" o "Psicométrico Simulado"
    val rangoEdad: String,
    val tiempoEstimadoMinutos: Int,
    val instrucciones: String,
    val estado: String = "Disponible"
)