package com.example.psicoappmpaz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuestionarios")
data class CuestionarioEntity(
    @PrimaryKey(autoGenerate = true) val idCuestionario: Int = 0,
    val titulo: String,
    val tipo: String,
    val rangoEdad: String,
    val tiempoEstimadoMinutos: Int,
    val instrucciones: String,
    val estado: String = "Disponible"
)