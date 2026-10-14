# Diseño del Microprocesador

## Diseño del Componente

El componente `microprocesador` se ejecuta sobre una SparkFun Pro nRF52840 Mini y publica una medición ficticia mediante BLE/iBeacon. La implementación se encuentra en `src/microprocesador/`.

### Tipos

```text
MedicionID = { O3, TEMPERATURA }
UUID = [N]_16
ValorImprimible = Text | N | Z | R
```

### Diseño de la clase Medidor

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
