package com.example.psicoappmpaz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.psicoappmpaz.data.entity.CuestionarioEntity

@Dao
interface CuestionarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCuestionario(cuestionario: CuestionarioEntity)

    @Query("SELECT * FROM cuestionarios")
    suspend fun obtenerTodos(): List<CuestionarioEntity>

    @Query("SELECT * FROM cuestionarios WHERE idCuestionario = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): CuestionarioEntity?
}