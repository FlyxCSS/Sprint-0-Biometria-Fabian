# Prompt para generar la lógica fake del navegador

Quiero que generes únicamente la lógica fake del navegador.

## Tecnología
JavaScript + `fetch()`, sin frameworks.

## Operación
```text
leerMedicion() --> Medicion | null
```

## REST
Debe usar:

```text
GET /api/medicion
```

La interfaz gráfica no debe llamar directamente a `fetch()`.

## Clase
Crea `LogicaFake` con:
```text
urlMedicion = "/api/medicion"
```

y método `leerMedicion()`.

Debe:
1. hacer `fetch(urlMedicion)`;
2. comprobar `respuesta.ok`;
3. lanzar error HTTP si falla;
4. devolver `respuesta.json()`.

No incluyas DOM, botones ni estilos.

## Tests
Haz tests aislados usando un `fetch` simulado para comprobar:
- lectura correcta;
- URL exacta `/api/medicion`;
- respuesta `null`;
- error HTTP.

Restaura `fetch` al terminar.

Genera:
```text
src/web/js/LogicaFake.js
src/web/tests/LogicaFakeTest.html
src/web/tests/LogicaFakeTest.js
```

No añadas operaciones nuevas.
