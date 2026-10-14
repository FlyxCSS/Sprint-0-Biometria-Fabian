# Diseño del Microprocesador

## Diseño del Componente

Este componente genera y publica la medición utilizada en el Sprint 0. Separa la obtención del valor, la construcción de los datos iBeacon, la emisión BLE y las tareas auxiliares de depuración e indicación visual.


El componente `microprocesador` se ejecuta sobre una SparkFun Pro nRF52840 Mini y publica una medición ficticia mediante BLE/iBeacon. La implementación se encuentra en `src/microprocesador/`.

### Tipos

```text
MedicionID = { O3, TEMPERATURA }
UUID = [N]_16
ValorImprimible = Text | N | Z | R
```

### Diseño de la clase Medidor

`Medidor` representa la fuente de la medición ambiental. En el Sprint 0 devuelve un valor ficticio de O3 para poder validar el flujo completo del sistema.


```text
                 ----------- Medidor ---------------
                 |
                 | valorO3PruebaPpb: N
                 |
                 |
                 iniciarMedidor() -->
                 |
                 |
              N <-- medirO3() <--
                 |
                 -----------------------------------
```

### Diseño de la clase Publicador

`Publicador` coordina la preparación y publicación de una medida. Construye `Major`, utiliza `Minor` como valor de la medición y delega la emisión BLE en `EmisoraBLE`.


```text
                 ------------ Publicador ----------------
                 |
                 | emisora: EmisoraBLE
                 |
                 |
           Text <-- obtenerNombreMicro() <--
                 |
                 |
tipo: MedicionID, contador: N --> construirMajor() --x
              N <--
                 |
                 |
                 encenderEmisora() -->
                 |
                 |
tipo: MedicionID, valor: N, contador: N, tiempo: N --> publicarMedida() -->
              N <--
                 |
                 ----------------------------------------
```

### Diseño de la clase EmisoraBLE

`EmisoraBLE` encapsula la configuración y emisión de anuncios BLE/iBeacon. Se encarga del nombre BLE, los parámetros de la baliza y el inicio o detención del anuncio.


```text
                 ------------ EmisoraBLE ----------------
                 |
                 | nombreEmisora: Text
                 | fabricanteId: N
                 | txPower: Z
                 |
                 |
nombre: Text, fabricante: N, potencia: Z --> EmisoraBLE() -->
                 |
                 |
                 encenderEmisora() -->
                 |
                 |
              B <-- estaAnunciando() <--
                 |
                 |
                 detenerAnuncio() -->
                 |
                 |
uuid: UUID, major: N, minor: N, txPower: Z --> emitirAnuncioIBeacon() -->
                 |
                 ----------------------------------------
```

### Diseño de la clase LED

`LED` encapsula el control del indicador luminoso de la placa. Permite encenderlo, apagarlo, alternarlo o mantenerlo activo durante un intervalo.


```text
                 --------------- LED ----------------
                 |
                 | numeroLed: Z
                 | encendido: B
                 |
                 |
numero: Z     --> LED() -->
                 |
                 |
                 encender() -->
                 |
                 |
                 apagar() -->
                 |
                 |
                 alternar() -->
                 |
                 |
tiempo: N     --> brillar() -->
                 |
                 ------------------------------------
```

### Diseño de la clase PuertoSerie

`PuertoSerie` encapsula la salida de información de depuración por puerto serie. Permite esperar la disponibilidad del puerto y escribir valores sin afectar al funcionamiento principal del sistema.


```text
                 ----------- PuertoSerie -------------
                 |
                 |
baudios: N    --> PuertoSerie() -->
                 |
                 |
tiempoMaximo: N --> esperarDisponible() -->
                 |
                 |
valor: ValorImprimible --> escribir() -->
                 |
                 |
valor: R, decimales: N --> escribirDecimal() -->
                 |
                 ------------------------------------
```

### Función del programa principal

El programa principal inicializa los componentes y ejecuta periódicamente el ciclo de medición, depuración y publicación BLE.


```text
ppb: N --> convertirPpbAPpm() --> R
```

```text
setup()
```

```text
loop()
```

### Codificación iBeacon

```text
Major = (tipo << 8) | contador
Minor = valor en ppb
```

Caso comprobado:

```text
tipo O3 = 11
contador = 5
Major = 2821
Minor = 1234
```

Nombre BLE:

```text
Fabian_GTI
```

## Aclaraciones del Diseño

- `Minor` conserva el valor en ppb.
- La conversión a ppm se usa para depuración.
- El byte alto de `Major` contiene el tipo.
- El byte bajo contiene el contador.
- El Serial Monitor es opcional.
- El valor O3 del Sprint 0 es ficticio.

## Reglas Generales

- Lenguaje de programación: C++ para Arduino.
- La implementación debe mantenerse en `src/microprocesador/`.
- El diseño lógico debe utilizar `N`, `Z`, `R`, `B`, `Text` o tipos definidos aquí.
- El encabezado de cada función o método debe incluir su diseño lógico entre líneas discontinuas.
- El código debe ser claro, legible y autoexplicativo.
- La publicación debe realizarse mediante BLE/iBeacon.
- La codificación de `Major` debe mantener su prueba estática.
