# Diseño de la Interfaz Web

## Diseño del Componente

El componente `web` contiene la interfaz gráfica utilizada para mostrar la última medición ambiental.

La interfaz utiliza exclusivamente `frontend_business_logic` para obtener datos.

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

### Elementos mostrados

La pantalla principal muestra:

```text
nombre del microprocesador
tipo
valor
fecha
hora
momento de la última actualización
estado de la operación
```

### mostrarMedicion

```text
Medicion | Nulo --> mostrarMedicion()
```

Responsabilidad:

Muestra en pantalla la medición recibida.

Si recibe una medición, presenta:

```text
nombreMicro
tipo
valor
fecha
hora
```

`fecha` y `hora` se obtienen visualmente a partir de:

```text
fechaHora
```

Si recibe `NULO`, los campos se muestran vacíos o con un valor neutro.

### actualizarTextoUltimaActualizacion

```text
actualizarTextoUltimaActualizacion()
```

Responsabilidad:

Actualiza el texto que indica el tiempo transcurrido desde la última actualización correcta.

Ejemplos:

```text
Actualizado ahora
Actualizado hace 1 minuto
Actualizado hace N minutos
```

No solicita nuevas mediciones.

### cargarMedicion

```text
cargarMedicion()
```

Responsabilidad:

Solicita la última medición a `frontend_business_logic` y actualiza la interfaz.

Flujo:

```text
cargarMedicion()
      |
      v
frontend_business_logic.leerMedicion()
      |
      v
Medicion | Nulo
      |
      v
mostrarMedicion()
```

### inicializarInterfaz

```text
inicializarInterfaz()
```

Responsabilidad:

Inicializa la interfaz.

Debe:

```text
asociar el botón de actualización
realizar una primera carga
activar la actualización visual del texto temporal
```

### Estructura lógica de la GUI

```text
----------------------------- Web -----------------------------
|
Medicion | Nulo --> mostrarMedicion()
|
|                actualizarTextoUltimaActualizacion()
|
|                cargarMedicion()
|
|                inicializarInterfaz()
|
---------------------------------------------------------------
```

## Aclaraciones del Diseño

- La interfaz muestra el nombre del microprocesador recibido en `nombre_micro`.
- La interfaz separa visualmente `fecha_hora` en fecha y hora.
- La GUI no conoce cómo se realiza la comunicación con el backend.
- La GUI utiliza únicamente `frontend_business_logic.leerMedicion()`.
- La interfaz puede actualizarse manualmente mediante el botón `Actualizar`.
- La actualización del texto temporal no realiza nuevas lecturas del backend.
- En el despliegue del Sprint 0, la página principal puede situarse en la raíz del servidor para que el dominio abra directamente la interfaz.

## Reglas Generales

- Lenguajes de implementación: HTML, CSS y JavaScript.
- La implementación del componente debe mantenerse conceptualmente separada de `frontend_business_logic`.
- El JavaScript de la GUI no debe realizar llamadas directas a `fetch()`.
- El encabezado de cada función clave debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- La GUI debe trabajar con tipos de dominio recibidos desde `frontend_business_logic`.
- Las rutas y detalles HTTP no deben formar parte de la lógica de interfaz.
