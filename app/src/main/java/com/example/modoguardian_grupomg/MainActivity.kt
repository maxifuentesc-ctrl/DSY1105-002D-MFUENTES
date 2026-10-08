package com.example.modoguardian_grupomg

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modoguardian_grupomg.navigation.NavigationEvent
import com.example.modoguardian_grupomg.navigation.Screen
import com.example.modoguardian_grupomg.viewmodel.MainViewModel
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel
import com.example.modoguardian_grupomg.repository.DemoLoginRepository
import com.example.modoguardian_grupomg.repository.LocalLoginPreferences
import kotlinx.coroutines.flow.collect
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.modoguardian_grupomg.navigation.AppIntegrada
import com.example.modoguardian_grupomg.ui.theme.ModoGuardian_GrupoMGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModoGuardian_GrupoMGTheme {
                val navController = rememberNavController()
                // Reutilizamos MainViewModel, NavigationEvent y AppIntegrada originales.
                val mainViewModel: MainViewModel = viewModel()
                val factory = remember {
                    LoginViewModel.Factory(DemoLoginRepository(), LocalLoginPreferences(applicationContext))
                }
                val loginViewModel: LoginViewModel = viewModel(factory = factory)
                val loginState by loginViewModel.uiState.collectAsState()
                val entry by navController.currentBackStackEntryAsState()
                val route = entry?.destination?.route

                LaunchedEffect(navController, mainViewModel) {
                    mainViewModel.navigationEvents.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> navController.navigate(event.route.route) {
                                event.popUpToRoute?.let { popUpTo(it.route) { inclusive = event.inclusive } }
                                launchSingleTop = event.singleTop
                            }
                            NavigationEvent.PopBackStack -> navController.popBackStack()
                            NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }
                // La sesión se guarda en memoria; una ruta restaurada no concede acceso.
                LaunchedEffect(loginState.usuario, route) {
                    val usuario = loginState.usuario
                    if (route != null) {
                        when {
                            usuario == null && route != "login" -> navController.navigate("login") {
                                popUpTo(navController.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                            usuario != null && route == "login" -> mainViewModel.navigateTo(
                                Screen.Menu, popUpToRoute = Screen.Login, inclusive = true, singleTop = true)
                            usuario != null && route.startsWith("guia") && !usuario.rol.puedeAbrirGuia(route) ->
                                mainViewModel.navigateTo(Screen.Menu, popUpToRoute = Screen.Menu, singleTop = true)
                        }
                    }
                }
                AppIntegrada(navController, mainViewModel, loginViewModel)
            }
        }
    }
}