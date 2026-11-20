package com.example.psicoappmpaz.network

import com.example.psicoappmpaz.data.models.*
import com.example.psicoappmpaz.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==========================================
    // 1. AUTENTICACIÓN Y REGISTRO (AuthController)
    // ==========================================
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<String>

    // ==========================================
    // 2. APLICACIONES DE EVALUACIÓN (AplicacionEvaluacionController)
    // ==========================================
    @GET("api/aplicaciones")
    suspend fun getAplicaciones(
        @Header("Authorization") token: String
    ): Response<List<Aplicacion>>

    // Endpoint LIBRE: El estudiante accede con su código temporal (ej. EST-101)
    @GET("api/aplicaciones/codigo/{codigoTemporal}")
    suspend fun getAplicacionPorCodigo(
        @Path("codigoTemporal") codigoTemporal: String
    ): Response<Aplicacion>

    @POST("api/aplicaciones")
    suspend fun crearAplicacion(
        @Header("Authorization") token: String,
        @Body request: CrearAplicacionRequest
    ): Response<Aplicacion>

    // Endpoint LIBRE: Envío de respuestas y puntaje desde Android
    @POST("api/aplicaciones/responder/{codigoTemporal}")
    suspend fun responderYFinalizar(
        @Path("codigoTemporal") codigoTemporal: String,
        @Query("puntajeCalculado") puntajeCalculado: Int? = null,
        @Body respuestas: List<RespuestaDetalleRequest>
    ): Response<Aplicacion>

    @PUT("api/aplicaciones/feedback/{id}")
    suspend fun agregarFeedback(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body feedback: FeedbackRequest
    ): Response<Aplicacion>

    // ==========================================
    // 3. CUESTIONARIOS (CuestionarioController)
    // ==========================================
    @GET("api/cuestionarios")
    suspend fun getCuestionarios(
        @Header("Authorization") token: String
    ): Response<List<Cuestionario>>

    @POST("api/cuestionarios")
    suspend fun crearCuestionario(
        @Header("Authorization") token: String,
        @Body request: CrearCuestionarioRequest
    ): Response<Cuestionario>

    // ==========================================
    // 4. ESTUDIANTES (EstudianteController)
    // ==========================================
    @GET("api/estudiantes")
    suspend fun getEstudiantes(
        @Header("Authorization") token: String
    ): Response<List<Estudiante>>

    @GET("api/estudiantes/curso/{curso}")
    suspend fun getEstudiantesPorCurso(
        @Header("Authorization") token: String,
        @Path("curso") curso: String
    ): Response<List<Estudiante>>

    @POST("api/estudiantes")
    suspend fun crearEstudiante(
        @Header("Authorization") token: String,
        @Body request: CrearEstudianteRequest
    ): Response<Estudiante>

    // ==========================================
    // 5. ASISTENCIA (AsistenciaController)
    // ==========================================
    @GET("api/asistencias")
    suspend fun getAsistencias(
        @Header("Authorization") token: String,
        @Query("fecha") fecha: String, // Formato "YYYY-MM-DD"
        @Query("curso") curso: String? = null
    ): Response<List<Asistencia>>

    @POST("api/asistencias")
    suspend fun registrarAsistencias(
        @Header("Authorization") token: String,
        @Body asistencias: List<AsistenciaRequest>
    ): Response<List<Asistencia>>
}