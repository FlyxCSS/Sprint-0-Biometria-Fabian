# Diseño de la Base de Datos

## Diseño del Componente

El componente `database` almacena las mediciones recibidas por el sistema.

Durante el Sprint 0 se utiliza una única tabla llamada `Mediciones`.

La representación lógica de una medición es:

```text
Mediciones = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

La estructura de la tabla, siguiendo la notación utilizada para el diseño de bases de datos, es:

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

---

### Campos

#### id

```text
id: N
```

Identificador único de cada medición.

En SQL se representa mediante:

```text
INT
```

Es la clave primaria de la tabla y se genera automáticamente mediante:

```text
AUTO_INCREMENT
```

---

#### fecha

```text
fecha: Text
```

Fecha y hora en la que la medición fue almacenada por la lógica de negocio.

En SQL se representa mediante:

```text
DATETIME
```

Ejemplo:

```text
2026-10-04 18:25:32
```

---

#### tipo

```text
tipo: Text
```

Indica el tipo de medición almacenada.

En el Sprint 0 se utiliza:

```text
O3
```

En SQL se representa mediante:

```text
VARCHAR(50)
```

---

#### valor

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

### Implementación SQL

La implementación de la tabla se encuentra en:

```text
src/database/database.sql
```

La tabla se crea mediante:

```sql
CREATE TABLE IF NOT EXISTS Mediciones (

    id INT AUTO_INCREMENT PRIMARY KEY,

    fecha DATETIME NOT NULL,

    tipo VARCHAR(50) NOT NULL,

    valor DOUBLE NOT NULL

);
```

La implementación coincide con el diseño definido anteriormente.

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

La lógica utiliza una operación equivalente a:

```sql
INSERT INTO Mediciones (
    fecha,
    tipo,
    valor
)
VALUES (
    :fecha,
    :tipo,
    :valor
);
```

Para recuperar la última medición utiliza:

```sql
SELECT *
FROM Mediciones
ORDER BY id DESC
LIMIT 1;
```

---

### Pruebas

El archivo `database.sql` contiene consultas de prueba manual comentadas.

Estas permiten comprobar:

```text
insertar una medición

leer todas las mediciones

leer la última medición almacenada
```

Las consultas permanecen comentadas para evitar modificar accidentalmente los datos reales.

Las pruebas automáticas del acceso a datos se realizan desde:

```text
src/logica/LogicaNegocioTest.php
```

De esta forma, la base de datos permanece como componente de almacenamiento y las operaciones se prueban desde la lógica de negocio que la utiliza.

---

## Aclaraciones del Diseño

- La base de datos utiliza SQL.
- Durante el Sprint 0 existe una única tabla llamada `Mediciones`.
- `id` es la clave primaria y es autoincremental.
- `fecha` almacena fecha y hora mediante `DATETIME`.
- `tipo` identifica el tipo de medición, por ejemplo `O3`.
- `valor` almacena el valor numérico de la medición.
- Todos los campos son obligatorios.
- La tabla no contiene claves foráneas.
- La base de datos no contiene lógica de negocio.
- Android y la web no acceden directamente a la base de datos.
- El acceso a los datos se realiza a través de `LogicaNegocio`.
- No se añaden campos ni tablas adicionales durante el Sprint 0.

## Reglas Generales

- Lenguaje de implementación: SQL.
- La implementación debe mantenerse en `src/database/`.
- La implementación debe coincidir con este diseño.
- La tabla debe llamarse `Mediciones`.
- No se deben añadir campos o tablas que no estén definidos en el diseño.
- El código SQL debe ser sencillo y legible.
- La creación de la tabla debe utilizar `CREATE TABLE IF NOT EXISTS`.
- `id` debe utilizar `AUTO_INCREMENT` y actuar como clave primaria.
- `fecha`, `tipo` y `valor` deben ser obligatorios.
- Las consultas de prueba manual deben permanecer comentadas para evitar modificar accidentalmente los datos reales.
- Las pruebas automáticas del acceso a datos deben realizarse desde la lógica de negocio.
