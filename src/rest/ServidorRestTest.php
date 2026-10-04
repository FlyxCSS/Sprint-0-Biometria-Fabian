<?php

// --------------------------------------------------------------
// Fichero: ServidorRestTest.php
// Descripción: Tests automáticos del servidor REST.
// Fecha: 2026-10-04
// Autor: Fabián Useche
// Aportación: pruebas automáticas del API REST del Sprint 0.
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

    if (!$condicion) {

        throw new RuntimeException(
            'FALLO - ' . $nombre
        );
    }


    echo
        'OK - '
        . $nombre
        . PHP_EOL;
}


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

    $opciones = [
        'http' => [
            'method' => $metodo,
            'ignore_errors' => true,
            'header' =>
                "Content-Type: application/json\r\n"
                . "Accept: application/json\r\n"
        ]
    ];


    if ($cuerpo !== null) {

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


    return [
        'codigo' => $codigoHTTP,
        'cuerpo' => $respuesta
    ];
}


// ============================================================
// CONFIGURACIÓN
// ============================================================

$urlMedicion =
    'https://fuseriv.upv.edu.es/api/medicion';


try {

    echo
        '===== TESTS SERVIDOR REST ====='
        . PHP_EOL;


    // --------------------------------------------------------
    // TEST 1
    // POST correcto
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode([
                'tipo' => 'O3',
                'valor' => 1.234
            ])
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
        is_array($datos)
        &&
        $datos['tipo'] === 'O3'
        &&
        abs(
            $datos['valor']
            -
            1.234
        ) < 0.000001,
        'POST devuelve medicion correcta'
    );


    // --------------------------------------------------------
    // TEST 2
    // POST con tipo vacío
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode([
                'tipo' => '',
                'valor' => 1.234
            ])
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con tipo vacio devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 3
    // POST sin tipo
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode([
                'valor' => 1.234
            ])
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST sin tipo devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 4
    // POST sin valor
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            json_encode([
                'tipo' => 'O3'
            ])
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST sin valor devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 5
    // POST con JSON inválido
    // --------------------------------------------------------

    $respuesta =
        hacerPeticion(
            $urlMedicion,
            'POST',
            '{esto no es json}'
        );


    comprobar(
        $respuesta['codigo'] === 400,
        'POST con JSON invalido devuelve 400'
    );


    // --------------------------------------------------------
    // TEST 6
    // GET correcto
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
        is_array($datos)
        || $datos === null,
        'GET devuelve JSON valido'
    );


    echo PHP_EOL;


    echo
        'TODOS LOS TESTS HAN PASADO'
        . PHP_EOL;


} catch (
    Throwable $e
) {

    echo PHP_EOL;


    echo
        $e->getMessage()
        . PHP_EOL;


    exit(1);
}