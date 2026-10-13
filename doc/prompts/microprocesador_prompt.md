# Prompt para generar el microprocesador

Quiero que generes o adaptes únicamente el código del microprocesador para mi proyecto de Biometría y Medio Ambiente.

## Contexto general

El Sprint 0 debe comprobar:

```text
SparkFun
 ↓ BLE / iBeacon
Android
 ↓
Communication
 ↓
Business Logic
 ↓
Database
 ↓
Frontend Business Logic
 ↓
Web
```

Este prompt afecta únicamente a:

```text
doc/microprocesador_design.md
src/microprocesador/
```

## Hardware

```text
SparkFun Pro nRF52840 Mini
```

Librería BLE:

```text
Adafruit Bluefruit nRF52
```

## Objetivo del Sprint 0

No es necesario leer todavía el sensor real.

Utiliza una medición ficticia:

```text
O3 = 1234 ppb
```

## Archivos

Mantén:

```text
src/microprocesador/
├── microprocesador.ino
├── Medidor.h
├── Publicador.h
├── EmisoraBLE.h
├── PuertoSerie.h
└── LED.h
```

## Medidor

`Medidor.h` debe proporcionar:

```text
iniciarMedidor()
medirO3() --> N
```

Durante el Sprint 0:

```text
medirO3() --> 1234
```

en ppb.

## BLE

Nombre:

```text
Fabian_GTI
```

UUID:

```text
EPSG-GTI-PROY-3A
```

Manufacturer ID:

```text
0x004C
```

RSSI de referencia:

```text
-53
```

Tipos:

```text
O3 = 11
TEMPERATURA = 12
```

## Major

Debe codificar:

```text
byte alto -> tipo
byte bajo -> contador
```

mediante:

```cpp
(tipo << 8) | contador
```

Ejemplo:

```text
O3 = 11
contador = 5
Major = 2821
```

## Minor

Debe contener directamente el valor en ppb.

Ejemplo:

```text
Minor = 1234
```

No conviertas Minor a ppm.

## Intervalo

Configura:

```text
INTERVALO_MEDICION_MS = 5000
DURACION_LED_MS = 100
ESPERA_SERIAL_MS = 2500
```

El programa debe funcionar aunque no haya Serial Monitor conectado.

## Serial

Puede mostrar la conversión:

```text
1234 ppb -> 1.234 ppm
```

solo para depuración.

BLE sigue enviando:

```text
Minor = 1234
```

## Test

Incluye:

```cpp
static_assert
```

para comprobar:

```text
O3 = 11
contador = 5
Major = 2821
```

## Restricciones

No implementes todavía el sensor real.

No añadas WiFi.

No hagas HTTP desde la SparkFun.

No conectes el microprocesador directamente al backend.

La comunicación de la SparkFun debe ser únicamente BLE hacia Android.
