# Diseño de Android

## Diseño del Componente

El componente `android` recibe anuncios BLE/iBeacon, interpreta la medición y solicita su almacenamiento al backend. La implementación se encuentra en `src/android/`.

```text
SparkFun --> Android --> LogicaFake --> PeticionarioREST --> Communication
```

### Tipos

```text
ResultadoBLE = (
    nombre: Text,
    direccion: Text,
    rssi: Z,
    datos: [N]
)

Medicion = (
    nombre_micro: Text,
    fecha_hora: Text,
    tipo: Text,
    valor: R
)

TramaIBeaconDatos = (
    uuid: [N]_16,
    major: [N]_2,
    minor: [N]_2,
    txPower: Z
)

Vista
EstadoActividad
RespuestaREST
```

### Diseño de la clase MainActivity

```text
                 -------------------- MainActivity ---------------------
                 |
                 | nombreBeacon: Text
                 | enviarValorEnPpm: B
                 | ultimoContadorEnviado: Z
                 | logicaFake: LogicaFake
                 |
                 |
              B <-- tengoPermisosBluetooth() <--
                 |
                 |
                 pedirPermisosBluetooth() -->
                 |
                 |
                 inicializarBluetooth() -->
                 |
                 |
              B <-- escanerPreparado() <--
                 |
                 |
                 buscarTodosLosDispositivosBTLE() -->
                 |
                 |
resultado: ResultadoBLE --> mostrarDispositivo() -->
                 |
                 |
resultado: ResultadoBLE --> obtenerNombreDispositivo() <--
           Text <--
                 |
                 |
                 buscarNuestroDispositivoBTLE() -->
                 |
                 |
tipo: N       --> obtenerNombreTipoMedicion() <--
           Text <--
                 |
                 |
minor: N      --> obtenerValorServidor() <--
              R <--
                 |
                 |
resultado: ResultadoBLE --> procesarResultadoBLE() -->
                 |
                 |
nombre_micro: Text, trama: TramaIBeaconDatos, major: N, minor: N,
tipo: Text, tipoId: N, contador: N, valor: R --> mostrarIBeacon() -->
                 |
                 |
nombre_micro: Text, tipo: Text, valor: R --> enviarMedicion() -->
                 |
                 |
                 detenerBusquedaDispositivosBTLE() -->
                 |
                 ------------------------------------------------------
```

### Diseño de la clase Medicion

```text
                 ------------- Medicion ----------------
                 |
                 | nombreMicro: Text
                 | fechaHora: Text
                 | tipo: Text
                 | valor: R
                 |
                 |
nombreMicro: Text, fechaHora: Text, tipo: Text, valor: R --> Medicion() -->
                 |
                 |
           Text <-- getNombreMicro() <--
                 |
                 |
           Text <-- getFechaHora() <--
                 |
                 |
           Text <-- getTipo() <--
                 |
                 |
              R <-- getValor() <--
                 |
                 ---------------------------------------
```

### Diseño de la clase TramaIBeacon

```text
                 ----------- TramaIBeacon -------------
                 |
                 | uuid: [N]_16
                 | major: [N]_2
                 | minor: [N]_2
                 | txPower: Z
                 |
                 |
bytes: [N]    --> TramaIBeacon() -->
                 |
                 |
       [N]_16 <-- getUUID() <--
                 |
                 |
        [N]_2 <-- getMajor() <--
                 |
                 |
        [N]_2 <-- getMinor() <--
                 |
                 |
              Z <-- getTxPower() <--
                 |
                 --------------------------------------
```

### Diseño de la clase Utilidades

Todos los métodos son estáticos.

```text
                 ------------- Utilidades -------------
                 |
                 |
bytes: [N]    --> bytesToString() --x
           Text <--
                 |
                 |
bytes: [N]    --> bytesToIntOK() --x
              N <--
                 |
                 |
bytes: [N]    --> bytesToHexString() --x
           Text <--
                 |
                 |
ppb: N        --> ppbAPpm() --x
              R <--
                 |
                 --------------------------------------
```

### Diseño de la clase LogicaFake

```text
                 ------------- LogicaFake -----------------
                 |
                 |
nombre_micro: Text, tipo: Text, valor: R --> guardarMedicion() -->
       Medicion <--
                 |
                 ------------------------------------------
```

La respuesta asíncrona de Java es un mecanismo de implementación; la operación lógica sigue siendo la anterior.

### Diseño de la clase PeticionarioREST

```text
                 ---------- PeticionarioREST ----------------
                 |
                 | metodo: Text
                 | urlDestino: Text
                 | cuerpoPeticion: Text | Nulo
                 |
                 |
metodo: Text, url: Text, cuerpo: Text | Nulo,
respuesta: RespuestaREST --> hacerPeticionREST() -->
                 |
                 |
params: [Text] --> doInBackground() -->
              B <--
                 |
                 |
resultado: B   --> onPostExecute() -->
                 |
                 -------------------------------------------
```

### Interpretación iBeacon

```text
tipo = (Major >> 8) & 0xFF
contador = Major & 0xFF
```

```text
11 = O3
12 = TEMPERATURA
```

`Minor` contiene ppb. Cuando está configurado el envío en ppm:

```text
1234 ppb --> 1.234 ppm
```

El filtro busca `Fabian_GTI`, pero el `nombre_micro` enviado se obtiene del anuncio BLE recibido.

### Pruebas

```text
UtilidadesTest
MedicionTest
```

## Aclaraciones del Diseño

- `MainActivity` coordina BLE.
- `LogicaFake` representa la operación de guardado.
- `PeticionarioREST` encapsula la comunicación remota.
- Android no genera `fecha_hora`.
- El contador evita duplicados.
- Callbacks y clases concretas de Android son detalles de implementación.

## Reglas Generales

- Lenguaje de programación: Java para Android.
- La implementación debe mantenerse en `src/android/`.
- El diseño lógico debe utilizar `N`, `Z`, `R`, `B`, `Text` o tipos definidos aquí.
- El encabezado de cada función o método clave debe incluir su diseño lógico entre líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las responsabilidades BLE, lógica y comunicación deben mantenerse separadas.
- Las funciones y métodos clave deben disponer de pruebas automatizadas.
