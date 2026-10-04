package com.example.fuseriv.aplicacionandroidble;


// --------------------------------------------------------------
// Fichero: UtilidadesTest.java
// Descripción: Tests automáticos de las funciones auxiliares
//              utilizadas por la aplicación Android.
// Fecha: 2026-10-04
// Autor: Fabián Useche
// Aportación: pruebas automáticas de Utilidades para Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class UtilidadesTest {


    // ------------------------------------------------------------
    // testPpbAPpm()
    //
    // Comprueba que la conversión de ppb a ppm sea correcta.
    // ------------------------------------------------------------
    @Test
    public void testPpbAPpm() {

        double resultado =
                Utilidades.ppbAPpm(
                        1234
                );


        assertEquals(
                1.234,
                resultado,
                0.000001
        );
    }


    // ------------------------------------------------------------
    // testBytesToIntOK()
    //
    // Comprueba la conversión de bytes en orden big-endian
    // a un número entero.
    // ------------------------------------------------------------
    @Test
    public void testBytesToIntOK() {

        byte[] bytes = {
                0x0B,
                0x05
        };


        int resultado =
                Utilidades.bytesToIntOK(
                        bytes
                );


        assertEquals(
                2821,
                resultado
        );
    }


    // ------------------------------------------------------------
    // testBytesToHexString()
    //
    // Comprueba la conversión de bytes a texto hexadecimal.
    // ------------------------------------------------------------
    @Test
    public void testBytesToHexString() {

        byte[] bytes = {
                0x0B,
                0x05
        };


        String resultado =
                Utilidades.bytesToHexString(
                        bytes
                );


        assertEquals(
                "0b:05",
                resultado
        );
    }


    // ------------------------------------------------------------
    // testBytesToString()
    //
    // Comprueba la conversión de bytes ASCII a texto.
    // ------------------------------------------------------------
    @Test
    public void testBytesToString() {

        byte[] bytes = {
                65,
                66,
                67
        };


        String resultado =
                Utilidades.bytesToString(
                        bytes
                );


        assertEquals(
                "ABC",
                resultado
        );
    }

}