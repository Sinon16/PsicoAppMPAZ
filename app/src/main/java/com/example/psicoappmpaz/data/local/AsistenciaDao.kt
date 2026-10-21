package com.example.psicoappmpaz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.psicoappmpaz.data.entity.AsistenciaEntity

@Dao
interface AsistenciaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarAsistencia(asistencia: AsistenciaEntity)

    @Query("SELECT * FROM asistencia WHERE curso = :curso AND fecha = :fecha")
    suspend fun obtenerPorCursoYFecha(curso: String, fecha: String): List<AsistenciaEntity>
}