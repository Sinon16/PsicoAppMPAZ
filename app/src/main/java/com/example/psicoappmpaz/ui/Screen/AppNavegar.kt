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

        // --- DASHBOARD PSICÓLOGO ---
        composable("psico_home/{nombreUsuario}") { backStackEntry ->
            val nombreRaw = backStackEntry.arguments?.getString("nombreUsuario") ?: "Profesional"
            val nombreDecodificado = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())

            PsicoHomeScreen(
                nombreUsuario = nombreDecodificado,
                onCrearAplicacion = { navController.navigate("crear_aplicacion") },
                onVerDetalle = { idApp -> navController.navigate("detalle_resultado/$idApp") },
                onIrAsistencia = { navController.navigate("asistencia") },
                onIrEvaluaciones = { navController.navigate("evaluaciones") },
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

        composable("asistencia") {
            AsistenciaScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable("evaluaciones") {
            EvaluacionesScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}