# Diseño de la Base de Datos

## Diseño del Componente

El componente `database` almacena las mediciones recibidas y validadas por el sistema.

Durante el Sprint 0 existe una única tabla llamada `Mediciones`.

La especificación de la tabla es:

```text
TABLE: Mediciones
DESCRIPTION: Almacena las mediciones recibidas y validadas por la lógica de negocio.

COLUMNS:
id | INT | NOT NULL | AUTO_INCREMENT
fecha | DATETIME | NOT NULL
tipo | VARCHAR(50) | NOT NULL
valor | DOUBLE | NOT NULL

PRIMARY KEY: id

FOREIGN KEYS: Ninguna

CONSTRAINTS:
id se genera automáticamente mediante AUTO_INCREMENT.
fecha, tipo y valor son obligatorios.
```

### Representación lógica

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

### Campos

```text
id: N
```

Identificador único y autoincremental.

```text
fecha: Text
```

Fecha y hora de almacenamiento.

```text
tipo: Text
```

Tipo de medición. Durante el Sprint 0 se utiliza `O3`.

```text
valor: R
```

Valor numérico de la medición.

### Implementación SQL

La implementación se encuentra en:

```text
src/database/database.sql
```

y debe corresponder con esta estructura:

```sql
CREATE TABLE IF NOT EXISTS Mediciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    valor DOUBLE NOT NULL
);
```

### Relación con Business Logic

El acceso a los datos se realiza mediante `business_logic`.

Para almacenar:

```text
Business Logic
      |
      | INSERT
      v
Mediciones
```

Para recuperar la última medición:

```text
Business Logic
      |
      | SELECT
      v
Mediciones
```

Las operaciones equivalentes de persistencia son:

```sql
INSERT INTO Mediciones (fecha, tipo, valor)
VALUES (:fecha, :tipo, :valor);
```

```sql
SELECT *
FROM Mediciones
ORDER BY id DESC
LIMIT 1;
```

### Pruebas

`database.sql` puede contener consultas manuales comentadas.

Las pruebas automáticas del acceso a datos se realizan desde:

```text
src/business_logic/LogicaNegocioTest.php
```

## Aclaraciones del Diseño

- La base de datos utiliza SQL.
- Durante el Sprint 0 existe una única tabla llamada `Mediciones`.
- `id` es la clave primaria y es autoincremental.
- `fecha` utiliza `DATETIME`.
- `tipo` utiliza `VARCHAR(50)`.
- `valor` utiliza `DOUBLE`.
- No existen claves foráneas en el Sprint 0.
- La base de datos no contiene lógica de negocio.
- No se añaden campos ni tablas adicionales durante el Sprint 0.

## Reglas Generales

- Lenguaje de implementación: SQL.
- La implementación debe mantenerse en `src/database/`.
- La implementación debe coincidir con este diseño.
- La tabla debe llamarse `Mediciones`.
- No se deben añadir campos o tablas no definidos.
- El código SQL debe ser sencillo y legible.
- La creación debe utilizar `CREATE TABLE IF NOT EXISTS`.
- `id` debe utilizar `AUTO_INCREMENT`.
- Las consultas manuales de prueba deben permanecer comentadas.
