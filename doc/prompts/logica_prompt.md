# Prompt para generar Business Logic

Quiero que generes o adaptes únicamente la lógica de negocio del backend para mi Sprint 0 del proyecto de Biometría y Medio Ambiente.

## Arquitectura

La arquitectura del backend es:

```text
Communication
      ↓
Business Logic
      ↓
Database
```

Este prompt afecta únicamente a:

```text
doc/business_logic_design.md
src/business_logic/
```

## Objetivo

Implementar en PHP la clase:

```text
LogicaNegocio
```

con las operaciones:

```text
guardarMedicion()
leerMedicion()
```

## Regla principal

La lógica de negocio debe ser 100 % independiente de la comunicación.

No debe conocer:

```text
HTTP
GET
POST
rutas
códigos 200/201/400/404/500
cabeceras HTTP
sockets
JSON de transporte
```

Puede utilizar PDO y consultas SQL como detalle de implementación para acceder a la base de datos.

## Entidad de dominio

La medición se representa como:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

La estructura debe coincidir con:

```text
doc/database_design.md
```

## guardarMedicion

Firma lógica:

```text
tipo: Text, valor: R
    --> guardarMedicion()
    --> Medicion
```

Debe:

1. comprobar que `tipo` no está vacío;
2. comprobar que `valor` es numérico;
3. generar la fecha en el backend;
4. utilizar la zona horaria `Europe/Madrid`;
5. usar el formato `Y-m-d H:i:s`;
6. insertar la medición mediante consulta preparada;
7. devolver la medición creada con `id`, `fecha`, `tipo` y `valor`.

## leerMedicion

Firma lógica:

```text
leerMedicion() --> Medicion | null
```

Debe recuperar únicamente la última medición almacenada.

Si no existen mediciones debe devolver:

```text
null
```

## Archivos

Genera o adapta:

```text
src/business_logic/LogicaNegocio.php
src/business_logic/LogicaNegocioTest.php
```

## Tests

Los tests deben comprobar al menos:

```text
guardar una medición válida
rechazar un tipo vacío
rechazar un valor no numérico
recuperar la última medición
devolver null si no existen mediciones
```

Deben poder ejecutarse manualmente con:

```bash
php LogicaNegocioTest.php
```

## Comentarios y diseño lógico

Cada función o método clave debe incluir:

- diseño lógico;
- breve descripción;
- bloque delimitado por líneas discontinuas.

El código debe ser sencillo, claro y fácil de explicar.

## Restricciones

No mezcles HTTP con la lógica de negocio.

No accedas directamente desde esta clase a Android o a la GUI.

No cambies la estructura de la tabla `Mediciones`.
