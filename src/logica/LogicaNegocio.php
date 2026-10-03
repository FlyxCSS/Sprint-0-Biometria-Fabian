<?php

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
    // conexion: PDO --> LogicaNegocio()
    //
    // Inicializa la lógica de negocio utilizando una conexión
    // PDO proporcionada desde el exterior.
    // ------------------------------------------------------------
    public function __construct(
        PDO $conexion
    ) {
        $this->conexion = $conexion;
    }


    // ------------------------------------------------------------
    // tipo: Text, valor: R --> guardarMedicion() --> Medicion
    //
    // Valida y almacena una nueva medición en la base de datos.
    // La fecha y hora se generan en el backend utilizando
    // la zona horaria Europe/Madrid.
    // ------------------------------------------------------------
    public function guardarMedicion(
        string $tipo,
        $valor
    ): array {

        $tipo = trim($tipo);


        if ($tipo === '') {

            throw new InvalidArgumentException(
                'El tipo de medición no puede estar vacío'
            );
        }


        if (!is_numeric($valor)) {

            throw new InvalidArgumentException(
                'El valor de la medición debe ser numérico'
            );
        }


        $valor =
            (float) $valor;


        /*
         * La fecha se genera en el servidor.
         *
         * Se indica explícitamente Europe/Madrid para no depender
         * de la configuración horaria del servidor Plesk.
         */
        $fecha = (
            new DateTimeImmutable(
                'now',
                new DateTimeZone(
                    'Europe/Madrid'
                )
            )
        )->format(
            'Y-m-d H:i:s'
        );


        $sql = '
            INSERT INTO Mediciones (
                fecha,
                tipo,
                valor
            )
            VALUES (
                :fecha,
                :tipo,
                :valor
            )
        ';


        $sentencia =
            $this->conexion->prepare(
                $sql
            );


        $sentencia->execute([
            ':fecha' => $fecha,
            ':tipo' => $tipo,
            ':valor' => $valor
        ]);


        $id =
            (int) $this->conexion->lastInsertId();


        return [
            'id' => $id,
            'fecha' => $fecha,
            'tipo' => $tipo,
            'valor' => $valor
        ];

    } // guardarMedicion()


    // ------------------------------------------------------------
    // leerMedicion() --> Medicion | null
    //
    // Recupera la última medición almacenada en la base de datos.
    // Si no existen mediciones devuelve null.
    // ------------------------------------------------------------
    public function leerMedicion(): ?array
    {

        $sql = '
            SELECT
                id,
                fecha,
                tipo,
                valor
            FROM Mediciones
            ORDER BY id DESC
            LIMIT 1
        ';


        $sentencia =
            $this->conexion->query(
                $sql
            );


        $medicion =
            $sentencia->fetch(
                PDO::FETCH_ASSOC
            );


        if ($medicion === false) {

            return null;
        }


        $medicion['id'] =
            (int) $medicion['id'];


        $medicion['valor'] =
            (float) $medicion['valor'];


        return $medicion;

    } // leerMedicion()

} // class LogicaNegocio