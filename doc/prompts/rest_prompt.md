# Prompt para generar Communication

Genera `src/communication/` a partir de `doc/communication_design.md` y utiliza `business_logic` exclusivamente mediante sus operaciones públicas.

Lenguaje: PHP.

Arquitectura obligatoria:

```text
Communication --> Business Logic --> Database
```

Puntos de entrada:

```text
POST /api/medicion
GET /api/medicion
```

POST recibe:

```json
{
  "nombre_micro": "Fabian_GTI",
  "tipo": "O3",
  "valor": 1.234
}
```

Debe comprobar estructuralmente:

- JSON válido;
- presencia de `nombre_micro`;
- presencia de `tipo`;
- presencia de `valor`.

Después debe delegar en:

```text
nombre_micro: Text, tipo: Text, valor: R
    --> guardarMedicion() --> Medicion
```

La respuesta correcta del POST es 201.

GET debe delegar en:

```text
leerMedicion() --> Medicion | Nulo
```

La respuesta correcta del GET es 200 cuando la operación se procesa correctamente.

Mantener códigos:

```text
200
201
400
404
500
```

No duplicar reglas de dominio en `communication`.

No almacenar credenciales reales en el repositorio.

Cada función debe incluir un encabezado lógico con tipos abstractos dentro de líneas discontinuas.

Mantener pruebas de integración para todos los casos descritos en `communication_design.md`.
