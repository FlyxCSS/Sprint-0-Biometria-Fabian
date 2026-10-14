# Diseño de la Lógica de Negocio

## Diseño del Componente

El componente `business_logic` contiene la lógica de negocio del Sprint 0.

Su responsabilidad es validar mediciones, generar su fecha de almacenamiento, guardarlas y recuperar la última medición disponible.

El componente trabaja únicamente con datos de dominio y con la persistencia definida en:

```text
database_design.md
```

### Tipos utilizados

```text
N     = número natural
R     = número real
Text  = texto
```

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

### LogicaNegocio

```text
-------------------- LogicaNegocio --------------------
|
| tipo: Text, valor: R
|     --> guardarMedicion() --> Medicion
|
| leerMedicion() --> Medicion | null
|
-------------------------------------------------------
```

### guardarMedicion

```text
-------------------- guardarMedicion --------------------
|
| tipo: Text, valor: R
|     --> guardarMedicion() --> Medicion
|
---------------------------------------------------------
```

Responsabilidad:

Valida y almacena una nueva medición.

Proceso lógico:

```text
tipo + valor
     |
     v
validar datos
     |
     v
generar fecha y hora
     |
     v
almacenar medición
     |
     v
obtener identificador
     |
     v
devolver Medicion
```

Validaciones:

```text
tipo no puede estar vacío
valor debe ser numérico
```

La fecha utiliza:

```text
zona horaria: Europe/Madrid
formato: Y-m-d H:i:s
```

La medición almacenada contiene:

```text
id
fecha
tipo
valor
```

### leerMedicion

```text
-------------------- leerMedicion --------------------
|
| leerMedicion() --> Medicion | null
|
-------------------------------------------------------
```

Responsabilidad:

Recupera la última medición almacenada.

Si no existe ninguna medición:

```text
leerMedicion() --> null
```

### Relación con la persistencia

La estructura utilizada debe coincidir con:

```text
doc/database_design.md
```

La implementación puede utilizar los mecanismos de acceso a datos necesarios siempre que respete la entidad `Medicion` y las operaciones definidas en este diseño.

### Pruebas

Se deben comprobar al menos:

```text
guardar una medición válida
rechazar un tipo vacío
rechazar un valor no numérico
recuperar la última medición
devolver null cuando no existen mediciones
```

## Aclaraciones del Diseño

- La lógica de negocio está implementada en PHP.
- `guardarMedicion()` valida los datos antes de almacenarlos.
- La fecha se genera dentro de este componente.
- La zona horaria utilizada es `Europe/Madrid`.
- `leerMedicion()` devuelve únicamente la última medición almacenada.
- Durante el Sprint 0 una medición contiene `id`, `fecha`, `tipo` y `valor`.
- La estructura de `Medicion` debe coincidir con `database_design.md`.
- El valor de una medición se representa mediante un número real.
- Los detalles tecnológicos de acceso a datos pertenecen a la implementación y no a las firmas lógicas.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/business_logic/`.
- Cada función o método clave debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe mantenerse sencillo, claro y autoexplicativo.
- Las operaciones sobre datos deben respetar `database_design.md`.
- Las inserciones deben utilizar mecanismos seguros.
- Los datos deben validarse antes de almacenarse.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
