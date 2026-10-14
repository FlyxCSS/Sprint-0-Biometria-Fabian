# Prompt — UX Web

Diseña una interfaz visual sencilla para el Sprint 0 de monitorización ambiental.

## Objetivo

La pantalla debe permitir visualizar claramente la última medición recibida.

## Información mostrada

Debe incluir:

```text
Micro
Tipo
Valor
Fecha
Hora
```

Ejemplo:

```text
Micro: Fabian_GTI
Tipo: O3
Valor: 1.234
Fecha: 2026-10-14
Hora: 13:31:35
```

## Estructura visual

La página debe contener:

- título `Calidad del Aire`;
- subtítulo `Monitorización ambiental`;
- tarjeta principal `Última medición`;
- campos claramente diferenciados;
- botón `Actualizar`;
- texto indicando cuándo se actualizó;
- mensaje de estado.

## Estilo

Debe ser:

- sencillo;
- limpio;
- legible;
- académico;
- sin decoración excesiva;
- fácil de explicar durante la demo.

No hace falta crear dashboards complejos, gráficas, autenticación ni navegación adicional.

## Restricción funcional

Este prompt describe únicamente la UX.

La comunicación con el backend debe quedar fuera de la GUI y realizarse mediante el componente `frontend_business_logic`.

## Resultado esperado

Proponer una interfaz coherente con `doc/web_design.md` y con los datos reales del Sprint 0.
