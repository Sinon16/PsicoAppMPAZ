package com.example.psicoappmpaz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiantes")
data class EstudianteEntity(
    @PrimaryKey(autoGenerate = true) val idEstudiante: Int = 0,
    val nombreEstudiante: String,
    val cursoEstudiante: String
)