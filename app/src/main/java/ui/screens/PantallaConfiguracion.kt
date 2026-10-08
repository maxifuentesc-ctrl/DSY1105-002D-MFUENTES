package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modoguardian_grupomg.navigation.Screen
import com.example.modoguardian_grupomg.viewmodel.MainViewModel

// Pantalla de configuración: los botones emiten eventos de navegación al ViewModel
@Composable
fun PantallaConfiguracion(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pantalla de Configuración (Settings)")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.navigateTo(Screen.Home, popUpToRoute = Screen.Home, singleTop = true) }) {
            Text("Volver al Inicio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.navigateTo(Screen.Profile) }) {
            Text("Ir a Perfil")
        }
    }
}