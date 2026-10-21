package com.example.psicoappmpaz.model

data class Aplicacion(
    val idAplicacion: Int = 0,
    val codigoTemporal: String,
    val idEstudianteFicticio: Int,
    val idCuestionario: Int,
    val estado: String, // "PENDIENTE", "EN_DESARROLLO", "FINALIZADA", "REVISADA"
    val fechaInicio: String,
    val fechaFin: String = "",
    val respuestasJson: String = "",
    val puntajeObtenido: Int = 0,
    val observacionPsicologo: String = "",
    val usuarioResponsable: String,
    val sincronizadoCloud: Boolean = false
)