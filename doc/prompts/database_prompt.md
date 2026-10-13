# Prompt para generar la base de datos

Quiero que generes o adaptes únicamente el componente de base de datos de mi Sprint 0 del proyecto de Biometría y Medio Ambiente.

## Arquitectura

La arquitectura actual es:

```text
SparkFun
   ↓
Android
   ↓
Communication
   ↓
Business Logic
   ↓
Database
   ↓
Frontend Business Logic
   ↓
Web
```

Este prompt afecta únicamente a:

```text
doc/database_design.md
src/database/
```

## Objetivo

Crear una base de datos MySQL sencilla para almacenar mediciones ambientales.

Durante el Sprint 0 se utiliza una única tabla:

```text
Mediciones
```

con esta estructura:

```text
id       INT          PRIMARY KEY AUTO_INCREMENT
fecha    DATETIME     NOT NULL
tipo     VARCHAR(50)  NOT NULL
valor    DOUBLE       NOT NULL
```

## Requisitos

Genera:

```text
src/database/database.sql
```

El archivo debe:

- crear la tabla `Mediciones`;
- utilizar `id` como clave primaria autoincremental;
- almacenar la fecha y hora en `fecha`;
- almacenar el tipo de medición en `tipo`;
- almacenar el valor numérico en `valor`;
- ser compatible con MySQL.

No añadas tablas adicionales en el Sprint 0.

## Correspondencia con el diseño

El código debe corresponder con:

```text
doc/database_design.md
```

El diseño utiliza el formato:

```text
TABLE
DESCRIPTION
COLUMNS
PRIMARY KEY
FOREIGN KEYS
CONSTRAINTS
```

No cambies los nombres de las columnas ni su significado.

## Alcance

No implementes PHP.

No implementes REST.

No implementes lógica de negocio.

No implementes Android ni web.

Genera únicamente el SQL necesario para la persistencia del Sprint 0.
