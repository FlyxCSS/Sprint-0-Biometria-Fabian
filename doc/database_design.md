# Diseño de la Base de Datos

## Diseño del Componente

El componente `database` define la persistencia relacional del Sprint 0. La implementación correspondiente se encuentra en `src/database/database.sql`.

```text
====================================================================================
TABLE: Mediciones
DESCRIPTION: Almacena las mediciones ambientales recibidas desde los microprocesadores.

COLUMNS:

+ nombre_micro | VARCHAR(100) | NOT NULL | Sin valor por defecto
+ tipo | VARCHAR(50) | NOT NULL | Sin valor por defecto
+ valor | DOUBLE | NOT NULL | Sin valor por defecto
+ fecha_hora | DATETIME | NOT NULL | Sin valor por defecto

PRIMARY KEY: (nombre_micro, fecha_hora)

FOREIGN KEYS:

+ Ninguna

CONSTRAINTS:

+ nombre_micro no puede ser NULL.
+ tipo no puede ser NULL.
+ valor no puede ser NULL.
+ fecha_hora no puede ser NULL.
+ La combinación (nombre_micro, fecha_hora) debe ser única.
====================================================================================
```

Tipo de dominio relacionado:

```text
Medicion = (
    nombre_micro: Text,
    tipo: Text,
    valor: R,
    fecha_hora: Text
)
```

Consulta de última medición:

```text
ORDER BY fecha_hora DESC, nombre_micro ASC
LIMIT 1
```

## Aclaraciones del Diseño

- No existe un campo `id`.
- No se utiliza `AUTO_INCREMENT`.
- `nombre_micro` identifica el origen de la medición.
- La clave primaria es compuesta.
- Dos micros diferentes pueden almacenar una medición en el mismo segundo.
- En el Sprint 0 no existen claves foráneas.

## Reglas Generales

- Lenguaje de definición de datos: SQL compatible con MySQL/MariaDB.
- La implementación debe mantenerse en `src/database/`.
- La tabla, columnas, clave primaria y restricciones deben coincidir exactamente con este diseño.
- El SQL debe ser claro, legible y autoexplicativo.
- Si se añaden funciones o procedimientos SQL, sus encabezados deben incluir su diseño lógico entre líneas discontinuas.
- Las operaciones de persistencia deben quedar cubiertas por pruebas automatizadas desde las capas que las utilizan.
