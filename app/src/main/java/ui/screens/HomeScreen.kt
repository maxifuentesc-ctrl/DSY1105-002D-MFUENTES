package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel
import com.example.modoguardian_grupomg.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen(loginViewModel: LoginViewModel) {
    val loginState by loginViewModel.uiState.collectAsState()
    val usuario = loginState.usuario ?: return
    val windowSizeClass = obtenerWindowSizeClass()
    Column(Modifier.fillMaxSize()) {
        Text("Rol: ${usuario.rol.etiqueta}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp))
        Box(Modifier.weight(1f)) {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> HomeScreenCompacta()
                WindowWidthSizeClass.Medium -> HomeScreenMediana()
                WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
            }
        }
    }
}
