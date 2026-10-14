# Diseño de Android

## Diseño del Componente

El componente `android` recibe mediante Bluetooth Low Energy la medición publicada por la SparkFun, interpreta la trama iBeacon y solicita al backend que almacene las mediciones nuevas.

El flujo es:

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
N     = número natural
Z     = número entero
R     = número real
B     = booleano
Text  = texto
Bytes = secuencia de bytes
```

```text
ResultadoBLE = (
    nombre: Text,
    direccion: Text,
    rssi: Z,
    datos: Bytes
)
```

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

### MainActivity

```text
--------------------------- MainActivity ---------------------------
|
| nombre_beacon: Text
| enviar_valor_en_ppm: B
| ultimo_contador_enviado: Z
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
resultado: ResultadoBLE --> mostrarDispositivo() -->
|
resultado: ResultadoBLE --> obtenerNombreDispositivo() --> Text
|
| buscarNuestroDispositivoBTLE() -->
|
tipo: N --> obtenerNombreTipoMedicion() --> Text
|
minor: N --> obtenerValorServidor() --> R
|
resultado: ResultadoBLE --> procesarResultadoBLE() -->
|
trama: TramaIBeacon, major: N, minor: N, tipo: Text,
tipoId: N, contador: N, valor: R --> mostrarIBeacon() -->
|
tipo: Text, valor: R --> enviarMedicion() -->
|
| detenerBusquedaDispositivosBTLE() -->
|
--------------------------------------------------------------------
```

Responsabilidad:

Coordina el escaneo BLE y el procesamiento de las mediciones.

No realiza directamente la comunicación HTTP.

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

El contador permite ignorar repeticiones del mismo anuncio.

### Conversión de Minor

Minor contiene la medición en ppb.

```text
1234 ppb --> 1.234 ppm
```

si:

```text
ENVIAR_VALOR_EN_PPM = true
```

### TramaIBeacon

```text
---------------- TramaIBeacon ----------------
|
bytes: Bytes --> TramaIBeacon()
|
getUUID() --> Bytes
|
getMajor() --> Bytes
|
getMinor() --> Bytes
|
getTxPower() --> Z
|
-----------------------------------------------
```

Responsabilidad:

Representa y extrae los campos utilizados de una trama iBeacon.

### Utilidades

```text
---------------- Utilidades ----------------
|
bytes: Bytes --> bytesToString() --x --> Text
|
bytes: Bytes --> bytesToIntOK() --x --> N
|
bytes: Bytes --> bytesToHexString() --x --> Text
|
ppb: N --> ppbAPpm() --x --> R
|
---------------------------------------------
```

### LogicaFake

```text
---------------- LogicaFake ----------------
|
tipo: Text, valor: R
    --> guardarMedicion()
    --> Medicion
|
---------------------------------------------
```

Responsabilidad:

Presenta en Android la operación de dominio `guardarMedicion()`.

La implementación entrega el resultado de forma asíncrona para no bloquear la aplicación.

### Medicion

```text
---------------- Medicion ----------------
|
id: N, fecha: Text, tipo: Text, valor: R
    --> Medicion()
|
getId() --> N
|
getFecha() --> Text
|
getTipo() --> Text
|
getValor() --> R
|
-------------------------------------------
```

### PeticionarioREST

Responsabilidad:

Encapsula la comunicación con el backend.

La mecánica concreta de ejecución asíncrona pertenece a la implementación y no forma parte de la interfaz lógica del dominio.

### Flujo de una medición

```text
Resultado BLE
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
LogicaFake.guardarMedicion()
     |
     v
Medicion
```

### Pruebas

Los tests de `Utilidades` comprueban:

```text
ppbAPpm(1234) --> 1.234
bytesToIntOK(0x0B, 0x05) --> 2821
bytesToHexString(0x0B, 0x05) --> "0b:05"
bytesToString(65, 66, 67) --> "ABC"
```

## Aclaraciones del Diseño

- La aplicación está implementada en Java con Android Studio.
- `MainActivity` coordina el flujo BLE.
- `TramaIBeacon` representa los datos del beacon.
- `Utilidades` contiene conversiones reutilizables.
- `LogicaFake` actúa como proxy de la lógica de negocio.
- `PeticionarioREST` encapsula la comunicación.
- `Medicion` representa el resultado de `guardarMedicion()`.
- La implementación utiliza callbacks para entregar resultados asíncronos.
- Los tipos del SDK de Android pertenecen a la implementación, no a las firmas lógicas anteriores.
- El contador evita almacenar varias veces el mismo anuncio BLE.

## Reglas Generales

- Lenguaje de programación: Java.
- La implementación debe mantenerse en `src/android/`.
- Cada método clave debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe ser claro y autoexplicativo.
- La interpretación BLE, la lógica fake y la comunicación deben mantenerse separadas.
- `MainActivity` no debe construir directamente peticiones HTTP.
- La aplicación no debe acceder directamente a la base de datos.
- Las funciones independientes del sistema Android deben disponer de pruebas automáticas cuando resulte adecuado.
- Los métodos estáticos deben identificarse mediante `--x`.
