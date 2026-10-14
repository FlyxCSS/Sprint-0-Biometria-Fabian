-- --------------------------------------------------------------
-- Fichero: database.sql
-- Descripción: Define la tabla utilizada para almacenar
--              mediciones ambientales durante el Sprint 0.
-- Fecha: 2026-10-14
-- Autor: Fabián Useche
-- Aportación: adaptación de la base de datos para utilizar
--             nombre_micro y fecha_hora como clave primaria.
-- Copyright: material académico y modificaciones del autor.
-- --------------------------------------------------------------


-- ------------------------------------------------------------
-- TABLA: Mediciones
--
-- Guarda las mediciones recibidas desde los microprocesadores.
--
-- Clave primaria compuesta:
-- (nombre_micro, fecha_hora)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Mediciones (

    nombre_micro VARCHAR(100) NOT NULL,

    tipo VARCHAR(50) NOT NULL,

    valor DOUBLE NOT NULL,

    fecha_hora DATETIME NOT NULL,

    PRIMARY KEY (
        nombre_micro,
        fecha_hora
    )

);


-- ------------------------------------------------------------
-- PRUEBAS MANUALES
--
-- Estas consultas están comentadas para que no se ejecuten
-- automáticamente al crear la base de datos.
-- ------------------------------------------------------------


-- Insertar una medición de prueba:
--
-- INSERT INTO Mediciones (
--     nombre_micro,
--     tipo,
--     valor,
--     fecha_hora
-- )
-- VALUES (
--     'Fabian_GTI',
--     'O3',
--     1.234,
--     '2026-10-14 12:00:00'
-- );


-- Insertar otra medición realizada a la misma hora
-- por un microprocesador diferente:
--
-- INSERT INTO Mediciones (
--     nombre_micro,
--     tipo,
--     valor,
--     fecha_hora
-- )
-- VALUES (
--     'Profesor_GTI',
--     'O3',
--     0.950,
--     '2026-10-14 12:00:00'
-- );


-- Consultar todas las mediciones:
--
-- SELECT *
-- FROM Mediciones;


-- Consultar la última medición almacenada:
--
-- SELECT
--     nombre_micro,
--     tipo,
--     valor,
--     fecha_hora
-- FROM Mediciones
-- ORDER BY
--     fecha_hora DESC,
--     nombre_micro ASC
-- LIMIT 1;
