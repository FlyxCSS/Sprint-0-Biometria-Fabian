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
obtiene el nombre real anunciado por BLE
interpreta la trama iBeacon
obtiene Major y Minor
interpreta el tipo y contador
evita mediciones duplicadas
convierte el valor si es necesario
envía nombre_micro, tipo y valor al backend
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
nombreMicro
fechaHora
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

La petición incluye:

```text
nombre_micro
tipo
valor
```

La respuesta incluye:

```text
nombre_micro
tipo
valor
fecha_hora
```
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
nombreMicro: Text, tipo: Text, valor: R
        --> guardarMedicion() --> Medicion
leerMedicion() --> Medicion | Nulo
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
nombre_micro VARCHAR(100) NOT NULL
tipo         VARCHAR(50)  NOT NULL
valor        DOUBLE       NOT NULL
fecha_hora   DATETIME     NOT NULL
PRIMARY KEY (nombre_micro, fecha_hora)
```

La clave primaria es compuesta. Dos microprocesadores distintos pueden almacenar una medición con la misma `fecha_hora`.

La última medición se recupera de forma determinista mediante:

```text
ORDER BY fecha_hora DESC, nombre_micro ASC
LIMIT 1
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
micro
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
|   |
|   ├── prompts/
|   |   ├── android_prompt.md
|   |   ├── database_prompt.md
|   |   ├── logica_prompt.md
|   |   ├── microprocesador_prompt.md
|   |   ├── rest_prompt.md
|   |   ├── ux_prompt.md
|   |   └── web_prompt.md
|   |
|   ├── android_design.md
|   ├── business_logic_design.md
|   ├── communication_design.md
|   ├── database_design.md
|   ├── frontend_business_logic_design.md
|   ├── microprocesador_design.md
|   └── web_design.md
|
└── src/
    |
    ├── android/
    |
    ├── business_logic/
    |   ├── LogicaNegocio.php
    |   └── LogicaNegocioTest.php
    |
    ├── communication/
    |   ├── index.php
    |   └── ServidorRestTest.php
    |
    ├── database/
    |   └── database.sql
    |
    ├── frontend_business_logic/
    |   ├── LogicaFake.js
    |   └── tests/
    |       ├── LogicaFakeTest.html
    |       └── LogicaFakeTest.js
    |
    ├── microprocesador/
    |
    └── web/
        ├── index.html
        |
        ├── css/
        |   └── estilos.css
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
La interfaz principal está disponible directamente en:
```text
https://fuseriv.upv.edu.es
```
En el repositorio, la GUI continúa manteniéndose separada dentro de `src/web/`.

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
rechazar un nombre_micro vacío
rechazar un tipo vacío
rechazar un valor no numérico
recuperar la última medición
permitir la misma fecha_hora para micros distintos
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
POST devuelve la medición correcta
POST sin nombre_micro -> HTTP 400
POST con nombre_micro vacío -> HTTP 400
POST sin tipo -> HTTP 400
POST con tipo vacío -> HTTP 400
POST sin valor -> HTTP 400
POST con valor no numérico -> HTTP 400
POST con JSON inválido -> HTTP 400
GET correcto -> HTTP 200
respuesta GET con la estructura correcta
```
Este test realiza peticiones reales al servidor desplegado.

---

### Tests Android
Los tests locales se encuentran en:
```text
src/android/app/src/test/
```
Los tests principales son:
```text
UtilidadesTest.java
MedicionTest.java
```
Se comprueba:
```text
conversión de ppb a ppm
conversión de bytes a entero
conversión de bytes a hexadecimal
conversión de bytes a texto
almacenamiento correcto de nombreMicro, fechaHora, tipo y valor
```
Se pueden ejecutar desde Android Studio con:
```text
Run 'UtilidadesTest'
```
El resultado esperado es:
```text
UtilidadesTest: 4 tests passed
MedicionTest: 1 test passed
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
comprobar nombre_micro, tipo, valor y fecha_hora
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
Durante la prueba se ha comprobado que se almacenan y muestran correctamente:
```text
nombre_micro = Fabian_GTI
tipo = O3
valor = 1.234
fecha_hora = fecha y hora generadas por el backend
```
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


---

## Guía de ejecución y pruebas (auditoría final)

> **Importante:** los tests del servidor REST usan la URL desplegada y hacen un POST real.
> Ejecútalos solo sobre un entorno de pruebas o una base de datos donde aceptes insertar una medida.

| Parte | Cómo ejecutar | Qué verifica |
|---|---|---|
| Lógica PHP | `cd src/business_logic` y `php LogicaNegocioTest.php` | Guardado, validación, lectura y BD temporal SQLite |
| REST PHP | `cd src/communication` y `php ServidorRestTest.php` | POST/GET sobre el servidor real; puede escribir en la BD |
| Android | Android Studio: abrir `src/android`, ejecutar los tests de `app/src/test` | `MedicionTest`, `UtilidadesTest`, `TramaIBeaconTest` |
| Android consola | Desde `src/android`: `gradlew.bat testDebugUnitTest` (Windows) | Pruebas unitarias locales con Gradle |
| Proxy web | Abrir `src/frontend_business_logic/tests/LogicaFakeTest.html` en el navegador | HTTP simulado, errores, respuesta vacía |
| Interfaz web | Abrir `src/web/tests/AppTest.html` en el navegador | Mostrar datos, ausencia, error y actualización |
| Microprocesador | Compilar `src/microprocesador/microprocesador.ino` con Arduino IDE y Bluefruit | `static_assert` verifica la codificación de Major durante compilación |

El test de lógica PHP requiere la extensión `pdo_sqlite` habilitada en el PHP de consola; el servidor desplegado utiliza MySQL.
El test REST requiere conexión de red y no se ha ejecutado en este paquete para evitar insertar registros en producción.

Si el navegador bloquea archivos locales, sirve la carpeta `src` con `python -m http.server 8000`
y visita `http://localhost:8000/web/tests/AppTest.html` o
`http://localhost:8000/frontend_business_logic/tests/LogicaFakeTest.html`.

### Criterio de aceptación del Sprint 0

1. Compilar y cargar `microprocesador.ino` en la SparkFun nRF52840 Mini.
2. Comprobar la emisión `Fabian_GTI` de una medida ficticia de O3, `Minor=1234` ppb.
3. Iniciar la app Android en un teléfono real con BLE, conceder permisos y buscar el beacon.
4. Verificar en Logcat que la app obtiene Major/Minor y envía una petición POST a `/api/medicion`.
5. Comprobar que el servidor devuelve la medición y crea una fila con fecha/hora en `Mediciones`.
6. Abrir la web desplegada; comprobar que aparecen `Fabian_GTI`, `O3` y `1.234` ppm.
7. Pulsar «Actualizar» y verificar que la web obtiene la última medición mediante `LogicaFake`.

Los tests automáticos no sustituyen a esta prueba integrada con el hardware y servidor reales.
Las credenciales de Plesk se configuran solo en el despliegue: el repositorio contiene marcadores `DB_HOST`, etc.
El ZIP de entrega excluye deliberadamente `.git/`, archivos locales y salidas de compilación.
Conserva la carpeta `.git/` de tu proyecto original si deseas mantener su historial y ramas.
