# Login agregado sobre el proyecto original

Se recuperó el ZIP original de Maximiliano Fuentes y Gonzalo Sandoval y se conservó su estructura. No se movió, renombró ni eliminó ningún archivo fuente original. Se mantuvieron el menú, AppIntegrada, MainViewModel, NavigationEvent, las cinco guías y sus rutas.

## Abrir

Descomprimir y abrir la carpeta `AppModoGuardian_GrupoMaximilianoFuentesGonzaloSandoval` en Android Studio. Seleccionar JDK 17, sincronizar Gradle y ejecutar `app` (Android API 24+). El SDK de compilación sigue siendo 35.

El nuevo flujo es Login → menú original → guías originales. Cerrar sesión limpia la pila de navegación. Home y el menú observan la misma instancia de LoginViewModel, sin argumentos de ruta para correo o rol.

## Usuarios ficticios

| Cuenta | Contraseña | Acceso al menú original |
| --- | --- | --- |
| admin@guardian.test | 123456 | Guías 9, 10, 11, 12 y 13 |
| supervisor@guardian.test | 123456 | Guías 9, 10, 12 y 13 |
| operador@guardian.test | 123456 | Guías 9 y 13 |

El Word solicita roles diferenciados sin indicar permisos específicos. Se definió esta distribución sobre las guías existentes: Admin accede al formulario; Supervisor puede activar el modo; Operador consulta la vista adaptable y usa su perfil.

El login usa un repositorio local con los tres usuarios requeridos. No necesita backend ni credenciales reales. El botón «Iniciar sesión» permanece activo aunque los campos estén vacíos o incorrectos. Los errores de correo y contraseña se muestran al pulsarlo; al editar un campo se limpia su error y se vuelve a validar en el próximo envío. Durante la carga, el botón sigue activo y el ViewModel ignora los toques repetidos para evitar solicitudes duplicadas. Oculta la contraseña, muestra carga y permite demostrar desconexión y reintento con un interruptor de prueba. Solo se puede recordar el correo: contraseña, rol y sesión permanecen en memoria.

## Archivos añadidos

- `ui/screens/LoginScreen.kt` y `res/drawable/logo_guardian.xml`.
- `viewmodel/LoginViewModel.kt`.
- `model/LoginUiState.kt`, `LoginErrores.kt`, `LoginValidator.kt`, `UsuarioAutenticado.kt` y `Rol.kt`.
- `repository/LoginRepository.kt`, `DemoLoginRepository.kt` y `LoginPreferences.kt`.
- Pruebas de lógica en `app/src/test/.../login/LoginTest.kt`.

## Integración en los archivos existentes

- MainActivity crea una sesión compartida y recoge eventos del MainViewModel original.
- AppIntegrada añade la ruta Login antes del menú y protege las guías por rol.
- MenuPrincipal mantiene sus botones originales; muestra correo/rol y añade cerrar sesión.
- HomeScreen conserva sus tres layouts originales y añade la observación del rol.
- Screen añade las rutas Login, menú y guías, manteniendo todas las anteriores.
- WindowSizeUtils mantiene nombre y función, y permite usarlos en Previews sin convertir el contexto a Activity.
- Se agregó únicamente la dependencia de las pruebas de coroutines. Se conservaron las versiones y la organización Gradle del proyecto original.

Los demás archivos fuente originales permanecen idénticos. Se incluyen en `CAMBIOS_LOGIN.md` la comparación con el ZIP original y en `VERIFICACION_LOGIN.md` los resultados de compilación/pruebas.

## Evaluación

El equipo debe completar GitHub privado, permisos del docente y compañero, Trello, evidencia en Device Manager y entrega AVA. Estos puntos externos no se presentan como realizados.

El ZIP omite cachés, `.git`, `.idea`, `local.properties`, compilaciones antiguas y el ZIP antiguo anidado. Conserva todos los archivos fuente originales y el wrapper Gradle.
