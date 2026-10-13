# Prompt para generar Android

Quiero que generes o adaptes únicamente el componente Android del Sprint 0 del proyecto de Biometría y Medio Ambiente.

## Arquitectura

El flujo es:

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
```

El código Android se encuentra en:

```text
src/android/
```

Debe desarrollarse en Java con Android Studio.

## Objetivo

La aplicación debe:

```text
escanear dispositivos BLE
mostrar todos los dispositivos cuando se solicite
buscar específicamente Fabian_GTI
interpretar la trama iBeacon
extraer Major y Minor
interpretar tipo y contador
evitar mediciones duplicadas
convertir ppb a ppm cuando corresponda
enviar la medición al backend
```

## Separación de responsabilidades

Mantén:

```text
MainActivity
    ↓
TramaIBeacon / Utilidades
    ↓
LogicaFake
    ↓
PeticionarioREST
```

`MainActivity` coordina BLE y la interacción.

`TramaIBeacon` interpreta la trama.

`Utilidades` realiza conversiones.

`LogicaFake` presenta operaciones de dominio.

`PeticionarioREST` encapsula HTTP.

## Beacon

Nombre:

```text
Fabian_GTI
```

Major:

```text
byte alto -> tipo
byte bajo -> contador
```

Tipos:

```text
O3 = 11
TEMPERATURA = 12
```

Extracción:

```text
tipo = (major >> 8) & 0xFF
contador = major & 0xFF
```

Minor contiene el valor en ppb.

Ejemplo:

```text
Minor = 1234
```

## Conversión

Debe existir una configuración:

```text
ENVIAR_VALOR_EN_PPM
```

Si es `true`:

```text
1234 ppb -> 1.234 ppm
```

Si es `false`:

```text
1234
```

se envía sin convertir.

## Anti-duplicados

La SparkFun repite el mismo advertising varias veces.

Android debe conservar:

```text
ultimoContadorEnviado
```

y enviar al servidor únicamente cuando el contador cambie.

## LogicaFake

La operación lógica debe mantener la misma interfaz de dominio que el backend:

```text
tipo: Text, valor: R
    --> guardarMedicion()
    --> Medicion
```

donde:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

Como Android no debe bloquear el hilo principal, la implementación puede entregar la `Medicion` mediante callback asíncrono.

Debe existir una clase:

```text
Medicion.java
```

con:

```text
id
fecha
tipo
valor
```

## PeticionarioREST

Debe realizar la petición HTTP fuera del hilo principal.

Usará:

```text
POST https://fuseriv.upv.edu.es/api/medicion
```

El JSON enviado será:

```json
{
  "tipo": "O3",
  "valor": 1.234
}
```

La respuesta correcta debe poder transformarse en un objeto `Medicion`.

## Logcat

Utiliza etiquetas claras:

```text
FABI_BLE
FABI_LOGICA
FABI_REST
```

Ejemplos de mensajes:

```text
[SCAN]
[IBEACON]
[MEDICION]
[SERVIDOR]
[HTTP]
```

## Tests

Mantén tests locales de `Utilidades` para:

```text
ppbAPpm
bytesToIntOK
bytesToHexString
bytesToString
```

Deben poder ejecutarse desde Android Studio y producir:

```text
4 tests passed
BUILD SUCCESSFUL
```

## Restricciones

No accedas directamente a MySQL desde Android.

No hagas HTTP directamente desde `MainActivity`.

No mezcles interpretación BLE con `PeticionarioREST`.

No añadas clases innecesarias.

Mantén el código sencillo y fácil de defender.
