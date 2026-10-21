package com.example.psicoappmpaz.data.remote

data class SyncModel(
    val idAplicacion: Int,
    val codigoTemporal: String,
    val respuestasJson: String,
    val puntajeObtenido: Int,
    val observacionPsicologo: String,
    val sincronizado: Boolean
)