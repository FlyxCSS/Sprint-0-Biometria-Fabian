<?php

// --------------------------------------------------------------
// Fichero: index.php
// Descripción: Servidor REST para guardar y recuperar mediciones.
// Fecha: 2026-10-04
// Autor: Fabián Useche
// Aportación: implementación del API REST utilizado durante
//             el Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------


require_once __DIR__ . '/../logica/LogicaNegocio.php';


header(
    'Content-Type: application/json; charset=utf-8'
);


// ------------------------------------------------------------
// crearConexion() --> PDO
//
// Crea y devuelve una conexión PDO con la base de datos.
//
// En el repositorio público se utilizan valores genéricos.
// En el servidor Plesk estos valores se sustituyen por las
// credenciales reales.
// ------------------------------------------------------------
function crearConexion(): PDO
{
    $host =
        'DB_HOST';

    $nombreBD =
        'DB_NAME';

    $usuario =
        'DB_USER';

    $contrasena =
        'DB_PASSWORD';


    $dsn =
        "mysql:host={$host};"
        . "dbname={$nombreBD};"
        . "charset=utf8mb4";


    return new PDO(
        $dsn,
        $usuario,
        $contrasena,
        [
            PDO::ATTR_ERRMODE
                => PDO::ERRMODE_EXCEPTION,

            PDO::ATTR_DEFAULT_FETCH_MODE
                => PDO::FETCH_ASSOC
        ]
    );

} // crearConexion()


// ------------------------------------------------------------
// datos, codigo_http: N --> responderJSON()
//
// Envía una respuesta JSON utilizando el código HTTP indicado
// y finaliza la ejecución de la petición.
// ------------------------------------------------------------
function responderJSON(
    $datos,
    int $codigoHTTP = 200
): void {

    http_response_code(
        $codigoHTTP
    );


    echo json_encode(
        $datos,
        JSON_UNESCAPED_UNICODE
    );


    exit;

} // responderJSON()


// ------------------------------------------------------------
// obtenerRuta() --> Text
//
// Obtiene la ruta solicitada a partir de la petición HTTP.
// ------------------------------------------------------------
function obtenerRuta(): string
{
    $ruta =
        parse_url(
            $_SERVER['REQUEST_URI'],
            PHP_URL_PATH
        );


    return rtrim(
        $ruta,
        '/'
    );

} // obtenerRuta()


try {

    $conexion =
        crearConexion();


    $logica =
        new LogicaNegocio(
            $conexion
        );


    $metodo =
        $_SERVER['REQUEST_METHOD'];


    $ruta =
        obtenerRuta();


    // ----------------------------------------------------------
    // POST /medicion
    //
    // Recibe una medición en JSON y solicita a la lógica de
    // negocio que la almacene.
    // ----------------------------------------------------------
    if (
        $metodo === 'POST'
        &&
        str_ends_with(
            $ruta,
            '/medicion'
        )
    ) {

        $contenido =
            file_get_contents(
                'php://input'
            );


        $datos =
            json_decode(
                $contenido,
                true
            );


        if (!is_array($datos)) {

            responderJSON(
                [
                    'error'
                        => 'El cuerpo debe contener JSON válido'
                ],
                400
            );
        }


        if (!array_key_exists(
            'tipo',
            $datos
        )) {

            responderJSON(
                [
                    'error'
                        => 'Falta el campo tipo'
                ],
                400
            );
        }


        if (!array_key_exists(
            'valor',
            $datos
        )) {

            responderJSON(
                [
                    'error'
                        => 'Falta el campo valor'
                ],
                400
            );
        }


        try {

            $medicion =
                $logica->guardarMedicion(
                    $datos['tipo'],
                    $datos['valor']
                );


            responderJSON(
                $medicion,
                201
            );


        } catch (
            InvalidArgumentException $e
        ) {

            responderJSON(
                [
                    'error'
                        => $e->getMessage()
                ],
                400
            );
        }
    }


    // ----------------------------------------------------------
    // GET /medicion
    //
    // Solicita a la lógica de negocio la última medición
    // almacenada y la devuelve en formato JSON.
    // ----------------------------------------------------------
    if (
        $metodo === 'GET'
        &&
        str_ends_with(
            $ruta,
            '/medicion'
        )
    ) {

        $medicion =
            $logica->leerMedicion();


        responderJSON(
            $medicion,
            200
        );
    }


    // ----------------------------------------------------------
    // RUTA NO ENCONTRADA
    // ----------------------------------------------------------

    responderJSON(
        [
            'error'
                => 'Ruta no encontrada'
        ],
        404
    );


} catch (Throwable $e) {

    // ----------------------------------------------------------
    // ERROR INTERNO
    // ----------------------------------------------------------

    responderJSON(
        [
            'error'
                => 'Error interno del servidor'
        ],
        500
    );
}