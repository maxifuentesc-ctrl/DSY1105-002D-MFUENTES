package com.example.modoguardian_grupomg.navigation

// Representa los distintos tipos de eventos de navegación
sealed class NavigationEvent {

    // Navegar a un destino. popUpToRoute e inclusive controlan qué se quita de la pila;
    // singleTop evita copias repetidas del mismo destino.
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    // Volver a la pantalla anterior
    data object PopBackStack : NavigationEvent()

    // Navegar "hacia arriba" en la jerarquía
    data object NavigateUp : NavigationEvent()
}