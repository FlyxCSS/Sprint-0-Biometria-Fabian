package com.example.fuseriv.aplicacionandroidble;

import org.junit.Test;
import static org.junit.Assert.*;

// --------------------------------------------------------------
// Fichero: TramaIBeaconTest.java
// Descripción: Pruebas unitarias del analizador iBeacon.
// Fecha: 2026-10-14
// Autor: Fabián Useche
// Aportación: verifica los campos usados por el Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------
public class TramaIBeaconTest {

    // ------------------------------------------------------------
    // testExtraerCampos() -->
    // Verifica UUID, Major, Minor y TxPower de una trama válida.
    // ------------------------------------------------------------
    @Test
    public void testExtraerCampos() {
        byte[] anuncio = new byte[30];
        for (int i = 0; i < 16; i++) anuncio[9 + i] = (byte)(i + 1);
        anuncio[25] = 0x0B; // O3 = 11
        anuncio[26] = 0x05; // contador 5
        anuncio[27] = 0x04; // 1234 ppb = 0x04D2
        anuncio[28] = (byte) 0xD2;
        anuncio[29] = (byte) -53;

        TramaIBeacon trama = new TramaIBeacon(anuncio);
        assertEquals(16, trama.getUUID().length);
        assertEquals(1, trama.getUUID()[0]);
        assertEquals(16, trama.getUUID()[15]);
        int major = Utilidades.bytesToIntOK(trama.getMajor());
        assertEquals(2821, major);
        assertEquals(11, (major >> 8) & 0xFF);
        assertEquals(5, major & 0xFF);
        assertEquals(1234, Utilidades.bytesToIntOK(trama.getMinor()));
        assertEquals(-53, trama.getTxPower());
    }

    // ------------------------------------------------------------
    // testRechazaTramaCorta() -->
    // Comprueba que se rechace una trama incompleta.
    // ------------------------------------------------------------
    @Test(expected = IllegalArgumentException.class)
    public void testRechazaTramaCorta() {
        new TramaIBeacon(new byte[29]);
    }

    // ------------------------------------------------------------
    // testRechazaTramaNula() -->
    // Comprueba que se rechace una trama ausente.
    // ------------------------------------------------------------
    @Test(expected = IllegalArgumentException.class)
    public void testRechazaTramaNula() {
        new TramaIBeacon(null);
    }
}
