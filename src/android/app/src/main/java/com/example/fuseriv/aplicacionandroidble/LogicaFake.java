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
    // nombreMicro: Text, tipo: Text, valor: R
    //      --> guardarMedicion()
    //      --> Medicion
    //
    // Envía al backend el nombre real recibido por BLE junto con
    // el tipo y el valor de la medición.
    // ------------------------------------------------------------
    public void guardarMedicion(
            String nombreMicro,
            String tipo,
            double valor,
            RespuestaGuardarMedicion respuesta
    ) {

        try {

            JSONObject json =
                    new JSONObject();


            json.put(
                    "nombre_micro",
                    nombreMicro
            );


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
                    "[GUARDAR] Micro="
                            + nombreMicro
                            + " | Tipo="
                            + tipo
                            + " | Valor="
                            + valor
            );


            PeticionarioREST peticionarioREST =
                    new PeticionarioREST();


            peticionarioREST.hacerPeticionREST(
                    "POST",
                    URL_MEDICION,
                    json.toString(),

                    new PeticionarioREST.RespuestaREST() {

                        // ------------------------------------------------------------
                        // codigo: Z, cuerpo: Text --> callback()
                        //
                        // Convierte la respuesta correcta del backend en Medicion.
                        // ------------------------------------------------------------
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

                                                    respuestaJSON.getString(
                                                            "nombre_micro"
                                                    ),

                                                    respuestaJSON.getString(
                                                            "fecha_hora"
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
                                            "[GUARDAR] Almacenada"
                                                    + " | Micro="
                                                    + medicion.getNombreMicro()
                                                    + " | FechaHora="
                                                    + medicion.getFechaHora()
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
                                        "[ERROR] Respuesta no valida: "
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
