package com.example.modoguardian_grupomg.model

// El estado de sesión nunca contiene la contraseña del usuario.
data class UsuarioAutenticado(val nombre: String, val email: String, val rol: Rol)
