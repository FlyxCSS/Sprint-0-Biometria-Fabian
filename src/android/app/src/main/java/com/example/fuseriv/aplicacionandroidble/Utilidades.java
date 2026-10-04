package com.example.fuseriv.aplicacionandroidble;


// --------------------------------------------------------------
// Fichero: Utilidades.java
// Descripción: Funciones auxiliares para interpretar los datos
//              recibidos mediante Bluetooth.
// Fecha: 2026-10-03
// Autor: Fabián Useche
// Base del código: Jordi Bataller i Mascarell
// Aportación: simplificación y conversión de ppb a ppm.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class Utilidades {


    // ------------------------------------------------------------
    // bytes: Bytes --> bytesToString() --x
    // Text <--
    //
    // Convierte un conjunto de bytes en texto.
    // ------------------------------------------------------------
    public static String bytesToString(
            byte[] bytes
    ) {

        if (bytes == null) {

            return "";
        }


        StringBuilder resultado =
                new StringBuilder();


        for (
                byte valor
                : bytes
        ) {

            resultado.append(
                    (char) valor
            );
        }


        return resultado.toString();

    } // bytesToString()


    // ------------------------------------------------------------
    // bytes: Bytes --> bytesToIntOK() --x
    // N <--
    //
    // Convierte hasta cuatro bytes sin signo y en orden
    // big-endian en un número entero.
    // ------------------------------------------------------------
    public static int bytesToIntOK(
            byte[] bytes
    ) {

        if (bytes == null) {

            return 0;
        }


        if (bytes.length > 4) {

            throw new IllegalArgumentException(
                    "Demasiados bytes para convertir a int"
            );
        }


        int resultado =
                0;


        for (
                byte valor
                : bytes
        ) {

            resultado =
                    (resultado << 8)
                            |
                            (valor & 0xFF);
        }


        return resultado;

    } // bytesToIntOK()


    // ------------------------------------------------------------
    // bytes: Bytes --> bytesToHexString() --x
    // Text <--
    //
    // Convierte bytes en una representación hexadecimal.
    // ------------------------------------------------------------
    public static String bytesToHexString(
            byte[] bytes
    ) {

        if (bytes == null) {

            return "";
        }


        StringBuilder resultado =
                new StringBuilder();


        for (
                int i = 0;
                i < bytes.length;
                i++
        ) {

            resultado.append(
                    String.format(
                            "%02x",
                            bytes[i] & 0xFF
                    )
            );


            if (
                    i
                            < bytes.length - 1
            ) {

                resultado.append(
                        ":"
                );
            }
        }


        return resultado.toString();

    } // bytesToHexString()


    // ------------------------------------------------------------
    // ppb: N --> ppbAPpm() --x
    // R <--
    //
    // Convierte una concentración expresada en ppb a ppm.
    // ------------------------------------------------------------
    public static double ppbAPpm(
            int ppb
    ) {

        return
                ppb / 1000.0;

    } // ppbAPpm()

} // class Utilidades