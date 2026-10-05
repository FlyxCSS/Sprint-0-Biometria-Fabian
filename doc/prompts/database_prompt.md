# Prompt para generar la base de datos

Quiero que generes únicamente la base de datos para mi proyecto de Biometría y Medio Ambiente, siguiendo el diseño previamente definido para el Sprint 0.

## Contexto
El sistema completo tiene esta arquitectura:

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon
        v
Aplicación Android
        |
        | POST /api/medicion
        v
Servidor REST
        |
        v
Lógica de negocio
        |
        v
Base de datos
        |
        | GET /api/medicion
        v
Interfaz Web
```

En esta tarea debes trabajar únicamente sobre la base de datos.

## Diseño requerido
La base de datos debe ser sencilla y contener una única tabla llamada `Mediciones`.

Cada fila representa una medición almacenada por la lógica de negocio.

La tabla debe contener exactamente:
- `id`: identificador único y clave primaria.
- `fecha`: fecha y hora de almacenamiento.
- `tipo`: tipo de medición, por ejemplo `O3`.
- `valor`: valor numérico.

## Restricciones
No añadas tablas ni columnas adicionales. Mantén el Sprint 0 deliberadamente simple.

## Implementación esperada
Genera un archivo SQL que:
1. Cree `Mediciones`.
2. Use `INT AUTO_INCREMENT PRIMARY KEY` para `id`.
3. Use `DATETIME NOT NULL` para `fecha`.
4. Use `VARCHAR(50) NOT NULL` para `tipo`.
5. Use `DOUBLE NOT NULL` para `valor`.
6. Use `CREATE TABLE IF NOT EXISTS`.

Diseño de referencia:

```text
====================================================================================
TABLE: Mediciones
DESCRIPTION: Almacena las mediciones recibidas y validadas por la lógica de negocio.

COLUMNS:
+ id    | INT         | NOT NULL | Auto-Increment
+ fecha | DATETIME    | NOT NULL
+ tipo  | VARCHAR(50) | NOT NULL
+ valor | DOUBLE      | NOT NULL

PRIMARY KEY:
+ id

FOREIGN KEYS:
+ Ninguna

CONSTRAINTS:
+ id se genera automáticamente mediante AUTO_INCREMENT.
+ Todos los campos son obligatorios.
====================================================================================
```

## Pruebas
Añade consultas de prueba manual comentadas para:
- insertar una medición;
- leer todas;
- leer la última con `ORDER BY id DESC LIMIT 1`.

Añade comentarios breves y no generes todavía lógica de negocio, REST, Android ni web.
