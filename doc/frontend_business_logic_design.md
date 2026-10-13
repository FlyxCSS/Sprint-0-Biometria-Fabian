# Diseño de la Lógica de Negocio del Frontend

## Diseño del Componente

El componente `frontend_business_logic` actúa como proxy de la lógica de negocio utilizada por la interfaz web.

Su responsabilidad es ofrecer a la GUI operaciones de dominio sencillas y encapsular los detalles de comunicación con el backend.

La GUI no debe realizar directamente peticiones HTTP.

El flujo es:

```text
GUI
 |
 v
Frontend Business Logic
 |
 v
HTTP / REST
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

---

### Operaciones

La interfaz web necesita únicamente recuperar la última medición almacenada.

```text
leerMedicion() --> Medicion | null
```

Donde:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

---

### leerMedicion

```text
leerMedicion() --> Medicion | null
```

Responsabilidad:

Solicita al backend la última medición disponible y devuelve el resultado a la GUI.

La GUI únicamente conoce esta operación y no conoce:

```text
fetch()
GET
HTTP
/api/medicion
códigos de estado
cabeceras HTTP
```

Estos detalles pertenecen a la implementación del proxy.

---

### Relación con la lógica de negocio del backend

La operación:

```text
leerMedicion() --> Medicion | null
```

mantiene la misma interfaz lógica que la operación equivalente definida en:

```text
business_logic_design.md
```

El frontend utiliza únicamente el subconjunto de operaciones de negocio necesarias para la interfaz gráfica.

En el Sprint 0 la web solo necesita:

```text
leerMedicion()
```

No necesita:

```text
guardarMedicion()
```

---

### Implementación del proxy

La implementación puede utilizar comunicación HTTP para acceder al backend.

En este Sprint se realiza mediante:

```text
GET /api/medicion
```

La comunicación HTTP queda encapsulada dentro del componente `frontend_business_logic`.

La GUI recibe únicamente:

```text
Medicion
```

o:

```text
null
```

---

### Pruebas

Se deben comprobar automáticamente al menos los siguientes casos:

```text
leer una medición correcta

utilizar el endpoint /api/medicion

aceptar una respuesta null

detectar errores HTTP
```

Las pruebas deben centrarse en el comportamiento del proxy y pueden simular la función de comunicación utilizada.

---

## Aclaraciones del Diseño

- El componente está implementado en JavaScript.
- Actúa como proxy o lógica fake de la lógica de negocio del backend.
- La GUI no realiza directamente peticiones HTTP.
- `leerMedicion()` mantiene la misma firma lógica que su equivalente del backend.
- El frontend solo implementa el subconjunto de operaciones que necesita la GUI.
- Los detalles de REST se encuentran encapsulados dentro de este componente.
- El componente devuelve datos de dominio a la GUI.

---

## Reglas Generales

- Lenguaje de programación: JavaScript.
- La implementación debe mantenerse en `src/frontend_business_logic/`.
- Cada función o método debe incluir su diseño lógico y una breve descripción dentro de un bloque delimitado por líneas discontinuas.
- El código debe ser sencillo, claro y autoexplicativo.
- La GUI no debe contener llamadas directas a `fetch()`.
- Los detalles HTTP deben permanecer encapsulados en este componente.
- La interfaz lógica debe ser compatible con la operación equivalente de `business_logic`.
- Las operaciones principales deben disponer de pruebas automáticas reproducibles.
