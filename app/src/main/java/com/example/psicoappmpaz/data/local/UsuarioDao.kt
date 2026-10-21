package com.example.psicoappmpaz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.psicoappmpaz.data.entity.*

@Dao
interface UsuarioDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario : UsuarioEntity)
    @Query("SELECT * FROM usuarios WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<UsuarioEntity>
}