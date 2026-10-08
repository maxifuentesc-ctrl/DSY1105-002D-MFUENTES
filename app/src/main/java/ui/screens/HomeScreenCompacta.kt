package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Modo Guardián - Móvil") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "¡Bienvenido (Vista Compacta)!", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { }) {
                Text("Presióname")
            }
        }
    }
}