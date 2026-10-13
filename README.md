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
Communication
        |
        v
Business Logic
        |
        v
Base de datos
        |
        | GET /api/medicion
        v
Frontend Business Logic
        |
        v
Interfaz Web
```

Durante este Sprint se utiliza una medida ficticia de ozono para comprobar que todos los componentes se comunican correctamente.

---

## Componentes

El proyecto está dividido en los siguientes componentes:

```text
src/
├── microprocesador/
├── android/
├── database/
├── business_logic/
├── communication/
├── frontend_business_logic/
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

El campo `Major` contiene el tipo de medición y un contador.

El campo `Minor` contiene el valor de la medición en ppb.

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

evita mediciones duplicadas

convierte el valor si es necesario

envía la medición al backend
```

La aplicación mantiene separadas las responsabilidades principales:

```text
MainActivity
    |
    v
TramaIBeacon / Utilidades
    |
    v
LogicaFake
    |
    v
PeticionarioREST
```

`MainActivity` coordina BLE y el flujo general.

`LogicaFake` actúa como proxy de la lógica de negocio.

`PeticionarioREST` encapsula la comunicación HTTP.

La respuesta del backend se representa mediante:

```text
Medicion
```

con:

```text
id
fecha
tipo
valor
```

---

### Communication

Implementado en PHP.

Este componente representa la capa de comunicación del backend.

Proporciona las rutas:

```text
POST /api/medicion

GET /api/medicion
```

`POST /api/medicion` recibe una nueva medición y delega su almacenamiento en `business_logic`.

`GET /api/medicion` solicita a `business_logic` la última medición almacenada.

La capa `communication` gestiona HTTP, rutas, JSON y códigos de estado.

No contiene lógica de negocio.

---

### Business Logic

Implementado en PHP mediante la clase:

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

Este componente no conoce detalles de HTTP.

El flujo del backend es:

```text
Communication
      |
      v
Business Logic
      |
      v
Database
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

### Frontend Business Logic

Implementado en JavaScript.

Este componente actúa como proxy o lógica fake para la interfaz web.

Proporciona:

```text
leerMedicion()
```

Internamente utiliza:

```text
GET /api/medicion
```

La interfaz web no realiza directamente peticiones HTTP.

El flujo es:

```text
Web
 |
 v
Frontend Business Logic
 |
 v
Communication
 |
 v
Business Logic
 |
 v
Database
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

La web utiliza:

```text
frontend_business_logic.leerMedicion()
```

para obtener los datos.

La interfaz muestra:

```text
tipo

valor

fecha

hora

momento de la última actualización
```

La GUI no contiene llamadas directas a `fetch()`.

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
|   |
|   ├── prompts/
|   |   ├── android_prompt.md
|   |   ├── database_prompt.md
|   |   ├── logica_prompt.md
|   |   ├── microprocesador_prompt.md
|   |   ├── rest_prompt.md
|   |   ├── ux_prompt.md
|   |   └── web_prompt.md
|   |
|   ├── android_design.md
|   ├── business_logic_design.md
|   ├── communication_design.md
|   ├── database_design.md
|   ├── frontend_business_logic_design.md
|   ├── microprocesador_design.md
|   └── web_design.md
|
└── src/
    |
    ├── android/
    |
    ├── business_logic/
    |   ├── LogicaNegocio.php
    |   └── LogicaNegocioTest.php
    |
    ├── communication/
    |   ├── index.php
    |   └── ServidorRestTest.php
    |
    ├── database/
    |   └── database.sql
    |
    ├── frontend_business_logic/
    |   ├── LogicaFake.js
    |   └── tests/
    |       ├── LogicaFakeTest.html
    |       └── LogicaFakeTest.js
    |
    ├── microprocesador/
    |
    └── web/
        ├── index.html
        |
        ├── css/
        |   └── estilos.css
        |
        └── js/
            └── app.js
```

---

## Documentación de diseño

La carpeta:

```text
doc/
```

contiene el diseño de cada componente.

Existe correspondencia entre los diseños y las implementaciones:

```text
doc/business_logic_design.md
        |
        v
src/business_logic/
```

```text
doc/communication_design.md
        |
        v
src/communication/
```

```text
doc/frontend_business_logic_design.md
        |
        v
src/frontend_business_logic/
```

```text
doc/web_design.md
        |
        v
src/web/
```

Los diseños incluyen:

```text
Diseño del Componente

Aclaraciones del Diseño

Reglas Generales
```

---

## Arquitectura en capas

El backend mantiene el flujo:

```text
Communication
      |
      v
Business Logic
      |
      v
Database
```

La capa `business_logic` no contiene lógica HTTP.

La capa `communication` se encarga de recibir las peticiones externas y delegarlas en la lógica de negocio.

En el frontend web se mantiene:

```text
Web
 |
 v
Frontend Business Logic
 |
 v
Communication
```

La interfaz gráfica no realiza directamente llamadas HTTP.

---

## Uso de Inteligencia Artificial

Durante el desarrollo del Sprint 0 se ha utilizado Inteligencia Artificial siguiendo un proceso basado en diseño previo y especificaciones.

Para cada componente se ha seguido el flujo:

```text
Diseño previo
    |
    v
Prompt detallado
    |
    v
Generación o adaptación mediante IA
    |
    v
Revisión manual
    |
    v
Tests
    |
    v
Corrección y validación
```

Los diseños utilizados como especificación se encuentran en:

```text
doc/
```

Los prompts utilizados para solicitar a la IA la generación o adaptación de los componentes se encuentran en:

```text
doc/prompts/
```

Actualmente se incluyen prompts para:

```text
base de datos

lógica de negocio

servidor REST / comunicación

microprocesador

Android

lógica fake del navegador

UX web
```

La IA se ha utilizado como herramienta de apoyo.

La arquitectura, las restricciones, la revisión del resultado y la aceptación final se han comprobado manualmente.

Entre las revisiones realizadas durante el Sprint se encuentran:

```text
separación entre communication y business_logic

separación entre GUI y frontend_business_logic

codificación de Major y Minor

control de mediciones BLE duplicadas

conversión de ppb a ppm

generación correcta de fecha y hora

pruebas automáticas

correspondencia entre diseño e implementación
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

La aplicación necesita los permisos Bluetooth correspondientes a la versión de Android utilizada.

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

### Backend

La capa de comunicación se encuentra en:

```text
src/communication/
```

La lógica de negocio se encuentra en:

```text
src/business_logic/
```

En el despliegue real del Sprint 0 el backend está alojado en Plesk.

El endpoint utilizado es:

```text
https://fuseriv.upv.edu.es/api/medicion
```

Las credenciales reales de la base de datos no se almacenan en el repositorio público.

En Plesk se utiliza un archivo `.htaccess` para redirigir las rutas del API hacia `index.php`.

---

### Interfaz Web

La GUI se encuentra en:

```text
src/web/
```

La lógica fake utilizada por la GUI se encuentra en:

```text
src/frontend_business_logic/
```

La web se despliega en el servidor web de Plesk.

---

## Pruebas automáticas

Los tests están separados del funcionamiento normal del sistema.

No se ejecutan automáticamente al:

```text
encender el microprocesador

abrir la aplicación Android

utilizar el servidor

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

### Tests de Business Logic

Archivo:

```text
src/business_logic/LogicaNegocioTest.php
```

Ejecutar desde:

```text
src/business_logic/
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

### Tests de Communication

Archivo:

```text
src/communication/ServidorRestTest.php
```

Ejecutar desde:

```text
src/communication/
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

El resultado esperado es:

```text
4 tests passed
BUILD SUCCESSFUL
```

También pueden ejecutarse desde:

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

### Tests de Frontend Business Logic

Los tests se encuentran en:

```text
src/frontend_business_logic/tests/
```

Archivo principal:

```text
LogicaFakeTest.html
```

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
POST
        |
        v
Communication
        |
        v
Business Logic
        |
        v
Base de datos
        |
        v
GET
        |
        v
Frontend Business Logic
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
