package com.example.modoguardian_grupomg.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// Componente reutilizable: muestra una imagen circular.
// Si no hay imagen (uri == null), muestra un ícono de perfil por defecto.
@Composable
fun ImagenInteligente(
    uri: Uri?,
    modifier: Modifier = Modifier,
    tamano: Dp = 160.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            // Hay imagen: se carga con Coil y se recorta para llenar el círculo
            AsyncImage(
                model = uri,
                contentDescription = "Imagen de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // No hay imagen: ícono de perfil por defecto
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Sin imagen de perfil",
                modifier = Modifier.size(tamano / 2),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}