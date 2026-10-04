# Diseño de la Interfaz Web

## Diseño del Componente

El componente `web` permite visualizar la última medición almacenada en el sistema.

La interfaz web no se comunica directamente con la base de datos ni con la lógica de negocio real.

La comunicación se realiza mediante una lógica fake que representa en el cliente la operación de lectura de mediciones.

El flujo general es:

```text
Interfaz Web
     |
     v
LogicaFake
     |
     | GET /api/medicion
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

Una medición recibida por la web se representa como:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

---

### LogicaFake

```text
---------------- LogicaFake ----------------
|
| urlMedicion: Text
|
leerMedicion() --> Medicion | null
|
---------------------------------------------
```

Responsabilidad:

Representa en el navegador la operación de lectura de mediciones.

`leerMedicion()` realiza una petición al servidor REST y devuelve la última medición disponible.

El flujo es:

```text
leerMedicion()
      |
      v
GET /api/medicion
      |
      v
Servidor REST
      |
      v
Medicion | null
```

La lógica fake no accede directamente a la base de datos.

---

### leerMedicion

```text
leerMedicion() --> Medicion | null
```

Responsabilidad:

Solicita al servidor REST la última medición almacenada.

La petición utilizada es:

```text
GET /api/medicion
```

Si la respuesta HTTP es correcta:

```text
respuesta JSON
      |
      v
Medicion | null
```

Si la respuesta HTTP contiene un error:

```text
leerMedicion()
      |
      v
Error
```

Este error se gestiona posteriormente desde la interfaz.

---

### mostrarMedicion

```text
medicion: Medicion
    --> mostrarMedicion()
```

Responsabilidad:

Muestra en la interfaz los datos recibidos desde la lógica fake.

Los campos mostrados son:

```text
tipo
valor
fecha
hora
```

La fecha recibida desde el servidor tiene el formato:

```text
Y-m-d H:i:s
```

La interfaz la separa únicamente para su representación:

```text
fecha = Y-m-d
hora  = H:i:s
```

Ejemplo:

```text
2026-10-04 14:42:10
```

se muestra como:

```text
Fecha: 2026-10-04

Hora: 14:42:10
```

Si no existe ninguna medición:

```text
mostrarMedicion(null)
```

los campos se muestran con:

```text
-
```

---

### cargarMedicion

```text
cargarMedicion()
```

Responsabilidad:

Controla el proceso completo de actualización de la información mostrada en pantalla.

El flujo es:

```text
cargarMedicion()
      |
      v
deshabilitar botón
      |
      v
LogicaFake.leerMedicion()
      |
      v
mostrarMedicion()
      |
      v
guardar momento de actualización
      |
      v
actualizar texto de última actualización
      |
      v
habilitar botón
```

Mientras se está realizando la petición:

```text
Actualizando...
```

Si no existen mediciones:

```text
No existen mediciones almacenadas.
```

Si ocurre un error:

```text
No se pudo recuperar la medición.
```

---

### actualizarTextoUltimaActualizacion

```text
actualizarTextoUltimaActualizacion()
```

Responsabilidad:

Calcula el tiempo transcurrido desde la última actualización correcta de los datos.

Ejemplos:

```text
Actualizado ahora · 14:42:10

Actualizado hace 1 minuto · 14:42:10

Actualizado hace 5 minutos · 14:42:10
```

Esta función utiliza:

```text
momentoUltimaActualizacion
```

para calcular la diferencia entre:

```text
hora actual
-
hora de la última actualización
```

Esta operación no realiza ninguna nueva petición al servidor REST.

---

### inicializarInterfaz

```text
inicializarInterfaz()
```

Responsabilidad:

Inicializa el comportamiento de la página web cuando el documento termina de cargarse.

Realiza las siguientes operaciones:

```text
configurar botón Actualizar

cargar primera medición

iniciar actualización periódica del texto
de última actualización
```

El botón:

```text
Actualizar
```

ejecuta:

```text
cargarMedicion()
```

El texto que indica cuánto tiempo ha pasado desde la última actualización se recalcula cada minuto.

---

### Estructura de la interfaz

La pantalla principal contiene:

```text
Calidad del Aire

Monitorización ambiental
```

Debajo se muestra una tarjeta con:

```text
Última medición
```

La tarjeta se divide visualmente en dos zonas.

```text
----------------------------------------------------
|                  Última medición                  |
|                                                  |
| Tipo   [ O3          ]   |                       |
| Valor  [ 1.234       ]   |     [ Actualizar ]    |
| Fecha  [ 2026-10-04  ]   |                       |
| Hora   [ 14:42:10    ]   |  Actualizado ahora    |
|                                                  |
----------------------------------------------------
```

La zona izquierda muestra:

```text
tipo
valor
fecha
hora
```

Cada valor se representa dentro de un bloque visual con:

```text
fondo gris
bordes redondeados
texto no editable
```

Estos elementos únicamente representan información y no son campos de entrada.

La zona derecha contiene:

```text
botón Actualizar
texto de última actualización
```

Entre ambas zonas se muestra una línea vertical divisoria.

La línea se sitúa en el centro de la tarjeta mediante la estructura CSS Grid del contenedor:

```text
1fr | 1px | 1fr
```

El separador se genera mediante `::before`, por lo que no es necesario añadir un elemento adicional en el HTML.

---

### Diseño visual

La interfaz utiliza una apariencia sencilla.

Los colores principales son:

```text
fondo general:
azul grisáceo claro

tarjeta:
blanco

color principal:
azul verdoso

campos de valores:
gris claro
```

Los títulos:

```text
Calidad del Aire

Monitorización ambiental

Última medición
```

se muestran centrados.

La tarjeta principal utiliza:

```text
bordes redondeados
sombra ligera
borde claro
```

La interfaz mantiene una estructura sencilla para facilitar la lectura durante la demostración del Sprint 0.

---

### Adaptación a pantallas pequeñas

En pantallas grandes:

```text
datos | controles
```

se muestran uno al lado del otro.

En pantallas pequeñas:

```text
datos
  |
  v
controles
```

se muestran verticalmente.

La línea vertical desaparece y se sustituye por una línea horizontal entre ambas zonas.

---

### Archivos del componente

La estructura del componente es:

```text
src/web/
|
├── index.html
|
├── css/
|   └── estilos.css
|
└── js/
    ├── LogicaFake.js
    └── app.js
```

`index.html`:

```text
estructura de la interfaz
```

`estilos.css`:

```text
diseño visual y distribución
```

`LogicaFake.js`:

```text
lógica fake
+
comunicación con REST
```

`app.js`:

```text
control de la interfaz
+
representación de los datos
```

---

### Comunicación con REST

La interfaz web utiliza únicamente:

```text
GET /api/medicion
```

La petición no se realiza directamente desde `app.js`.

El flujo correcto es:

```text
app.js
   |
   v
LogicaFake.leerMedicion()
   |
   v
GET /api/medicion
```

La respuesta esperada tiene la forma:

```json
{
    "id": 1,
    "fecha": "2026-10-04 14:42:10",
    "tipo": "O3",
    "valor": 1.234
}
```

La interfaz utiliza:

```text
tipo
valor
fecha
```

El campo:

```text
id
```

no se muestra durante el Sprint 0.

---

## Aclaraciones del Diseño

- La web utiliza HTML, CSS y JavaScript.
- La interfaz dispone de una lógica fake.
- La lógica fake contiene `leerMedicion()`.
- `leerMedicion()` representa en el cliente la operación equivalente de `LogicaNegocio.leerMedicion()`.
- La lógica fake se comunica con REST mediante `GET /api/medicion`.
- `app.js` no realiza peticiones HTTP directamente.
- La web no accede directamente a la base de datos.
- La web no contiene lógica de negocio real.
- La interfaz muestra únicamente la última medición.
- Se muestran tipo, valor, fecha y hora.
- Los valores se muestran dentro de bloques visuales no editables.
- La tarjeta se divide en una zona de datos y una zona de controles.
- Ambas zonas están separadas visualmente mediante una línea.
- El botón `Actualizar` solicita nuevamente la última medición.
- La hora de actualización representa el momento en el que el navegador recibió correctamente los datos.
- El texto de tiempo transcurrido se actualiza sin realizar nuevas peticiones al servidor.
- La interfaz se adapta a pantallas pequeñas.

## Reglas Generales

- Lenguajes utilizados: HTML, CSS y JavaScript.
- La implementación debe mantenerse en `src/web/`.
- El diseño y la implementación deben coincidir.
- La comunicación con REST debe realizarse desde `LogicaFake`.
- La interfaz no debe comunicarse directamente con la base de datos.
- La interfaz no debe contener lógica de negocio.
- La lógica fake debe proporcionar `leerMedicion()`.
- La URL utilizada debe ser `/api/medicion`.
- Las funciones JavaScript deben incluir su diseño lógico y una breve descripción.
- El código debe mantenerse sencillo, legible y autoexplicativo.
- La distribución visual debe mantenerse sencilla para facilitar la demostración del Sprint 0.
