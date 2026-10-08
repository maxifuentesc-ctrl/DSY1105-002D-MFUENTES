package com.example.modoguardian_grupomg.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel del perfil: guarda la imagen elegida (desde galería o cámara)
class PerfilViewModel : ViewModel() {

    // Estado interno mutable: URI de la imagen. null = todavía no hay imagen
    private val _imagenUri = MutableStateFlow<Uri?>(null)

    // Estado expuesto a la UI (solo lectura)
    val imagenUri: StateFlow<Uri?> = _imagenUri

    // Actualiza la imagen elegida desde la galería.
    // Si el usuario cancela, el selector devuelve null y se mantiene la imagen anterior.
    fun actualizarImagenDesdeGaleria(uri: Uri?) {
        if (uri != null) {
            _imagenUri.value = uri
        }
    }

    // Actualiza la imagen capturada con la cámara
    fun actualizarImagenDesdeCamara(uri: Uri?) {
        if (uri != null) {
            _imagenUri.value = uri
        }
    }
}