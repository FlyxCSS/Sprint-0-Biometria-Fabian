package com.example.fuseriv.aplicacionandroidble;

import java.util.Arrays;


// --------------------------------------------------------------
// Fichero: TramaIBeacon.java
// Descripción: Representación de los campos utilizados de una
//              trama iBeacon recibida mediante BLE.
// Fecha: 2026-10-03
// Autor: Fabián Useche
// Base del código: Jordi Bataller i Mascarell
// Aportación: simplificación para UUID, Major, Minor y TxPower.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class TramaIBeacon {

    private final byte[] uuid;

    private final byte[] major;

    private final byte[] minor;

    private final byte txPower;


    // ------------------------------------------------------------
    // bytes --> TramaIBeacon()
    //
    // Extrae UUID, Major, Minor y TxPower de una trama iBeacon.
    // ------------------------------------------------------------
    public TramaIBeacon(
            byte[] bytes
    ) {

        if (
                bytes == null
                        ||
                        bytes.length < 30
        ) {

            throw new IllegalArgumentException(
                    "La trama iBeacon necesita al menos 30 bytes"
            );
        }


        uuid =
                Arrays.copyOfRange(
                        bytes,
                        9,
                        25
                );


        major =
                Arrays.copyOfRange(
                        bytes,
                        25,
                        27
                );


        minor =
                Arrays.copyOfRange(
                        bytes,
                        27,
                        29
                );


        txPower =
                bytes[29];

    } // TramaIBeacon()


    // ------------------------------------------------------------
    // getUUID() --> [N]_16
    //
    // Devuelve el UUID recibido.
    // ------------------------------------------------------------
    public byte[] getUUID() {

        return uuid;

    } // getUUID()


    // ------------------------------------------------------------
    // getMajor() --> [N]_2
    //
    // Devuelve los dos bytes del campo Major.
    // ------------------------------------------------------------
    public byte[] getMajor() {

        return major;

    } // getMajor()


    // ------------------------------------------------------------
    // getMinor() --> [N]_2
    //
    // Devuelve los dos bytes del campo Minor.
    // ------------------------------------------------------------
    public byte[] getMinor() {

        return minor;

    } // getMinor()


    // ------------------------------------------------------------
    // getTxPower() --> Z
    //
    // Devuelve el TxPower recibido en la trama.
    // ------------------------------------------------------------
    public byte getTxPower() {

        return txPower;

    } // getTxPower()

} // class TramaIBeacon