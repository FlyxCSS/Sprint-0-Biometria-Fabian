# Diseño de la Interfaz Web

## Diseño del Componente

Este componente implementa únicamente la presentación de la última medición. Su responsabilidad es mostrar los datos recibidos, permitir una actualización manual y reflejar el estado de la interfaz sin realizar directamente la comunicación con el backend.


El componente `web` implementa la GUI del Sprint 0. Su implementación se encuentra en `src/web/`.

```text
Web --> Frontend Business Logic --> Communication --> Business Logic --> Database
```

### Tipos

```text
Medicion = (
    nombre_micro: Text,
    tipo: Text,
    valor: R,
    fecha_hora: Text
)

Nulo = { NULO }
```

### Funciones de la GUI

```text
medicion: Medicion | Nulo --> mostrarMedicion()
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

### Datos visibles

```text
Micro
Tipo
Valor
Fecha
Hora
Última actualización
Estado
```

La GUI solo depende directamente de:

```text
Web --> Frontend Business Logic
```

## Aclaraciones del Diseño

- `fecha_hora` se separa en fecha y hora únicamente para mostrarla.
- El botón `Actualizar` solicita una nueva lectura mediante `frontend_business_logic`.
- El texto de última actualización puede cambiar sin realizar una nueva lectura.
- La GUI no realiza comunicación remota directa.

## Reglas Generales

- Lenguajes de programación: HTML, CSS y JavaScript.
- La implementación debe mantenerse en `src/web/`.
- La GUI debe permanecer separada de `src/frontend_business_logic/`.
- El encabezado de cada función debe incluir su diseño lógico entre líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- Las funciones clave deben quedar cubiertas por pruebas automatizadas o pruebas de integración del frontend.
