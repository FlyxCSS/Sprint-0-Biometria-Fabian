# Prompt para generar Microprocesador

Genera `src/microprocesador/` a partir de `doc/microprocesador_design.md`.

Hardware:

```text
SparkFun Pro nRF52840 Mini
```

Lenguaje:

```text
C++ para Arduino
```

Durante el Sprint 0:

```text
O3 = 1234 ppb
nombre BLE = Fabian_GTI
```

Comunicación:

```text
BLE / iBeacon
```

Codificación:

```text
Major = (tipo << 8) | contador
Minor = valor en ppb
```

Caso de prueba obligatorio:

```text
tipo O3 = 11
contador = 5
Major = 2821
```

Mantener una comprobación estática de ese caso.

Separar responsabilidades en:

```text
Medidor
Publicador
EmisoraBLE
LED
PuertoSerie
microprocesador.ino
```

No añadir comunicación directa con el backend.

El Serial Monitor debe ser opcional.

Intervalos actuales:

```text
INTERVALO_MEDICION_MS = 5000
DURACION_LED_MS = 100
ESPERA_SERIAL_MS = 2500
```

Usar tipos lógicos `N`, `Z`, `R`, `B`, `Text` en los encabezados de diseño, delimitados por líneas discontinuas.

Mantener el código claro, legible y fácil de explicar.
