# Prompt para generar Android y la lógica fake móvil

Quiero que generes o adaptes la parte Android del proyecto.

## Tecnología
Android Studio + Java + BLE + iBeacon + HttpURLConnection.

## Objetivo
La app debe:
1. detectar la SparkFun por BLE;
2. interpretar iBeacon;
3. extraer UUID, Major, Minor y TxPower;
4. obtener tipo y contador desde Major;
5. tratar Minor como valor en ppb;
6. evitar duplicados;
7. convertir ppb a ppm si se configura;
8. enviar una nueva medición al servidor mediante lógica fake.

## Beacon
Nombre: `Fabian_GTI`.

Debe existir:
- escaneo general para depuración;
- escaneo filtrado por `Fabian_GTI`.

## Major
```text
byte alto -> tipo
byte bajo -> contador
```

```text
tipo = (major >> 8) & 0xFF
contador = major & 0xFF
```

Tipos:
```text
11 -> O3
12 -> TEMPERATURA
```

## Minor
Minor contiene ppb.

Crea:
```text
ENVIAR_VALOR_EN_PPM
```

Si `true`: 1234 -> 1.234.
Si `false`: 1234 -> 1234.

## Anti-duplicados
Solo enviar si:

```text
contador != ultimoContadorEnviado
```

## Clases
- `MainActivity`: permisos, escaneo y coordinación.
- `TramaIBeacon`: extracción de UUID/Major/Minor/TxPower.
- `Utilidades`: conversiones.
- `LogicaFake`: `guardarMedicion(tipo, valor)`.
- `PeticionarioREST`: cliente HTTP genérico.

## REST
POST:
`https://fuseriv.upv.edu.es/api/medicion`

JSON:
```json
{
  "tipo": "O3",
  "valor": 1.234
}
```

`MainActivity` no debe construir HTTP directamente.

## Logs
Mostrar nombre, UUID, Major, tipo, contador, Minor, TxPower, si se envía o ignora y respuesta HTTP.

## Tests
Crear tests para:
- 1234 ppb -> 1.234 ppm;
- `0x0B 0x05` -> 2821;
- bytes -> hex;
- bytes ASCII -> texto.

Los métodos estáticos deben reflejarse con `--x` en la notación de diseño.
