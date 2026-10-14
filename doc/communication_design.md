# Diseño de Communication

## Diseño del Componente

El componente `communication` define los puntos de entrada externos del backend. Su implementación se encuentra en `src/communication/`.

```text
Communication --> Business Logic --> Database
```

### Tipos

```text
Medicion = (
    nombre_micro: Text,
    tipo: Text,
    valor: R,
    fecha_hora: Text
)

Nulo = { NULO }
DatosJSON = Text
ConexionBD
```

### Funciones del componente

```text
crearConexion() --> ConexionBD
```

```text
datos: DatosJSON, codigoHTTP: N --> responderJSON()
```

```text
obtenerRuta() --> Text
```

### POST /api/medicion

Entrada:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

Invoca:

```text
nombre_micro: Text, tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

Respuesta correcta:

```text
201
```

### GET /api/medicion

Invoca:

```text
leerMedicion() --> Medicion | Nulo
```

Respuesta correcta:

```text
200
```

### Códigos usados

```text
200
201
400
404
500
```

### Pruebas

`ServidorRestTest.php` prueba los casos correctos y los errores de estructura del POST y GET.

## Aclaraciones del Diseño

- Este componente recibe las peticiones externas y construye las respuestas.
- Delega las reglas de dominio en `business_logic`.
- Puede depender de `business_logic`.
- `fecha_hora` no se recibe al guardar; es devuelta por la lógica de negocio.
- Las credenciales reales no deben almacenarse en el repositorio.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/communication/`.
- Debe respetarse el flujo `communication --> business_logic --> database`.
- El encabezado de cada función o método debe incluir su diseño lógico entre líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las reglas de dominio no deben duplicarse en este componente.
- Las operaciones externas clave deben disponer de pruebas automatizadas de integración.
