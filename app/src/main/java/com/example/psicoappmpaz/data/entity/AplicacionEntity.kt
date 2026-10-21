package com.example.psicoappmpaz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aplicaciones")
data class AplicacionEntity(
    @PrimaryKey(autoGenerate = true) val idAplicacion: Int = 0,
    val codigoTemporal: String,
    val idEstudianteFicticio: Int,
    val idCuestionario: Int,
    val estado: String,
    val fechaInicio: String,
    val fechaFin: String = "",
    val respuestasJson: String = "",
    val puntajeObtenido: Int = 0,
    val observacionPsicologo: String = "",
    val usuarioResponsable: String,
    val sincronizadoCloud: Boolean = false
)