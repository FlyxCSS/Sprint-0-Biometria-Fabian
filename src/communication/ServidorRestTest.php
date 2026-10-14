<?php
declare(strict_types=1);

// --------------------------------------------------------------
// Fichero: ServidorRestTest.php
// Descripción: Pruebas automáticas del servidor REST.
// Fecha: 2026-10-14
// Autor: Fabián Useche
// Aportación: adaptación de las pruebas del API REST al campo
//             nombre_micro y a la nueva respuesta de medición.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------


// ------------------------------------------------------------
// condicion: B, nombre: Text --> comprobar()
//
// Comprueba una condición.
// Si es correcta muestra OK.
// Si falla lanza una excepción.
// ------------------------------------------------------------
function comprobar(
    bool $condicion,
    string $nombre
): void {

    if (
        !$condicion
    ) {

        throw new RuntimeException(
            'FALLO - '
            . $nombre
        );
    }


    echo
        'OK - '
        . $nombre
        . PHP_EOL;

} // comprobar()


// ------------------------------------------------------------
// url: Text, metodo: Text, cuerpo: Text | null
//     --> hacerPeticion() --> RespuestaHTTP
//
// Realiza una petición HTTP al servidor REST y devuelve
// el código HTTP y el cuerpo recibido.
// ------------------------------------------------------------
function hacerPeticion(
    string $url,
    string $metodo,
    ?string $cuerpo = null
): array {

    $opciones =
        [
            'http' =>
                [
                    'method'
                        => $metodo,

                    'ignore_errors'
                        => true,

                    'header'
                        => "Content-Type: application/json\r\n"
                        . "Accept: application/json\r\n"
                ]
        ];


    if (
        $cuerpo !== null
    ) {

        $opciones['http']['content'] =
            $cuerpo;
    }


    $contexto =
        stream_context_create(
            $opciones
        );


    $respuesta =
        file_get_contents(
            $url,
            false,
            $contexto
        );


    $codigoHTTP =
        0;


    if (
        isset(
            $http_response_header[0]
        )
    ) {

        preg_match(
            '/\s(\d{3})\s/',
            $http_response_header[0],
            $coincidencias
        );


        if (
            isset(
                $coincidencias[1]
            )
        ) {

            $codigoHTTP =
                (int) $coincidencias[1];
        }
    }


    return
        [
            'codigo'
                => $codigoHTTP,

            'cuerpo'
                => $respuesta
        ];

} // hacerPeticion()


// ============================================================
// CONFIGURACIÓN
// ============================================================

$urlMedicion =
    'https://fuseriv.upv.edu.es/api/medicion';


try {

    echo
        '===== TESTS SERVIDOR REST ====='
        . PHP_EOL
        . PHP_EOL;


    // --------------------------------------------------------
    // TEST 1
    // POST correcto.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => 'Fabian_GTI',

                    'tipo'
                        => 'O3',

                    'valor'
                        => 1.234
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 201,
        'POST correcto devuelve 201'
    );


    $datos =
        json_decode(
            $respuesta['cuerpo'],
            true
        );


    comprobar(
        is_array(
            $datos
        )
            &&
        $datos['nombre_micro'] === 'Fabian_GTI'
            &&
        $datos['tipo'] === 'O3'
            &&
        abs(
            $datos['valor']
            -
            1.234
        ) < 0.000001
            &&
        isset(
            $datos['fecha_hora']
        ),
        'POST devuelve medicion correcta'
    );


    // --------------------------------------------------------
    // TEST 2
    // POST sin nombre_micro.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'tipo'
                        => 'O3',

                    'valor'
                        => 1.234
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST sin nombre_micro devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 3
    // POST con nombre_micro vacío.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => '',

                    'tipo'
                        => 'O3',

                    'valor'
                        => 1.234
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con nombre_micro vacio devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 4
    // POST sin tipo.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => 'Fabian_GTI',

                    'valor'
                        => 1.234
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST sin tipo devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 5
    // POST con tipo vacío.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => 'Fabian_GTI',

                    'tipo'
                        => '',

                    'valor'
                        => 1.234
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con tipo vacio devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 6
    // POST sin valor.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => 'Fabian_GTI',

                    'tipo'
                        => 'O3'
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST sin valor devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 7
    // POST con valor no numérico.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode(
                [
                    'nombre_micro'
                        => 'Fabian_GTI',

                    'tipo'
                        => 'O3',

                    'valor'
                        => 'abc'
                ]
            )
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con valor no numerico devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 8
    // POST con JSON inválido.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            '{json invalido'
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con JSON invalido devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 9
    // GET correcto.
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'GET'
        );


    comprobar(
        $respuesta['codigo'] === 200,
        'GET correcto devuelve 200'
    );


    $datos =
        json_decode(
            $respuesta['cuerpo'],
            true
        );


    comprobar(
        is_array(
            $datos
        )
            &&
        array_key_exists(
            'nombre_micro',
            $datos
        )
            &&
        array_key_exists(
            'tipo',
            $datos
        )
            &&
        array_key_exists(
            'valor',
            $datos
        )
            &&
        array_key_exists(
            'fecha_hora',
            $datos
        ),
        'GET devuelve medicion con la estructura correcta'
    );


    echo
        PHP_EOL
        . 'TODOS LOS TESTS HAN PASADO'
        . PHP_EOL;


} catch (
    Throwable $e
) {

    echo
        'ERROR - '
        . $e->getMessage()
        . PHP_EOL;


    exit(1);

}
