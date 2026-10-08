package com.example.modoguardian_grupomg.model

data class LoginErrores(val email: String? = null, val password: String? = null) {
    val hayErrores: Boolean get() = email != null || password != null
}
