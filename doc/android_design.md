# Diseño de Android

## Diseño del Componente

El componente `android` recibe anuncios BLE/iBeacon, interpreta las mediciones nuevas y solicita su almacenamiento mediante el backend.

El nombre del microprocesador enviado al backend se obtiene del anuncio BLE recibido.

```text
SparkFun
   |
   | BLE / iBeacon
   v
Android
   |
   v
LogicaFake
   |
   v
PeticionarioREST
   |
   v
Communication
```

### Tipos utilizados

```text
N       = número natural
Z       = número entero
R       = número real
Texto   = texto
VoF     = verdadero o falso
[N]     = vector de números naturales
```

```text
ResultadoBLE = (
    nombre: Texto,
    direccion: Texto,
    rssi: Z,
    datos: [N]
)
```

```text
Medicion = (
    nombreMicro: Texto,
    fechaHora: Texto,
    tipo: Texto,
    valor: R
)
```

```text
TramaIBeaconDatos = (
    uuid: [N],
    major: [N],
    minor: [N],
    txPower: Z
)
```

### MainActivity

```text
----------------------------- MainActivity -----------------------------
|
| nombreBeacon: Texto
| enviarValorEnPpm: VoF
| ultimoContadorEnviado: Z
|
| tengoPermisosBluetooth() --> VoF
|
| pedirPermisosBluetooth() -->
|
| inicializarBluetooth() -->
|
| escanerPreparado() --> VoF
|
| buscarTodosLosDispositivosBTLE() -->
|
resultado: ResultadoBLE --> mostrarDispositivo()
|
resultado: ResultadoBLE --> obtenerNombreDispositivo() --> Texto
|
| buscarNuestroDispositivoBTLE() -->
|
tipo: N --> obtenerNombreTipoMedicion() --> Texto
|
minor: N --> obtenerValorServidor() --> R
|
resultado: ResultadoBLE --> procesarResultadoBLE()
|
nombreMicro: Texto, trama: TramaIBeaconDatos,
major: N, minor: N, tipo: Texto, tipoId: N,
contador: N, valor: R
        --> mostrarIBeacon()
|
nombreMicro: Texto, tipo: Texto, valor: R
        --> enviarMedicion()
|
| detenerBusquedaDispositivosBTLE() -->
|
eventoVista --> botonBuscarTodosLosDispositivosBTLEPulsado()
|
eventoVista --> botonBuscarNuestroDispositivoBTLEPulsado()
|
eventoVista --> botonDetenerBusquedaDispositivosBTLEPulsado()
|
estadoInicial --> onCreate()
|
resultadoPermisos --> onRequestPermissionsResult()
|
| onDestroy() -->
|
-------------------------------------------------------------------------
```

Responsabilidad:

Coordina permisos, escaneo BLE, decodificación y envío de mediciones.

No implementa directamente las operaciones HTTP.

### Interpretación de Major

```text
tipo = (Major >> 8) & 0xFF
contador = Major & 0xFF
```

Tipos conocidos:

```text
11 --> O3
12 --> TEMPERATURA
```

El contador evita reenviar repetidamente el mismo anuncio.

### Interpretación de Minor

`Minor` contiene el valor en ppb.

Si:

```text
enviarValorEnPpm = VERDADERO
```

entonces:

```text
1234 ppb --> 1.234 ppm
```

antes de solicitar el almacenamiento.

### Nombre del microprocesador

La constante:

```text
Fabian_GTI
```

se utiliza para filtrar el dispositivo BLE buscado.

Sin embargo, el valor enviado al backend se obtiene del resultado BLE:

```text
ResultadoBLE --> obtenerNombreDispositivo() --> Texto
```

y después:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> enviarMedicion()
```

### TramaIBeacon

```text
-------------------------- TramaIBeacon --------------------------
|
bytes: [N] --> TramaIBeacon()
|
| getUUID() --> [N]
|
| getMajor() --> [N]
|
| getMinor() --> [N]
|
| getTxPower() --> Z
|
------------------------------------------------------------------
```

Responsabilidad:

Representa los campos necesarios de una trama iBeacon.

### Utilidades

Los métodos son estáticos y se marcan con `--x`.

```text
---------------------------- Utilidades ----------------------------
|
bytes: [N] --> bytesToString() --x --> Texto
|
bytes: [N] --> bytesToIntOK() --x --> N
|
bytes: [N] --> bytesToHexString() --x --> Texto
|
ppb: N --> ppbAPpm() --x --> R
|
-------------------------------------------------------------------
```

### Medicion

```text
----------------------------- Medicion -----------------------------
|
| nombreMicro: Texto
| fechaHora: Texto
| tipo: Texto
| valor: R
|
nombreMicro: Texto, fechaHora: Texto, tipo: Texto, valor: R
        --> Medicion()
|
| getNombreMicro() --> Texto
|
| getFechaHora() --> Texto
|
| getTipo() --> Texto
|
| getValor() --> R
|
--------------------------------------------------------------------
```

### LogicaFake

La forma lógica de la operación no expresa el mecanismo asíncrono utilizado por Java.

```text
---------------------------- LogicaFake ----------------------------
|
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
|
--------------------------------------------------------------------
```

Responsabilidad:

Presenta a Android la operación de almacenamiento utilizando datos de dominio.

El objeto enviado contiene conceptualmente:

```text
nombreMicro
tipo
valor
```

La fecha y hora son generadas por el backend.

### PeticionarioREST

`PeticionarioREST` encapsula el mecanismo concreto de comunicación remota.

Diseño conceptual:

```text
------------------------- PeticionarioREST -------------------------
|
peticion --> hacerPeticionREST() --> respuesta
|
| doInBackground()
|
respuesta --> onPostExecute()
|
--------------------------------------------------------------------
```

Los detalles de hilos, callbacks y objetos de Android pertenecen a la implementación.

### Flujo de una medición

```text
ResultadoBLE
     |
     v
obtener nombreMicro
     |
     v
TramaIBeacon
     |
     v
Major + Minor
     |
     v
tipo + contador + valor
     |
     v
comprobar duplicado
     |
     v
LogicaFake.guardarMedicion(
    nombreMicro,
    tipo,
    valor
)
     |
     v
Medicion
```

### Pruebas

Las pruebas automáticas deben comprobar al menos:

```text
conversiones de Utilidades
almacenamiento correcto de los campos de Medicion
```

En la versión actual:

```text
UtilidadesTest --> 4 pruebas
MedicionTest --> 1 prueba
```

## Aclaraciones del Diseño

- La aplicación está implementada en Java.
- El nombre del microprocesador enviado al backend es el recibido por BLE.
- `Fabian_GTI` continúa utilizándose como filtro de búsqueda durante el Sprint 0.
- Android no genera `fechaHora`.
- Android convierte el valor a ppm antes de enviarlo cuando la configuración correspondiente está activa.
- `MainActivity` coordina BLE y utiliza `LogicaFake` para guardar la medición.
- `PeticionarioREST` encapsula la comunicación remota.
- La notación lógica no representa callbacks ni detalles concretos del framework Android.
- Las operaciones de dominio utilizan `Texto`, `N`, `Z`, `R` y `VoF`.

## Reglas Generales

- Lenguaje de programación: Java para Android.
- La implementación debe mantenerse en `src/android/`.
- Los nombres y tipos del diseño deben seguir la notación oficial utilizada en clase.
- El encabezado de cada función o método clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- La lógica BLE, la lógica fake y el mecanismo REST deben mantenerse separados por responsabilidad.
- Los datos enviados al backend deben proceder de la medición realmente recibida.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
