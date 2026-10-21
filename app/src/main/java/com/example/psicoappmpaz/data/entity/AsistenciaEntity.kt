package com.example.psicoappmpaz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asistencia")
data class AsistenciaEntity(
    @PrimaryKey(autoGenerate = true) val idAsistencia: Int = 0,
    val idEstudiante: Int,
    val curso: String,
    val fecha: String,
    val estadoAsistencia: String,
    val usuarioRegistro: String
)