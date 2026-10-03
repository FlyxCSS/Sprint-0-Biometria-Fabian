# Diseño de Android

## Diseño del Componente

El componente `android` se ejecuta como una aplicación Java para Android.

Su función en el Sprint 0 es recibir mediante Bluetooth Low Energy la medición publicada por la SparkFun, interpretar la trama iBeacon y solicitar al servidor REST que almacene la medición.

La aplicación actúa como intermediaria entre el microprocesador y el servidor.

El flujo principal es:

```text
SparkFun
   |
   | BLE / iBeacon
   v
MainActivity
   |
   +--> TramaIBeacon
   |       |
   |       +--> UUID
   |       +--> Major
   |       +--> Minor
   |       +--> TxPower
   |
   +--> Utilidades
   |       |
   |       +--> interpretar bytes
   |       +--> convertir ppb a ppm
   |
   v
LogicaFake
   |
   | guardarMedicion()
   v
PeticionarioREST
   |
   | HTTP POST
   v
Servidor REST
```

### Tipos utilizados

```text
N     = número natural
Z     = número entero
R     = número real
B     = booleano
Text  = texto
Bytes = secuencia de bytes
```

---

### MainActivity

```text
--------------------------- MainActivity ---------------------------
|
| nombre_beacon: Text
| enviar_valor_en_ppm: B
| ultimo_contador_enviado: Z
| logica_fake: LogicaFake
|
B <-- tengoPermisosBluetooth() <--
|
| pedirPermisosBluetooth() -->
|
| inicializarBluetooth() -->
|
B <-- escanerPreparado() <--
|
| buscarTodosLosDispositivosBTLE() -->
|
| buscarNuestroDispositivoBTLE() -->
|
tipo: N --> obtenerNombreTipoMedicion() --> Text
|
resultado: ScanResult --> procesarResultadoBLE() -->
|
| detenerBusquedaDispositivosBTLE() -->
|
v: View --> botonBuscarTodosLosDispositivosBTLEPulsado() -->
|
v: View --> botonBuscarNuestroDispositivoBTLEPulsado() -->
|
v: View --> botonDetenerBusquedaDispositivosBTLEPulsado() -->
|
savedInstanceState: Bundle --> onCreate() -->
|
requestCode: N, permissions, grantResults
    --> onRequestPermissionsResult() -->
|
| onDestroy() -->
|
--------------------------------------------------------------------
```

Responsabilidad:

Gestiona la interacción principal de la aplicación Android.

Se encarga de:

```text
solicitar permisos BLE
inicializar Bluetooth
escanear dispositivos
filtrar Fabian_GTI
recibir tramas
interpretar Major y Minor
detectar nuevas mediciones
convertir la unidad cuando sea necesario
invocar guardarMedicion()
```

Dispone de dos modos de búsqueda:

```text
Buscar todos los dispositivos BLE
```

permite comprobar qué dispositivos se encuentran disponibles y verificar el funcionamiento general del escaneo.

```text
Buscar Fabian_GTI
```

utiliza un filtro por nombre para procesar únicamente el beacon del proyecto.

El nombre buscado se mantiene en:

```text
NOMBRE_BEACON = Fabian_GTI
```

por lo que puede modificarse desde un único punto.

---

### Interpretación de Major

El campo `Major` recibido contiene:

```text
+----------------+----------------+
| tipo medición  |    contador    |
|    1 byte      |     1 byte     |
+----------------+----------------+
```

Android recupera ambos valores mediante:

```text
tipo = (Major >> 8) & 0xFF

contador = Major & 0xFF
```

Los tipos conocidos actualmente son:

```text
11 --> O3
12 --> TEMPERATURA
```

En el Sprint 0 se utiliza `O3`.

El contador permite diferenciar nuevas mediciones de las repeticiones del mismo anuncio BLE.

Por ejemplo:

```text
contador = 5
contador = 5
contador = 5
```

representa una única medición.

Cuando llega:

```text
contador = 6
```

Android reconoce una nueva medición y la envía al servidor.

---

### Conversión de la medición

La SparkFun almacena directamente la medición de O3 en ppb dentro del campo `Minor`.

Ejemplo:

```text
Minor = 1234
```

representa:

```text
1234 ppb
```

Por defecto Android convierte este valor a ppm antes de enviarlo al servidor:

```text
ppm = ppb / 1000
```

Por ejemplo:

```text
1234 ppb
   |
   v
1.234 ppm
```

La configuración se controla mediante:

```text
ENVIAR_VALOR_EN_PPM
```

Si:

```text
ENVIAR_VALOR_EN_PPM = true
```

se envía:

```text
1.234
```

Si:

```text
ENVIAR_VALOR_EN_PPM = false
```

se envía directamente:

```text
1234
```

Esto permite cambiar fácilmente entre ppm y ppb sin modificar la comunicación BLE, la lógica fake ni el servidor REST.

---

### TramaIBeacon

```text
---------------- TramaIBeacon ----------------
|
| uuid: Bytes
| major: Bytes
| minor: Bytes
| tx_power: Z
|
bytes: Bytes --> TramaIBeacon() -->
|
Bytes <-- getUUID() <--
|
Bytes <-- getMajor() <--
|
Bytes <-- getMinor() <--
|
Z <-- getTxPower() <--
|
-----------------------------------------------
```

Responsabilidad:

Representa los campos utilizados de una trama iBeacon recibida mediante Bluetooth.

Extrae:

```text
UUID
Major
Minor
TxPower
```

a partir de los bytes recibidos por Android.

---

### Utilidades

```text
---------------- Utilidades ----------------
|
bytes: Bytes --> bytesToString() --> Text
|
bytes: Bytes --> bytesToIntOK() --> N
|
bytes: Bytes --> bytesToHexString() --> Text
|
ppb: N --> ppbAPpm() --> R
|
---------------------------------------------
```

Responsabilidad:

Agrupa operaciones auxiliares utilizadas para interpretar la información Bluetooth.

#### bytesToString()

```text
bytes: Bytes --> bytesToString() --> Text
```

Convierte una secuencia de bytes en texto.

Se utiliza principalmente para mostrar el UUID recibido.

#### bytesToIntOK()

```text
bytes: Bytes --> bytesToIntOK() --> N
```

Convierte hasta cuatro bytes sin signo, utilizando orden big-endian, en un valor entero.

Se utiliza para interpretar `Major` y `Minor`.

#### bytesToHexString()

```text
bytes: Bytes --> bytesToHexString() --> Text
```

Genera una representación hexadecimal de una secuencia de bytes para tareas de depuración.

#### ppbAPpm()

```text
ppb: N --> ppbAPpm() --> R
```

Convierte una concentración expresada en ppb a ppm:

```text
ppm = ppb / 1000
```

---

### LogicaFake

```text
---------------- LogicaFake ----------------
|
| url_medicion: Text
|
tipo: Text, valor: R --> guardarMedicion() -->
|
---------------------------------------------
```

Responsabilidad:

Representa en Android la operación `guardarMedicion()` de la lógica del cliente.

```text
guardarMedicion(tipo, valor)
        |
        +--> representar los datos como JSON
        |
        v
PeticionarioREST
        |
        v
POST /medicion
```

`LogicaFake` no accede directamente a la base de datos.

Su función es ofrecer al cliente la misma operación conceptual de guardado y utilizar el API REST para solicitarla al servidor.

### PeticionarioREST

```text
----------------------- PeticionarioREST -----------------------
|
| metodo: Text
| url_destino: Text
| cuerpo_peticion: Text
| codigo_respuesta: Z
| cuerpo_respuesta: Text
|
metodo: Text, url: Text, cuerpo: Text, respuesta
    --> hacerPeticionREST() -->
|
params --> doInBackground() --> B
|
resultado: B --> onPostExecute() -->
|
----------------------------------------------------------------
```

Responsabilidad:

Realiza peticiones HTTP al servidor REST.

No conoce el significado de O3, ppm, ppb, Major o Minor.

Recibe únicamente:

```text
método HTTP
URL
cuerpo JSON
callback de respuesta
```

En el flujo del Sprint 0 se utiliza:

```text
POST /api/medicion
```

El trabajo se realiza fuera del hilo principal de Android para evitar bloquear la interfaz.

---

### Flujo completo de una medición

```text
SparkFun
   |
   | Minor = 1234
   | Major = tipo + contador
   v
MainActivity
   |
   +--> TramaIBeacon
   |
   +--> tipo = O3
   |
   +--> contador
   |
   +--> Minor = 1234 ppb
   |
   +--> comprobar contador nuevo
   |
   +--> Utilidades.ppbAPpm()
   |
   |    1.234 ppm
   v
LogicaFake.guardarMedicion(
    "O3",
    1.234
)
   |
   v
PeticionarioREST
   |
   | POST
   v
Servidor REST
```

Android no accede directamente a la base de datos.

---

### Interfaz gráfica

La interfaz del Sprint 0 contiene tres botones centrados en pantalla:

```text
Buscar todos los dispositivos BLE

Buscar Fabian_GTI

Detener búsqueda
```

El primer botón permite comprobar el funcionamiento general del escaneo.

El segundo aplica el filtro utilizado durante el funcionamiento normal.

El tercero detiene el escaneo BLE activo.

---

## Aclaraciones del Diseño

- La aplicación está desarrollada en Java con Android Studio.
- La SparkFun publica el valor de O3 en ppb dentro de `Minor`.
- Android puede convertir ese valor a ppm antes de enviarlo al servidor.
- La configuración `ENVIAR_VALOR_EN_PPM` permite seleccionar fácilmente entre ppm y ppb.
- `Major` contiene el tipo de medición y el contador.
- El contador permite evitar que una misma medición se almacene varias veces debido a las repeticiones del advertising BLE.
- `MainActivity` gestiona Bluetooth y coordina el flujo de la aplicación.
- `TramaIBeacon` representa los datos recibidos mediante iBeacon.
- `Utilidades` contiene operaciones auxiliares de conversión.
- `LogicaFake` representa la operación `guardarMedicion()` desde el cliente.
- `PeticionarioREST` realiza únicamente la comunicación HTTP.
- La lógica fake no accede directamente a la base de datos.
- El botón de escaneo general se conserva para permitir comprobar el funcionamiento de BLE y localizar dispositivos sin aplicar filtros.
- El nombre del beacon se mantiene centralizado en `NOMBRE_BEACON` para facilitar su modificación.

## Reglas Generales

- Lenguaje de programación: Java utilizando Android Studio.
- La implementación debe mantenerse en `src/android/` y corresponder con este diseño.
- Cada fichero debe incluir una cabecera con nombre, descripción, fecha, autor, aportación y copyright.
- Cada función o método debe incluir su diseño lógico y una breve descripción dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro y autoexplicativo, evitando comentarios innecesarios.
- La lógica de Bluetooth, interpretación de datos, lógica fake y comunicación REST deben mantenerse separadas.
- `MainActivity` no debe construir directamente peticiones HTTP.
- `LogicaFake` debe utilizar `PeticionarioREST` para acceder al servidor.
- La aplicación no debe acceder directamente a la base de datos.
- Las funciones de lógica independientes del sistema Android o del hardware deben poder probarse automáticamente cuando resulte adecuado.
- Los permisos Bluetooth y de red deben declararse explícitamente en `AndroidManifest.xml`.