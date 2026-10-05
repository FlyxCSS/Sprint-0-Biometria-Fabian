# Prompt para generar el servidor REST

Quiero que generes únicamente el servidor REST del backend.

## Contexto
Ya existe `LogicaNegocio` con:
- `guardarMedicion(tipo, valor)`
- `leerMedicion()`

## Regla principal
El servidor REST NO debe acceder directamente a la base de datos. Siempre debe delegar en `LogicaNegocio`.

## Tecnología
PHP + PDO + JSON + HTTP.

## POST /medicion
Debe recibir:

```json
{
  "tipo": "O3",
  "valor": 1.234
}
```

La fecha la genera la lógica de negocio.

Proceso:
1. leer cuerpo;
2. decodificar JSON;
3. validar JSON;
4. comprobar `tipo`;
5. comprobar `valor`;
6. llamar a `guardarMedicion`;
7. devolver la medición.

Éxito: HTTP 201.
Errores de entrada: HTTP 400.

## GET /medicion
Debe llamar a `leerMedicion()` y devolver la última medición o `null`.

Éxito: HTTP 200.

Rutas desconocidas: 404.
Errores internos: 500 sin exponer información sensible.

## Funciones auxiliares
Mantén funciones sencillas como:
- `crearConexion()`
- `responderJSON()`
- `obtenerRuta()`

En GitHub usa placeholders:
`DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.

## Tests
Comprueba:
- POST válido -> 201;
- respuesta correcta;
- tipo vacío -> 400;
- sin tipo -> 400;
- sin valor -> 400;
- JSON inválido -> 400;
- GET -> 200;
- GET devuelve JSON válido.

Genera:
```text
src/rest/index.php
src/rest/ServidorRestTest.php
```

No añadas rutas nuevas.
