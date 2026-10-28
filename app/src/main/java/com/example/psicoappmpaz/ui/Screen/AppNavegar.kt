package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavegar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        // --- PANTALLA DE LOGIN ---
        composable("login") {
            LoginScreen(
                onLoginExitosoPsico = { nombreUsuario ->
                    val nombreEncoded = URLEncoder.encode(nombreUsuario, StandardCharsets.UTF_8.toString())
                    navController.navigate("psico_home/$nombreEncoded") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginExitosoProfe = { nombreUsuario ->
                    val nombreEncoded = URLEncoder.encode(nombreUsuario, StandardCharsets.UTF_8.toString())
                    navController.navigate("profesor_home/$nombreEncoded") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onIngresoEstudiante = {
                    navController.navigate("estudiante_home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        // Ruta para registrar usuarios en la base de datos local
        composable("registrar_usuario") {
            RegistrarUsuarioScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // --- DASHBOARD PSICÓLOGO ---
        composable("psico_home/{nombreUsuario}") { backStackEntry ->
            val nombreRaw = backStackEntry.arguments?.getString("nombreUsuario") ?: "Profesional"
            val nombreDecodificado = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())

            PsicoHomeScreen(
                nombreUsuario = nombreDecodificado,
                onCrearAplicacion = { navController.navigate("crear_aplicacion") },
                onVerDetalle = { idApp -> navController.navigate("detalle_resultado/$idApp") },
                // Construye correctamente las rutas con rol y el nombre codificado
                onIrAsistencia = { navController.navigate("asistencia/PSICOLOGO/$nombreRaw") },
                onIrEvaluaciones = { navController.navigate("evaluaciones/PSICOLOGO/$nombreRaw") },
                onIrPerfil = { navController.navigate("perfil/PSICOLOGO/$nombreRaw") },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // --- DASHBOARD PROFESOR ---
        composable("profesor_home/{nombreUsuario}") { backStackEntry ->
            ProfesorHomeScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // --- MÓDULO ESTUDIANTE ---
        composable("estudiante_home") {
            EstudianteHomeScreen(
                onIniciarTest = { navController.navigate("estudiante_test") }
            )
        }

        composable("estudiante_test") {
            EstudianteTestScreen(
                onFinalizar = { navController.popBackStack("login", inclusive = false) }
            )
        }

        // --- MÓDULOS SECUNDARIOS ---
        composable("crear_aplicacion") {
            CrearAplicacionScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable("detalle_resultado/{idApp}") {
            DetalleResultadoScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // --- RUTAS CON BARRA DE NAVEGACIÓN ---
        composable("asistencia/{rolUsuario}/{nombreUsuario}") { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rolUsuario") ?: "PSICOLOGO"
            val nombreRaw = backStackEntry.arguments?.getString("nombreUsuario") ?: ""
            val nombre = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())

            AsistenciaScreen(
                rolUsuario = rol,
                nombreUsuario = nombre,
                onIrInicio = {
                    val destino = if (rol == "PROFESOR") "profesor_home/$nombreRaw" else "psico_home/$nombreRaw"
                    navController.navigate(destino) { popUpTo("login") { inclusive = false } }
                },
                onIrEvaluaciones = { navController.navigate("evaluaciones/$rol/$nombreRaw") },
                onIrPerfil = { navController.navigate("perfil/$rol/$nombreRaw") },
            )
        }

        composable("evaluaciones/{rolUsuario}/{nombreUsuario}") { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rolUsuario") ?: "PSICOLOGO"
            val nombreRaw = backStackEntry.arguments?.getString("nombreUsuario") ?: ""
            val nombre = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())

            EvaluacionesScreen(
                rolUsuario = rol,
                nombreUsuario = nombre,
                onIrCrearEvaluacion = { navController.navigate("crear_evaluacion") },
                onIrInicio = {
                    val destino = if (rol == "PROFESOR") "profesor_home/$nombreRaw" else "psico_home/$nombreRaw"
                    navController.navigate(destino) { popUpTo("login") { inclusive = false } }
                },
                onIrAsistencia = { navController.navigate("asistencia/$rol/$nombreRaw") },
                onIrPerfil = { navController.navigate("perfil/$rol/$nombreRaw") },
                onVolver = { navController.popBackStack() }
            )
        }

        composable("crear_evaluacion") {
            CrearEvaluacionScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable("perfil/{rolUsuario}/{nombreUsuario}") { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rolUsuario") ?: "PSICOLOGO"
            val nombreRaw = backStackEntry.arguments?.getString("nombreUsuario") ?: ""
            val nombre = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())

            PerfilScreen(
                rolUsuario = rol,
                nombreUsuario = nombre,
                onIrInicio = {
                    val destino = if (rol == "PROFESOR") "profesor_home/$nombreRaw" else "psico_home/$nombreRaw"
                    navController.navigate(destino) { popUpTo("login") { inclusive = false } }
                },
                onIrEvaluaciones = { navController.navigate("evaluaciones/$rol/$nombreRaw") },
                onIrAsistencia = { navController.navigate("asistencia/$rol/$nombreRaw") },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}