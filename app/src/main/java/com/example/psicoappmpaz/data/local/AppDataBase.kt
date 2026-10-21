package com.example.psicoappmpaz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.psicoappmpaz.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioEntity::class,
        EstudianteEntity::class,
        CuestionarioEntity::class,
        AplicacionEntity::class,
        AsistenciaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun estudianteDao(): EstudianteDao
    abstract fun cuestionarioDao(): CuestionarioDao
    abstract fun aplicacionDao(): AplicacionDao
    abstract fun asistenciaDao(): AsistenciaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun obtenerBaseDatos(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "psico_evalua_db"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Poblar base de datos inicial con información ficticia
        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        poblarDatosIniciales(database)
                    }
                }
            }

            suspend fun poblarDatosIniciales(db: AppDataBase) {
                // Usuarios
                db.usuarioDao().insertarUsuario(
                    UsuarioEntity(username = "psico1", password = "123", nombreCompleto = "Psicólogo PIE Demo", rol = "PSICOLOGO")
                )
                db.usuarioDao().insertarUsuario(
                    UsuarioEntity(username = "profe1", password = "123", nombreCompleto = "Profesor Guía Demo", rol = "PROFESOR")
                )

                // Estudiantes Ficticios
                db.estudianteDao().insertarEstudiante(
                    EstudianteEntity(nombreEstudiante = "Estudiante Ficticio A", cursoEstudiante = "5° Básico A")
                )
                db.estudianteDao().insertarEstudiante(
                    EstudianteEntity(nombreEstudiante = "Estudiante Ficticio B", cursoEstudiante = "6° Básico B")
                )

                // Cuestionario Simulado
                db.cuestionarioDao().insertarCuestionario(
                    CuestionarioEntity(
                        titulo = "Cuestionario de Hábitos de Estudio (Simulado)",
                        tipo = "Educativo",
                        rangoEdad = "10 a 14 años",
                        tiempoEstimadoMinutos = 15,
                        instrucciones = "Lee cada enunciado y selecciona la opción que mejor describa tu rutina.",
                        estado = "Disponible"
                    )
                )
            }
        }
    }
}