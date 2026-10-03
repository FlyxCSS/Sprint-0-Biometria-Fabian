package com.example.fuseriv.aplicacionandroidble;

import android.util.Log;


// --------------------------------------------------------------
// Fichero: LogicaFake.java
// Descripción: Lógica fake del cliente Android.
// Fecha: 2026-10-03
// Autor: Fabián Useche
// Aportación: implementación de guardarMedicion() para comunicar
//             Android con el servidor REST.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class LogicaFake {

    private static final String ETIQUETA_LOG =
            ">>>>";


    private static final String URL_MEDICION =
            "https://fuseriv.upv.edu.es/api/medicion";


    // ------------------------------------------------------------
    // tipo: Text, valor: R --> guardarMedicion()
    //
    // Representa en el cliente la operación guardarMedicion().
    // Convierte los datos a JSON y solicita al servidor REST
    // que almacene la medición mediante POST /medicion.
    // ------------------------------------------------------------
    public void guardarMedicion(
            String tipo,
            double valor
    ) {

        String cuerpoJSON =
                "{"
                        + "\"tipo\":\""
                        + tipo
                        + "\","
                        + "\"valor\":"
                        + valor
                        + "}";


        Log.d(
                ETIQUETA_LOG,
                "LOGICA FAKE - guardarMedicion()"
        );


        Log.d(
                ETIQUETA_LOG,
                "JSON = "
                        + cuerpoJSON
        );


        PeticionarioREST peticionario =
                new PeticionarioREST();


        peticionario.hacerPeticionREST(
                "POST",
                URL_MEDICION,
                cuerpoJSON,

                new PeticionarioREST.RespuestaREST() {

                    @Override
                    public void callback(
                            int codigo,
                            String cuerpo
                    ) {

                        Log.d(
                                ETIQUETA_LOG,
                                "RESPUESTA REST"
                        );


                        Log.d(
                                ETIQUETA_LOG,
                                "Codigo HTTP = "
                                        + codigo
                        );


                        Log.d(
                                ETIQUETA_LOG,
                                "Cuerpo = "
                                        + cuerpo
                        );
                    }
                }
        );

    } // guardarMedicion()

} // class LogicaFake