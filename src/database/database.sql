-- ------------------------------------------------------------
-- Fichero: database.sql
-- Descripción: Creación de la base de datos utilizada para
--              almacenar las mediciones del Sprint 0.
-- Fecha: 2026-10-04
-- Autor: Fabián Useche
-- Aportación: diseño e implementación de la tabla Mediciones.
-- Copyright: material académico y modificaciones del autor.
-- ------------------------------------------------------------


-- ------------------------------------------------------------
-- Tabla: Mediciones
--
-- Diseño:
--
-- Mediciones = (
--     id: N,
--     fecha: Text,
--     tipo: Text,
--     valor: R
-- )
--
-- Cada fila representa una medición almacenada por la lógica
-- de negocio.
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Mediciones (

    id INT AUTO_INCREMENT PRIMARY KEY,

    fecha DATETIME NOT NULL,

    tipo VARCHAR(50) NOT NULL,

    valor DOUBLE NOT NULL

);


-- ------------------------------------------------------------
-- CONSULTAS DE PRUEBA MANUAL
--
-- Estas consultas quedan comentadas para evitar modificar
-- accidentalmente los datos reales de la base de datos.
-- ------------------------------------------------------------


-- Insertar una medición de prueba:
--
-- INSERT INTO Mediciones (
--     fecha,
--     tipo,
--     valor
-- )
-- VALUES (
--     '2026-10-04 12:00:00',
--     'O3',
--     1.234
-- );


-- Leer todas las mediciones:
--
-- SELECT *
-- FROM Mediciones;


-- Leer la última medición almacenada:
--
-- SELECT *
-- FROM Mediciones
-- ORDER BY id DESC
-- LIMIT 1;