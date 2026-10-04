/*
------------------------------------------------------------
Fichero: app.js
Descripción: Control de la interfaz gráfica de la web.
Fecha: 2026-10-04
Autor: Fabián Useche
Aportación: comportamiento de la interfaz web del Sprint 0.
Copyright: material académico y modificaciones del autor.
------------------------------------------------------------
*/


let momentoUltimaActualizacion =
    null;


// ------------------------------------------------------------
// mostrarMedicion(medicion)
//
// Muestra en la interfaz los datos de una medición recibida
// desde la lógica fake.
// ------------------------------------------------------------
function mostrarMedicion(
    medicion
) {

    if (medicion === null) {

        document.getElementById(
            "tipo"
        ).textContent = "-";


        document.getElementById(
            "valor"
        ).textContent = "-";


        document.getElementById(
            "fecha"
        ).textContent = "-";


        document.getElementById(
            "hora"
        ).textContent = "-";


        return;
    }


    document.getElementById(
        "tipo"
    ).textContent =
        medicion.tipo;


    document.getElementById(
        "valor"
    ).textContent =
        medicion.valor;


    const fechaHora =
        medicion.fecha.split(
            " "
        );


    document.getElementById(
        "fecha"
    ).textContent =
        fechaHora[0] ?? "-";


    document.getElementById(
        "hora"
    ).textContent =
        fechaHora[1] ?? "-";
}


// ------------------------------------------------------------
// actualizarTextoUltimaActualizacion()
//
// Calcula cuánto tiempo ha pasado desde la última actualización
// correcta de la interfaz.
// ------------------------------------------------------------
function actualizarTextoUltimaActualizacion() {

    if (
        momentoUltimaActualizacion === null
    ) {

        return;
    }


    const ahora =
        new Date();


    const diferenciaMilisegundos =
        ahora
        - momentoUltimaActualizacion;


    const minutos =
        Math.floor(
            diferenciaMilisegundos
            / 60000
        );


    const hora =
        momentoUltimaActualizacion
            .toLocaleTimeString(
                "es-ES",
                {
                    hour: "2-digit",
                    minute: "2-digit",
                    second: "2-digit"
                }
            );


    let texto;


    if (minutos < 1) {

        texto =
            "Actualizado ahora · "
            + hora;

    } else if (minutos === 1) {

        texto =
            "Actualizado hace 1 minuto · "
            + hora;

    } else {

        texto =
            "Actualizado hace "
            + minutos
            + " minutos · "
            + hora;
    }


    document.getElementById(
        "ultimaActualizacion"
    ).textContent =
        texto;
}


// ------------------------------------------------------------
// cargarMedicion()
//
// Solicita la última medición a la lógica fake y actualiza
// los datos mostrados en la interfaz.
// ------------------------------------------------------------
async function cargarMedicion() {

    const estado =
        document.getElementById(
            "estado"
        );


    const boton =
        document.getElementById(
            "botonActualizar"
        );


    estado.textContent =
        "Actualizando...";


    boton.disabled =
        true;


    try {

        const medicion =
            await logicaFake
                .leerMedicion();


        mostrarMedicion(
            medicion
        );


        if (medicion === null) {

            estado.textContent =
                "No existen mediciones almacenadas.";

            return;
        }


        momentoUltimaActualizacion =
            new Date();


        actualizarTextoUltimaActualizacion();


        estado.textContent =
            "";


    } catch (error) {

        console.error(
            error
        );


        estado.textContent =
            "No se pudo recuperar la medición.";


    } finally {

        boton.disabled =
            false;
    }
}


// ------------------------------------------------------------
// inicializarInterfaz()
//
// Configura los eventos de la interfaz y realiza la primera
// lectura de la última medición.
// ------------------------------------------------------------
function inicializarInterfaz() {

    const botonActualizar =
        document.getElementById(
            "botonActualizar"
        );


    botonActualizar.addEventListener(
        "click",
        cargarMedicion
    );


    cargarMedicion();


    /*
     * Actualiza cada minuto únicamente el texto que indica
     * cuánto tiempo ha pasado desde la última actualización.
     *
     * No realiza nuevas peticiones al servidor.
     */
    setInterval(
        actualizarTextoUltimaActualizacion,
        60000
    );
}


document.addEventListener(
    "DOMContentLoaded",
    inicializarInterfaz
);