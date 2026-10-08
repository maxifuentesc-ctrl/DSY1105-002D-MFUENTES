package com.example.modoguardian_grupomg.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel
import com.example.modoguardian_grupomg.viewmodel.MainViewModel
import com.example.modoguardian_grupomg.ui.screens.LoginScreen
import com.example.modoguardian_grupomg.ui.screens.HomeScreen
import com.example.modoguardian_grupomg.ui.screens.MenuPrincipal
import com.example.modoguardian_grupomg.ui.screens.PantallaPrincipal
import com.example.modoguardian_grupomg.ui.screens.PerfilScreen

// App integrada: un menú principal que lleva a cada una de las guías
@Composable
fun AppIntegrada(navController: NavHostController, mainViewModel: MainViewModel, loginViewModel: LoginViewModel) {
    val loginState by loginViewModel.uiState.collectAsState()
    val rol = loginState.usuario?.rol

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(loginViewModel) }

        composable("menu") {
            if (rol != null) MenuPrincipal(
                onNavegar = { ruta ->
                    if (rol.puedeAbrirGuia(ruta)) Screen.guiaPorRuta(ruta)?.let { mainViewModel.navigateTo(it, singleTop = true) }
                },
                loginViewModel = loginViewModel
            )
        }

        // Guía 9: diseño adaptable (compacta, mediana, expandida)
        composable("guia9") { if (rol?.puedeAbrirGuia("guia9") == true) HomeScreen(loginViewModel) }

        // Guía 10: navegación con menú lateral y barra inferior
        composable("guia10") { if (rol?.puedeAbrirGuia("guia10") == true) NavegacionGuia10() }

        // Guía 11: formulario con validaciones y resumen
        composable("guia11") {
            if (rol?.puedeAbrirGuia("guia11") == true) Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) { AppNavigation() }
            }
        }

        // Guía 12: estado, DataStore y animaciones
        composable("guia12") {
            if (rol?.puedeAbrirGuia("guia12") == true) Scaffold { innerPadding ->
                PantallaPrincipal(modifier = Modifier.padding(innerPadding))
            }
        }

        // Guía 13: cámara y galería
        composable("guia13") {
            if (rol?.puedeAbrirGuia("guia13") == true) Scaffold { innerPadding ->
                PerfilScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}