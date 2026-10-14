# Prompt — Microprocesador

Genera el código del componente `microprocesador` del Sprint 0 a partir del diseño definido en `doc/microprocesador_design.md`.

## Contexto

El hardware es una SparkFun Pro nRF52840 Mini y el lenguaje de implementación debe ser C++ para Arduino.

Durante el Sprint 0 el microprocesador debe:

- generar una medición ficticia de O3 de `1234 ppb`;
- anunciarse por BLE con el nombre `Fabian_GTI`;
- publicar la medición mediante iBeacon;
- codificar en `Major` el tipo de medición y un contador;
- codificar en `Minor` el valor de O3 en ppb;
- mantener un intervalo aproximado de 5000 ms entre publicaciones;
- continuar funcionando aunque no exista Serial Monitor.

La codificación de `Major` debe ser:

```text
Major = (tipo << 8) | contador
```

Para:

```text
O3 = 11
contador = 5
```

el resultado esperado es:

```text
Major = 2821
```

El nombre BLE `Fabian_GTI` representa también el nombre del microprocesador que Android obtendrá posteriormente y enviará como `nombre_micro`.

## Restricciones

- No añadir WiFi.
- No comunicar directamente el microprocesador con el backend.
- Mantener BLE/iBeacon como mecanismo de comunicación con Android.
- Mantener separación entre `Medidor`, `Publicador`, `EmisoraBLE`, `PuertoSerie` y `LED`.
- Mantener el código sencillo y fácil de explicar.
- No introducir clases innecesarias.
- Mantener una prueba automática de compilación con `static_assert` para comprobar la construcción de `Major`.

## Comentarios y diseño lógico

Cada función o método clave debe tener un encabezado entre líneas discontinuas con su diseño lógico siguiendo la notación oficial de clase.

Ejemplos:

```text
ppb: N --> convertirPpbAPpm() --> R
```

```text
tipo: MedicionID, contador: N
        --> construirMajor() --x --> N
```

No sustituir la notación lógica por tipos concretos del lenguaje cuando el diseño use tipos abstractos.

## Resultado esperado

Genera únicamente los archivos necesarios dentro de:

```text
src/microprocesador/
```

La implementación debe corresponder exactamente con `doc/microprocesador_design.md`.
