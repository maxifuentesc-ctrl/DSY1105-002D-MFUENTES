package com.example.modoguardian_grupomg.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.modoguardian_grupomg.R
import com.example.modoguardian_grupomg.model.LoginUiState
import com.example.modoguardian_grupomg.ui.theme.ModoGuardian_GrupoMGTheme
import com.example.modoguardian_grupomg.ui.utils.obtenerWindowSizeClass
import com.example.modoguardian_grupomg.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsState()
    LoginContent(state, viewModel::onEmailChange, viewModel::onPasswordChange,
        viewModel::onRecordarCorreoChange, viewModel::onSimularSinConexionChange, viewModel::login)
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRecordarCorreoChange: (Boolean) -> Unit = {},
    onSimularSinConexionChange: (Boolean) -> Unit = {},
    onLogin: () -> Unit = {}
) {
    val compact = obtenerWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact
    // El contenido se desplaza con el teclado y en móviles apaisados de poca altura.
    Scaffold { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding).imePadding()
            .verticalScroll(rememberScrollState()).padding(if (compact) 20.dp else 32.dp),
            contentAlignment = Alignment.Center) {
            if (compact) {
                Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    GuardianBrand()
                    LoginForm(state, onEmailChange, onPasswordChange, onRecordarCorreoChange,
                        onSimularSinConexionChange, onLogin)
                }
            } else {
                Card(Modifier.widthIn(max = 1040.dp).fillMaxWidth()) {
                    Row(Modifier.padding(32.dp), horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { GuardianBrand() }
                        Column(Modifier.weight(1.2f)) {
                            LoginForm(state, onEmailChange, onPasswordChange, onRecordarCorreoChange,
                                onSimularSinConexionChange, onLogin)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuardianBrand() {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.logo_guardian), "Logo de Modo Guardián", Modifier.size(96.dp))
        Text("Modo Guardián", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Text("Acceso seguro según tu rol", style = MaterialTheme.typography.titleMedium)
        Text("Demostración académica · Solo datos ficticios", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun LoginForm(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRecordarCorreoChange: (Boolean) -> Unit,
    onSimularSinConexionChange: (Boolean) -> Unit,
    onLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val submit = { focusManager.clearFocus(); onLogin() }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)
        // Los errores se muestran después de enviar; contraseña siempre oculta.
        OutlinedTextField(state.email, onEmailChange, modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo electrónico") }, singleLine = true, enabled = !state.isLoading,
            isError = state.errores.email != null,
            supportingText = { state.errores.email?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next))
        OutlinedTextField(state.password, onPasswordChange, modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") }, singleLine = true, enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(), isError = state.errores.password != null,
            supportingText = { state.errores.password?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (state.isLoginEnabled) submit() }))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(state.recordarCorreo, onRecordarCorreoChange, enabled = !state.isLoading)
            Text("Recordar solo mi correo", style = MaterialTheme.typography.bodyMedium)
        }
        state.errorGeneral?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
        state.aviso?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        // El botón permanece activo. El ViewModel evita solicitudes duplicadas en carga.
        Button(submit, enabled = state.isLoginEnabled, modifier = Modifier.fillMaxWidth()) {
            if (state.isLoading) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(10.dp))
                Text("Ingresando…")
            } else Text("Iniciar sesión")
        }
        HorizontalDivider()
        Text("Usuarios de prueba", style = MaterialTheme.typography.titleSmall)
        Text("Admin: admin@guardian.test\nSupervisor: supervisor@guardian.test\nOperador: operador@guardian.test",
            style = MaterialTheme.typography.bodySmall)
        Text("Contraseña para los tres: 123456", style = MaterialTheme.typography.bodySmall)
        // Permite demostrar el error y reintento sin depender de un servidor externo.
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Switch(state.simularSinConexion, onSimularSinConexionChange, enabled = !state.isLoading)
            Text("Simular desconexión", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(name = "Compact · Celular", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Medium · Horizontal", widthDp = 700, heightDp = 400, showBackground = true)
@Preview(name = "Expanded · Tablet", widthDp = 1100, heightDp = 800, showBackground = true)
@Composable
private fun LoginPreview() { ModoGuardian_GrupoMGTheme { LoginContent(LoginUiState()) } }

@Preview(name = "Error de formulario", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun LoginErrorPreview() {
    ModoGuardian_GrupoMGTheme {
        LoginContent(LoginUiState(email = "correo incorrecto", password = "12",
            errores = com.example.modoguardian_grupomg.model.LoginValidator.validar("correo incorrecto", "12")))
    }
}
