# Prompt para generar Communication

Quiero que generes o adaptes únicamente la capa de comunicación del backend para mi Sprint 0 del proyecto de Biometría y Medio Ambiente.

## Arquitectura

El backend debe respetar:

```text
Communication
      ↓
Business Logic
      ↓
Database
```

Este prompt afecta únicamente a:

```text
doc/communication_design.md
src/communication/
```

## Objetivo

Implementar en PHP una API REST sencilla que reciba peticiones externas y delegue la lógica en:

```text
src/business_logic/LogicaNegocio.php
```

La capa `communication` puede conocer HTTP.

La capa `business_logic` no debe conocerlo.

## Endpoints

Implementa:

```text
POST /api/medicion
GET /api/medicion
```

## POST /api/medicion

Debe:

1. leer el JSON recibido;
2. comprobar que existe `tipo`;
3. comprobar que existe `valor`;
4. delegar en:

```text
LogicaNegocio.guardarMedicion(tipo, valor)
```

5. responder en JSON;
6. utilizar HTTP 201 cuando se crea correctamente;
7. utilizar HTTP 400 cuando la petición sea incorrecta.

El JSON de entrada es:

```json
{
  "tipo": "O3",
  "valor": 1.234
}
```

La respuesta correcta debe incluir la `Medicion` creada.

## GET /api/medicion

Debe llamar a:

```text
LogicaNegocio.leerMedicion()
```

y devolver la última medición en JSON.

Si no hay mediciones puede devolver `null`.

Una petición correcta utiliza HTTP 200.

## Errores

Utiliza:

```text
400 -> petición incorrecta
404 -> ruta no encontrada
500 -> error interno
```

## Archivos

Genera o adapta:

```text
src/communication/index.php
src/communication/ServidorRestTest.php
```

## Dependencias

`src/communication/` puede depender de:

```text
src/business_logic/
```

pero nunca al revés.

## Tests

Comprueba al menos:

```text
POST correcto -> 201
POST con tipo vacío -> 400
POST sin tipo -> 400
POST sin valor -> 400
POST con JSON inválido -> 400
GET correcto -> 200
respuesta GET en JSON válido
```

## Restricciones

No escribas SQL directamente en `communication`.

No dupliques las validaciones de negocio que ya pertenecen a `LogicaNegocio`.

No mezcles HTML o GUI con este componente.
