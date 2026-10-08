package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modoguardian_grupomg.viewmodel.EstadoViewModel

@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    viewModel: EstadoViewModel = viewModel()
) {
    // Observa los estados del ViewModel
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    // rememberSaveable: este contador sobrevive a rotaciones de pantalla
    var cambios by rememberSaveable { mutableStateOf(0) }

    val valorEstado = estado

    if (valorEstado == null) {
        // Mientras carga el estado guardado, mostramos el loader
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val estaActivo = valorEstado

        // Animación 1: el color del botón cambia suavemente según el estado
        val colorAnimado by animateColorAsState(
            targetValue = if (estaActivo) Color(0xFF4CAF50) else Color(0xFFB0BEC5),
            animationSpec = tween(durationMillis = 500),
            label = "colorBoton"
        )

        // Texto derivado del estado
        val textoBoton by remember(estaActivo) {
            derivedStateOf { if (estaActivo) "Desactivar" else "Activar" }
        }

        // Animación extra 2: el ícono cambia de tamaño
        val tamanoIcono by animateDpAsState(
            targetValue = if (estaActivo) 120.dp else 80.dp,
            animationSpec = tween(durationMillis = 500),
            label = "tamanoIcono"
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícono que cambia de color y de tamaño
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Estado del modo",
                tint = colorAnimado,
                modifier = Modifier.size(tamanoIcono)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animación extra 3: el texto de estado cambia con una transición suave
            Crossfade(targetState = estaActivo, label = "textoEstado") { activo ->
                Text(
                    text = if (activo) "Modo Guardián ACTIVO" else "Modo Guardián apagado",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.alternarEstado()
                    cambios++
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(textoBoton, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de éxito con animación de aparecer y desaparecer
            AnimatedVisibility(visible = mostrarMensaje) {
                Text(
                    text = "¡Estado guardado exitosamente!",
                    color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Veces que cambiaste el modo: $cambios",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}