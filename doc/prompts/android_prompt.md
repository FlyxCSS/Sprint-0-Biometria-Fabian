# Prompt para generar Android

Genera `src/android/` a partir de `doc/android_design.md`.

Lenguaje: Java para Android Studio.

Flujo:

```text
SparkFun --> Android --> LogicaFake --> PeticionarioREST --> Communication
```

El dispositivo objetivo del Sprint 0 se anuncia como:

```text
Fabian_GTI
```

La aplicación puede usar ese valor como filtro de búsqueda, pero el `nombre_micro` enviado al backend debe obtenerse del anuncio BLE real recibido.

Decodificación de `Major`:

```text
tipo = (Major >> 8) & 0xFF
contador = Major & 0xFF
```

Tipos:

```text
11 = O3
12 = TEMPERATURA
```

`Minor` contiene el valor en ppb.

Cuando corresponda:

```text
1234 ppb --> 1.234 ppm
```

El contador debe evitar reenvíos duplicados.

Modelo:

```text
Medicion = (
    nombre_micro: Text,
    fecha_hora: Text,
    tipo: Text,
    valor: R
)
```

No crear `id`.

La operación lógica de guardado es:

```text
nombre_micro: Text, tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

JSON enviado:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

Android no genera `fecha_hora`.

Mantener responsabilidades:

```text
MainActivity
LogicaFake
PeticionarioREST
TramaIBeacon
Utilidades
Medicion
```

`MainActivity` no debe implementar directamente el mecanismo de comunicación remota.

Mantener logs diferenciados para BLE, lógica y REST.

Cada función o método clave debe incluir diseño lógico con tipos abstractos entre líneas discontinuas.

Mantener pruebas de `Utilidades` y `Medicion`.
