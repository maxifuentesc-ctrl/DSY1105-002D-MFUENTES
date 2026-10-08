package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modoguardian_grupomg.viewmodel.UsuarioViewModel

// Pantalla de resumen: muestra los datos ingresados en el formulario
@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    // Observa el MISMO ViewModel que usa el formulario
    val estado by viewModel.estado.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Resumen del Registro", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${estado.nombre}")
        Text("Correo: ${estado.correo}")
        Text("Dirección: ${estado.direccion}")
        // La contraseña se oculta con asteriscos
        Text("Contraseña: ${"*".repeat(estado.clave.length)}")
        Text("Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")
    }
}