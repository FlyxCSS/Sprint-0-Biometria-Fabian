# Diseño de Frontend Business Logic

## Diseño del Componente

El componente `frontend_business_logic` es el proxy de lógica de negocio utilizado por la interfaz web.

La GUI utiliza este componente y no conoce el mecanismo de comunicación empleado para obtener los datos.

El flujo es:

```text
Web
 |
 v
Frontend Business Logic
 |
 v
Communication
 |
 v
Business Logic
```

### Tipos utilizados

```text
R      = número real
Texto  = texto
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
Nulo = {NULO}
```

### LogicaFake

La interfaz lógica consumida por la GUI debe ser idéntica a la operación equivalente del backend:

```text
-------------------------- LogicaFake --------------------------
|
|       leerMedicion() --> Medicion | Nulo
|
---------------------------------------------------------------
```

Diseño lógico:

```text
leerMedicion() --> Medicion | Nulo
```

Responsabilidad:

Obtiene la última medición para la interfaz web.

La GUI recibe únicamente:

```text
Medicion
```

o:

```text
NULO
```

Los detalles utilizados internamente por el proxy no forman parte de la firma lógica.

### Correspondencia con business_logic

En `business_logic_design.md` existe:

```text
leerMedicion() --> Medicion | Nulo
```

En este componente existe exactamente:

```text
leerMedicion() --> Medicion | Nulo
```

Por tanto, la operación mantiene:

```text
mismo nombre
mismos parámetros
mismo tipo de retorno
```

El frontend implementa únicamente el subconjunto que necesita la web.

No necesita exponer:

```text
guardarMedicion()
```

### Implementación del proxy

Durante el Sprint 0, la implementación encapsula la lectura remota mediante:

```text
GET /api/medicion
```

Este detalle pertenece a la implementación del proxy.

La GUI nunca debe realizar directamente la petición.

### Pruebas

Las pruebas automáticas deben comprobar al menos:

```text
leer una medición correcta
comprobar nombreMicro
comprobar tipo
comprobar valor
comprobar fechaHora
utilizar /api/medicion
aceptar NULO
detectar un error de comunicación
```

## Aclaraciones del Diseño

- El componente actúa como proxy o lógica fake del backend.
- La interfaz de dominio presentada a la GUI es `leerMedicion() --> Medicion | Nulo`.
- La implementación puede utilizar comunicación remota de forma interna.
- `nombre_micro` y `fecha_hora` recibidos externamente se representan en el dominio como `nombreMicro` y `fechaHora`.
- La GUI no conoce rutas, códigos de estado ni llamadas `fetch()`.
- Solo se replica el subconjunto de la lógica de negocio necesario para la interfaz web.

## Reglas Generales

- Lenguaje de programación: JavaScript.
- La implementación debe mantenerse en `src/frontend_business_logic/`.
- La interfaz lógica debe mantener paridad con la operación equivalente definida en `business_logic_design.md`.
- La GUI no debe contener comunicación HTTP directa.
- El encabezado de cada función o método clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Los detalles de comunicación deben permanecer encapsulados dentro de este componente.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
