<?php
declare(strict_types=1);

// --------------------------------------------------------------
// Fichero: LogicaNegocio.php
// Descripción: Lógica de negocio para guardar y recuperar
//              mediciones ambientales.
// Fecha: 2026-10-04
// Autor: Fabián Useche
// Aportación: implementación de la lógica de negocio utilizada
//             durante el Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

class LogicaNegocio
{

    private PDO $conexion;


    // ------------------------------------------------------------
    // conexion --> LogicaNegocio()
    //
    // Guarda la conexión utilizada para acceder a la base de datos.
    // ------------------------------------------------------------
    public function __construct(
        PDO $conexion
    ) {

        $this->conexion =
            $conexion;

    } // __construct()


    // ------------------------------------------------------------
    // nombreMicro: Text, tipo: Text, valor: R
    //      --> guardarMedicion() --> Medicion
    //
    // Valida y almacena una nueva medición.
    // La fecha y hora se generan en el servidor.
    // ------------------------------------------------------------
    public function guardarMedicion(
        string $nombreMicro,
        string $tipo,
        mixed $valor
    ): array {

        $nombreMicro =
            trim(
                $nombreMicro
            );


        $tipo =
            trim(
                $tipo
            );


        if (
            $nombreMicro === ''
        ) {

            throw new InvalidArgumentException(
                'El nombre del micro no puede estar vacio.'
            );
        }


        if (
            $tipo === ''
        ) {

            throw new InvalidArgumentException(
                'El tipo no puede estar vacio.'
            );
        }


        if (
            !is_numeric(
                $valor
            )
        ) {

            throw new InvalidArgumentException(
                'El valor debe ser numerico.'
            );
        }


        $valorNumerico =
            (float) $valor;


        $zonaHoraria =
            new DateTimeZone(
                'Europe/Madrid'
            );


        $fechaHora =
            (
                new DateTimeImmutable(
                    'now',
                    $zonaHoraria
                )
            )->format(
                'Y-m-d H:i:s'
            );


        $consulta =
            $this->conexion->prepare(
                'INSERT INTO Mediciones
                    (
                        nombre_micro,
                        tipo,
                        valor,
                        fecha_hora
                    )
                 VALUES
                    (
                        :nombre_micro,
                        :tipo,
                        :valor,
                        :fecha_hora
                    )'
            );


        $consulta->execute(
            [
                ':nombre_micro' =>
                    $nombreMicro,

                ':tipo' =>
                    $tipo,

                ':valor' =>
                    $valorNumerico,

                ':fecha_hora' =>
                    $fechaHora,
            ]
        );


        return
            [
                'nombre_micro' =>
                    $nombreMicro,

                'tipo' =>
                    $tipo,

                'valor' =>
                    $valorNumerico,

                'fecha_hora' =>
                    $fechaHora,
            ];

    } // guardarMedicion()


    // ------------------------------------------------------------
    // leerMedicion() --> Medicion | null
    //
    // Recupera la última medición almacenada.
    // ------------------------------------------------------------
    public function leerMedicion(): ?array
    {

        $consulta =
            $this->conexion->query(
                'SELECT
                    nombre_micro,
                    tipo,
                    valor,
                    fecha_hora
                 FROM Mediciones
                 ORDER BY
                    fecha_hora DESC,
                    nombre_micro ASC
                LIMIT 1'
            );


        $medicion =
            $consulta->fetch(
                PDO::FETCH_ASSOC
            );


        if (
            $medicion === false
        ) {

            return null;
        }


        $medicion['valor'] =
            (float) $medicion['valor'];


        return
            $medicion;

    } // leerMedicion()

} // class LogicaNegocio
