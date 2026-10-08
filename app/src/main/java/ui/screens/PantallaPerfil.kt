package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.modoguardian_grupomg.navigation.Screen
import com.example.modoguardian_grupomg.viewmodel.MainViewModel

// Pantalla de perfil: tiene NavigationBar (BottomBar)
@Composable
fun PantallaPerfil(viewModel: MainViewModel) {
    val items = listOf(Screen.Home, Screen.Profile)
    val itemSeleccionado = 1 // estamos en Perfil

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = itemSeleccionado == index,
                        onClick = {
                            // Navega sin duplicar pantallas en la pila
                            viewModel.navigateTo(screen, popUpToRoute = Screen.Home, singleTop = true)
                        },
                        label = { Text(if (screen == Screen.Home) "Inicio" else "Perfil") },
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Home) Icons.Default.Home else Icons.Default.Person,
                                contentDescription = screen.route
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido al Perfil!")
        }
    }
}