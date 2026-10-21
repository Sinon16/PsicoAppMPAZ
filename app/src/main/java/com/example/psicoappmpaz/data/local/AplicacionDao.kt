package com.example.psicoappmpaz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.psicoappmpaz.data.entity.AplicacionEntity

@Dao
interface AplicacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun crearAplicacion(aplicacion: AplicacionEntity)

    @Update
    suspend fun actualizarAplicacion(aplicacion: AplicacionEntity)

    @Query("SELECT * FROM aplicaciones WHERE codigoTemporal = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(codigo: String): AplicacionEntity?

    @Query("SELECT * FROM aplicaciones")
    suspend fun obtenerTodas(): List<AplicacionEntity>

    @Query("SELECT * FROM aplicaciones WHERE idAplicacion = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): AplicacionEntity?
}