# Diseño de la Lógica de Negocio

## Diseño del Componente

El componente `logica` contiene la lógica de negocio utilizada por el servidor durante el Sprint 0.

Su responsabilidad es validar las mediciones, almacenarlas en la base de datos y recuperar la última medición disponible.

No gestiona HTTP ni contiene información de conexión a la base de datos.

El flujo es:

```text
Servidor REST
     |
     v
LogicaNegocio
     |
     | PDO
     v
Base de datos
```

### Tipos utilizados

```text
N     = número natural
R     = número real
Text  = texto
```

Una medición se representa como:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

---

### LogicaNegocio

```text
-------------------- LogicaNegocio --------------------
|
| conexion: PDO
|
conexion: PDO --> LogicaNegocio() -->
|
tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
|
leerMedicion() --> Medicion | null
|
-------------------------------------------------------
```

Responsabilidad:

Gestiona las operaciones de negocio relacionadas con las mediciones almacenadas.

La conexión a la base de datos se recibe desde el exterior mediante el constructor.

---

### guardarMedicion

```text
tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

Responsabilidad:

Valida y almacena una nueva medición.

El proceso realizado es:

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
INSERT en Mediciones
     |
     v
obtener id generado
     |
     v
devolver Medicion
```

Las validaciones realizadas son:

```text
tipo no puede estar vacío

valor debe ser numérico
```

La fecha se genera en el backend utilizando:

```text
Europe/Madrid
```

y el formato:

```text
Y-m-d H:i:s
```

Ejemplo:

```text
2026-10-04 18:25:32
```

La consulta utiliza parámetros preparados:

```text
:fecha
:tipo
:valor
```

para evitar introducir directamente los valores recibidos en la sentencia SQL.

---

### leerMedicion

```text
leerMedicion() --> Medicion | null
```

Responsabilidad:

Recupera la última medición almacenada.

La consulta utiliza:

```text
ORDER BY id DESC
LIMIT 1
```

por lo que devuelve únicamente el registro más reciente.

Si la tabla no contiene ninguna medición:

```text
leerMedicion() --> null
```

---

### Relación con otros componentes

`LogicaNegocio` no realiza peticiones HTTP.

El componente REST es quien recibe las solicitudes externas y utiliza la lógica:

```text
POST /medicion
      |
      v
guardarMedicion()
      |
      v
Base de datos
```

Para consultar:

```text
GET /medicion
     |
     v
leerMedicion()
     |
     v
Base de datos
```

La lógica tampoco contiene credenciales.

La conexión se crea fuera del componente y se entrega mediante:

```text
LogicaNegocio(PDO)
```

---

### Pruebas de la lógica

Las operaciones principales de la lógica deben poder comprobarse automáticamente.

Los casos principales son:

```text
guardar una medición válida
rechazar un tipo vacío
rechazar un valor no numérico
recuperar la última medición
devolver null cuando no existen mediciones
```

Las pruebas deben comprobar directamente el comportamiento de `LogicaNegocio`, sin depender de la interfaz gráfica.

## Aclaraciones del Diseño

- La lógica de negocio utiliza PHP.
- La clase recibe una conexión `PDO` desde el exterior.
- Las credenciales de la base de datos no pertenecen a este componente.
- La lógica no gestiona peticiones HTTP.
- `guardarMedicion()` valida los datos antes de almacenarlos.
- La fecha se genera en el backend, no en Android.
- La zona horaria utilizada es `Europe/Madrid`.
- `leerMedicion()` devuelve únicamente la última medición almacenada.
- Durante el Sprint 0 una medición contiene únicamente `id`, `fecha`, `tipo` y `valor`.
- El valor puede llegar en ppm o ppb; para la lógica de negocio sigue siendo simplemente un número real. La interpretación de la unidad corresponde al diseño global del sistema.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/logica/` y corresponder con este diseño.
- Cada fichero debe incluir una cabecera con nombre, descripción, fecha, autor, aportación y copyright.
- Cada función o método debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe mantenerse sencillo, claro y autoexplicativo.
- La comunicación HTTP debe permanecer en el componente REST.
- Las credenciales de la base de datos no deben almacenarse en `LogicaNegocio.php`.
- La conexión a la base de datos debe proporcionarse mediante `PDO`.
- Las inserciones deben utilizar consultas preparadas.
- La lógica debe validar los datos recibidos antes de almacenarlos.
- Las operaciones principales de la lógica deben disponer de pruebas automáticas reproducibles.