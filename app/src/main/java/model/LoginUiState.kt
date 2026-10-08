package com.example.modoguardian_grupomg.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val errorGeneral: String? = null,
    val aviso: String? = null,
    val usuario: UsuarioAutenticado? = null,
    val recordarCorreo: Boolean = true,
    val simularSinConexion: Boolean = false
) {
    val isLoginEnabled: Boolean
        // La validación ocurre al enviar, sin impedir pulsar con datos inválidos.
        get() = true
}
