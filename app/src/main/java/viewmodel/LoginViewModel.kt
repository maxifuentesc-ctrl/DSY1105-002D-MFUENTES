package com.example.modoguardian_grupomg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.modoguardian_grupomg.model.LoginUiState
import com.example.modoguardian_grupomg.model.LoginValidator
import com.example.modoguardian_grupomg.repository.LoginPreferences
import com.example.modoguardian_grupomg.repository.LoginRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val preferences: LoginPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    val usuariosDePrueba get() = repository.usuariosDePrueba()

    init {
        // Un archivo local corrupto no debe impedir abrir el formulario.
        try {
            _uiState.update { it.copy(email = preferences.leerCorreo()) }
        } catch (e: Exception) {
            _uiState.update { it.copy(aviso = "No se pudo recuperar el correo guardado. Puedes ingresar igualmente.") }
        }
    }

    fun onEmailChange(value: String) {
        if (_uiState.value.isLoading) return
        _uiState.update {
            it.copy(email = value, errorGeneral = null, errores = it.errores.copy(
                email = null
            ))
        }
    }

    fun onPasswordChange(value: String) {
        if (_uiState.value.isLoading) return
        _uiState.update {
            it.copy(password = value, errorGeneral = null, errores = it.errores.copy(
                password = null
            ))
        }
    }

    fun onRecordarCorreoChange(value: Boolean) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(recordarCorreo = value) }
        if (!value) guardarCorreoSeguro("")
        else _uiState.value.usuario?.let { guardarCorreoSeguro(it.email) }
    }

    fun onSimularSinConexionChange(value: Boolean) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(simularSinConexion = value, errorGeneral = null) }
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val errores = LoginValidator.validar(estado.email, estado.password)
        _uiState.update { it.copy(errores = errores) }
        return !errores.hayErrores
    }

    fun login() {
        if (_uiState.value.isLoading || _uiState.value.usuario != null || !validarFormulario()) return
        val solicitud = _uiState.value
        // Se marca antes de lanzar la coroutine para evitar solicitudes por doble toque.
        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
        viewModelScope.launch {
            try {
                val usuario = repository.autenticar(solicitud.email, solicitud.password, solicitud.simularSinConexion)
                if (usuario == null) {
                    _uiState.update { it.copy(errorGeneral = "Correo o contraseña incorrectos. Revisa los usuarios de prueba.") }
                } else {
                    guardarCorreoSeguro(if (solicitud.recordarCorreo) usuario.email else "")
                    _uiState.update { it.copy(usuario = usuario, email = usuario.email, password = "") }
                }
            } catch (e: CancellationException) {
                throw e // La cancelación del ciclo de vida no es un error de autenticación.
            } catch (e: IOException) {
                _uiState.update { it.copy(errorGeneral = "No se pudo conectar al servicio de prueba. Desactiva la simulación de desconexión y vuelve a intentar.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorGeneral = "No se pudo iniciar sesión. Puedes volver a intentarlo.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun guardarCorreoSeguro(email: String) {
        try {
            preferences.guardarCorreo(email)
        } catch (e: Exception) {
            _uiState.update { it.copy(aviso = "No se pudo guardar el correo. La sesión actual sigue disponible.") }
        }
    }

    fun logout() {
        if (_uiState.value.isLoading) return
        val correo = if (_uiState.value.recordarCorreo) _uiState.value.email else ""
        _uiState.value = LoginUiState(email = correo, recordarCorreo = _uiState.value.recordarCorreo)
    }

    class Factory(private val repository: LoginRepository, private val preferences: LoginPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(LoginViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, preferences) as T
        }
    }
}
