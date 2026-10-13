# Diseño de la Interfaz Web

## Diseño del Componente

El componente `web` contiene la interfaz gráfica web utilizada durante el Sprint 0.

Su responsabilidad es mostrar al usuario la última medición disponible y permitir actualizar manualmente la información mostrada.

La interfaz web no realiza directamente comunicación HTTP.

Toda operación de acceso al backend se realiza mediante el componente:

```text
frontend_business_logic
```

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
 |
 v
Database
```

---

### Elementos de la interfaz

La pantalla principal muestra:

```text
tipo

valor

fecha

hora

momento de la última actualización

estado de la operación
```

También incluye un botón para solicitar manualmente la última medición.

---

### inicializarInterfaz

```text
inicializarInterfaz()
```

Responsabilidad:

Inicializa la interfaz web.

Debe:

```text
asociar eventos

realizar una primera carga de datos

inicializar el texto de última actualización
```

---

### cargarMedicion

```text
cargarMedicion()
```

Responsabilidad:

Solicita la última medición mediante el componente `frontend_business_logic`.

El flujo es:

```text
usuario pulsa actualizar
        |
        v
cargarMedicion()
        |
        v
frontend_business_logic.leerMedicion()
        |
        v
Medicion | null
        |
        v
mostrarMedicion()
```

La función no debe utilizar directamente:

```text
fetch()

GET

POST

HTTP

/api/medicion
```

---

### mostrarMedicion

```text
medicion: Medicion | null
    --> mostrarMedicion()
```

Responsabilidad:

Actualiza los elementos visuales de la página con la medición recibida.

Si existe una medición debe mostrar:

```text
tipo

valor

fecha

hora
```

Si no existe ninguna medición debe mostrar un estado adecuado al usuario.

---

### actualizarTextoUltimaActualizacion

```text
actualizarTextoUltimaActualizacion()
```

Responsabilidad:

Actualiza únicamente el texto visual que indica cuánto tiempo ha pasado desde la última actualización.

Esta función no realiza peticiones al backend.

Ejemplos:

```text
Actualizado ahora

Actualizado hace 1 minuto

Actualizado hace 5 minutos
```

---

### Relación con frontend_business_logic

La interfaz web utiliza únicamente la operación:

```text
leerMedicion() --> Medicion | null
```

definida en:

```text
frontend_business_logic_design.md
```

El componente `web` no conoce cómo se obtiene la medición.

Por tanto, no debe conocer:

```text
fetch()

HTTP

REST

rutas

códigos de estado
```

Estos detalles quedan encapsulados en `frontend_business_logic`.

---

### Estructura del componente

La interfaz se implementa mediante:

```text
HTML

CSS

JavaScript
```

Los elementos principales se encuentran en:

```text
src/web/
├── index.html
├── css/
│   └── estilos.css
└── js/
    └── app.js
```

La lógica fake utilizada para acceder al backend no pertenece a este componente.

Se encuentra en:

```text
src/frontend_business_logic/
```

y está documentada en:

```text
doc/frontend_business_logic_design.md
```

---

## Aclaraciones del Diseño

- El componente `web` representa la interfaz gráfica del navegador.
- La interfaz está implementada con HTML, CSS y JavaScript.
- La interfaz únicamente presenta información y gestiona la interacción con el usuario.
- La interfaz no realiza llamadas HTTP directas.
- La obtención de mediciones se delega en `frontend_business_logic`.
- `app.js` controla el comportamiento visual de la página.
- `index.html` define la estructura de la interfaz.
- `estilos.css` define la apariencia visual.
- La actualización del texto temporal no provoca nuevas peticiones al servidor.
- El botón de actualización solicita una nueva lectura mediante `frontend_business_logic`.
- `LogicaFake.js` no pertenece al componente `web`, sino a `frontend_business_logic`.

---

## Reglas Generales

- Lenguajes utilizados: HTML, CSS y JavaScript.
- La implementación debe mantenerse en `src/web/` y corresponder con este diseño.
- Cada fichero debe incluir una cabecera con nombre, descripción, fecha, autor, aportación y copyright cuando corresponda.
- Las funciones JavaScript clave deben incluir su diseño lógico y una breve descripción dentro de bloques de comentarios delimitados por líneas discontinuas.
- El código debe mantenerse claro, sencillo y autoexplicativo.
- El componente `web` no debe contener llamadas directas a `fetch()`.
- El componente `web` no debe conocer rutas REST ni códigos HTTP.
- Toda comunicación con el backend debe realizarse mediante `frontend_business_logic`.
- La implementación debe mantener separadas las responsabilidades de presentación y comunicación.
