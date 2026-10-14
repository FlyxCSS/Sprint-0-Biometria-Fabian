# Prompt para generar Web

Genera `src/web/` a partir de `doc/web_design.md`.

Tecnologías:

```text
HTML
CSS
JavaScript
```

La interfaz debe mostrar:

```text
Micro
Tipo
Valor
Fecha
Hora
Última actualización
Estado
```

La GUI debe obtener datos exclusivamente mediante:

```text
frontend_business_logic.leerMedicion()
```

No debe contener llamadas directas a:

```text
fetch()
XMLHttpRequest
/api/medicion
```

Funciones lógicas clave:

```text
medicion: Medicion | Nulo --> mostrarMedicion()
actualizarTextoUltimaActualizacion()
cargarMedicion()
inicializarInterfaz()
```

`fecha_hora` debe separarse únicamente para presentación en fecha y hora.

Debe existir un botón `Actualizar`.

Mantener una interfaz sencilla, legible y fácil de explicar.

Cada función JavaScript debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas discontinuas.

Las funciones principales deben quedar cubiertas por pruebas de frontend o integración.


## Frontend Business Logic asociado

Además de la GUI, conserva o genera el componente `src/frontend_business_logic/` conforme a `doc/frontend_business_logic_design.md`.

Su interfaz debe ser exactamente:

```text
leerMedicion() --> Medicion | Nulo
```

y debe encapsular internamente la comunicación remota. La GUI no debe contener `fetch()`.

Mantener sus pruebas automáticas de lectura correcta, ruta utilizada, ausencia de medición y error de comunicación.
