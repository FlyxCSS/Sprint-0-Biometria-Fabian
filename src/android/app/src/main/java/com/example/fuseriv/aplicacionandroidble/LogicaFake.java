package com.example.fuseriv.aplicacionandroidble;

import android.util.Log;

import org.json.JSONObject;


// --------------------------------------------------------------
// Fichero: LogicaFake.java
// Descripción: Lógica fake del cliente Android.
// Fecha: 2026-10-13
// Autor: Fabián Useche
// Aportación: implementación de guardarMedicion() para comunicar
//             Android con el servidor REST.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class LogicaFake {


    private static final String ETIQUETA_LOG =
            "FABI_LOGICA";


    private static final String URL_MEDICION =
            "https://fuseriv.upv.edu.es/api/medicion";


    // ------------------------------------------------------------
    // Interfaz utilizada para devolver de forma asíncrona
    // el resultado de guardarMedicion().
    // ------------------------------------------------------------
    public interface RespuestaGuardarMedicion {

        void alGuardar(
                Medicion medicion
        );

        void alFallar(
                String error
        );
    }


    // ------------------------------------------------------------
    // tipo: Text, valor: R
    //      --> guardarMedicion()
    //      --> Medicion
    //
    // Representa en Android la operación guardarMedicion().
    // La Medicion se entrega de forma asíncrona mediante callback.
    // ------------------------------------------------------------
    public void guardarMedicion(
            String tipo,
            double valor,
            RespuestaGuardarMedicion respuesta
    ) {

        try {

            JSONObject json =
                    new JSONObject();


            json.put(
                    "tipo",
                    tipo
            );


            json.put(
                    "valor",
                    valor
            );


            Log.d(
                    ETIQUETA_LOG,
                    "[GUARDAR] Enviando medicion"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "[GUARDAR] Tipo = "
                            + tipo
                            + " | Valor = "
                            + valor
            );


            PeticionarioREST peticionarioREST =
                    new PeticionarioREST();


            peticionarioREST.hacerPeticionREST(
                    "POST",
                    URL_MEDICION,
                    json.toString(),

                    new PeticionarioREST.RespuestaREST() {

                        @Override
                        public void callback(
                                int codigo,
                                String cuerpo
                        ) {

                            try {

                                if (
                                        codigo >= 200
                                                &&
                                                codigo < 300
                                ) {

                                    JSONObject respuestaJSON =
                                            new JSONObject(
                                                    cuerpo
                                            );


                                    Medicion medicion =
                                            new Medicion(

                                                    respuestaJSON.getInt(
                                                            "id"
                                                    ),

                                                    respuestaJSON.getString(
                                                            "fecha"
                                                    ),

                                                    respuestaJSON.getString(
                                                            "tipo"
                                                    ),

                                                    respuestaJSON.getDouble(
                                                            "valor"
                                                    )
                                            );


                                    Log.d(
                                            ETIQUETA_LOG,
                                            "[GUARDAR] Medicion almacenada correctamente"
                                    );


                                    Log.d(
                                            ETIQUETA_LOG,
                                            "[GUARDAR] ID = "
                                                    + medicion.getId()
                                                    + " | Tipo = "
                                                    + medicion.getTipo()
                                                    + " | Valor = "
                                                    + medicion.getValor()
                                    );


                                    respuesta.alGuardar(
                                            medicion
                                    );

                                } else {

                                    Log.d(
                                            ETIQUETA_LOG,
                                            "[ERROR] HTTP "
                                                    + codigo
                                                    + " | "
                                                    + cuerpo
                                    );


                                    respuesta.alFallar(
                                            "HTTP "
                                                    + codigo
                                                    + ": "
                                                    + cuerpo
                                    );
                                }

                            } catch (Exception e) {

                                Log.d(
                                        ETIQUETA_LOG,
                                        "[ERROR] No se pudo interpretar la respuesta: "
                                                + e.getMessage()
                                );


                                respuesta.alFallar(
                                        e.getMessage()
                                );
                            }
                        }
                    }
            );

        } catch (Exception e) {

            Log.d(
                    ETIQUETA_LOG,
                    "[ERROR] No se pudo preparar la medicion: "
                            + e.getMessage()
            );


            respuesta.alFallar(
                    e.getMessage()
            );
        }

    } // guardarMedicion()

} // class LogicaFake