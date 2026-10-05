# Prompt para generar la UX del navegador

Quiero que generes la interfaz web del proyecto usando la lógica fake existente.

## Tecnología
- HTML: estructura
- CSS: presentación
- JavaScript: comportamiento
- sin frameworks

## Regla principal
La interfaz NO debe realizar peticiones REST directamente. Debe usar `logicaFake.leerMedicion()`.

## Diseño
Título:
```text
Calidad del Aire
```

Subtítulo:
```text
Monitorización ambiental
```

Tarjeta:
```text
Última medición
```

Datos:
- Tipo
- Valor
- Fecha
- Hora

Controles:
- botón `Actualizar`
- texto de última actualización

## Comportamiento
Al cargar:
1. configurar botón;
2. cargar primera medición;
3. mostrarla.

Al pulsar Actualizar:
1. mostrar `Actualizando...`;
2. desactivar botón;
3. llamar `leerMedicion()`;
4. actualizar datos;
5. guardar momento de actualización;
6. reactivar botón.

Sin datos:
`No existen mediciones almacenadas.`

Error:
`No se pudo recuperar la medición.`

## Fecha
Si llega:
```text
2026-10-04 14:42:10
```

mostrar:
```text
Fecha: 2026-10-04
Hora: 14:42:10
```

## Última actualización
Ejemplos:
- `Actualizado ahora · 14:42:10`
- `Actualizado hace 1 minuto · 14:42:10`
- `Actualizado hace 5 minutos · 14:42:10`

Actualizar ese texto cada minuto no debe provocar nuevos GET.

## Visual
Diseño sencillo, limpio, centrado y responsive.

En escritorio:
```text
datos | controles
```

En móvil:
```text
datos
----
controles
```

## Archivos
Genera:
```text
src/web/index.html
src/web/css/estilos.css
src/web/js/app.js
```

Carga `LogicaFake.js` antes de `app.js`.

No añadas gráficas, mapas, usuarios, login ni funcionalidades extra.
