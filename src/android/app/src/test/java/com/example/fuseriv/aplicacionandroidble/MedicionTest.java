package com.example.fuseriv.aplicacionandroidble;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


// --------------------------------------------------------------
// Fichero: MedicionTest.java
// Descripción: Tests del modelo Medicion.
// Fecha: 2026-10-13
// Autor: Fabián Useche
// --------------------------------------------------------------

public class MedicionTest {


    // ------------------------------------------------------------
    // testMedicionGuardaTodosLosCampos()
    //
    // Comprueba los cuatro campos del nuevo modelo de medición.
    // ------------------------------------------------------------
    @Test
    public void testMedicionGuardaTodosLosCampos() {

        Medicion medicion =
                new Medicion(
                        "Fabian_GTI",
                        "2026-10-13 22:57:10",
                        "O3",
                        1.234
                );


        assertEquals(
                "Fabian_GTI",
                medicion.getNombreMicro()
        );


        assertEquals(
                "2026-10-13 22:57:10",
                medicion.getFechaHora()
        );


        assertEquals(
                "O3",
                medicion.getTipo()
        );


        assertEquals(
                1.234,
                medicion.getValor(),
                0.0001
        );
    }

} // class MedicionTest
