# Prompt para generar Business Logic

Genera el componente `src/business_logic/` a partir de `doc/business_logic_design.md` y `doc/database_design.md`.

Lenguaje: PHP.

La clase principal debe ser `LogicaNegocio` y debe recibir una conexión de persistencia en el constructor.

Operaciones de dominio:

```text
nombre_micro: Text, tipo: Text, valor: R
    --> guardarMedicion() --> Medicion

leerMedicion() --> Medicion | Nulo
```

`guardarMedicion()` debe:

- recortar `nombre_micro` y `tipo`;
- rechazar `nombre_micro` vacío;
- rechazar `tipo` vacío;
- rechazar valores no numéricos;
- convertir el valor a real;
- generar `fecha_hora` en `Europe/Madrid` con formato `Y-m-d H:i:s`;
- insertar exactamente `nombre_micro`, `tipo`, `valor`, `fecha_hora`;
- devolver esos cuatro campos.

`leerMedicion()` debe usar:

```text
ORDER BY fecha_hora DESC, nombre_micro ASC
LIMIT 1
```

y devolver ausencia de medición cuando la tabla esté vacía.

Restricciones arquitectónicas:

- No depender de `src/communication/`.
- No introducir conceptos propios de la capa de comunicación en la lógica de dominio.
- Mantener correspondencia exacta con `database_design.md`.
- Utilizar consultas preparadas para el guardado.
- Mantener el código claro y autoexplicativo.

Encabezados:

Cada función o método debe incluir su diseño lógico entre líneas discontinuas y usar tipos `N`, `Z`, `R`, `B`, `Text` o tipos de dominio.

Pruebas:

Genera o conserva pruebas automatizadas para:

- medición válida;
- `nombre_micro` vacío;
- `tipo` vacío;
- valor no numérico;
- lectura de última medición;
- misma `fecha_hora` para micros distintos;
- tabla vacía.
