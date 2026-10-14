# Diseño de Communication

## Diseño del Componente

El componente `communication` expone al exterior las operaciones del backend y delega la lógica de dominio en `business_logic`.

El flujo es:

```text
Cliente
   |
   v
Communication
   |
   v
Business Logic
   |
   v
Database
```

### Tipos utilizados

```text
N      = número natural
R      = número real
Texto  = texto
```

```text
MedicionEntrada = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R
)
```

```text
Medicion = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R,
    fechaHora: Texto
)
```

```text
Respuesta = (
    codigo: N,
    cuerpo: Texto
)
```

### Funciones auxiliares

```text
crearConexion() --> AccesoPersistencia
```

Responsabilidad:

Obtiene el acceso a persistencia utilizado para construir la lógica de negocio.

```text
datos, codigo: N --> responderJSON()
```

Responsabilidad:

Construye la respuesta externa y finaliza el procesamiento de la petición.

```text
obtenerRuta() --> Texto
```

Responsabilidad:

Obtiene la ruta solicitada por el cliente.

### Operación de guardar

Interfaz lógica que se invoca en `business_logic`:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
```

Mensaje conceptual según la notación de protocolos utilizada en clase:

```text
GUARDAR
<nombreMicro:Texto>
<tipo:Texto>
<valor:R>
```

Mapeo REST utilizado en el Sprint 0:

```text
POST /api/medicion
```

Entrada concreta:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

Salida correcta:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234,
  "fecha_hora": "2026-10-14 12:00:00"
}
```

Flujo:

```text
recibir petición
      |
      v
leer entrada
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
respuesta
```

La comprobación estructural verifica:

```text
JSON válido
existe nombre_micro
existe tipo
existe valor
```

La validación del significado de esos datos corresponde a `business_logic`.

### Operación de lectura

Interfaz lógica que se invoca en `business_logic`:

```text
leerMedicion() --> Medicion | Nulo
```

Mensaje conceptual:

```text
LEER_ULTIMA_MEDICION
```

Mapeo REST utilizado en el Sprint 0:

```text
GET /api/medicion
```

Entrada:

```text
sin cuerpo
```

Salida correcta:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234,
  "fecha_hora": "2026-10-14 12:00:00"
}
```

Si no existe ninguna medición, el dominio puede devolver un valor nulo.

### Respuestas del protocolo

La implementación utiliza los siguientes resultados:

```text
201 --> medición creada correctamente
200 --> lectura correcta
400 --> petición incorrecta
404 --> ruta no encontrada
500 --> error interno
```

### Pruebas

Las pruebas de integración deben comprobar al menos:

```text
POST correcto devuelve 201
POST devuelve la medición correcta
POST sin nombre_micro devuelve 400
POST con nombre_micro vacío devuelve 400
POST sin tipo devuelve 400
POST con tipo vacío devuelve 400
POST sin valor devuelve 400
POST con valor no numérico devuelve 400
POST con JSON inválido devuelve 400
GET correcto devuelve 200
GET devuelve una medición con la estructura correcta
```

## Aclaraciones del Diseño

- `communication` es la única capa del backend que conoce REST y los códigos HTTP.
- La capa no contiene reglas de dominio.
- `communication` delega el guardado y la lectura en `business_logic`.
- La fecha y hora no se reciben en el POST; son generadas por `business_logic`.
- `nombre_micro` debe recibirse del cliente y trasladarse como `nombreMicro` a la operación de dominio.
- El acceso concreto a la base de datos utilizado para construir `LogicaNegocio` es un detalle de implementación.
- La ruta pública utilizada durante el Sprint 0 es `/api/medicion`.

## Reglas Generales

- Lenguaje de programación: PHP.
- La implementación debe mantenerse en `src/communication/`.
- `communication` puede depender de `business_logic`.
- `business_logic` nunca debe depender de `communication`.
- El encabezado de cada función o método clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las reglas de dominio deben permanecer en `business_logic`.
- Las operaciones externas principales deben disponer de pruebas automáticas de integración.
