package com.example.psicoappmpaz.model

data class Usuario(
    val id: Int = 0,
    val username: String,
    val nombreCompleto: String,
    val rol: String // "PSICOLOGO", "PROFESOR", "DIRECTOR"
)