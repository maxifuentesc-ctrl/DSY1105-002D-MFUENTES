package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenExpandida() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Vista Expandida (Pantalla Ancha)", style = MaterialTheme.typography.headlineMedium)
    }
}