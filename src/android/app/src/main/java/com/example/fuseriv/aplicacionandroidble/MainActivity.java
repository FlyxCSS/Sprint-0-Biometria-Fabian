package com.example.fuseriv.aplicacionandroidble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Collections;
import java.util.List;


// --------------------------------------------------------------
// Fichero: MainActivity.java
// Descripción: Escaneo BLE y procesamiento de tramas iBeacon.
// Fecha: 2026-10-13
// Autor: Fabián Useche
// Base del código: Jordi Bataller i Mascarell
// Aportación: adaptación para recibir mediciones de la SparkFun
//             y enviarlas mediante la lógica fake.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class MainActivity extends AppCompatActivity {


    private static final String ETIQUETA_LOG =
            "FABI_BLE";

    private static final int CODIGO_PETICION_PERMISOS =
            11223344;

    private static final String NOMBRE_BEACON =
            "Fabian_GTI";

    private static final boolean ENVIAR_VALOR_EN_PPM =
            true;


    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner escanerBLE;

    private ScanCallback callbackEscaneo;


    private final LogicaFake logicaFake =
            new LogicaFake();


    private int ultimoContadorEnviado =
            -1;


    // ------------------------------------------------------------
    // tengoPermisosBluetooth() --> B
    //
    // Comprueba si están concedidos los permisos necesarios.
    // ------------------------------------------------------------
    private boolean tengoPermisosBluetooth() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            return
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED

                            &&

                            ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED

                            &&

                            ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED;
        }


        return
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

    }


    // ------------------------------------------------------------
    // pedirPermisosBluetooth()
    //
    // Solicita los permisos necesarios para BLE.
    // ------------------------------------------------------------
    private void pedirPermisosBluetooth() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS
            );

        } else {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS
            );
        }

    }


    // ------------------------------------------------------------
    // inicializarBluetooth()
    //
    // Prepara el adaptador y el escáner BLE.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void inicializarBluetooth() {

        if (!tengoPermisosBluetooth()) {

            pedirPermisosBluetooth();

            return;
        }


        bluetoothAdapter =
                BluetoothAdapter.getDefaultAdapter();


        if (
                bluetoothAdapter == null
                        ||
                        !bluetoothAdapter.isEnabled()
        ) {

            Log.d(
                    ETIQUETA_LOG,
                    "[BLE][ERROR] Bluetooth no disponible"
            );

            return;
        }


        escanerBLE =
                bluetoothAdapter.getBluetoothLeScanner();


        Log.d(
                ETIQUETA_LOG,
                "[BLE] Bluetooth preparado"
        );

    }


    // ------------------------------------------------------------
    // escanerPreparado() --> B
    //
    // Comprueba que se puede iniciar un escaneo BLE.
    // ------------------------------------------------------------
    private boolean escanerPreparado() {

        if (!tengoPermisosBluetooth()) {

            pedirPermisosBluetooth();

            return false;
        }


        if (escanerBLE == null) {

            inicializarBluetooth();
        }


        return escanerBLE != null;

    }


    // ------------------------------------------------------------
    // buscarTodosLosDispositivosBTLE()
    //
    // Inicia un escaneo BLE sin filtros.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarTodosLosDispositivosBTLE() {

        if (!escanerPreparado()) {
            return;
        }


        detenerBusquedaDispositivosBTLE();


        callbackEscaneo =
                new ScanCallback() {

                    // ------------------------------------------------------------
                    // resultado: ResultadoBLE --> onScanResult()
                    //
                    // Procesa un resultado del escaneo general.
                    // ------------------------------------------------------------
                    @Override
                    public void onScanResult(
                            int callbackType,
                            ScanResult resultado
                    ) {

                        mostrarDispositivo(
                                resultado
                        );
                    }


                    // ------------------------------------------------------------
                    // errorCode: N --> onScanFailed()
                    //
                    // Registra un error producido durante el escaneo.
                    // ------------------------------------------------------------
                    @Override
                    public void onScanFailed(
                            int errorCode
                    ) {

                        Log.d(
                                ETIQUETA_LOG,
                                "[SCAN][ERROR] Codigo="
                                        + errorCode
                        );
                    }
                };


        Log.d(
                ETIQUETA_LOG,
                "[SCAN] Buscando todos los dispositivos BLE"
        );


        escanerBLE.startScan(
                callbackEscaneo
        );

    }


    // ------------------------------------------------------------
    // resultado: ResultadoBLE --> mostrarDispositivo()
    //
    // Muestra un dispositivo encontrado durante el escaneo general.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void mostrarDispositivo(
            ScanResult resultado
    ) {

        if (
                resultado == null
                        ||
                        resultado.getDevice() == null
        ) {

            return;
        }


        BluetoothDevice dispositivo =
                resultado.getDevice();


        String nombre =
                obtenerNombreDispositivo(
                        resultado
                );


        Log.d(
                ETIQUETA_LOG,
                "[SCAN] Nombre="
                        + nombre
                        + " | MAC="
                        + dispositivo.getAddress()
                        + " | RSSI="
                        + resultado.getRssi()
        );

    }


    // ------------------------------------------------------------
    // resultado: ResultadoBLE --> obtenerNombreDispositivo() --> Text
    //
    // Obtiene el nombre real anunciado por el dispositivo BLE.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private String obtenerNombreDispositivo(
            ScanResult resultado
    ) {

        if (
                resultado.getScanRecord() != null
                        &&
                        resultado.getScanRecord().getDeviceName() != null
        ) {

            return
                    resultado
                            .getScanRecord()
                            .getDeviceName();
        }


        if (resultado.getDevice() != null) {

            return
                    resultado
                            .getDevice()
                            .getName();
        }


        return null;

    }


    // ------------------------------------------------------------
    // buscarNuestroDispositivoBTLE()
    //
    // Busca únicamente el beacon definido en NOMBRE_BEACON.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarNuestroDispositivoBTLE() {

        if (!escanerPreparado()) {
            return;
        }


        detenerBusquedaDispositivosBTLE();


        callbackEscaneo =
                new ScanCallback() {

                    // ------------------------------------------------------------
                    // resultado: ResultadoBLE --> onScanResult()
                    //
                    // Procesa un resultado del escaneo filtrado.
                    // ------------------------------------------------------------
                    @Override
                    public void onScanResult(
                            int callbackType,
                            ScanResult resultado
                    ) {

                        procesarResultadoBLE(
                                resultado
                        );
                    }


                    // ------------------------------------------------------------
                    // resultados: [ResultadoBLE] --> onBatchScanResults()
                    //
                    // Procesa un conjunto de resultados BLE recibidos.
                    // ------------------------------------------------------------
                    @Override
                    public void onBatchScanResults(
                            List<ScanResult> resultados
                    ) {

                        for (ScanResult resultado : resultados) {

                            procesarResultadoBLE(
                                    resultado
                            );
                        }
                    }


                    // ------------------------------------------------------------
                    // errorCode: N --> onScanFailed()
                    //
                    // Registra un error producido durante el escaneo.
                    // ------------------------------------------------------------
                    @Override
                    public void onScanFailed(
                            int errorCode
                    ) {

                        Log.d(
                                ETIQUETA_LOG,
                                "[SCAN][ERROR] Codigo="
                                        + errorCode
                        );
                    }
                };


        ScanFilter filtro =
                new ScanFilter.Builder()
                        .setDeviceName(
                                NOMBRE_BEACON
                        )
                        .build();


        ScanSettings settings =
                new ScanSettings.Builder()
                        .setScanMode(
                                ScanSettings.SCAN_MODE_LOW_LATENCY
                        )
                        .build();


        Log.d(
                ETIQUETA_LOG,
                "[SCAN] Buscando "
                        + NOMBRE_BEACON
        );


        escanerBLE.startScan(
                Collections.singletonList(
                        filtro
                ),
                settings,
                callbackEscaneo
        );

    }


    // ------------------------------------------------------------
    // tipo: N --> obtenerNombreTipoMedicion() --> Text
    //
    // Traduce el identificador del tipo de medición.
    // ------------------------------------------------------------
    private String obtenerNombreTipoMedicion(
            int tipo
    ) {

        if (tipo == 11) {
            return "O3";
        }

        if (tipo == 12) {
            return "TEMPERATURA";
        }

        return null;

    }


    // ------------------------------------------------------------
    // minor: N --> obtenerValorServidor() --> R
    //
    // Convierte el valor a ppm si la configuración lo indica.
    // ------------------------------------------------------------
    private double obtenerValorServidor(
            int minor
    ) {

        if (ENVIAR_VALOR_EN_PPM) {

            return
                    Utilidades.ppbAPpm(
                            minor
                    );
        }


        return minor;

    }


    // ------------------------------------------------------------
    // resultado: ResultadoBLE --> procesarResultadoBLE()
    //
    // Interpreta una trama iBeacon y procesa la medición.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void procesarResultadoBLE(
            ScanResult resultado
    ) {

        if (
                resultado == null
                        ||
                        resultado.getScanRecord() == null
        ) {

            return;
        }


        String nombreMicro =
                obtenerNombreDispositivo(
                        resultado
                );


        if (!NOMBRE_BEACON.equals(nombreMicro)) {

            return;
        }


        byte[] bytes =
                resultado
                        .getScanRecord()
                        .getBytes();


        if (
                bytes == null
                        ||
                        bytes.length < 30
        ) {

            return;
        }


        try {

            TramaIBeacon trama =
                    new TramaIBeacon(
                            bytes
                    );


            int major =
                    Utilidades.bytesToIntOK(
                            trama.getMajor()
                    );


            int minor =
                    Utilidades.bytesToIntOK(
                            trama.getMinor()
                    );


            int tipoId =
                    (major >> 8)
                            & 0xFF;


            int contador =
                    major
                            & 0xFF;


            String tipo =
                    obtenerNombreTipoMedicion(
                            tipoId
                    );


            if (tipo == null) {

                Log.d(
                        ETIQUETA_LOG,
                        "[IBEACON][ERROR] Tipo desconocido="
                                + tipoId
                );

                return;
            }


            double valor =
                    obtenerValorServidor(
                            minor
                    );


            mostrarIBeacon(
                    nombreMicro,
                    trama,
                    major,
                    minor,
                    tipo,
                    tipoId,
                    contador,
                    valor
            );


            if (
                    contador
                            == ultimoContadorEnviado
            ) {

                Log.d(
                        ETIQUETA_LOG,
                        "[MEDICION] Repetida -> no se envia"
                );

                return;
            }


            ultimoContadorEnviado =
                    contador;


            enviarMedicion(
                    nombreMicro,
                    tipo,
                    valor
            );


        } catch (IllegalArgumentException e) {

            Log.d(
                    ETIQUETA_LOG,
                    "[IBEACON][ERROR] "
                            + e.getMessage()
            );
        }

    }


    // ------------------------------------------------------------
    // nombreMicro: Text, trama: TramaIBeaconDatos, major: N, minor: N,
    // tipo: Text, tipoId: N, contador: N, valor: R --> mostrarIBeacon()
    //
    // Muestra en Logcat los datos principales de la trama.
    // ------------------------------------------------------------
    private void mostrarIBeacon(
            String nombreMicro,
            TramaIBeacon trama,
            int major,
            int minor,
            String tipo,
            int tipoId,
            int contador,
            double valor
    ) {

        String unidad =
                ENVIAR_VALOR_EN_PPM
                        ? "ppm"
                        : "ppb";


        Log.d(
                ETIQUETA_LOG,
                "[IBEACON] Micro="
                        + nombreMicro
        );


        Log.d(
                ETIQUETA_LOG,
                "[IBEACON] UUID="
                        + Utilidades.bytesToString(
                        trama.getUUID()
                )
        );


        Log.d(
                ETIQUETA_LOG,
                "[IBEACON] Major="
                        + major
                        + " | Tipo="
                        + tipo
                        + "("
                        + tipoId
                        + ")"
                        + " | Contador="
                        + contador
        );


        Log.d(
                ETIQUETA_LOG,
                "[IBEACON] Minor="
                        + minor
                        + " ppb"
        );


        Log.d(
                ETIQUETA_LOG,
                "[IBEACON] Valor servidor="
                        + valor
                        + " "
                        + unidad
                        + " | TxPower="
                        + trama.getTxPower()
        );

    }


    // ------------------------------------------------------------
    // nombreMicro: Text, tipo: Text, valor: R --> enviarMedicion()
    //
    // Envía una nueva medición mediante la lógica fake utilizando
    // el nombre real obtenido del anuncio BLE.
    // ------------------------------------------------------------
    private void enviarMedicion(
            String nombreMicro,
            String tipo,
            double valor
    ) {

        Log.d(
                ETIQUETA_LOG,
                "[MEDICION] Nueva"
                        + " | Micro="
                        + nombreMicro
                        + " -> se envia al servidor"
        );


        logicaFake.guardarMedicion(
                nombreMicro,
                tipo,
                valor,

                new LogicaFake.RespuestaGuardarMedicion() {

                    // ------------------------------------------------------------
                    // medicion: Medicion --> alGuardar()
                    //
                    // Procesa la confirmación de almacenamiento.
                    // ------------------------------------------------------------
                    @Override
                    public void alGuardar(
                            Medicion medicion
                    ) {

                        Log.d(
                                ETIQUETA_LOG,
                                "[SERVIDOR] Guardada"
                                        + " | Micro="
                                        + medicion.getNombreMicro()
                                        + " | FechaHora="
                                        + medicion.getFechaHora()
                                        + " | Tipo="
                                        + medicion.getTipo()
                                        + " | Valor="
                                        + medicion.getValor()
                        );
                    }


                    // ------------------------------------------------------------
                    // error: Text --> alFallar()
                    //
                    // Registra un error al almacenar la medición.
                    // ------------------------------------------------------------
                    @Override
                    public void alFallar(
                            String error
                    ) {

                        Log.d(
                                ETIQUETA_LOG,
                                "[SERVIDOR][ERROR] "
                                        + error
                        );
                    }
                }
        );

    }


    // ------------------------------------------------------------
    // detenerBusquedaDispositivosBTLE()
    //
    // Detiene el escaneo BLE actual.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void detenerBusquedaDispositivosBTLE() {

        if (
                escanerBLE == null
                        ||
                        callbackEscaneo == null
                        ||
                        !tengoPermisosBluetooth()
        ) {

            return;
        }


        escanerBLE.stopScan(
                callbackEscaneo
        );


        callbackEscaneo =
                null;


        Log.d(
                ETIQUETA_LOG,
                "[SCAN] Escaneo detenido"
        );

    }


    // ------------------------------------------------------------
    // vista: Vista --> botonBuscarTodosLosDispositivosBTLEPulsado()
    // ------------------------------------------------------------
    public void botonBuscarTodosLosDispositivosBTLEPulsado(
            View v
    ) {

        buscarTodosLosDispositivosBTLE();

    }


    // ------------------------------------------------------------
    // vista: Vista --> botonBuscarNuestroDispositivoBTLEPulsado()
    // ------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(
            View v
    ) {

        buscarNuestroDispositivoBTLE();

    }


    // ------------------------------------------------------------
    // vista: Vista --> botonDetenerBusquedaDispositivosBTLEPulsado()
    // ------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(
            View v
    ) {

        detenerBusquedaDispositivosBTLE();

    }


    // ------------------------------------------------------------
    // estado: EstadoActividad | Nulo --> onCreate()
    //
    // Inicializa la aplicación.
    // ------------------------------------------------------------
    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );


        setContentView(
                R.layout.activity_main
        );


        Log.d(
                ETIQUETA_LOG,
                "[APP] Aplicacion iniciada"
        );


        inicializarBluetooth();

    }


    // ------------------------------------------------------------
    // codigo: N, permisos: [Text], resultados: [Z] --> onRequestPermissionsResult()
    //
    // Procesa el resultado de los permisos solicitados.
    // ------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );


        if (
                requestCode
                        != CODIGO_PETICION_PERMISOS
        ) {

            return;
        }


        for (int resultado : grantResults) {

            if (
                    resultado
                            != PackageManager.PERMISSION_GRANTED
            ) {

                Log.d(
                        ETIQUETA_LOG,
                        "[PERMISOS][ERROR] No concedidos"
                );

                return;
            }
        }


        Log.d(
                ETIQUETA_LOG,
                "[PERMISOS] Concedidos"
        );


        inicializarBluetooth();

    }


    // ------------------------------------------------------------
    // onDestroy() -->
    //
    // Detiene el escaneo antes de cerrar la actividad.
    // ------------------------------------------------------------
    @Override
    protected void onDestroy() {

        detenerBusquedaDispositivosBTLE();

        super.onDestroy();

    }

} // class MainActivity
