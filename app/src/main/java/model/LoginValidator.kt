package com.example.modoguardian_grupomg.model

// Validación sin dependencias Android para probar entradas erróneas en JVM.
object LoginValidator {
    private val patronEmail = Regex("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$")
    fun emailValido(email: String): Boolean = patronEmail.matches(email.trim())
    fun validar(email: String, password: String) = LoginErrores(
        email = when {
            email.isBlank() -> "Ingresa tu correo electrónico"
            !emailValido(email) -> "Ingresa un correo válido, por ejemplo admin@guardian.test"
            else -> null
        },
        password = when {
            password.isBlank() -> "Ingresa tu contraseña"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    )
}
