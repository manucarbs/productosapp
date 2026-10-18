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

## Usuario de prueba

- Correo: `mmartinez@temp.com`
- Contraseña: `Temp123.` (con el punto final)

También se puede crear una cuenta nueva desde la pantalla "Crear cuenta".

## Cómo ejecutarla

1. Iniciar la API y verificar en el navegador del celular que responde:
   `http://TU_IP:8085/api/v1/demoapirestdam235/products`
   (debe mostrar "No autorizado: Token no es el correcto o no proporcionado").
2. Abrir esta carpeta en Android Studio y esperar la sincronización de Gradle.
3. Cambiar `BASE_URL` (ver arriba).
4. Seleccionar el dispositivo y pulsar Run.
5. Iniciar sesión con el usuario de prueba.

#
