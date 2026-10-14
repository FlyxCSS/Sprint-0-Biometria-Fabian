# Prompt — Communication / API REST

Genera el componente `communication` del Sprint 0 a partir de `doc/communication_design.md`.

## Lenguaje

PHP.

## Arquitectura

Debe mantenerse:

```text
Communication
      |
      v
Business Logic
      |
      v
Database
```

`communication` puede invocar `business_logic`, pero no debe implementar reglas de dominio.

## Endpoint de guardado

```text
POST /api/medicion
```

Entrada:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

La capa `communication` debe comprobar estructuralmente:

- JSON válido;
- presencia de `nombre_micro`;
- presencia de `tipo`;
- presencia de `valor`.

Después debe delegar en:

```text
nombreMicro: Texto, tipo: Texto, valor: R
        --> guardarMedicion() --> Medicion
```

No generar `fecha_hora` en `communication`.

Respuesta correcta:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234,
  "fecha_hora": "2026-10-14 12:00:00"
}
```

Código:

```text
201
```

## Endpoint de lectura

```text
GET /api/medicion
```

Sin cuerpo de entrada.

Debe invocar:

```text
leerMedicion() --> Medicion | Nulo
```

Respuesta correcta:

```text
200
```

con los campos:

```text
nombre_micro
tipo
valor
fecha_hora
```

## Errores

Mantener:

```text
400 --> petición incorrecta
404 --> ruta no encontrada
500 --> error interno
```

## Pruebas

Crear o mantener pruebas de integración para:

- POST correcto = 201;
- respuesta POST correcta;
- POST sin `nombre_micro` = 400;
- POST con `nombre_micro` vacío = 400;
- POST sin `tipo` = 400;
- POST con `tipo` vacío = 400;
- POST sin `valor` = 400;
- POST con valor no numérico = 400;
- POST con JSON inválido = 400;
- GET correcto = 200;
- GET con estructura correcta.

## Comentarios

Mantener la cabecera de archivo del proyecto.

Cada función clave debe tener un bloque delimitado por líneas discontinuas con diseño lógico.

## Resultado esperado

Generar únicamente:

```text
src/communication/
```

La implementación debe corresponder con `doc/communication_design.md` y utilizar las interfaces definidas en `doc/business_logic_design.md`.
