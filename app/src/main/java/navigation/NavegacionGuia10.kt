package com.example.modoguardian_grupomg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modoguardian_grupomg.ui.screens.PantallaConfiguracion
import com.example.modoguardian_grupomg.ui.screens.PantallaInicio
import com.example.modoguardian_grupomg.ui.screens.PantallaPerfil
import com.example.modoguardian_grupomg.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

// Navegación de la Guía 10 (Inicio, Perfil, Configuración) como componente reutilizable
@Composable
fun NavegacionGuia10() {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberNavController()

    // Escucha los eventos de navegación emitidos por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let {
                            popUpTo(it.route) { inclusive = event.inclusive }
                        }
                        launchSingleTop = event.singleTop
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { PantallaInicio(viewModel) }
        composable(Screen.Profile.route) { PantallaPerfil(viewModel) }
        composable(Screen.Settings.route) { PantallaConfiguracion(viewModel) }
    }
}