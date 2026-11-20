package com.example.psicoappmpaz.model

data class Estudiante(
    val id: Long = 0L,
    val nombre: String,
    val curso: String,
    val esTemporal: Boolean = false
)