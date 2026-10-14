# Diseño de la Base de Datos

## Diseño del Componente

El componente `database` define la persistencia relacional utilizada por el Sprint 0.

La tabla almacenará las mediciones ambientales recibidas desde distintos microprocesadores.

```text
TABLE: Mediciones

DESCRIPTION:
Almacena las mediciones ambientales recibidas desde los microprocesadores.

COLUMNS:
nombre_micro | VARCHAR(100) | NOT NULL | Nombre BLE del microprocesador que generó la medición.
tipo         | VARCHAR(50)  | NOT NULL | Tipo de medición ambiental.
valor        | DOUBLE       | NOT NULL | Valor numérico de la medición.
fecha_hora   | DATETIME     | NOT NULL | Fecha y hora de almacenamiento generadas por el backend.

PRIMARY KEY:
(nombre_micro, fecha_hora)

FOREIGN KEYS:
Ninguna.

CONSTRAINTS:
- nombre_micro no puede ser nulo.
- tipo no puede ser nulo.
- valor no puede ser nulo.
- fecha_hora no puede ser nula.
- La combinación (nombre_micro, fecha_hora) debe ser única.
```

La clave primaria es compuesta:

```text
(nombre_micro, fecha_hora)
```

Por tanto, estas dos mediciones son válidas simultáneamente:

```text
Fabian_GTI    2026-10-14 12:00:00
Profesor_GTI  2026-10-14 12:00:00
```

porque el valor de `nombre_micro` es diferente.

En cambio, no pueden existir dos filas con exactamente la misma combinación:

```text
nombre_micro + fecha_hora
```

La entidad de dominio asociada es:

```text
Medicion = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R,
    fechaHora: Texto
)
```

## Aclaraciones del Diseño

- La fecha y hora se almacenan mediante `DATETIME`, sin milisegundos.
- La fecha y hora son generadas por el backend al almacenar la medición.
- `nombre_micro` corresponde al nombre BLE del microprocesador, por ejemplo `Fabian_GTI`.
- La clave primaria compuesta permite que dos microprocesadores diferentes almacenen una medición en el mismo segundo.
- Durante el Sprint 0 el mismo microprocesador no genera dos mediciones en el mismo segundo.
- El nombre físico de las columnas debe mantenerse exactamente como se especifica en este diseño.

## Reglas Generales

- Lenguaje de definición de datos: SQL para MySQL/MariaDB.
- La implementación debe mantenerse en `src/database/`.
- La estructura implementada debe respetar exactamente `TABLE`, `COLUMNS`, `PRIMARY KEY`, `FOREIGN KEYS` y `CONSTRAINTS`.
- Los nombres de tabla y columnas no deben modificarse sin actualizar los demás diseños dependientes.
- El código SQL debe ser claro y legible.
- Las consultas manuales de comprobación pueden mantenerse comentadas dentro de `database.sql`.
- Las operaciones principales de persistencia deben quedar cubiertas mediante pruebas automáticas desde `business_logic`.
