# Diseño del Servidor REST

## Diseño del Componente

El componente `rest` proporciona el API HTTP utilizado para comunicar los clientes con la lógica de negocio.

Su responsabilidad es recibir peticiones HTTP, validar su estructura básica, invocar las operaciones correspondientes de `LogicaNegocio` y devolver respuestas en formato JSON.

El servidor REST no contiene lógica de negocio ni ejecuta consultas SQL directamente.

El flujo general es:

```text
Cliente
   |
   | HTTP
   v
Servidor REST
   |
   v
LogicaNegocio
   |
   v
Base de datos
```

### Tipos utilizados

```text
N     = número natural
R     = número real
Text  = texto
```

Una medición recibida mediante POST se representa como:

```text
MedicionEntrada = (
    tipo: Text,
    valor: R
)
```

Una medición devuelta por el servidor se representa como:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

---

### Funciones auxiliares

#### crearConexion

```text
crearConexion() --> PDO
```

Responsabilidad:

Crea una conexión PDO con la base de datos.

En el repositorio público se utilizan valores genéricos para evitar publicar credenciales reales.

La conexión se entrega posteriormente a:

```text
LogicaNegocio(PDO)
```

---

#### responderJSON

```text
datos, codigo_http: N --> responderJSON()
```

Responsabilidad:

Envía una respuesta al cliente en formato JSON utilizando el código HTTP indicado y finaliza la petición.

---

#### obtenerRuta

```text
obtenerRuta() --> Text
```

Responsabilidad:

Obtiene la ruta solicitada a partir de la petición HTTP.

---

### POST /medicion

Entrada:

```text
MedicionEntrada = (
    tipo: Text,
    valor: R
)
```

Ejemplo:

```json
{
    "tipo": "O3",
    "valor": 1.234
}
```

Flujo:

```text
POST /medicion
      |
      v
leer cuerpo JSON
      |
      v
comprobar JSON válido
      |
      v
comprobar tipo y valor
      |
      v
LogicaNegocio.guardarMedicion()
      |
      v
devolver medición almacenada
```

Si la operación es correcta:

```text
HTTP 201
```

Si el JSON es incorrecto, falta un campo o la lógica rechaza los datos:

```text
HTTP 400
```

La validación estructural corresponde al REST:

```text
JSON válido
existe tipo
existe valor
```

La validación del significado de los datos corresponde a `LogicaNegocio`.

Por ejemplo:

```text
tipo vacío
valor no numérico
```

---

### GET /medicion

Entrada:

```text
ninguna
```

Flujo:

```text
GET /medicion
     |
     v
LogicaNegocio.leerMedicion()
     |
     v
última medición
     |
     v
respuesta JSON
```

Si la petición es correcta:

```text
HTTP 200
```

Si no existen mediciones, la respuesta puede ser:

```text
null
```

---

### Códigos HTTP

El servidor utiliza:

```text
200 --> petición GET correcta

201 --> medición creada correctamente

400 --> petición o datos incorrectos

404 --> ruta no encontrada

500 --> error interno del servidor
```

---

### Relación con LogicaNegocio

El servidor REST no ejecuta SQL.

Para guardar:

```text
POST /medicion
      |
      v
guardarMedicion()
```

Para leer:

```text
GET /medicion
     |
     v
leerMedicion()
```

Por tanto:

```text
REST
→ comunicación HTTP

LogicaNegocio
→ reglas y acceso a datos
```

---

### Manejo de errores

Si `LogicaNegocio.guardarMedicion()` lanza:

```text
InvalidArgumentException
```

el REST transforma ese error en:

```text
HTTP 400
```

y devuelve el mensaje en JSON.

Ejemplo:

```json
{
    "error": "El tipo de medición no puede estar vacío"
}
```

Los errores internos no controlados se transforman en:

```text
HTTP 500
```

sin exponer información interna sensible al cliente.

---

### Pruebas del componente

Los principales casos que deben comprobarse son:

```text
POST correcto con tipo y valor válidos

POST con JSON inválido

POST sin tipo

POST sin valor

POST con datos rechazados por LogicaNegocio

GET con una medición existente

GET sin mediciones

ruta inexistente
```

Las pruebas del REST deben comprobar tanto el código HTTP como el contenido JSON devuelto.

## Aclaraciones del Diseño

- El servidor REST está implementado en PHP.
- Durante el Sprint 0 existen únicamente las operaciones `POST /medicion` y `GET /medicion`.
- Los datos intercambiados utilizan JSON.
- El REST no contiene reglas de negocio.
- El REST no ejecuta consultas SQL directamente.
- `LogicaNegocio` recibe una conexión PDO creada por el servidor.
- Las credenciales reales de la base de datos no deben almacenarse en el repositorio público.
- `POST /medicion` utiliza `guardarMedicion()`.
- `GET /medicion` utiliza `leerMedicion()`.
- Los errores de validación de la lógica se devuelven como HTTP 400.
- Los errores internos se devuelven como HTTP 500.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/rest/` y corresponder con este diseño.
- Cada fichero debe incluir una cabecera con nombre, descripción, fecha, autor, aportación y copyright.
- Cada función debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe ser sencillo, legible y autoexplicativo.
- La comunicación HTTP debe mantenerse separada de la lógica de negocio.
- Las consultas SQL deben realizarse únicamente desde el componente de lógica.
- Las respuestas del API deben utilizar JSON.
- Se deben utilizar códigos HTTP adecuados según el resultado de cada operación.
- Las credenciales reales de la base de datos no deben publicarse en el repositorio.
- Las operaciones principales del REST deben poder comprobarse mediante pruebas automáticas reproducibles.