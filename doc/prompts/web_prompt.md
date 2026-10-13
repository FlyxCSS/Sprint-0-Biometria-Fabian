# Prompt para generar Frontend Business Logic

Quiero que generes o adaptes únicamente el proxy de lógica de negocio utilizado por la interfaz web.

## Arquitectura

La parte web debe respetar:

```text
Web
 ↓
Frontend Business Logic
 ↓
Communication
 ↓
Business Logic
 ↓
Database
```

Este prompt afecta únicamente a:

```text
doc/frontend_business_logic_design.md
src/frontend_business_logic/
```

## Objetivo

Implementar una lógica fake/proxy en JavaScript que permita a la GUI usar:

```text
leerMedicion()
```

sin conocer directamente HTTP.

## Firma lógica

Debe mantener la misma firma lógica que el backend:

```text
leerMedicion() --> Medicion | null
```

donde:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

## Implementación

El archivo principal será:

```text
src/frontend_business_logic/LogicaFake.js
```

Internamente puede utilizar:

```javascript
fetch()
```

para realizar:

```text
GET /api/medicion
```

La GUI no debe contener `fetch()`.

## Comportamiento

`leerMedicion()` debe:

1. hacer GET a `/api/medicion`;
2. comprobar que la respuesta HTTP es correcta;
3. devolver el JSON recibido;
4. aceptar una respuesta `null`;
5. lanzar o propagar un error cuando la respuesta HTTP sea incorrecta.

## Tests

Los tests estarán en:

```text
src/frontend_business_logic/tests/
```

y deben comprobar:

```text
leer una medición correcta
usar /api/medicion
aceptar null
detectar errores HTTP
```

Puede simularse `fetch()` para que los tests sean reproducibles.

## Restricciones

No incluyas código de presentación HTML.

No manipules directamente el DOM.

No añadas estilos.

No muevas la lógica HTTP a `src/web/`.
