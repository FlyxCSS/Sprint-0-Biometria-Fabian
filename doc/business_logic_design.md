# Diseño de la Lógica de Negocio

## Diseño del Componente

El componente `business_logic` contiene la lógica de negocio utilizada por el backend durante el Sprint 0.

Su responsabilidad es validar las mediciones, almacenarlas en la base de datos y recuperar la última medición disponible.

Este componente es independiente de la capa de comunicación.

No gestiona:

- HTTP;
- rutas REST;
- códigos de estado HTTP;
- cabeceras;
- sockets;
- elementos de interfaz gráfica.

El flujo lógico del backend es:

```text
Communication
      |
      v
Business Logic
      |
      v
Database
```

La lógica de negocio puede utilizar mecanismos de acceso a la base de datos en su implementación, pero esos detalles tecnológicos no forman parte de sus firmas lógicas.

---

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
| tipo: Text, valor: R
|     --> guardarMedicion() --> Medicion
|
| leerMedicion() --> Medicion | null
|
-------------------------------------------------------
```

Responsabilidad:

Gestiona las operaciones de negocio relacionadas con las mediciones almacenadas.

La forma concreta de conexión con la base de datos pertenece a la implementación y no modifica la interfaz lógica del componente.

---

### guardarMedicion

```text
tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

Responsabilidad:

Valida y almacena una nueva medición.

El proceso lógico es:

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

Las validaciones realizadas son:

```text
tipo no puede estar vacío

valor debe ser numérico
```

La fecha se genera en el backend utilizando la zona horaria:

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

La medición almacenada debe corresponder con la entidad definida en:

```text
database_design.md
```

y contener:

```text
id
fecha
tipo
valor
```

---

### leerMedicion

```text
leerMedicion() --> Medicion | null
```

Responsabilidad:

Recupera la última medición almacenada.

La operación devuelve únicamente la medición más reciente.

Si no existe ninguna medición almacenada:

```text
leerMedicion() --> null
```

La implementación puede utilizar el criterio definido en la base de datos para obtener el último registro.

---

### Relación con la base de datos

La lógica de negocio es el componente responsable de realizar las operaciones necesarias sobre las mediciones almacenadas.

Debe respetar el diseño definido en:

```text
doc/database_design.md
```

La entidad utilizada es:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

La implementación podrá utilizar consultas, repositorios o mecanismos de acceso a datos siempre que respeten este diseño.

---

### Relación con la capa de comunicación

La lógica de negocio no conoce detalles de comunicación.

El componente `communication` es quien recibe las peticiones externas y utiliza las operaciones de la lógica de negocio.

El flujo es:

```text
Communication
      |
      v
guardarMedicion()
      |
      v
Business Logic
      |
      v
Database
```

Para consultar:

```text
Communication
      |
      v
leerMedicion()
      |
      v
Business Logic
      |
      v
Database
```

La lógica de negocio no debe hacer referencia a:

```text
GET
POST
HTTP 200
HTTP 201
HTTP 400
HTTP 404
HTTP 500
rutas REST
JSON de transporte
cabeceras HTTP
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

Las pruebas deben comprobar directamente el comportamiento del componente `business_logic` y no depender de la interfaz gráfica ni de la capa de comunicación.

---

## Aclaraciones del Diseño

- La lógica de negocio utiliza PHP.
- La lógica de negocio es independiente de la capa de comunicación.
- La lógica no conoce HTTP, rutas REST ni códigos de estado.
- `guardarMedicion()` valida los datos antes de almacenarlos.
- La fecha se genera en el backend, no en Android.
- La zona horaria utilizada es `Europe/Madrid`.
- `leerMedicion()` devuelve únicamente la última medición almacenada.
- Durante el Sprint 0 una medición contiene únicamente `id`, `fecha`, `tipo` y `valor`.
- La estructura de `Medicion` debe coincidir con el diseño definido en `database_design.md`.
- El valor de la medición se representa lógicamente mediante un número real.
- La unidad concreta de la medición no modifica la interfaz lógica de este componente.
- Los detalles tecnológicos de acceso a la base de datos pertenecen a la implementación y no a las firmas lógicas del diseño.

---

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/business_logic/` y corresponder con este diseño.
- Cada fichero debe incluir una cabecera con nombre, descripción, fecha, autor, aportación y copyright.
- Cada función o método debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe mantenerse sencillo, claro y autoexplicativo.
- La comunicación HTTP debe permanecer exclusivamente en el componente `communication`.
- La lógica de negocio no debe importar ni depender de frameworks o componentes de comunicación.
- La implementación puede utilizar mecanismos de acceso a base de datos cuando sean necesarios.
- Las operaciones sobre la base de datos deben respetar `database_design.md`.
- Las inserciones deben utilizar mecanismos seguros, como consultas preparadas.
- La lógica debe validar los datos recibidos antes de almacenarlos.
- Las operaciones principales de la lógica deben disponer de pruebas automáticas reproducibles.
