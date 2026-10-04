/*
------------------------------------------------------------
Fichero: LogicaFakeTest.js
Descripción: Tests automáticos de la lógica fake de la web.
Fecha: 2026-10-04
Autor: Fabián Useche
Aportación: pruebas automáticas de LogicaFake para Sprint 0.
Copyright: material académico y modificaciones del autor.
------------------------------------------------------------
*/


let textoResultado =
    "";


// ------------------------------------------------------------
// condicion: B, nombre: Text --> comprobar()
//
// Comprueba una condición.
// Si es correcta muestra OK.
// Si falla lanza un error.
// ------------------------------------------------------------
function comprobar(
    condicion,
    nombre
) {

    if (!condicion) {

        throw new Error(
            "FALLO - "
            + nombre
        );
    }


    textoResultado +=
        "OK - "
        + nombre
        + "\n";
}


// ------------------------------------------------------------
// ejecutarTests()
//
// Ejecuta automáticamente los tests principales de LogicaFake.
// ------------------------------------------------------------
async function ejecutarTests() {

    const fetchOriginal =
        window.fetch;


    try {

        textoResultado =
            "===== TESTS LOGICA FAKE WEB =====\n";


        // ----------------------------------------------------
        // TEST 1
        // leerMedicion devuelve una medición correcta
        // ----------------------------------------------------

        let urlRecibida =
            null;


        window.fetch =
            async function (
                url
            ) {

                urlRecibida =
                    url;


                return {

                    ok: true,

                    json: async function () {

                        return {

                            id: 1,

                            fecha:
                                "2026-10-04 14:42:10",

                            tipo:
                                "O3",

                            valor:
                                1.234
                        };
                    }
                };
            };


        const medicion =
            await logicaFake
                .leerMedicion();


        comprobar(

            medicion.tipo === "O3"

            &&

            Math.abs(
                medicion.valor
                -
                1.234
            ) < 0.000001,

            "leer medicion correcta"
        );


        // ----------------------------------------------------
        // TEST 2
        // Comprobar URL utilizada por la lógica fake
        // ----------------------------------------------------

        comprobar(

            urlRecibida
            ===
            "/api/medicion",

            "usar URL /api/medicion"
        );


        // ----------------------------------------------------
        // TEST 3
        // El servidor puede devolver null
        // ----------------------------------------------------

        window.fetch =
            async function () {

                return {

                    ok: true,

                    json: async function () {

                        return null;
                    }
                };
            };


        const medicionVacia =
            await logicaFake
                .leerMedicion();


        comprobar(

            medicionVacia
            ===
            null,

            "aceptar respuesta null"
        );


        // ----------------------------------------------------
        // TEST 4
        // Una respuesta HTTP incorrecta debe producir error
        // ----------------------------------------------------

        window.fetch =
            async function () {

                return {

                    ok: false,

                    status: 500
                };
            };


        let errorDetectado =
            false;


        try {

            await logicaFake
                .leerMedicion();

        } catch (error) {

            errorDetectado =
                true;
        }


        comprobar(

            errorDetectado,

            "detectar error HTTP"
        );


        textoResultado +=
            "\nTODOS LOS TESTS HAN PASADO";


    } catch (error) {

        textoResultado +=
            "\n"
            + error.message;


    } finally {

        /*
         * Restauramos fetch para no modificar
         * el comportamiento normal del navegador.
         */

        window.fetch =
            fetchOriginal;


        document.getElementById(
            "resultado"
        ).textContent =
            textoResultado;
    }
}


ejecutarTests();