# Sprint 0 - Proyecto de Biometría y Medio Ambiente

## Descripción

Este repositorio contiene el desarrollo del Sprint 0 del proyecto de Biometría y Medio Ambiente.

El objetivo del Sprint 0 es comprobar el funcionamiento completo de la arquitectura del sistema mediante una medida ficticia generada en el microprocesador.

El flujo completo es:

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon
        v
Aplicación Android
        |
        | POST /api/medicion
        v
Servidor REST
        |
        v
LogicaNegocio
        |
        v
Base de datos
        |
        | GET /api/medicion
        v
Interfaz Web
```

Durante este Sprint se utiliza una medida ficticia de ozono para comprobar que todos los componentes se comunican correctamente.

---

## Componentes

El proyecto está dividido en los siguientes componentes:

```text
src/
|
├── microprocesador/
|
├── android/
|
├── database/
|
├── logica/
|
├── rest/
|
└── web/
```

### Microprocesador

Implementado en C++ para la placa:

```text
SparkFun Pro nRF52840 Mini
```

El microprocesador genera una medida ficticia de O3 y la transmite mediante BLE utilizando una trama iBeacon.

Durante el Sprint 0 se utiliza como valor de prueba:

```text
1234 ppb
```

que corresponde a:

```text
1.234 ppm
```

para su visualización.

---

### Aplicación Android

Implementada en Java con Android Studio.

La aplicación:

```text
escanea dispositivos BLE

detecta Fabian_GTI

interpreta la trama iBeacon

obtiene Major y Minor

interpreta el tipo y contador

convierte el valor si es necesario

envía la medición al servidor REST
```

La comunicación con el servidor se realiza mediante una lógica fake:

```text
LogicaFake.guardarMedicion()
```

---

### Servidor REST

Implementado en PHP.

Proporciona las rutas:

```text
POST /api/medicion

GET /api/medicion
```

`POST /api/medicion` permite almacenar una nueva medición.

`GET /api/medicion` permite recuperar la última medición almacenada.

---

### Lógica de negocio

Implementada en PHP mediante la clase:

```text
LogicaNegocio
```

Operaciones principales:

```text
guardarMedicion()

leerMedicion()
```

La lógica:

```text
valida las mediciones

genera la fecha y hora

almacena los datos

recupera la última medición
```

---

### Base de datos

Se utiliza una base de datos MySQL con una única tabla:

```text
Mediciones
```

Estructura:

```text
id       INT          PRIMARY KEY AUTO_INCREMENT

fecha    DATETIME     NOT NULL

tipo     VARCHAR(50)  NOT NULL

valor    DOUBLE       NOT NULL
```

---

### Interfaz Web

Implementada con:

```text
HTML

CSS

JavaScript
```

La interfaz muestra la última medición almacenada.

La comunicación se realiza mediante una lógica fake:

```text
LogicaFake.leerMedicion()
```

que utiliza:

```text
GET /api/medicion
```

La interfaz muestra:

```text
tipo

valor

fecha

hora

momento de la última actualización
```

---

## Estructura del repositorio

```text
Sprint 0-Biometria/
|
├── README.md
├── author.md
├── .gitignore
├── .gitattributes
|
├── doc/
|   ├── android_design.md
|   ├── database_design.md
|   ├── logica_design.md
|   ├── microprocesador_design.md
|   ├── rest_design.md
|   └── web_design.md
|
└── src/
    |
    ├── android/
    |
    ├── database/
    |   └── database.sql
    |
    ├── logica/
    |   ├── LogicaNegocio.php
    |   └── LogicaNegocioTest.php
    |
    ├── microprocesador/
    |
    ├── rest/
    |   ├── index.php
    |   └── ServidorRestTest.php
    |
    └── web/
        ├── index.html
        |
        ├── css/
        |   └── estilos.css
        |
        ├── js/
        |   ├── app.js
        |   └── LogicaFake.js
        |
        └── tests/
            ├── LogicaFakeTest.html
            └── LogicaFakeTest.js
```

---

## Documentación de diseño

La carpeta:

```text
doc/
```

contiene el diseño de cada componente.

Existe una correspondencia entre:

```text
doc/xxx_design.md
```

y:

```text
src/xxx/
```

Los diseños incluyen:

```text
Diseño del Componente

Aclaraciones del Diseño

Reglas Generales
```

---

## Despliegue

### Microprocesador

El código del microprocesador se compila y carga mediante Arduino IDE sobre:

```text
SparkFun Pro nRF52840 Mini
```

Se utiliza la librería:

```text
Adafruit Bluefruit nRF52
```

La placa transmite una trama iBeacon mediante BLE.

---

### Android

Abrir:

```text
src/android/
```

con Android Studio.

Después:

```text
sincronizar Gradle

compilar el proyecto

ejecutar la aplicación en un dispositivo Android compatible con BLE
```

La aplicación necesita permisos Bluetooth y localización según la versión de Android.

---

### Base de datos

Ejecutar:

```text
src/database/database.sql
```

sobre una base de datos MySQL.

Este archivo crea la tabla:

```text
Mediciones
```

---

### Servidor REST

El servidor REST se encuentra en:

```text
src/rest/
```

En el despliegue real del Sprint 0 está alojado en Plesk.

El endpoint utilizado es:

```text
https://fuseriv.upv.edu.es/api/medicion
```

Las credenciales reales de la base de datos no se almacenan en el repositorio público.

En Plesk se utiliza también un archivo `.htaccess` para redirigir las rutas del API hacia `index.php`.

---

### Interfaz Web

La web se encuentra en:

```text
src/web/
```

y se despliega en el servidor web de Plesk.

La interfaz utiliza:

```text
GET /api/medicion
```

para recuperar la última medición.

---

## Pruebas automáticas

Los tests están separados del funcionamiento normal del sistema.

No se ejecutan automáticamente al:

```text
encender el microprocesador

abrir la aplicación Android

utilizar el servidor REST

abrir la web
```

Se ejecutan únicamente cuando se lanzan manualmente.

---

### Test del microprocesador

El microprocesador incluye una comprobación en tiempo de compilación mediante:

```cpp
static_assert
```

para verificar la codificación del campo `Major`.

Ejemplo comprobado:

```text
tipo O3 = 11

contador = 5

Major = 2821
```

---

### Tests de lógica de negocio

Archivo:

```text
src/logica/LogicaNegocioTest.php
```

Ejecutar desde:

```text
src/logica/
```

con:

```bash
php LogicaNegocioTest.php
```

Se comprueba:

```text
guardar una medición válida

rechazar un tipo vacío

rechazar un valor no numérico

recuperar la última medición

devolver null si no existen mediciones
```

Resultado esperado:

```text
TODOS LOS TESTS HAN PASADO
```

---

### Tests del servidor REST

Archivo:

```text
src/rest/ServidorRestTest.php
```

Ejecutar desde:

```text
src/rest/
```

con:

```bash
php ServidorRestTest.php
```

Se comprueba:

```text
POST correcto -> HTTP 201

POST con tipo vacío -> HTTP 400

POST sin tipo -> HTTP 400

POST sin valor -> HTTP 400

POST con JSON inválido -> HTTP 400

GET correcto -> HTTP 200

respuesta GET en JSON válido
```

Este test realiza peticiones reales al servidor desplegado.

---

### Tests Android

Los tests locales se encuentran en:

```text
src/android/app/src/test/
```

El test principal es:

```text
UtilidadesTest.java
```

Se comprueba:

```text
conversión de ppb a ppm

conversión de bytes a entero

conversión de bytes a hexadecimal

conversión de bytes a texto
```

Se pueden ejecutar desde Android Studio con:

```text
Run 'UtilidadesTest'
```

o desde:

```text
src/android/
```

mediante:

```bash
gradlew test
```

En Windows:

```bash
gradlew.bat test
```

---

### Tests Web

Los tests se encuentran en:

```text
src/web/tests/
```

Archivo principal:

```text
LogicaFakeTest.html
```

Para ejecutarlos se abre este archivo mediante un servidor local, por ejemplo Live Server.

Se comprueba:

```text
leer una medición correcta

utilizar /api/medicion

aceptar una respuesta null

detectar errores HTTP
```

Resultado esperado:

```text
TODOS LOS TESTS HAN PASADO
```

---

## Test de funcionamiento completo

El criterio principal del Sprint 0 consiste en comprobar el recorrido completo de una medida ficticia:

```text
Medida ficticia en SparkFun
        |
        v
BLE / iBeacon
        |
        v
Aplicación Android
        |
        v
POST REST
        |
        v
LogicaNegocio
        |
        v
Base de datos
        |
        v
GET REST
        |
        v
Interfaz Web
```

La medida utilizada durante las pruebas es:

```text
1234 ppb
```

equivalente a:

```text
1.234 ppm
```

La medición enviada desde el microprocesador puede visualizarse finalmente en la interfaz web.

Este recorrido completo ha sido comprobado durante el Sprint 0.

---

## Ramas Git

El repositorio utiliza:

```text
master

develop
```

`develop` se utiliza durante el desarrollo del Sprint.

`master` contiene la versión estable del proyecto.

---

## Autor

El autor del proyecto se encuentra indicado en:

```text
author.md
```
