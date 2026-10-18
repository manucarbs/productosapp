# Inventario (App Android)

App móvil de gestión de productos para la evaluación práctica de DAM235.
Consume la API REST `apirestdemo` (Spring Boot + JWT) con Retrofit y Gson.


## Requisitos

- Android Studio (proyecto Java, Gradle Kotlin DSL).
- Android 7.0 o superior (minSdk 24).
- La API `apirestdemo` corriendo (ver su README).
- Celular con depuración USB, o emulador.

## Lo que HAY QUE CAMBIAR antes de ejecutar

### 1. La IP de la API (obligatorio)

Archivo: `app/src/main/java/edu/sv/ues/dam235/productosapp/api/ApiClient.java`

```java
private static final String BASE_URL =
        "http://192.168.1.5:8085/api/v1/demoapirestdam235/";
```

Cambiar `192.168.1.5` según el caso:

| Dónde se prueba | IP a usar |
|-----------------|-----------|
| Celular real | IP local de la PC donde corre la API (`hostname -I` en Linux, `ipconfig` en Windows) |
| Emulador de Android Studio | `10.0.2.2` |

Reglas:
- La URL debe terminar con `/`.
- El celular y la PC deben estar en la **misma red WiFi**.
- Si la PC cambia de red, la IP cambia y hay que actualizarla.
- Si no conecta, abrir el puerto en el firewall: `sudo ufw allow 8085`.

### 2. La base de datos (se cambia en la API, no en la app)

La app no guarda datos: todo lo lee y escribe a través de la API. Por eso
depende de la base que use la API:

- **Por defecto la API usa H2 en memoria** (el servidor MariaDB de la cátedra
  `52.7.114.182` estaba apagado). Los datos se pierden al reiniciar la API:
  cuentas creadas, productos agregados y tokens revocados.
  Tras reiniciar, solo existe el usuario de prueba y 2 productos iniciales.
- Para usar MariaDB hay que cambiar el perfil en la API
  (`spring.profiles.active=mariadb` en `application.properties`).
  **La app no necesita ningún cambio** al cambiar de base de datos.

## Usuario de prueba

- Correo: `mmartinez@temp.com`
- Contraseña: `Temp123.` (con el punto final)

También se puede crear una cuenta nueva desde la pantalla "Crear cuenta".

## Funcionalidades

| Pantalla | Qué hace | Endpoint |
|----------|----------|----------|
| Crear cuenta | Registra un usuario (maneja 201, 409 y 400) | `POST /auth/register` |
| Inicio de sesión | Valida credenciales y recibe el token JWT | `POST /auth/login` |
| Mi inventario | Lista los productos | `GET /products` |
| + Agregar producto | Diálogo con nombre y estado | `POST /products` |
| Tocar un producto | Diálogo de edición | `PUT /products/{code}` |
| Eliminar (en el diálogo) | Pide confirmación | `DELETE /products/{code}` |
| Cerrar sesión | Revoca el token y vuelve al login | `POST /auth/logout` |

El token se envía en el header `Authorization: Bearer <token>`. Se pasa de
la pantalla de login a la de productos y no se guarda en el dispositivo:
al cerrar la app hay que iniciar sesión de nuevo.

## Cómo ejecutarla

1. Iniciar la API y verificar en el navegador del celular que responde:
   `http://TU_IP:8085/api/v1/demoapirestdam235/products`
   (debe mostrar "No autorizado: Token no es el correcto o no proporcionado").
2. Abrir esta carpeta en Android Studio y esperar la sincronización de Gradle.
3. Cambiar `BASE_URL` (ver arriba).
4. Seleccionar el dispositivo y pulsar Run.
5. Iniciar sesión con el usuario de prueba.

## Estructura

```
app/src/main/java/edu/sv/ues/dam235/productosapp/
├── MainActivity.java        Login
├── RegisterActivity.java    Crear cuenta
├── ProductsActivity.java    Lista, agregar, editar, eliminar, cerrar sesión
├── ProductAdapter.java      Adaptador del RecyclerView
├── api/
│   ├── ApiClient.java       Configuración de Retrofit (BASE_URL aquí)
│   └── ApiService.java      Definición de los endpoints
└── models/                  LoginRequest, TokenResponse, Product,
                             ProductRequest, RegisterRequest
```

#
