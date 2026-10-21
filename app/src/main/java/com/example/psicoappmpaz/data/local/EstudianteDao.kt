package com.example.psicoappmpaz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.psicoappmpaz.data.entity.EstudianteEntity

@Dao
interface EstudianteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEstudiante(estudiante: EstudianteEntity)

    @Query("SELECT * FROM estudiantes")
    suspend fun obtenerTodos(): List<EstudianteEntity>

    @Query("SELECT * FROM estudiantes WHERE idEstudiante = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): EstudianteEntity?
}