<?php

// --------------------------------------------------------------
// Fichero: LogicaNegocioTest.php
// Descripción: Tests automáticos de la lógica de negocio.
// Fecha: 2026-10-04
// Autor: Fabián Useche
// Aportación: pruebas automáticas de LogicaNegocio para Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------


require_once __DIR__ . '/LogicaNegocio.php';


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
// FakePDOStatement
//
// Simula las operaciones de PDOStatement necesarias para probar
// LogicaNegocio sin utilizar la base de datos real.
// ------------------------------------------------------------
class FakePDOStatement extends PDOStatement
{
    private FakePDO $conexionFake;

    private string $operacion;


    // ------------------------------------------------------------
    // conexion: FakePDO, operacion: Text
    //     --> FakePDOStatement()
    //
    // Inicializa una sentencia falsa para los tests.
    // ------------------------------------------------------------
    public function __construct(
        FakePDO $conexionFake,
        string $operacion
    ) {

        $this->conexionFake =
            $conexionFake;


        $this->operacion =
            $operacion;
    }


    // ------------------------------------------------------------
    // parametros: array --> execute() --> B
    //
    // Simula la ejecución del INSERT utilizado por
    // LogicaNegocio.guardarMedicion().
    // ------------------------------------------------------------
    public function execute(
        ?array $params = null
    ): bool {

        if (
            $this->operacion
            ===
            'insert'
        ) {

            $this->conexionFake
                ->insertar(
                    $params ?? []
                );
        }


        return true;
    }


    // ------------------------------------------------------------
    // fetch() --> Medicion | false
    //
    // Devuelve la última medición almacenada en la base falsa.
    // ------------------------------------------------------------
    public function fetch(
        int $mode = PDO::FETCH_DEFAULT,
        int $cursorOrientation = PDO::FETCH_ORI_NEXT,
        int $cursorOffset = 0
    ): mixed {

        return
            $this->conexionFake
                ->ultima();
    }
}


// ------------------------------------------------------------
// FakePDO
//
// Simula una base de datos únicamente durante los tests.
//
// No utiliza MySQL.
// No necesita credenciales.
// No modifica la base de datos real.
// ------------------------------------------------------------
class FakePDO extends PDO
{
    private array $mediciones = [];

    private int $ultimoId = 0;


    // ------------------------------------------------------------
    // FakePDO()
    //
    // Crea la base de datos falsa utilizada únicamente por
    // LogicaNegocioTest.php.
    // ------------------------------------------------------------
    public function __construct()
    {
        /*
         * No se llama al constructor real de PDO.
         *
         * Esta clase funciona únicamente como sustituto
         * temporal durante los tests.
         */
    }


    // ------------------------------------------------------------
    // query: Text --> prepare() --> PDOStatement
    //
    // Devuelve una sentencia falsa para simular el INSERT.
    // ------------------------------------------------------------
    public function prepare(
        string $query,
        array $options = []
    ): PDOStatement|false {

        return new FakePDOStatement(
            $this,
            'insert'
        );
    }


    // ------------------------------------------------------------
    // query: Text --> query() --> PDOStatement
    //
    // Devuelve una sentencia falsa para simular el SELECT.
    // ------------------------------------------------------------
    public function query(
        string $query,
        ?int $fetchMode = null,
        mixed ...$fetchModeArgs
    ): PDOStatement|false {

        return new FakePDOStatement(
            $this,
            'select'
        );
    }


    // ------------------------------------------------------------
    // lastInsertId() --> Text
    //
    // Devuelve el último identificador generado por la base falsa.
    // ------------------------------------------------------------
    public function lastInsertId(
        ?string $name = null
    ): string|false {

        return
            (string) $this->ultimoId;
    }


    // ------------------------------------------------------------
    // parametros: array --> insertar()
    //
    // Guarda una medición dentro de la memoria utilizada
    // únicamente por el test.
    // ------------------------------------------------------------
    public function insertar(
        array $parametros
    ): void {

        $this->ultimoId++;


        $this->mediciones[] = [

            'id'
                => $this->ultimoId,

            'fecha'
                => $parametros[':fecha'],

            'tipo'
                => $parametros[':tipo'],

            'valor'
                => $parametros[':valor']
        ];
    }


    // ------------------------------------------------------------
    // ultima() --> Medicion | false
    //
    // Devuelve la última medición guardada en la base falsa.
    // ------------------------------------------------------------
    public function ultima(): array|false
    {

        if (
            count(
                $this->mediciones
            )
            ===
            0
        ) {

            return false;
        }


        return
            $this->mediciones[
                count(
                    $this->mediciones
                ) - 1
            ];
    }
}


// ============================================================
// EJECUCIÓN DE LOS TESTS
// ============================================================

try {

    echo
        '===== TESTS LOGICA DE NEGOCIO ====='
        . PHP_EOL;


    $conexion =
        new FakePDO();


    $logica =
        new LogicaNegocio(
            $conexion
        );


    // --------------------------------------------------------
    // TEST 1
    // Guardar una medición válida
    // --------------------------------------------------------

    $medicion =
        $logica->guardarMedicion(
            'O3',
            1.234
        );


    comprobar(

        $medicion['id'] === 1

        &&

        $medicion['tipo'] === 'O3'

        &&

        abs(
            $medicion['valor']
            -
            1.234
        )
        <
        0.000001,

        'guardar medicion valida'
    );


    // --------------------------------------------------------
    // TEST 2
    // Rechazar un tipo vacío
    // --------------------------------------------------------

    $tipoVacioRechazado =
        false;


    try {

        $logica->guardarMedicion(
            '',
            1.234
        );

    } catch (
        InvalidArgumentException $e
    ) {

        $tipoVacioRechazado =
            true;
    }


    comprobar(
        $tipoVacioRechazado,
        'rechazar tipo vacio'
    );


    // --------------------------------------------------------
    // TEST 3
    // Rechazar un valor no numérico
    // --------------------------------------------------------

    $valorIncorrectoRechazado =
        false;


    try {

        $logica->guardarMedicion(
            'O3',
            'hola'
        );

    } catch (
        InvalidArgumentException $e
    ) {

        $valorIncorrectoRechazado =
            true;
    }


    comprobar(
        $valorIncorrectoRechazado,
        'rechazar valor no numerico'
    );


    // --------------------------------------------------------
    // TEST 4
    // Recuperar la última medición
    // --------------------------------------------------------

    $logica->guardarMedicion(
        'O3',
        2.345
    );


    $ultima =
        $logica->leerMedicion();


    comprobar(

        $ultima !== null

        &&

        $ultima['id'] === 2

        &&

        abs(
            $ultima['valor']
            -
            2.345
        )
        <
        0.000001,

        'leer ultima medicion'
    );


    // --------------------------------------------------------
    // TEST 5
    // Devolver null cuando no existen mediciones
    // --------------------------------------------------------

    $conexionVacia =
        new FakePDO();


    $logicaVacia =
        new LogicaNegocio(
            $conexionVacia
        );


    comprobar(
        $logicaVacia->leerMedicion()
        ===
        null,

        'devolver null sin mediciones'
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