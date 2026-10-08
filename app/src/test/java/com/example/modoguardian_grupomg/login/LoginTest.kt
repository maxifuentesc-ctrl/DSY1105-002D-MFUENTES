package com.example.modoguardian_grupomg.login

import com.example.modoguardian_grupomg.model.LoginValidator
import com.example.modoguardian_grupomg.model.Rol
import com.example.modoguardian_grupomg.repository.DemoLoginRepository
import com.example.modoguardian_grupomg.repository.LoginPreferences
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun teardown() { Dispatchers.resetMain() }

    private class MemoryPreferences(var email: String = "", val corrupt: Boolean = false) : LoginPreferences {
        override fun leerCorreo(): String {
            if (corrupt) error("Datos dañados")
            return email
        }
        override fun guardarCorreo(email: String) { this.email = email }
    }
    private fun vm(prefs: LoginPreferences = MemoryPreferences()) = LoginViewModel(DemoLoginRepository(), prefs)
    private fun credentials(vm: LoginViewModel, email: String = "admin@guardian.test", password: String = "123456") {
        vm.onEmailChange(email)
        vm.onPasswordChange(password)
    }

    @Test fun camposInvalidosNoAutentican() = runTest(dispatcher) {
        val vm = vm()
        assertTrue(vm.uiState.value.isLoginEnabled)
        assertFalse(vm.uiState.value.errores.hayErrores)
        vm.login()
        assertNotNull(vm.uiState.value.errores.email)
        assertNotNull(vm.uiState.value.errores.password)
        credentials(vm, "sin-arroba", "12")
        vm.login()
        advanceUntilIdle()
        assertTrue(vm.uiState.value.isLoginEnabled)
        assertFalse(vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.usuario)
    }

    @Test fun escribirDatosInvalidosNoMuestraErroresHastaEnviar() = runTest(dispatcher) {
        val vm = vm()
        credentials(vm, "sin-arroba", "12")
        assertTrue(vm.uiState.value.isLoginEnabled)
        assertFalse(vm.uiState.value.errores.hayErrores)
        vm.login()
        assertNotNull(vm.uiState.value.errores.email)
        assertNotNull(vm.uiState.value.errores.password)
        assertFalse(vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.usuario)
    }

    @Test fun editarLimpiaErrorDelCampoYReintentoValidaNuevamente() = runTest(dispatcher) {
        val vm = vm()
        vm.login()
        vm.onEmailChange("todavia-invalido")
        assertNull(vm.uiState.value.errores.email)
        assertNotNull(vm.uiState.value.errores.password)
        vm.onPasswordChange("12")
        assertFalse(vm.uiState.value.errores.hayErrores)
        vm.login()
        assertTrue(vm.uiState.value.errores.hayErrores)
        credentials(vm)
        assertFalse(vm.uiState.value.errores.hayErrores)
        vm.login()
        advanceUntilIdle()
        assertEquals(Rol.Admin, vm.uiState.value.usuario?.rol)
    }

    @Test fun losTresUsuariosObtienenSuRol() = runTest(dispatcher) {
        listOf("admin" to Rol.Admin, "supervisor" to Rol.Supervisor, "operador" to Rol.Operador).forEach { (nombre, rol) ->
            val vm = vm()
            credentials(vm, "$nombre@guardian.test")
            vm.login()
            assertTrue(vm.uiState.value.isLoading)
            assertTrue(vm.uiState.value.isLoginEnabled)
            advanceUntilIdle()
            assertEquals(rol, vm.uiState.value.usuario?.rol)
            assertEquals("", vm.uiState.value.password)
            assertFalse(vm.uiState.value.isLoading)
        }
    }

    @Test fun noAdmiteCredencialesDistintas() = runTest(dispatcher) {
        val vm = vm()
        credentials(vm, password = "654321")
        vm.login(); advanceUntilIdle()
        assertNull(vm.uiState.value.usuario)
        assertNotNull(vm.uiState.value.errorGeneral)
        credentials(vm, "otro@guardian.test")
        vm.login(); advanceUntilIdle()
        assertNull(vm.uiState.value.usuario)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test fun desconexionPermiteReintentarSinReiniciar() = runTest(dispatcher) {
        val vm = vm()
        credentials(vm)
        vm.onSimularSinConexionChange(true)
        vm.login(); advanceUntilIdle()
        assertNull(vm.uiState.value.usuario)
        assertNotNull(vm.uiState.value.errorGeneral)
        assertFalse(vm.uiState.value.isLoading)
        assertTrue(vm.uiState.value.isLoginEnabled)
        vm.onSimularSinConexionChange(false)
        vm.login(); advanceUntilIdle()
        assertEquals(Rol.Admin, vm.uiState.value.usuario?.rol)
        assertNull(vm.uiState.value.errorGeneral)
    }

    @Test fun normalizaCorreoSinAlterarPassword() = runTest(dispatcher) {
        val vm = vm()
        credentials(vm, "  ADMIN@guardian.test  ")
        vm.login(); advanceUntilIdle()
        assertEquals("admin@guardian.test", vm.uiState.value.usuario?.email)
    }

    @Test fun soloPersisteCorreoYLogoutBorraSesion() = runTest(dispatcher) {
        val prefs = MemoryPreferences()
        val vm = vm(prefs)
        credentials(vm)
        vm.login(); advanceUntilIdle()
        assertEquals("admin@guardian.test", prefs.email)
        vm.logout()
        assertNull(vm.uiState.value.usuario)
        assertEquals("", vm.uiState.value.password)
        val nuevaInstancia = vm(prefs)
        assertEquals("admin@guardian.test", nuevaInstancia.uiState.value.email)
        assertNull(nuevaInstancia.uiState.value.usuario)
        assertEquals("", nuevaInstancia.uiState.value.password)
    }

    @Test fun noRecordarEliminaCorreo() = runTest(dispatcher) {
        val prefs = MemoryPreferences("admin@guardian.test")
        val vm = vm(prefs)
        vm.onRecordarCorreoChange(false)
        assertEquals("", prefs.email)
        credentials(vm)
        vm.login(); advanceUntilIdle()
        vm.logout()
        assertEquals("", vm.uiState.value.email)
        assertEquals("", prefs.email)
    }

    @Test fun datosLocalesCorruptosNoBloqueanLogin() = runTest(dispatcher) {
        val vm = vm(MemoryPreferences(corrupt = true))
        assertNotNull(vm.uiState.value.aviso)
        credentials(vm)
        vm.login(); advanceUntilIdle()
        assertEquals(Rol.Admin, vm.uiState.value.usuario?.rol)
    }

    @Test fun falloAlGuardarNoPierdeSesion() = runTest(dispatcher) {
        val vm = vm(object : LoginPreferences {
            override fun leerCorreo() = ""
            override fun guardarCorreo(email: String) { error("Almacenamiento no disponible") }
        })
        credentials(vm)
        vm.login(); advanceUntilIdle()
        assertEquals(Rol.Admin, vm.uiState.value.usuario?.rol)
        assertNotNull(vm.uiState.value.aviso)
    }

    @Test fun dobleToqueNoGeneraOtraSolicitudNiModificaCredenciales() = runTest(dispatcher) {
        val vm = vm()
        credentials(vm)
        vm.login(); vm.login()
        vm.onEmailChange("operador@guardian.test")
        advanceUntilIdle()
        assertEquals(Rol.Admin, vm.uiState.value.usuario?.rol)
        assertNull(vm.uiState.value.errorGeneral)
    }

    @Test fun permisosDiferenciadosYFormatoCorreo() {
        assertTrue(Rol.Admin.puedeAbrirGuia("guia11"))
        assertTrue(Rol.Supervisor.puedeAbrirGuia("guia12"))
        assertFalse(Rol.Supervisor.puedeAbrirGuia("guia11"))
        assertTrue(Rol.Operador.puedeAbrirGuia("guia13"))
        assertFalse(Rol.Operador.puedeAbrirGuia("guia12"))
        assertFalse(Rol.Admin.puedeAbrirGuia("ruta_desconocida"))
        assertTrue(LoginValidator.emailValido("operador@guardian.test"))
        assertFalse(LoginValidator.emailValido("a@@guardian.test"))
        assertFalse(LoginValidator.emailValido("a@"))
        assertFalse(LoginValidator.emailValido(""))
    }
}
