package com.example.modoguardian_grupomg.navigation

// Sealed class para definir rutas tipo-safe en la navegación
sealed class Screen(val route: String) {

    data object Login : Screen("login")
    data object Menu : Screen("menu")
    data object Guia9 : Screen("guia9")
    data object Guia10 : Screen("guia10")
    data object Guia11 : Screen("guia11")
    data object Guia12 : Screen("guia12")
    data object Guia13 : Screen("guia13")

    companion object {
        fun guiaPorRuta(ruta: String): Screen? = when (ruta) {
            "guia9" -> Guia9
            "guia10" -> Guia10
            "guia11" -> Guia11
            "guia12" -> Guia12
            "guia13" -> Guia13
            else -> null
        }
    }

    // Rutas simples (sin argumentos)
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")

    // Ejemplo de ruta con argumento (no se usa en este ejercicio)
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        fun buildRoute(): String = route.replace("{itemId}", itemId)
    }
}