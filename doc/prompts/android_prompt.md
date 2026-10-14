# Prompt — Android

Genera el componente Android del Sprint 0 a partir de `doc/android_design.md`.

## Contexto

La aplicación debe implementarse en Java para Android Studio.

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
Backend
```

El dispositivo BLE buscado durante el Sprint 0 se llama:

```text
Fabian_GTI
```

La aplicación debe obtener el nombre real recibido desde el anuncio BLE y enviarlo posteriormente como:

```text
nombre_micro
```

Aunque se filtre por `Fabian_GTI`, el valor enviado al backend debe proceder del resultado BLE recibido.

## Decodificación iBeacon

`Major` contiene:

```text
byte alto = tipo
byte bajo = contador
```

Por tanto:

```text
tipo = (Major >> 8) & 0xFF
contador = Major & 0xFF
```

Tipos:

```text
11 --> O3
12 --> TEMPERATURA
```

`Minor` contiene el valor en ppb.

Cuando la configuración de envío en ppm esté activa:

```text
1234 ppb --> 1.234 ppm
```

## Evitar duplicados

El contador de `Major` debe utilizarse para no reenviar varias veces la misma medición recibida por BLE.

## Modelo Medicion

Debe existir un modelo equivalente a:

```text
Medicion = (
    nombreMicro: Texto,
    fechaHora: Texto,
    tipo: Texto,
    valor: R
)
```

No utilizar `id`.

## Guardado

La operación lógica utilizada por Android es:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
```

El JSON enviado al backend debe contener:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

Android no debe enviar `fecha_hora`; la genera el backend.

La respuesta esperada contiene:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234,
  "fecha_hora": "2026-10-14 12:00:00"
}
```

## Arquitectura

Mantener separadas las responsabilidades:

```text
MainActivity
   |
   v
LogicaFake
   |
   v
PeticionarioREST
```

`MainActivity` no debe implementar directamente las peticiones HTTP.

## Logs

Mantener etiquetas diferenciadas:

```text
FABI_BLE
FABI_LOGICA
FABI_REST
```

## Pruebas

Mantener o generar pruebas para:

- conversiones de `Utilidades`;
- almacenamiento de los cuatro campos de `Medicion`.

## Comentarios y diseño lógico

Cada función o método clave debe incluir un encabezado entre líneas discontinuas con notación lógica oficial.

Ejemplos:

```text
resultado: ResultadoBLE --> obtenerNombreDispositivo() --> Texto
```

```text
minor: N --> obtenerValorServidor() --> R
```

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> enviarMedicion()
```

Evitar expresar detalles del framework Android dentro de las firmas lógicas cuando no sean parte del dominio.

## Resultado esperado

Genera únicamente los archivos necesarios dentro de:

```text
src/android/
```

La implementación debe corresponder exactamente con `doc/android_design.md`.
