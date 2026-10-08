# Cambios sobre el proyecto original

Se conservaron los 31 archivos Kotlin originales en sus mismas rutas, sin mover, renombrar ni eliminar ninguno. 25 permanecen idénticos byte por byte; seis se modificaron para integrar Login y permitir sus Previews.

Se conservan MainViewModel, NavigationEvent, el menú y las cinco guías originales. Las tres vistas de Home, registro/resumen, estado/animaciones y cámara/galería mantienen sus implementaciones originales.

## Archivos existentes modificados

- `app/build.gradle.kts`
- `app/src/main/java/com/example/modoguardian_grupomg/MainActivity.kt`
- `app/src/main/java/navigation/AppIntegrada.kt`
- `app/src/main/java/navigation/Screen.kt`
- `app/src/main/java/ui/screens/HomeScreen.kt`
- `app/src/main/java/ui/screens/MenuPrincipal.kt`
- `app/src/main/java/ui/utils/WindowSizeUtils.kt`

## Archivos añadidos

- `LOGIN_LEEME.md`
- `app/src/main/java/model/LoginErrores.kt`
- `app/src/main/java/model/LoginUiState.kt`
- `app/src/main/java/model/LoginValidator.kt`
- `app/src/main/java/model/Rol.kt`
- `app/src/main/java/model/UsuarioAutenticado.kt`
- `app/src/main/java/repository/DemoLoginRepository.kt`
- `app/src/main/java/repository/LoginPreferences.kt`
- `app/src/main/java/repository/LoginRepository.kt`
- `app/src/main/java/ui/screens/LoginScreen.kt`
- `app/src/main/java/viewmodel/LoginViewModel.kt`
- `app/src/main/res/drawable/logo_guardian.xml`
- `app/src/test/java/com/example/modoguardian_grupomg/login/LoginTest.kt`
