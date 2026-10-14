/* --------------------------------------------------------------
Fichero: AppTest.js
Descripción: Pruebas automáticas de la presentación web.
Fecha: 2026-10-14
Autor: Fabián Useche
Aportación: cobertura de las funciones clave de la GUI.
Copyright: material académico y modificaciones del autor.
-------------------------------------------------------------- */

// ------------------------------------------------------------
// condicion: B, descripcion: Text --> comprobar()
// Registra el resultado y rechaza cualquier condición falsa.
// ------------------------------------------------------------
function comprobar(condicion, descripcion) {
    if (!condicion) throw new Error("FALLO: " + descripcion);
    return "OK: " + descripcion + "\n";
}

// ------------------------------------------------------------
// ejecutarTests() -->
// Ejecuta los casos de presentación con un proxy simulado.
// ------------------------------------------------------------
async function ejecutarTests() {
    const salida = document.getElementById("resultado");
    let texto = "";
    try {
        mostrarMedicion(null);
        texto += comprobar(document.getElementById("nombreMicro").textContent === "-", "sin datos");
        const ejemplo = {nombre_micro:"Fabian_GTI",tipo:"O3",valor:1.234,fecha_hora:"2026-10-14 12:00:00"};
        mostrarMedicion(ejemplo);
        texto += comprobar(document.getElementById("nombreMicro").textContent === "Fabian_GTI", "muestra micro");
        texto += comprobar(document.getElementById("tipo").textContent === "O3", "muestra tipo");
        texto += comprobar(document.getElementById("valor").textContent === "1.234", "muestra valor");
        texto += comprobar(document.getElementById("fecha").textContent === "2026-10-14", "separa fecha");
        texto += comprobar(document.getElementById("hora").textContent === "12:00:00", "separa hora");
        let llamadas = 0;
        logicaFake.leerMedicion = async () => { llamadas++; return ejemplo; };
        await cargarMedicion();
        texto += comprobar(llamadas === 1, "consulta mediante proxy");
        texto += comprobar(document.getElementById("botonActualizar").disabled === false, "rehabilita botón");
        texto += comprobar(document.getElementById("ultimaActualizacion").textContent.includes("Actualizado"), "última actualización");
        logicaFake.leerMedicion = async () => null;
        await cargarMedicion();
        texto += comprobar(document.getElementById("estado").textContent.includes("No existen"), "sin mediciones");
        logicaFake.leerMedicion = async () => { throw new Error("simulado"); };
        const errorOriginal = console.error;
        console.error = () => {};
        try { await cargarMedicion(); } finally { console.error = errorOriginal; }
        texto += comprobar(document.getElementById("estado").textContent.includes("No se pudo"), "error de consulta");
        texto += "\nTODOS LOS TESTS HAN PASADO";
    } catch (error) { texto += "\n" + error.message; }
    salida.textContent = texto;
}

window.addEventListener("load", ejecutarTests);
