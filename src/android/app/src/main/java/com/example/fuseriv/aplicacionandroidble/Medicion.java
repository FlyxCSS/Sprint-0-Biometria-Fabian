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


    private final int id;

    private final String fecha;

    private final String tipo;

    private final double valor;


    // ------------------------------------------------------------
    // id: N, fecha: Text, tipo: Text, valor: R
    //      --> Medicion()
    //
    // Crea una medición con los datos devueltos por el backend.
    // ------------------------------------------------------------
    public Medicion(
            int id,
            String fecha,
            String tipo,
            double valor
    ) {

        this.id =
                id;

        this.fecha =
                fecha;

        this.tipo =
                tipo;

        this.valor =
                valor;
    }


    // ------------------------------------------------------------
    // getId() --> N
    //
    // Devuelve el identificador de la medición.
    // ------------------------------------------------------------
    public int getId() {

        return id;
    }


    // ------------------------------------------------------------
    // getFecha() --> Text
    //
    // Devuelve la fecha de la medición.
    // ------------------------------------------------------------
    public String getFecha() {

        return fecha;
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

}