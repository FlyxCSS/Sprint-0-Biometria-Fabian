package com.example.fuseriv.aplicacionandroidble;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// --------------------------------------------------------------
// Fichero: PeticionarioREST.java
// Descripción: Cliente HTTP para realizar peticiones al API REST.
// Fecha: 2026-10-13
// Autor: Fabián Useche
// Base del código: Jordi Bataller i Mascarell
// Aportación: limpieza y adaptación para el Sprint 0.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class PeticionarioREST
        extends AsyncTask<Void, Void, Boolean> {


    private static final String ETIQUETA_LOG =
            "FABI_REST";


    // ------------------------------------------------------------
    // Interfaz utilizada para devolver la respuesta HTTP.
    // ------------------------------------------------------------
    public interface RespuestaREST {

        void callback(
                int codigo,
                String cuerpo
        );
    }


    private String metodo;

    private String urlDestino;

    private String cuerpoPeticion;

    private RespuestaREST respuestaREST;


    private int codigoRespuesta =
            -1;


    private String cuerpoRespuesta =
            "";


    // ------------------------------------------------------------
    // metodo: Text, url: Text, cuerpo: Text | Nulo, respuesta: RespuestaREST
    //        --> hacerPeticionREST() -->
    //
    // Configura una petición HTTP y comienza su ejecución
    // asíncrona.
    // ------------------------------------------------------------
    public void hacerPeticionREST(
            String metodo,
            String urlDestino,
            String cuerpo,
            RespuestaREST respuesta
    ) {

        this.metodo =
                metodo;

        this.urlDestino =
                urlDestino;

        this.cuerpoPeticion =
                cuerpo;

        this.respuestaREST =
                respuesta;


        execute();

    } // hacerPeticionREST()


    // ------------------------------------------------------------
    // params: [Text] --> doInBackground() --> B
    //
    // Ejecuta la petición HTTP fuera del hilo principal.
    // ------------------------------------------------------------
    @Override
    protected Boolean doInBackground(
            Void... params
    ) {

        HttpURLConnection conexion =
                null;


        try {

            Log.d(
                    ETIQUETA_LOG,
                    "[HTTP] "
                            + metodo
                            + " "
                            + urlDestino
            );


            URL url =
                    new URL(
                            urlDestino
                    );


            conexion =
                    (HttpURLConnection)
                            url.openConnection();


            conexion.setRequestMethod(
                    metodo
            );


            conexion.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=utf-8"
            );


            conexion.setRequestProperty(
                    "Accept",
                    "application/json"
            );


            conexion.setConnectTimeout(
                    10000
            );


            conexion.setReadTimeout(
                    10000
            );


            conexion.setDoInput(
                    true
            );


            if (
                    !"GET".equals(metodo)
                            &&
                            cuerpoPeticion != null
            ) {

                conexion.setDoOutput(
                        true
                );


                DataOutputStream salida =
                        new DataOutputStream(
                                conexion.getOutputStream()
                        );


                salida.writeBytes(
                        cuerpoPeticion
                );


                salida.flush();

                salida.close();
            }


            codigoRespuesta =
                    conexion.getResponseCode();


            InputStream entrada;


            if (
                    codigoRespuesta >= 200
                            &&
                            codigoRespuesta < 400
            ) {

                entrada =
                        conexion.getInputStream();

            } else {

                entrada =
                        conexion.getErrorStream();
            }


            if (entrada != null) {

                BufferedReader lector =
                        new BufferedReader(
                                new InputStreamReader(
                                        entrada
                                )
                        );


                StringBuilder acumulador =
                        new StringBuilder();


                String linea;


                while (
                        (linea = lector.readLine())
                                != null
                ) {

                    acumulador.append(
                            linea
                    );
                }


                lector.close();


                cuerpoRespuesta =
                        acumulador.toString();
            }


            Log.d(
                    ETIQUETA_LOG,
                    "[HTTP] Codigo = "
                            + codigoRespuesta
            );


            Log.d(
                    ETIQUETA_LOG,
                    "[HTTP] Respuesta = "
                            + cuerpoRespuesta
            );


            return true;


        } catch (Exception e) {

            Log.d(
                    ETIQUETA_LOG,
                    "[HTTP][ERROR] "
                            + e.getMessage()
            );


            return false;


        } finally {

            if (conexion != null) {

                conexion.disconnect();
            }
        }

    } // doInBackground()


    // ------------------------------------------------------------
    // resultado: B --> onPostExecute()
    //
    // Entrega al callback la respuesta recibida del servidor.
    // ------------------------------------------------------------
    @Override
    protected void onPostExecute(
            Boolean resultado
    ) {

        super.onPostExecute(
                resultado
        );


        Log.d(
                ETIQUETA_LOG,
                "[HTTP] Peticion terminada = "
                        + resultado
        );


        if (respuestaREST != null) {

            respuestaREST.callback(
                    codigoRespuesta,
                    cuerpoRespuesta
            );
        }

    } // onPostExecute()

} // class PeticionarioREST
