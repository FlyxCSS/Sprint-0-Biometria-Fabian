# Prompt para generar el microprocesador

Quiero que generes o adaptes únicamente el código del microprocesador para mi proyecto de Biometría y Medio Ambiente.

## Contexto general

El Sprint 0 debe comprobar el recorrido completo de una medición ficticia a través de esta arquitectura:

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon
        v
Aplicación Android
        |
        | POST /api/medicion
        v
Servidor REST
        |
        v
Lógica de negocio
        |
        v
Base de datos
        |
        | GET /api/medicion
        v
Interfaz Web
```

En esta tarea debes trabajar únicamente sobre el componente del microprocesador.

## Hardware

Placa utilizada:

```text
SparkFun Pro nRF52840 Mini
```

Librería BLE:

```text
Adafruit Bluefruit nRF52
```

## Objetivo del Sprint 0

Durante este Sprint no es obligatorio leer todavía el sensor real de ozono.

Quiero utilizar una medición ficticia de O3 para comprobar el funcionamiento de toda la arquitectura.

El valor de prueba será:

```text
1234 ppb
```

La implementación debe quedar preparada para sustituir este valor ficticio por una lectura real en futuros Sprints sin tener que rehacer toda la arquitectura.

## Estructura del componente

Quiero mantener una separación sencilla de responsabilidades mediante estos archivos:

```text
src/microprocesador/
├── microprocesador.ino
├── Medidor.h
├── Publicador.h
├── EmisoraBLE.h
├── PuertoSerie.h
└── LED.h
```

Responsabilidades:

### microprocesador.ino

Debe coordinar el funcionamiento general del nodo.

Debe:

1. inicializar los componentes;
2. esperar al Serial Monitor solo durante un tiempo limitado;
3. continuar funcionando aunque no exista un ordenador conectado;
4. obtener una nueva medición;
5. mostrar información de depuración;
6. publicar la medición por BLE;
7. incrementar un contador de mediciones.

## Configuración

Centraliza los valores configurables.

Quiero como mínimo:

```text
INTERVALO_MEDICION_MS = 5000

DURACION_LED_MS = 100

ESPERA_SERIAL_MS = 2500
```

`INTERVALO_MEDICION_MS` debe poder modificarse fácilmente si durante la demostración se solicita cambiar la frecuencia de publicación.

## Medidor.h

Crea una clase:

```text
Medidor
```

Su responsabilidad será obtener las mediciones ambientales.

Durante el Sprint 0 debe contener una medición ficticia:

```text
VALOR_O3_PRUEBA_PPB = 1234
```

Debe proporcionar:

```text
iniciarMedidor()
```

y:

```text
medirO3() --> N
```

`medirO3()` debe devolver el valor en ppb.

No implementes todavía la lectura real del sensor.

Deja la estructura preparada para que en el futuro se pueda sustituir la implementación de `medirO3()` por la lectura del sensor real.

## Publicador.h

Crea una clase:

```text
Publicador
```

Debe definir los identificadores de tipos de medición:

```text
O3 = 11
TEMPERATURA = 12
```

Debe utilizar un UUID de 16 bytes correspondiente a:

```text
EPSG-GTI-PROY-3A
```

Nombre BLE:

```text
Fabian_GTI
```

Manufacturer ID:

```text
0x004C
```

RSSI/TxPower de referencia para la trama iBeacon:

```text
-53
```

## Codificación de Major

Major debe contener:

```text
byte alto  -> tipo de medición
byte bajo  -> contador
```

La función debe ser:

```text
tipo: MedicionID, contador: N --> construirMajor() --x
N <--
```

La codificación será:

```cpp
(tipo << 8) | contador
```

Ejemplo:

```text
tipo O3 = 11
contador = 5
```

Resultado esperado:

```text
Major = 0x0B05
Major decimal = 2821
```

`construirMajor()` debe ser estática.

## Codificación de Minor

Minor debe contener directamente el valor de la medición de O3 en ppb.

Ejemplo:

```text
1234 ppb
```

se debe publicar como:

```text
Minor = 1234
```

No conviertas Minor a ppm dentro del microprocesador.

## publicarMedida()

`Publicador` debe proporcionar una operación equivalente a:

```text
tipo: MedicionID, valor: N, contador: N, tiempo: N
    --> publicarMedida() --> N
```

Debe:

1. construir Major;
2. utilizar el valor recibido como Minor;
3. solicitar a `EmisoraBLE` que publique el iBeacon;
4. mantener activo el anuncio durante el tiempo indicado;
5. detener el anuncio;
6. devolver el Major utilizado.

## EmisoraBLE.h

Crea una clase:

```text
EmisoraBLE
```

Su única responsabilidad debe ser gestionar Bluetooth Low Energy.

Debe proporcionar:

```text
encenderEmisora()
estaAnunciando()
detenerAnuncio()
emitirAnuncioIBeacon()
```

Debe utilizar Bluefruit para:

- inicializar BLE;
- configurar potencia;
- configurar nombre;
- crear la trama `BLEBeacon`;
- configurar Manufacturer ID;
- añadir el nombre al Scan Response;
- iniciar el advertising;
- detener el advertising cuando corresponda.

El intervalo interno entre anuncios de una misma medición puede mantenerse cercano a 62,5 ms mediante los valores adecuados de `Bluefruit.Advertising.setInterval()`.

No mezcles aquí lógica de medición ni conversiones de unidades.

## PuertoSerie.h

Crea una clase:

```text
PuertoSerie
```

Su función será únicamente la depuración mediante Serial.

Debe permitir:

```text
esperarDisponible()
escribir()
escribirDecimal()
```

Es importante que la placa NO dependa del Serial Monitor.

`esperarDisponible()` debe esperar únicamente durante el tiempo indicado.

Después debe continuar aunque:

```text
Serial == false
```

## LED.h

Crea una clase:

```text
LED
```

Debe permitir:

```text
encender()
apagar()
alternar()
brillar(tiempo)
```

En cada nueva medición, el programa principal debe hacer un destello corto para indicar visualmente que la placa sigue funcionando.

## Conversión ppb a ppm para depuración

En `microprocesador.ino` puede existir una función:

```text
ppb: N --> convertirPpbAPpm() --> R
```

que haga:

```text
ppm = ppb / 1000.0
```

Esta conversión se utilizará únicamente para mostrar el valor por Serial.

No debe modificar el valor enviado en Minor.

Por ejemplo:

```text
valor real del Sprint 0 = 1234 ppb

Serial:
1.234 ppm

Minor:
1234
```

## Flujo del programa principal

El flujo del `loop()` debe ser:

```text
destello LED
    |
    v
Medidor.medirO3()
    |
    v
valor O3 en ppb
    |
    v
conversión a ppm solo para Serial
    |
    v
obtener contador
    |
    v
Publicador.construirMajor()
    |
    v
mostrar información de depuración
    |
    v
Publicador.publicarMedida()
    |
    v
incrementar contador
```

El contador debe comenzar en:

```text
1
```

y debe ocupar un byte dentro de Major.

Por tanto, al superar 255 puede volver a 0 mediante conversión a `uint8_t`.

## Información de depuración

Quiero que el Serial Monitor muestre de forma clara:

```text
Nueva medición - Loop X

Tipo: O3

Contador: X

Valor O3: X.XXX ppm

Major enviado: N

Minor enviado: N

Intervalo: N ms
```

También debe mostrar un banner inicial indicando que el microprocesador se ha iniciado.

## Comentarios y notación

Mantén comentarios breves y útiles.

Cada función o método debe incluir:

1. diseño lógico usando la notación de la asignatura;
2. breve descripción;
3. bloque delimitado por líneas discontinuas.

Los métodos estáticos deben marcarse mediante:

```text
--x
```

Ejemplo:

```text
tipo: MedicionID, contador: N --> construirMajor() --x
N <--
```

No elimines comentarios originales útiles si estás adaptando código existente.

## Test automático

Quiero al menos una prueba automática reproducible para comprobar la codificación de Major.

Utiliza:

```cpp
static_assert
```

y comprueba:

```text
O3 = 11
contador = 5
Major esperado = 2821
```

La prueba debe fallar en tiempo de compilación si `construirMajor()` deja de devolver ese resultado.

## Restricciones

No añadas clases innecesarias.

No implementes todavía la lectura real del sensor de O3.

No añadas conexión WiFi.

No conectes directamente el microprocesador con el servidor REST.

El microprocesador debe comunicarse únicamente con Android mediante BLE.

No cambies la arquitectura definida.

## Resultado esperado

Genera o adapta únicamente los archivos del componente:

```text
src/microprocesador/
├── microprocesador.ino
├── Medidor.h
├── Publicador.h
├── EmisoraBLE.h
├── PuertoSerie.h
└── LED.h
```

El resultado debe ser sencillo, legible, fácil de explicar durante una defensa y coherente con el diseño del Sprint 0.
