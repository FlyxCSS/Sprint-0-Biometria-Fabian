<?php
declare(strict_types=1);

// --------------------------------------------------------------
// Fichero: index.php
// Descripción: Servidor REST para guardar y recuperar mediciones.
// Fecha: 2026-10-14
// Autor: Fabián Useche
// Aportación: adaptación del API REST para recibir y devolver
//             el nombre del microprocesador.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------


require_once __DIR__ . '/../business_logic/LogicaNegocio.php';


header(
    'Content-Type: application/json; charset=utf-8'
);


// ------------------------------------------------------------
// crearConexion() --> ConexionBD
//
// Crea y devuelve la conexión utilizada por la lógica de negocio.
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


    return
        new PDO(
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
// datos, codigoHTTP: N --> responderJSON()
//
// Envía una respuesta JSON utilizando el código HTTP indicado
// y finaliza la ejecución de la petición.
// ------------------------------------------------------------
function responderJSON(
    mixed $datos,
    int $codigoHTTP = 200
): void {

    http_response_code(
        $codigoHTTP
    );


    echo
        json_encode(
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


    return
        rtrim(
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
    // Entrada:
    // {
    //     "nombre_micro": "Fabian_GTI",
    //     "tipo": "O3",
    //     "valor": 1.234
    // }
    //
    // Guarda una nueva medición.
    // La fecha_hora la genera la lógica de negocio.
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


        if (
            !is_array(
                $datos
            )
        ) {

            responderJSON(
                [
                    'error'
                        => 'El cuerpo debe contener JSON valido'
                ],
                400
            );
        }


        if (
            !array_key_exists(
                'nombre_micro',
                $datos
            )
        ) {

            responderJSON(
                [
                    'error'
                        => 'Falta el campo nombre_micro'
                ],
                400
            );
        }


        if (
            !array_key_exists(
                'tipo',
                $datos
            )
        ) {

            responderJSON(
                [
                    'error'
                        => 'Falta el campo tipo'
                ],
                400
            );
        }


        if (
            !array_key_exists(
                'valor',
                $datos
            )
        ) {

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
                    $datos['nombre_micro'],
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
    // Devuelve la última medición almacenada.
    //
    // Salida:
    // {
    //     "nombre_micro": "Fabian_GTI",
    //     "tipo": "O3",
    //     "valor": 1.234,
    //     "fecha_hora": "2026-10-14 12:00:00"
    // }
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


} catch (
    Throwable $e
) {

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
