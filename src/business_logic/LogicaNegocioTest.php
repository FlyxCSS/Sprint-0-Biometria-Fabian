<?php
declare(strict_types=1);

// --------------------------------------------------------------
// Fichero: LogicaNegocioTest.php
// Descripción: Pruebas automáticas de la lógica de negocio.
// Fecha: 2026-10-14
// Autor: Fabián Useche
// Aportación: adaptación de las pruebas a la nueva estructura
//             de la tabla Mediciones.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

require_once __DIR__ . '/LogicaNegocio.php';


// ------------------------------------------------------------
// condicion: B, mensaje: Text --> comprobar()
//
// Comprueba una condición y muestra el resultado del test.
// ------------------------------------------------------------
function comprobar(
    bool $condicion,
    string $mensaje
): void {

    if (
        !$condicion
    ) {

        echo
            "ERROR - "
            . $mensaje
            . PHP_EOL;

        exit(1);
    }


    echo
        "OK - "
        . $mensaje
        . PHP_EOL;

} // comprobar()


// ------------------------------------------------------------
// crearConexionPrueba() --> PDO
//
// Crea una base de datos temporal para ejecutar los tests.
// ------------------------------------------------------------
function crearConexionPrueba(): PDO
{

    $conexion =
        new PDO(
            'sqlite::memory:'
        );


    $conexion->setAttribute(
        PDO::ATTR_ERRMODE,
        PDO::ERRMODE_EXCEPTION
    );


    $conexion->exec(
        'CREATE TABLE Mediciones (
            nombre_micro TEXT NOT NULL,
            tipo TEXT NOT NULL,
            valor REAL NOT NULL,
            fecha_hora TEXT NOT NULL,

            PRIMARY KEY (
                nombre_micro,
                fecha_hora
            )
        )'
    );


    return
        $conexion;

} // crearConexionPrueba()


echo
    "===== TESTS LOGICA DE NEGOCIO ====="
    . PHP_EOL
    . PHP_EOL;


// ------------------------------------------------------------
// TEST 1
// Guardar una medición válida.
// ------------------------------------------------------------

$conexion =
    crearConexionPrueba();


$logica =
    new LogicaNegocio(
        $conexion
    );


$medicion =
    $logica->guardarMedicion(
        'Fabian_GTI',
        'O3',
        1.234
    );


comprobar(
    $medicion['nombre_micro'] === 'Fabian_GTI'
        &&
    $medicion['tipo'] === 'O3'
        &&
    abs(
        $medicion['valor'] - 1.234
    ) < 0.000001
        &&
    isset(
        $medicion['fecha_hora']
    ),
    'guardar medicion valida'
);


// ------------------------------------------------------------
// TEST 2
// Rechazar un nombre de micro vacío.
// ------------------------------------------------------------

try {

    $logica->guardarMedicion(
        '',
        'O3',
        1.234
    );


    comprobar(
        false,
        'rechazar nombre_micro vacio'
    );

} catch (
    InvalidArgumentException $e
) {

    comprobar(
        true,
        'rechazar nombre_micro vacio'
    );
}


// ------------------------------------------------------------
// TEST 3
// Rechazar un tipo vacío.
// ------------------------------------------------------------

try {

    $logica->guardarMedicion(
        'Fabian_GTI',
        '',
        1.234
    );


    comprobar(
        false,
        'rechazar tipo vacio'
    );

} catch (
    InvalidArgumentException $e
) {

    comprobar(
        true,
        'rechazar tipo vacio'
    );
}


// ------------------------------------------------------------
// TEST 4
// Rechazar un valor no numérico.
// ------------------------------------------------------------

try {

    $logica->guardarMedicion(
        'Fabian_GTI',
        'O3',
        'abc'
    );


    comprobar(
        false,
        'rechazar valor no numerico'
    );

} catch (
    InvalidArgumentException $e
) {

    comprobar(
        true,
        'rechazar valor no numerico'
    );
}


// ------------------------------------------------------------
// TEST 5
// Recuperar la última medición almacenada.
// ------------------------------------------------------------

$ultima =
    $logica->leerMedicion();


comprobar(
    $ultima !== null
        &&
    $ultima['nombre_micro'] === 'Fabian_GTI'
        &&
    $ultima['tipo'] === 'O3'
        &&
    abs(
        $ultima['valor'] - 1.234
    ) < 0.000001,
    'leer ultima medicion'
);


// ------------------------------------------------------------
// TEST 6
// Permitir que dos micros diferentes tengan la misma fecha_hora.
// ------------------------------------------------------------

$conexion->exec(
    "INSERT INTO Mediciones
        (
            nombre_micro,
            tipo,
            valor,
            fecha_hora
        )
     VALUES
        (
            'Fabian_GTI',
            'O3',
            1.100,
            '2026-10-14 12:00:00'
        ),
        (
            'Profesor_GTI',
            'O3',
            2.200,
            '2026-10-14 12:00:00'
        )"
);


$numeroFilas =
    (int) $conexion
        ->query(
            "SELECT COUNT(*)
             FROM Mediciones
             WHERE fecha_hora = '2026-10-14 12:00:00'"
        )
        ->fetchColumn();


comprobar(
    $numeroFilas === 2,
    'permitir misma fecha_hora para micros distintos'
);


// ------------------------------------------------------------
// TEST 7
// Devolver null cuando no existen mediciones.
// ------------------------------------------------------------

$conexionVacia =
    crearConexionPrueba();


$logicaVacia =
    new LogicaNegocio(
        $conexionVacia
    );


comprobar(
    $logicaVacia->leerMedicion() === null,
    'devolver null sin mediciones'
);


echo
    PHP_EOL
    . "TODOS LOS TESTS HAN PASADO"
    . PHP_EOL;
