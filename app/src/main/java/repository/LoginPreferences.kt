package com.example.modoguardian_grupomg.repository

import android.content.Context

interface LoginPreferences {
    fun leerCorreo(): String
    fun guardarCorreo(email: String)
}

// Persistencia limitada: únicamente el último correo, nunca contraseña o sesión.
class LocalLoginPreferences(context: Context) : LoginPreferences {
    private val preferencias = context.applicationContext.getSharedPreferences("login_guardian", Context.MODE_PRIVATE)
    override fun leerCorreo(): String = preferencias.getString("ultimo_correo", "").orEmpty()
    override fun guardarCorreo(email: String) {
        preferencias.edit().apply {
            if (email.isBlank()) remove("ultimo_correo") else putString("ultimo_correo", email)
        }.apply()
    }
}
