package com.example.psicoappmpaz.data.models

// ==========================================
// 1. AUTENTICACIÓN
// ==========================================
data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val username: String? = null,
    val nombreCompleto: String? = null,
    val rol: String? = null
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val nombreCompleto: String,
    val rol: String
)

// ==========================================
// 2. REFERENCIA DE ID PARA LLAVES FORÁNEAS
// ==========================================
data class IdRef(val id: Long)

// ==========================================
// 3. APLICACIÓN DE EVALUACIÓN Y RESPUESTAS
// ==========================================
data class CrearAplicacionRequest(
    val codigoTemporal: String,
    val estado: String = "PENDIENTE",
    val fechaInicio: String? = null,
    val estudiante: IdRef,
    val cuestionario: IdRef,
    val usuarioResponsable: IdRef? = null
)

data class RespuestaDetalleRequest(
    val opcionSeleccionada: String,
    val valorPuntaje: Int,
    val pregunta: IdRef
)

data class FeedbackRequest(
    val observaciones: String,
    val indicaciones: String
)

// ==========================================
// 4. ASISTENCIA
// ==========================================
data class AsistenciaRequest(
    val fecha: String, // Formato "YYYY-MM-DD"
    val presente: Boolean,
    val estudiante: IdRef,
    val registradoPor: IdRef? = null
)

// ==========================================
// 5. CREAR CUESTIONARIO Y PREGUNTAS
// ==========================================
// CORREGIDO: Ajustado a Pregunta.java (enunciado, orden)
data class CrearPreguntaRequest(
    val enunciado: String,
    val orden: Int
)

data class CrearCuestionarioRequest(
    val titulo: String,
    val descripcion: String,
    val instruccionesProfesor: String? = null,
    val especificacionesTecnicasPsicologo: String? = null,
    val rangoEdad: String? = "6 - 14 años",
    val tiempoEstimadoMinutos: Int? = 15,
    val preguntas: List<CrearPreguntaRequest>
)

// ==========================================
// 6. CREAR ESTUDIANTE TEMPORAL
// ==========================================
// CORREGIDO: Ajustado a Estudiante.java (nombre, curso, esTemporal)
data class CrearEstudianteRequest(
    val nombre: String,
    val curso: String,
    val esTemporal: Boolean = true
)