# Prompt — Base de Datos

Genera la definición SQL del componente `database` a partir de `doc/database_design.md`.

## Tecnología

Usar SQL compatible con MySQL/MariaDB.

## Tabla requerida

```text
TABLE: Mediciones
```

Campos:

```text
nombre_micro VARCHAR(100) NOT NULL
tipo         VARCHAR(50)  NOT NULL
valor        DOUBLE       NOT NULL
fecha_hora   DATETIME     NOT NULL
```

Clave primaria:

```text
PRIMARY KEY (nombre_micro, fecha_hora)
```

No utilizar:

```text
id
AUTO_INCREMENT
```

No existen claves foráneas en el Sprint 0.

## Significado

- `nombre_micro`: nombre BLE del microprocesador que originó la medición.
- `tipo`: tipo de medición ambiental.
- `valor`: valor numérico.
- `fecha_hora`: fecha y hora generadas por el backend al almacenar la medición.

La clave primaria compuesta debe permitir:

```text
Fabian_GTI     2026-10-14 12:00:00
Profesor_GTI   2026-10-14 12:00:00
```

porque los nombres de microprocesador son distintos.

## Consulta de última medición

La consulta debe utilizar un orden determinista:

```text
fecha_hora DESC
nombre_micro ASC
```

seguido de:

```text
LIMIT 1
```

## Pruebas manuales

Puede incluir consultas de prueba comentadas para:

- insertar una medición;
- insertar dos micros diferentes con la misma `fecha_hora`;
- consultar todas las mediciones;
- consultar la última medición.

## Resultado esperado

Genera:

```text
src/database/database.sql
```

El resultado debe corresponder exactamente con `doc/database_design.md`.
