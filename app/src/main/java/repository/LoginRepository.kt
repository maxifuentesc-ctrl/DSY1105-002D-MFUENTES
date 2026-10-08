package com.example.modoguardian_grupomg.repository

import com.example.modoguardian_grupomg.model.UsuarioAutenticado

interface LoginRepository {
    suspend fun autenticar(email: String, password: String, sinConexion: Boolean): UsuarioAutenticado?
    fun usuariosDePrueba(): List<UsuarioAutenticado>
}
