package com.example.modoguardian_grupomg.repository

import com.example.modoguardian_grupomg.model.Rol
import com.example.modoguardian_grupomg.model.UsuarioAutenticado
import java.io.IOException
import java.util.Locale
import kotlinx.coroutines.delay

// Servicio local de prueba. No realiza peticiones ni acepta credenciales reales.
// La demora y el interruptor reproducen carga/error de red de forma determinista.
class DemoLoginRepository(private val demoraMillis: Long = 900) : LoginRepository {
    private val usuarios = listOf(
        UsuarioAutenticado("Administrador de prueba", "admin@guardian.test", Rol.Admin),
        UsuarioAutenticado("Supervisor de prueba", "supervisor@guardian.test", Rol.Supervisor),
        UsuarioAutenticado("Operador de prueba", "operador@guardian.test", Rol.Operador)
    )

    override suspend fun autenticar(email: String, password: String, sinConexion: Boolean): UsuarioAutenticado? {
        delay(demoraMillis)
        if (sinConexion) throw IOException("Conectividad simulada")
        if (password != "123456") return null
        val correoNormalizado = email.trim().lowercase(Locale.ROOT)
        return usuarios.firstOrNull { it.email == correoNormalizado }
    }

    override fun usuariosDePrueba(): List<UsuarioAutenticado> = usuarios.toList()
}
