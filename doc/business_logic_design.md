# Diseño de la Lógica de Negocio

## Diseño del Componente

El componente `business_logic` contiene la lógica de dominio necesaria para guardar y recuperar mediciones ambientales.

Su diseño es independiente de cualquier mecanismo de comunicación.

La persistencia utilizada debe coincidir con `database_design.md`.

### Tipos utilizados

Según la notación utilizada en clase:

```text
N      = número natural
Z      = número entero
R      = número real
Texto  = texto
VoF    = verdadero o falso
```

Tipo estructurado:

```text
Medicion = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R,
    fechaHora: Texto
)
```

Valor especial para indicar que no existe ninguna medición:

```text
Nulo = {NULO}
```

### LogicaNegocio

```text
-------------------------- LogicaNegocio --------------------------
|
| accesoPersistencia
|
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
|
|       leerMedicion() --> Medicion | Nulo
|
------------------------------------------------------------------
```

### guardarMedicion

Diseño lógico:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
```

Responsabilidad:

Valida y almacena una nueva medición.

Proceso lógico:

```text
nombreMicro, tipo, valor
          |
          v
validar los datos
          |
          v
generar fechaHora
          |
          v
almacenar Medicion
          |
          v
devolver Medicion
```

Condiciones:

```text
nombreMicro != ""
tipo != ""
valor debe ser numérico
```

La fecha y hora se generan utilizando:

```text
zona horaria = Europe/Madrid
formato = Y-m-d H:i:s
```

La medición almacenada debe corresponder exactamente con:

```text
Mediciones.nombre_micro
Mediciones.tipo
Mediciones.valor
Mediciones.fecha_hora
```

### leerMedicion

Diseño lógico:

```text
leerMedicion() --> Medicion | Nulo
```

Responsabilidad:

Obtiene la última medición almacenada.

La última medición se determina primero por:

```text
fechaHora descendente
```

Si dos mediciones tienen exactamente la misma `fechaHora`, se utiliza como desempate:

```text
nombreMicro ascendente
```

Por tanto, el criterio es determinista.

Si no existe ninguna medición:

```text
leerMedicion() --> NULO
```

### Pruebas

Las pruebas automáticas deben comprobar al menos:

```text
guardar una medición válida
rechazar nombreMicro vacío
rechazar tipo vacío
rechazar valor no numérico
recuperar la última medición
permitir la misma fechaHora para micros diferentes
devolver NULO cuando no existen mediciones
```

## Aclaraciones del Diseño

- `business_logic` no conoce rutas, protocolos, cabeceras ni códigos de estado.
- `guardarMedicion()` recibe únicamente datos de dominio.
- `nombreMicro` procede conceptualmente del microprocesador que originó la medición.
- La fecha y hora se generan en este componente y no las envía el cliente.
- La estructura `Medicion` coincide con la tabla `Mediciones` definida en `database_design.md`.
- La implementación puede utilizar acceso a persistencia, pero ese mecanismo no forma parte de la interfaz lógica del dominio.
- El desempate por `nombreMicro` solo se utiliza cuando dos mediciones comparten exactamente la misma `fechaHora`.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/business_logic/`.
- Las firmas lógicas deben utilizar tipos abstractos y de dominio, no tipos de transporte.
- El componente no debe depender de `communication` ni de mecanismos de transporte.
- El encabezado de cada función o método clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las operaciones sobre persistencia deben respetar `database_design.md`.
- Los datos deben validarse antes de almacenarse.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
