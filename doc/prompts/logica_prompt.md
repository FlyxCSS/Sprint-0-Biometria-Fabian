# Prompt para generar la lógica de negocio

Quiero que generes únicamente la lógica de negocio real del backend para mi proyecto de Biometría y Medio Ambiente.

## Contexto
Existe una tabla `Mediciones` con `id`, `fecha`, `tipo` y `valor`.

La lógica de negocio será la única parte que acceda directamente a la base de datos.

## Tecnología
PHP + PDO + MySQL.

## Clase
Crea `LogicaNegocio` y recibe la conexión PDO por constructor.

La clase no debe conocer rutas HTTP, GET, POST, códigos HTTP, Android ni la web.

## Operaciones
```text
tipo: Text, valor: R --> guardarMedicion() --> Medicion
leerMedicion() --> Medicion | null
```

### guardarMedicion()
Debe:
1. recibir `tipo` y `valor`;
2. hacer `trim` del tipo;
3. rechazar tipo vacío;
4. comprobar que el valor sea numérico;
5. convertirlo a float;
6. generar fecha/hora en backend;
7. usar zona `Europe/Madrid`;
8. hacer INSERT preparado;
9. recuperar el id;
10. devolver `id`, `fecha`, `tipo`, `valor`.

### leerMedicion()
Debe recuperar únicamente la última medición con:

```sql
ORDER BY id DESC
LIMIT 1
```

Si no existe ninguna, devuelve `null`.

## Separación
No incluyas rutas REST, JSON HTTP, callbacks, Bluetooth ni interfaz.

## Comentarios
Cada función importante debe llevar cabecera con diseño lógico y breve descripción siguiendo la notación de la asignatura.

## Tests automáticos
Genera tests para:
- guardar una medición válida;
- rechazar tipo vacío;
- rechazar valor no numérico;
- recuperar la última medición;
- devolver `null` sin mediciones.

Los tests deben ser reproducibles y ejecutables sin arrancar toda la aplicación.

Genera:
```text
src/logica/LogicaNegocio.php
src/logica/LogicaNegocioTest.php
```
