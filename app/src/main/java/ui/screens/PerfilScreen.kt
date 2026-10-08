package com.example.modoguardian_grupomg.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modoguardian_grupomg.ui.components.ImagenInteligente
import com.example.modoguardian_grupomg.viewmodel.PerfilViewModel
import java.io.File

// Crea un archivo temporal en la caché y devuelve su URI seguro (vía FileProvider)
private fun crearUriParaFoto(context: Context): Uri {
    val carpeta = File(context.cacheDir, "images").apply { mkdirs() }
    val archivo = File.createTempFile("foto_", ".jpg", carpeta)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current

    // Observa la imagen guardada en el ViewModel
    val imagenUri by viewModel.imagenUri.collectAsState()

    // URI temporal donde la cámara guardará la foto
    var uriTemporal by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Lanzador de la GALERÍA: devuelve el URI elegido (o null si cancela)
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.actualizarImagenDesdeGaleria(uri)
    }

    // Lanzador de la CÁMARA: devuelve true si se tomó la foto, false si se canceló
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            viewModel.actualizarImagenDesdeCamara(uriTemporal)
        }
    }

    // Abre la cámara creando primero el archivo temporal
    fun abrirCamara() {
        val uri = crearUriParaFoto(context)
        uriTemporal = uri
        camaraLauncher.launch(uri)
    }

    // Lanzador del PERMISO de cámara: si lo conceden, abre la cámara
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara()
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)

        // Componente reutilizable: foto circular o ícono por defecto
        ImagenInteligente(uri = imagenUri)

        // Botón 1: abrir la galería
        Button(
            onClick = { galeriaLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Elegir de la galería")
        }

        // Botón 2: abrir la cámara (pide permiso si hace falta)
        Button(
            onClick = {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) {
                    abrirCamara()
                } else {
                    permisoLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto con la cámara")
        }
    }
}