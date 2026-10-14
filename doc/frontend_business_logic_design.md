# Diseño de Frontend Business Logic

## Diseño del Componente

Este componente actúa como proxy entre la interfaz web y el backend. Expone a la GUI una operación de dominio sencilla y oculta por completo el mecanismo utilizado para obtener la medición desde el servidor.


El componente `frontend_business_logic` es el proxy utilizado por la GUI web. Su implementación se encuentra en `src/frontend_business_logic/`.

### Tipos

```text
Medicion = (
    nombre_micro: Text,
    tipo: Text,
    valor: R,
    fecha_hora: Text
)

Nulo = { NULO }
```

### Diseño de la clase LogicaFake

`LogicaFake` es el proxy consumido por la interfaz web. Presenta la misma operación lógica de lectura que el backend y mantiene encapsulado el acceso remoto.


```text
                 ----------- LogicaFake --------------
                 |
                 | urlMedicion: Text
                 |
                 |
                 LogicaFake() -->
                 |
                 |
Medicion | Nulo <-- leerMedicion() <--
                 |
                 -------------------------------------
```

La firma pública coincide exactamente con la operación equivalente de `business_logic`:

```text
leerMedicion() --> Medicion | Nulo
```

La implementación encapsula internamente:

```text
GET /api/medicion
```

### Pruebas

Las pruebas comprueban lectura correcta, ruta, ausencia de medición y errores.

## Aclaraciones del Diseño

- La GUI usa exclusivamente este componente para consultar el backend.
- El proxy expone únicamente el subconjunto que necesita la GUI.
- `NULO` puede representarse mediante `null` en JavaScript.
- La comunicación remota queda encapsulada dentro de `LogicaFake`.

## Reglas Generales

- Lenguaje de programación: JavaScript.
- La implementación debe mantenerse en `src/frontend_business_logic/`.
- La firma debe ser idéntica a la operación equivalente de `business_logic_design.md`.
- La GUI no debe realizar comunicación remota directamente.
- El encabezado de cada función o método debe incluir su diseño lógico entre líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las funciones clave deben disponer de pruebas automatizadas.
