# Prompt — Business Logic

Genera el componente `business_logic` del Sprint 0 a partir de `doc/business_logic_design.md`.

## Lenguaje

PHP.

## Restricción arquitectónica principal

Este componente debe ser 100 % independiente de la capa de comunicación.

No debe contener referencias a:

- HTTP;
- GET o POST;
- rutas;
- códigos 200, 201, 400, 404 o 500;
- cabeceras;
- JSON como concepto de transporte;
- `communication`.

Debe trabajar únicamente con datos de dominio y persistencia.

## Tipo de dominio

```text
Medicion = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R,
    fechaHora: Texto
)
```

## Operación de guardado

Diseño lógico:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
```

Debe:

1. eliminar espacios sobrantes de `nombreMicro` y `tipo`;
2. rechazar `nombreMicro` vacío;
3. rechazar `tipo` vacío;
4. rechazar un valor no numérico;
5. generar `fechaHora` en el backend;
6. almacenar la medición;
7. devolver la medición almacenada.

Usar:

```text
Europe/Madrid
Y-m-d H:i:s
```

La tabla es:

```text
Mediciones
```

con columnas:

```text
nombre_micro
tipo
valor
fecha_hora
```

## Operación de lectura

Diseño lógico:

```text
leerMedicion() --> Medicion | Nulo
```

Debe recuperar la última medición con:

```text
ORDER BY fecha_hora DESC, nombre_micro ASC
LIMIT 1
```

Si no existen mediciones debe devolver `null`.

## Pruebas automáticas

Crear pruebas para:

- guardar una medición válida;
- rechazar `nombreMicro` vacío;
- rechazar `tipo` vacío;
- rechazar valor no numérico;
- recuperar la última medición;
- permitir la misma `fechaHora` para micros diferentes;
- devolver `null` cuando la tabla está vacía.

## Comentarios y notación

Cada método clave debe incluir su diseño lógico entre líneas discontinuas.

Ejemplo:

```text
// ------------------------------------------------------------
// nombreMicro: Texto, tipo: Texto, valor: R
//      --> guardarMedicion() --> Medicion
// ------------------------------------------------------------
```

No utilizar tipos concretos de transporte en la notación lógica.

## Resultado esperado

Generar únicamente dentro de:

```text
src/business_logic/
```

La implementación debe corresponder exactamente con `doc/business_logic_design.md` y `doc/database_design.md`.
