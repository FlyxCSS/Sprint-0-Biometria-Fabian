package com.example.fuseriv.aplicacionandroidble;


// --------------------------------------------------------------
// Fichero: Medicion.java
// Descripción: Representa una medición recibida del backend.
// Fecha: 2026-10-13
// Autor: Fabián Useche
// Aportación: modelo de datos utilizado por la lógica fake.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class Medicion {


    private final String nombreMicro;

    private final String fechaHora;

    private final String tipo;

    private final double valor;


    // ------------------------------------------------------------
    // nombreMicro: Text, fechaHora: Text, tipo: Text, valor: R
    //      --> Medicion()
    //
    // Crea una medición con los datos devueltos por el backend.
    // ------------------------------------------------------------
    public Medicion(
            String nombreMicro,
            String fechaHora,
            String tipo,
            double valor
    ) {

        this.nombreMicro =
                nombreMicro;

        this.fechaHora =
                fechaHora;

        this.tipo =
                tipo;

        this.valor =
                valor;
    }


    // ------------------------------------------------------------
    // getNombreMicro() --> Text
    //
    // Devuelve el nombre del microprocesador que generó la medida.
    // ------------------------------------------------------------
    public String getNombreMicro() {

        return nombreMicro;
    }


    // ------------------------------------------------------------
    // getFechaHora() --> Text
    //
    // Devuelve la fecha y hora asignadas por el backend.
    // ------------------------------------------------------------
    public String getFechaHora() {

        return fechaHora;
    }


    // ------------------------------------------------------------
    // getTipo() --> Text
    //
    // Devuelve el tipo de la medición.
    // ------------------------------------------------------------
    public String getTipo() {

        return tipo;
    }


    // ------------------------------------------------------------
    // getValor() --> R
    //
    // Devuelve el valor de la medición.
    // ------------------------------------------------------------
    public double getValor() {

        return valor;
    }

} // class Medicion
