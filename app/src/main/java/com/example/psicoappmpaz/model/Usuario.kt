package com.example.psicoappmpaz.model

data class Usuario(
    val id: Long = 0L,
    val username: String,
    val nombreCompleto: String,
    val rol: String, // "PSICOLOGO", "PROFESOR", "DIRECTOR"
    val fechaCreacion: String? = null
)