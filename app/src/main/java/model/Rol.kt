package com.example.modoguardian_grupomg.model

// Permisos centralizados: también se comprueban antes de mostrar un destino.
enum class Rol(val etiqueta: String) {
    Admin("Admin"), Supervisor("Supervisor"), Operador("Operador");

    // Los nombres de las guías y sus rutas son los mismos del proyecto original.
    fun puedeAbrirGuia(ruta: String): Boolean = when (this) {
        Admin -> ruta in setOf("guia9", "guia10", "guia11", "guia12", "guia13")
        Supervisor -> ruta in setOf("guia9", "guia10", "guia12", "guia13")
        Operador -> ruta in setOf("guia9", "guia13")
    }

}
