# Prompt para generar Database

Genera `src/database/database.sql` tomando como única especificación funcional `doc/database_design.md`.

Requisitos obligatorios:

- SQL compatible con MySQL/MariaDB.
- Tabla `Mediciones`.
- Columnas exactas:
  - `nombre_micro VARCHAR(100) NOT NULL`
  - `tipo VARCHAR(50) NOT NULL`
  - `valor DOUBLE NOT NULL`
  - `fecha_hora DATETIME NOT NULL`
- Clave primaria compuesta `(nombre_micro, fecha_hora)`.
- No crear `id`.
- No usar `AUTO_INCREMENT`.
- No crear claves foráneas.
- Mantener consultas manuales de comprobación comentadas si resultan útiles.
- La consulta de última medición debe ser compatible con el criterio `fecha_hora DESC, nombre_micro ASC`.
- No introducir tablas o columnas no descritas en el diseño.
- Mantener el código claro y fácil de explicar.

Antes de generar el código, verifica que la implementación propuesta coincide literalmente con los apartados `TABLE`, `DESCRIPTION`, `COLUMNS`, `PRIMARY KEY`, `FOREIGN KEYS` y `CONSTRAINTS` de `database_design.md`.
