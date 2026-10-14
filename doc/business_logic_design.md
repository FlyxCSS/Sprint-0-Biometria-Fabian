# Diseño de la Lógica de Negocio

## Diseño del Componente

El componente `business_logic` contiene la lógica de dominio para almacenar y recuperar mediciones ambientales. La implementación correspondiente se encuentra en `src/business_logic/`.

El diseño está alineado con la tabla `Mediciones` descrita en `database_design.md`.

### Tipos

```text
Medicion = (
    nombre_micro: Text,
    tipo: Text,
    valor: R,
    fecha_hora: Text
)

Nulo = { NULO }

ConexionBD
```

### Diseño de la clase LogicaNegocio

```text
                 -------- LogicaNegocio ----------------
                 |
                 | conexion: ConexionBD
                 |
                 |
conexion: ConexionBD --> LogicaNegocio() -->
                 |
                 |
nombre_micro: Text, tipo: Text, valor: R --> guardarMedicion() -->
       Medicion <--
                 |
                 |
Medicion | Nulo <-- leerMedicion() <--
                 |
                 ----------------------------------------
```

### guardarMedicion

```text
nombre_micro: Text, tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

- Valida `nombre_micro`.
- Valida `tipo`.
- Valida que `valor` sea numérico.
- Genera `fecha_hora` con `Europe/Madrid` y formato `Y-m-d H:i:s`.
- Inserta una fila en `Mediciones`.
- Devuelve la medición almacenada.

Correspondencia con la base de datos:

```text
Medicion.nombre_micro <-> Mediciones.nombre_micro
Medicion.tipo         <-> Mediciones.tipo
Medicion.valor        <-> Mediciones.valor
Medicion.fecha_hora   <-> Mediciones.fecha_hora
```

### leerMedicion

```text
leerMedicion() --> Medicion | Nulo
```

La última medición se selecciona con:

```text
fecha_hora DESC
nombre_micro ASC
LIMIT 1
```

Cuando no hay mediciones devuelve conceptualmente `NULO`.

### Pruebas

`LogicaNegocioTest.php` comprueba:

```text
guardar medición válida
rechazar nombre_micro vacío
rechazar tipo vacío
rechazar valor no numérico
leer última medición
permitir misma fecha_hora para micros distintos
devolver NULO sin mediciones
```

## Aclaraciones del Diseño

- La clase `LogicaNegocio` recibe la conexión de persistencia mediante el constructor.
- Las operaciones trabajan con datos de dominio y con la persistencia descrita en `database_design.md`.
- `NULO` es el valor lógico de ausencia; PHP puede implementarlo mediante `null`.
- La fecha y hora se generan al guardar la medición.
- El desempate por `nombre_micro` hace determinista la lectura de la última medición.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/business_logic/`.
- Las firmas lógicas deben utilizar `N`, `Z`, `R`, `B`, `Text` o tipos definidos en este documento.
- `business_logic` no debe depender de `src/communication/`.
- La persistencia debe coincidir con `database_design.md`.
- El encabezado de cada función o método debe incluir su diseño lógico dentro de comentarios delimitados por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las funciones y métodos clave deben disponer de pruebas automatizadas.
