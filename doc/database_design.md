# Diseño de la Base de Datos

## Diseño del Componente

El componente `database` almacena las mediciones recibidas por el sistema.

Durante el Sprint 0 se utiliza una única tabla llamada `Mediciones`.

```text
Mediciones = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

La estructura física de la tabla es:

```text
---------------- Mediciones ----------------
id       : INT          PK AUTO_INCREMENT
fecha    : DATETIME     NOT NULL
tipo     : VARCHAR(50)  NOT NULL
valor    : DOUBLE       NOT NULL
---------------------------------------------
```

### Campos

```text
id: N
```

Identificador único de cada medición.

Es la clave primaria y se genera automáticamente.

---

```text
fecha: Text
```

Fecha y hora en la que la medición fue almacenada.

En SQL se representa mediante:

```text
DATETIME
```

Ejemplo:

```text
2026-10-04 18:25:32
```

---

```text
tipo: Text
```

Indica el tipo de medición.

Ejemplo:

```text
O3
```

En SQL se representa mediante:

```text
VARCHAR(50)
```

---

```text
valor: R
```

Valor numérico de la medición.

En SQL se representa mediante:

```text
DOUBLE
```

Ejemplo:

```text
1.234
```

---

### Ejemplo de contenido

| id | fecha               | tipo | valor |
|---:|---------------------|------|------:|
| 1 | 2026-10-04 18:25:32 | O3 | 1.234 |
| 2 | 2026-10-04 18:26:10 | O3 | 1.231 |

Cada fila representa una medición almacenada por la lógica de negocio.

---

### Relación con la lógica de negocio

La base de datos no es utilizada directamente por Android ni por la interfaz web.

El acceso se realiza mediante `LogicaNegocio`.

Para almacenar una medición:

```text
Android
   |
   v
REST
   |
   v
LogicaNegocio
   |
   | INSERT
   v
Mediciones
```

Para recuperar la última medición:

```text
Web
 |
 v
REST
 |
 v
LogicaNegocio
 |
 | SELECT
 v
Mediciones
```

La lógica utiliza:

```text
INSERT INTO Mediciones
```

para almacenar una medición.

Para recuperar la última utiliza:

```text
SELECT *
FROM Mediciones
ORDER BY id DESC
LIMIT 1
```

## Aclaraciones del Diseño

- La base de datos utiliza SQL.
- Durante el Sprint 0 existe una única tabla llamada `Mediciones`.
- `id` es la clave primaria y es autoincremental.
- `fecha` almacena fecha y hora mediante `DATETIME`.
- `tipo` identifica el tipo de medición, por ejemplo `O3`.
- `valor` almacena el valor numérico de la medición.
- Todos los campos son obligatorios.
- La base de datos no contiene lógica de negocio.
- Android y la web no acceden directamente a la base de datos.
- El acceso a los datos se realiza a través de `LogicaNegocio`.
- No se añaden campos adicionales durante el Sprint 0.

## Reglas Generales

- Lenguaje de implementación: SQL.
- La implementación debe mantenerse en `src/database/`.
- La implementación debe coincidir con este diseño.
- La tabla debe llamarse `Mediciones`.
- No se deben añadir campos o tablas que no estén definidos en el diseño.
- El código SQL debe ser sencillo y legible.
- La creación de la tabla debe utilizar `CREATE TABLE IF NOT EXISTS`.
- Las consultas de prueba manual deben permanecer comentadas para evitar modificar accidentalmente los datos reales.
- Las pruebas automáticas del acceso a datos deben realizarse desde la lógica de negocio.