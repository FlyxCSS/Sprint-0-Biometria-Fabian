/*
------------------------------------------------------------
Fichero: LogicaFake.js
Descripción: Lógica fake del cliente web utilizada para
             solicitar mediciones al servidor REST.
Fecha: 2026-10-04
Autor: Fabián Useche
Aportación: lógica fake del navegador para el Sprint 0.
Copyright: material académico y modificaciones del autor.
------------------------------------------------------------
*/


class LogicaFake {

    constructor() {

        this.urlMedicion =
            "/api/medicion";
    }


    /*
    ------------------------------------------------------------
    leerMedicion() --> Medicion | null

    Solicita al servidor REST la última medición almacenada.
    ------------------------------------------------------------
    */
    async leerMedicion() {

        const respuesta =
            await fetch(
                this.urlMedicion
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error HTTP: "
                + respuesta.status
            );
        }


        return await respuesta.json();
    }

}


const logicaFake =
    new LogicaFake();