# Diseño del Microprocesador

## Diseño del Componente

El componente `microprocesador` se ejecuta sobre una SparkFun Pro nRF52840 Mini.

Durante el Sprint 0 genera una medición ficticia de O3, la codifica en una trama iBeacon y la publica mediante Bluetooth Low Energy.

El nombre BLE identifica al microprocesador y será utilizado posteriormente como `nombre_micro`.

```text
Medidor
   |
   v
Publicador
   |
   v
EmisoraBLE
   |
   v
iBeacon
   |
   v
Android
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
MedicionID = {
    O3,
    TEMPERATURA
}
```

### Función de conversión

```text
ppb: N --> convertirPpbAPpm() --> R
```

Responsabilidad:

Convierte ppb a ppm únicamente para depuración por Serial.

```text
ppm = ppb / 1000
```

El valor transmitido en `Minor` continúa siendo el valor original en ppb.

### Medidor

```text
--------------------------- Medidor ---------------------------
|
| valorO3PruebaPpb: N
|
| iniciarMedidor() -->
|
| medirO3() --> N
|
---------------------------------------------------------------
```

Durante el Sprint 0:

```text
medirO3() --> 1234
```

### Publicador

`construirMajor()` es estático. Se marca con `--x` según la notación de clase.

```text
-------------------------- Publicador --------------------------
|
| beaconUUID: [N]
| emisora: EmisoraBLE
| rssi: Z
|
| obtenerNombreMicro() --> Texto
|
tipo: MedicionID, contador: N
        --> construirMajor() --x --> N
|
| encenderEmisora() -->
|
tipo: MedicionID, valor: N, contador: N, tiempo: N
        --> publicarMedida() --> N
|
----------------------------------------------------------------
```

Responsabilidad:

Codifica y publica la medición.

Nombre utilizado:

```text
Fabian_GTI
```

`obtenerNombreMicro()` devuelve ese mismo nombre.

Codificación de `Major`:

```text
Major = (tipo << 8) | contador
```

Por tanto:

```text
byte alto = tipo
byte bajo = contador
```

`Minor` contiene:

```text
valor de la medición en ppb
```

Ejemplo:

```text
tipo O3 = 11
contador = 5
valor = 1234

Major = 2821
Minor = 1234
```

### EmisoraBLE

```text
-------------------------- EmisoraBLE --------------------------
|
| nombreEmisora: Texto
| fabricanteId: N
| txPower: Z
|
nombre: Texto, fabricante: N, potencia: Z
        --> EmisoraBLE()
|
| encenderEmisora() -->
|
| estaAnunciando() --> VoF
|
| detenerAnuncio() -->
|
uuid: [N], major: N, minor: N, txPower: Z
        --> emitirAnuncioIBeacon()
|
----------------------------------------------------------------
```

Configuración del Sprint 0:

```text
nombre BLE = Fabian_GTI
Manufacturer ID = 0x004C
UUID = EPSG-GTI-PROY-3A
RSSI = -53
```

### PuertoSerie

```text
-------------------------- PuertoSerie --------------------------
|
baudios: N --> PuertoSerie()
|
tiempoMaximo: N --> esperarDisponible()
|
valor --> escribir()
|
valor: R, decimales: N --> escribirDecimal()
|
----------------------------------------------------------------
```

Responsabilidad:

Muestra información de depuración.

La ejecución continúa aunque no exista Serial Monitor.

La información mostrada incluye:

```text
nombre del microprocesador
tipo
contador
valor O3
Major
Minor
intervalo
```

### LED

```text
------------------------------ LED ------------------------------
|
| numeroLed: Z
| encendido: VoF
|
numero: Z --> LED()
|
| encender() -->
|
| apagar() -->
|
tiempo: N --> brillar()
|
----------------------------------------------------------------
```

Responsabilidad:

Indica visualmente el inicio de una nueva medición.

### Ciclo principal

```text
setup()
```

Inicializa:

```text
PuertoSerie
Publicador
Medidor
```

```text
loop()
```

Flujo:

```text
medir O3
   |
   v
obtener contador
   |
   v
construir Major
   |
   v
mostrar depuración
   |
   v
publicar iBeacon
   |
   v
incrementar contador
```

El intervalo del Sprint 0 es:

```text
5000 ms
```

### Pruebas

Debe mantenerse una prueba automática de compilación para la codificación de `Major`.

Caso:

```text
O3 = 11
contador = 5
```

Resultado esperado:

```text
Major = 2821
```

## Aclaraciones del Diseño

- El microprocesador no se comunica directamente con el backend.
- `Fabian_GTI` es el nombre BLE y también identifica conceptualmente el origen de la medición.
- Android obtiene ese nombre del anuncio BLE y lo envía posteriormente como `nombre_micro`.
- `Minor` contiene el valor de O3 en ppb.
- La conversión a ppm se utiliza únicamente para depuración en el microprocesador.
- El contador ocupa el byte bajo de `Major`.
- El tipo ocupa el byte alto de `Major`.
- El Serial Monitor es opcional.
- Durante el Sprint 0 el medidor devuelve un valor ficticio.

## Reglas Generales

- Lenguaje de programación: C++ para Arduino.
- La implementación debe mantenerse en `src/microprocesador/`.
- Los nombres de funciones, tipos y parámetros del diseño deben seguir la notación oficial de la asignatura.
- El encabezado de cada función o método clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- La comunicación del microprocesador debe realizarse mediante BLE/iBeacon.
- Las operaciones clave deben disponer de pruebas automáticas cuando sea posible.
