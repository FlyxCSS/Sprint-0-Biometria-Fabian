# Diseño de Communication

## Diseño del Componente

El componente `communication` gestiona la comunicación externa del backend.

Su responsabilidad es recibir peticiones, interpretar su estructura, invocar las operaciones correspondientes de `business_logic` y construir la respuesta.

El flujo es:

```text
Cliente
   |
   v
Communication
   |
   v
Business Logic
```

### Tipos utilizados

```text
N     = número natural
R     = número real
Text  = texto
```

```text
MedicionEntrada = (
    tipo: Text,
    valor: R
)
```

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

### responderJSON

```text
-------------------- responderJSON --------------------
|
| datos, codigo: N --> responderJSON()
|
-------------------------------------------------------
```

Responsabilidad:

Construye y envía una respuesta JSON con el código indicado.

### obtenerRuta

```text
-------------------- obtenerRuta --------------------
|
| obtenerRuta() --> Text
|
-----------------------------------------------------
```

Responsabilidad:

Obtiene la ruta solicitada por el cliente.

### guardar una medición

Entrada:

```text
MedicionEntrada = (
    tipo: Text,
    valor: R
)
```

Flujo:

```text
petición de creación
        |
        v
leer datos
        |
        v
comprobar estructura
        |
        v
business_logic.guardarMedicion()
        |
        v
Medicion
        |
        v
respuesta JSON
```

La comprobación estructural incluye:

```text
entrada válida
existe tipo
existe valor
```

La validación del significado de `tipo` y `valor` corresponde a `business_logic`.

### leer la última medición

Flujo:

```text
petición de lectura
        |
        v
business_logic.leerMedicion()
        |
        v
Medicion | null
        |
        v
respuesta JSON
```

### Protocolo utilizado

La implementación del Sprint 0 utiliza REST sobre HTTP:

```text
POST /api/medicion
GET /api/medicion
```

Códigos utilizados:

```text
200 --> lectura correcta
201 --> medición creada
400 --> petición incorrecta
404 --> ruta inexistente
500 --> error interno
```

### Relación con Business Logic

`communication` invoca:

```text
guardarMedicion()
leerMedicion()
```

definidas en:

```text
doc/business_logic_design.md
```

No realiza operaciones de persistencia directamente.

### Pruebas

Se deben comprobar al menos:

```text
POST correcto
POST con entrada inválida
POST sin tipo
POST sin valor
GET correcto
respuesta JSON válida
ruta inexistente
```

## Aclaraciones del Diseño

- El componente está implementado en PHP.
- La implementación utiliza REST sobre HTTP y JSON.
- Las credenciales y el mecanismo concreto de conexión a persistencia son detalles de implementación.
- `communication` crea o recibe los recursos necesarios para construir `LogicaNegocio`, sin incorporarlos a sus firmas lógicas.
- Las reglas de validación de dominio pertenecen a `business_logic`.
- Las operaciones de persistencia pertenecen a `business_logic`.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/communication/`.
- Cada función clave debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe ser sencillo, legible y autoexplicativo.
- La comunicación debe mantenerse separada de la lógica de negocio.
- Las respuestas deben utilizar JSON.
- Se deben utilizar códigos de respuesta adecuados al resultado.
- Las credenciales reales no deben publicarse en el repositorio.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
