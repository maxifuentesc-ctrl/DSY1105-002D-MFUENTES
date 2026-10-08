package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Menú principal: un botón por cada guía. Recibe una función para navegar a la ruta elegida
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipal(onNavegar: (String) -> Unit, loginViewModel: LoginViewModel) {
    // El menú original es el inicio autenticado: observa la misma sesión del Login.
    val loginState by loginViewModel.uiState.collectAsState()
    val usuario = loginState.usuario ?: return
    // Pares de (ruta, título del botón)
    val guias = listOf(
        "guia9" to "Guía 9 · Diseño adaptable",
        "guia10" to "Guía 10 · Navegación y menú",
        "guia11" to "Guía 11 · Formulario y resumen",
        "guia12" to "Guía 12 · Estado y animaciones",
        "guia13" to "Guía 13 · Cámara y galería"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ModoGuardian") }, actions = {
                TextButton(onClick = loginViewModel::logout) { Text("Cerrar sesión") }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${usuario.email} · Rol: ${usuario.rol.etiqueta}",
                style = MaterialTheme.typography.bodyMedium)
            loginState.aviso?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text("Elige una guía", style = MaterialTheme.typography.headlineMedium)

            guias.filter { usuario.rol.puedeAbrirGuia(it.first) }.forEach { (ruta, titulo) ->
                Button(
                    onClick = { onNavegar(ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(titulo)
                }
            }
        }
    }
}