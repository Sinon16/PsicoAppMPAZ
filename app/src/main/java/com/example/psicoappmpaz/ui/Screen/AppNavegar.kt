package com.example.psicoappmpaz.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
        // --- PANTALLAS TEMPORALES EN CONSTRUCCIÓN ---
        composable("login") {
            LoginScreen(
                onLoginExitosoPsico = { navController.navigate("psico_home") },
                onLoginExitosoProfe = { navController.navigate("profesor_home") },
                onIngresoEstudiante = { navController.navigate("estudiante_home") }
            )
        }

        composable("psico_home") {
            PsicoHomeScreen(
                onCrearAplicacion = { navController.navigate("crear_aplicacion") },
                onVerDetalle = { idApp -> navController.navigate("detalle_resultado/$idApp") },
                onIrAsistencia = { navController.navigate("asistencia") }
            )
        }

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

        composable("profesor_home") {
            ProfesorHomeScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable("asistencia") {
            AsistenciaScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}