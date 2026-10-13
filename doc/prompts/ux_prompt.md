# Prompt para generar la UX / GUI Web

Quiero que generes o adaptes únicamente la interfaz web del Sprint 0.

## Arquitectura

La GUI debe respetar:

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
doc/web_design.md
src/web/
```

## Objetivo

Crear una interfaz web sencilla con:

```text
HTML
CSS
JavaScript
```

que muestre la última medición almacenada.

## Estructura

La GUI debe mantenerse en:

```text
src/web/
├── index.html
├── css/
│   └── estilos.css
└── js/
    └── app.js
```

La lógica de comunicación NO pertenece a esta carpeta.

Se encuentra en:

```text
src/frontend_business_logic/LogicaFake.js
```

## Datos mostrados

La página debe mostrar:

```text
tipo
valor
fecha
hora
momento de la última actualización
estado de la operación
```

También debe tener un botón para actualizar manualmente la medición.

## Regla principal

La GUI nunca debe hacer directamente:

```javascript
fetch()
```

Debe solicitar la información mediante:

```text
leerMedicion()
```

del componente:

```text
frontend_business_logic
```

## Comportamiento

`app.js` puede incluir funciones como:

```text
inicializarInterfaz()
cargarMedicion()
mostrarMedicion()
actualizarTextoUltimaActualizacion()
```

`actualizarTextoUltimaActualizacion()` solo debe cambiar el texto visual y no realizar nuevas peticiones al servidor.

## Diseño visual

Quiero una interfaz sencilla y fácil de entender.

No es necesario un diseño complejo.

Debe ser adecuada para demostrar el Sprint 0 y mostrar claramente la última medición recibida.

## Restricciones

No pongas lógica de negocio en la GUI.

No pongas llamadas HTTP directas en `app.js`.

No dupliques `LogicaFake.js` dentro de `src/web/`.
