# Prompt — Web

Genera la interfaz web del Sprint 0 a partir de `doc/web_design.md`.

## Tecnologías

Usar:

- HTML;
- CSS;
- JavaScript.

## Objetivo

Mostrar la última medición ambiental.

Campos visibles:

```text
Micro
Tipo
Valor
Fecha
Hora
```

También debe existir:

- botón `Actualizar`;
- texto con el momento de la última actualización;
- área de estado para errores o ausencia de datos.

## Tipo recibido

```text
Medicion = (
    nombreMicro: Texto,
    tipo: Texto,
    valor: R,
    fechaHora: Texto
)
```

Los datos concretos procedentes del proxy pueden llegar como:

```text
nombre_micro
tipo
valor
fecha_hora
```

La GUI debe mostrar `fecha_hora` separada visualmente en:

```text
Fecha
Hora
```

## Separación obligatoria

La GUI NO debe usar directamente:

```text
fetch()
XMLHttpRequest
HTTP
/api/medicion
```

Debe utilizar exclusivamente:

```text
frontend_business_logic.leerMedicion()
```

La arquitectura debe ser:

```text
Web
 |
 v
Frontend Business Logic
 |
 v
Backend
```

## Funciones de interfaz

Mantener funciones equivalentes a:

```text
Medicion | Nulo --> mostrarMedicion()
```

```text
actualizarTextoUltimaActualizacion()
```

```text
cargarMedicion()
```

```text
inicializarInterfaz()
```

## Despliegue

El código fuente del repositorio puede mantenerse dentro de:

```text
src/web/
```

En el despliegue de Plesk, `index.html`, `css/` y `js/` pueden situarse en la raíz de `httpdocs` para que:

```text
https://fuseriv.upv.edu.es
```

abra directamente la interfaz.

Este detalle de despliegue no debe romper la separación del repositorio.

## Comentarios

Mantener cabeceras de archivo del proyecto.

Las funciones JavaScript clave deben incluir encabezado de diseño lógico entre líneas discontinuas.

## Resultado esperado

Generar únicamente los archivos del componente:

```text
src/web/
```

sin introducir comunicación HTTP directa en la GUI.
