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

import java.util.ArrayList;
import java.util.List;


// --------------------------------------------------------------
// Fichero: MainActivity.java
// Descripción: Escaneo BLE y procesamiento de tramas iBeacon.
// Fecha: 2026-10-03
// Autor: Fabián Useche
// Base del código: Jordi Bataller i Mascarell
// Aportación: adaptación para recibir mediciones de la SparkFun
//             y enviarlas mediante la lógica fake.
// Copyright: material académico y modificaciones del autor.
// --------------------------------------------------------------

public class MainActivity extends AppCompatActivity {


    // ----------------------------------------------------------
    // CONFIGURACIÓN
    // ----------------------------------------------------------

    private static final String ETIQUETA_LOG =
            ">>>>";


    private static final int CODIGO_PETICION_PERMISOS =
            11223344;


    /*
     * Nombre BLE de nuestra SparkFun.
     *
     * Si durante la demostración se cambia el nombre del beacon,
     * solo es necesario modificar esta constante.
     */
    private static final String NOMBRE_BEACON =
            "Fabian_GTI";


    /*
     * true:
     *
     * Minor = 1234 ppb
     * se convierte a:
     * 1.234 ppm
     *
     * y se envía 1.234 al servidor.
     *
     *
     * false:
     *
     * Minor = 1234
     * se envía directamente como 1234.
     *
     * Para cambiar entre ppm y ppb durante una demostración
     * solamente hay que cambiar esta constante.
     */
    private static final boolean ENVIAR_VALOR_EN_PPM =
            true;


    // ----------------------------------------------------------
    // BLUETOOTH
    // ----------------------------------------------------------

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner escanerBLE;

    private ScanCallback callbackEscaneo;


    // ----------------------------------------------------------
    // LÓGICA FAKE
    // ----------------------------------------------------------

    private final LogicaFake logicaFake =
            new LogicaFake();


    // ----------------------------------------------------------
    // CONTROL DE MEDICIONES
    // ----------------------------------------------------------

    /*
     * Guarda el último contador enviado al servidor.
     *
     * La SparkFun anuncia varias veces una misma medición.
     * Solo se envía al servidor cuando cambia el contador.
     */
    private int ultimoContadorEnviado =
            -1;


    // ------------------------------------------------------------
    // tengoPermisosBluetooth() --> B
    //
    // Comprueba si la aplicación dispone de los permisos
    // necesarios para realizar el escaneo BLE.
    // ------------------------------------------------------------
    private boolean tengoPermisosBluetooth() {

        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
        ) {

            return
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_SCAN
                    )
                            == PackageManager.PERMISSION_GRANTED

                            &&

                            ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.BLUETOOTH_CONNECT
                            )
                                    == PackageManager.PERMISSION_GRANTED

                            &&

                            ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            )
                                    == PackageManager.PERMISSION_GRANTED;
        }


        return
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                        == PackageManager.PERMISSION_GRANTED;

    } // tengoPermisosBluetooth()


    // ------------------------------------------------------------
    // pedirPermisosBluetooth()
    //
    // Solicita los permisos necesarios para realizar
    // búsquedas Bluetooth Low Energy.
    // ------------------------------------------------------------
    private void pedirPermisosBluetooth() {

        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
        ) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS
            );

        } else {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS
            );
        }

    } // pedirPermisosBluetooth()


    // ------------------------------------------------------------
    // inicializarBluetooth()
    //
    // Inicializa el adaptador Bluetooth y obtiene el escáner BLE.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void inicializarBluetooth() {

        Log.d(
                ETIQUETA_LOG,
                "Inicializando Bluetooth"
        );


        if (!tengoPermisosBluetooth()) {

            Log.d(
                    ETIQUETA_LOG,
                    "Faltan permisos Bluetooth"
            );


            pedirPermisosBluetooth();

            return;
        }


        bluetoothAdapter =
                BluetoothAdapter.getDefaultAdapter();


        if (bluetoothAdapter == null) {

            Log.d(
                    ETIQUETA_LOG,
                    "ERROR: dispositivo sin Bluetooth"
            );

            return;
        }


        if (!bluetoothAdapter.isEnabled()) {

            Log.d(
                    ETIQUETA_LOG,
                    "Bluetooth desactivado"
            );

            return;
        }


        escanerBLE =
                bluetoothAdapter.getBluetoothLeScanner();


        if (escanerBLE == null) {

            Log.d(
                    ETIQUETA_LOG,
                    "ERROR obteniendo escaner BLE"
            );

            return;
        }


        Log.d(
                ETIQUETA_LOG,
                "Bluetooth preparado correctamente"
        );

    } // inicializarBluetooth()


    // ------------------------------------------------------------
    // escanerPreparado() --> B
    //
    // Comprueba que existe un escáner BLE disponible.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private boolean escanerPreparado() {

        if (!tengoPermisosBluetooth()) {

            pedirPermisosBluetooth();

            return false;
        }


        if (
                bluetoothAdapter == null
                        ||
                        escanerBLE == null
        ) {

            inicializarBluetooth();
        }


        return escanerBLE != null;

    } // escanerPreparado()


    // ------------------------------------------------------------
    // buscarTodosLosDispositivosBTLE()
    //
    // Inicia un escaneo BLE sin filtros.
    // Se utiliza para comprobar qué dispositivos existen cerca
    // y verificar inicialmente que Fabian_GTI puede encontrarse.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarTodosLosDispositivosBTLE() {

        if (!escanerPreparado()) {
            return;
        }


        detenerBusquedaDispositivosBTLE();


        callbackEscaneo =
                new ScanCallback() {

                    @Override
                    public void onScanResult(
                            int callbackType,
                            ScanResult resultado
                    ) {

                        super.onScanResult(
                                callbackType,
                                resultado
                        );


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
                                null;


                        if (
                                resultado.getScanRecord()
                                        != null
                        ) {

                            nombre =
                                    resultado
                                            .getScanRecord()
                                            .getDeviceName();
                        }


                        if (nombre == null) {

                            nombre =
                                    dispositivo.getName();
                        }


                        Log.d(
                                ETIQUETA_LOG,
                                "Dispositivo BLE"
                        );

                        Log.d(
                                ETIQUETA_LOG,
                                "Nombre: "
                                        + nombre
                        );

                        Log.d(
                                ETIQUETA_LOG,
                                "Direccion: "
                                        + dispositivo.getAddress()
                        );

                        Log.d(
                                ETIQUETA_LOG,
                                "RSSI: "
                                        + resultado.getRssi()
                        );
                    }


                    @Override
                    public void onScanFailed(
                            int errorCode
                    ) {

                        super.onScanFailed(
                                errorCode
                        );


                        Log.d(
                                ETIQUETA_LOG,
                                "ERROR escaneando. Codigo: "
                                        + errorCode
                        );
                    }
                };


        ScanSettings settings =
                new ScanSettings.Builder()
                        .setScanMode(
                                ScanSettings.SCAN_MODE_LOW_LATENCY
                        )
                        .build();


        Log.d(
                ETIQUETA_LOG,
                "Buscando todos los dispositivos BLE"
        );


        escanerBLE.startScan(
                null,
                settings,
                callbackEscaneo
        );

    } // buscarTodosLosDispositivosBTLE()


    // ------------------------------------------------------------
    // buscarNuestroDispositivoBTLE()
    //
    // Inicia un escaneo BLE filtrando únicamente por el nombre
    // definido en NOMBRE_BEACON.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarNuestroDispositivoBTLE() {

        if (!escanerPreparado()) {
            return;
        }


        detenerBusquedaDispositivosBTLE();


        callbackEscaneo =
                new ScanCallback() {

                    @Override
                    public void onScanResult(
                            int callbackType,
                            ScanResult resultado
                    ) {

                        super.onScanResult(
                                callbackType,
                                resultado
                        );


                        procesarResultadoBLE(
                                resultado
                        );
                    }


                    @Override
                    public void onBatchScanResults(
                            List<ScanResult> resultados
                    ) {

                        super.onBatchScanResults(
                                resultados
                        );


                        for (
                                ScanResult resultado
                                : resultados
                        ) {

                            procesarResultadoBLE(
                                    resultado
                            );
                        }
                    }


                    @Override
                    public void onScanFailed(
                            int errorCode
                    ) {

                        super.onScanFailed(
                                errorCode
                        );


                        Log.d(
                                ETIQUETA_LOG,
                                "ERROR escaneando. Codigo: "
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


        List<ScanFilter> filtros =
                new ArrayList<>();


        filtros.add(
                filtro
        );


        ScanSettings settings =
                new ScanSettings.Builder()
                        .setScanMode(
                                ScanSettings.SCAN_MODE_LOW_LATENCY
                        )
                        .build();


        Log.d(
                ETIQUETA_LOG,
                "Buscando "
                        + NOMBRE_BEACON
        );


        escanerBLE.startScan(
                filtros,
                settings,
                callbackEscaneo
        );

    } // buscarNuestroDispositivoBTLE()


    // ------------------------------------------------------------
    // tipo: N --> obtenerNombreTipoMedicion() --> Text
    //
    // Traduce el identificador recibido en Major al nombre
    // correspondiente del tipo de medición.
    // ------------------------------------------------------------
    private String obtenerNombreTipoMedicion(
            int tipo
    ) {

        switch (tipo) {

            case 11:
                return "O3";

            case 12:
                return "TEMPERATURA";

            default:
                return null;
        }

    } // obtenerNombreTipoMedicion()


    // ------------------------------------------------------------
    // resultado: ScanResult --> procesarResultadoBLE()
    //
    // Interpreta una trama iBeacon recibida desde la SparkFun,
    // extrae Major y Minor y envía las nuevas mediciones
    // mediante la lógica fake.
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


        byte[] bytes =
                resultado
                        .getScanRecord()
                        .getBytes();


        if (
                bytes == null
                        ||
                        bytes.length < 30
        ) {

            Log.d(
                    ETIQUETA_LOG,
                    "Trama BLE demasiado corta"
            );

            return;
        }


        String nombre =
                resultado
                        .getScanRecord()
                        .getDeviceName();


        if (
                nombre == null
                        &&
                        resultado.getDevice() != null
        ) {

            nombre =
                    resultado
                            .getDevice()
                            .getName();
        }


        if (!NOMBRE_BEACON.equals(nombre)) {

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


            /*
             * Major está dividido en:
             *
             * byte alto -> tipo
             * byte bajo -> contador
             */
            int tipoMedicion =
                    (major >> 8)
                            & 0xFF;


            int contador =
                    major
                            & 0xFF;


            String tipo =
                    obtenerNombreTipoMedicion(
                            tipoMedicion
                    );


            if (tipo == null) {

                Log.d(
                        ETIQUETA_LOG,
                        "Tipo desconocido: "
                                + tipoMedicion
                );

                return;
            }


            /*
             * Minor llega desde la SparkFun en ppb.
             *
             * Si ENVIAR_VALOR_EN_PPM = true:
             *      1234 -> 1.234
             *
             * Si ENVIAR_VALOR_EN_PPM = false:
             *      1234 -> 1234
             */
            double valorServidor;


            if (ENVIAR_VALOR_EN_PPM) {

                valorServidor =
                        Utilidades.ppbAPpm(
                                minor
                        );

            } else {

                valorServidor =
                        minor;
            }


            String unidad =
                    ENVIAR_VALOR_EN_PPM
                            ? "ppm"
                            : "ppb";


            Log.d(
                    ETIQUETA_LOG,
                    "=================================="
            );


            Log.d(
                    ETIQUETA_LOG,
                    "DISPOSITIVO DETECTADO"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "Nombre: "
                            + nombre
            );


            Log.d(
                    ETIQUETA_LOG,
                    "UUID: "
                            + Utilidades.bytesToString(
                            trama.getUUID()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "MAJOR = "
                            + major
            );


            Log.d(
                    ETIQUETA_LOG,
                    "TIPO = "
                            + tipo
                            + " ("
                            + tipoMedicion
                            + ")"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "CONTADOR = "
                            + contador
            );


            Log.d(
                    ETIQUETA_LOG,
                    "MINOR = "
                            + minor
            );


            Log.d(
                    ETIQUETA_LOG,
                    "VALOR = "
                            + valorServidor
                            + " "
                            + unidad
            );


            Log.d(
                    ETIQUETA_LOG,
                    "TX POWER = "
                            + trama.getTxPower()
            );


            /*
             * La SparkFun emite varias veces la misma medición.
             *
             * Solo se envía al servidor cuando cambia
             * el contador de Major.
             */
            if (
                    contador
                            != ultimoContadorEnviado
            ) {

                ultimoContadorEnviado =
                        contador;


                Log.d(
                        ETIQUETA_LOG,
                        "Nueva medicion"
                );


                logicaFake.guardarMedicion(
                        tipo,
                        valorServidor
                );

            } else {

                Log.d(
                        ETIQUETA_LOG,
                        "Medicion repetida. No se envia."
                );
            }


            Log.d(
                    ETIQUETA_LOG,
                    "=================================="
            );


        } catch (IllegalArgumentException e) {

            Log.d(
                    ETIQUETA_LOG,
                    "ERROR interpretando iBeacon: "
                            + e.getMessage()
            );
        }

    } // procesarResultadoBLE()


    // ------------------------------------------------------------
    // detenerBusquedaDispositivosBTLE()
    //
    // Detiene el escaneo Bluetooth Low Energy actual.
    // ------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void detenerBusquedaDispositivosBTLE() {

        if (
                callbackEscaneo == null
                        ||
                        escanerBLE == null
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
                "Escaneo detenido"
        );

    } // detenerBusquedaDispositivosBTLE()


    // ------------------------------------------------------------
    // v: View --> botonBuscarTodosLosDispositivosBTLEPulsado()
    //
    // Inicia un escaneo BLE sin filtros.
    // ------------------------------------------------------------
    public void botonBuscarTodosLosDispositivosBTLEPulsado(
            View v
    ) {

        buscarTodosLosDispositivosBTLE();

    } // botonBuscarTodosLosDispositivosBTLEPulsado()


    // ------------------------------------------------------------
    // v: View --> botonBuscarNuestroDispositivoBTLEPulsado()
    //
    // Inicia la búsqueda del beacon indicado en NOMBRE_BEACON.
    // ------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(
            View v
    ) {

        buscarNuestroDispositivoBTLE();

    } // botonBuscarNuestroDispositivoBTLEPulsado()


    // ------------------------------------------------------------
    // v: View --> botonDetenerBusquedaDispositivosBTLEPulsado()
    //
    // Detiene el escaneo BLE actual.
    // ------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(
            View v
    ) {

        detenerBusquedaDispositivosBTLE();

    } // botonDetenerBusquedaDispositivosBTLEPulsado()


    // ------------------------------------------------------------
    // savedInstanceState: Bundle --> onCreate()
    //
    // Inicializa la actividad y prepara Bluetooth.
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
                "Aplicacion iniciada"
        );


        inicializarBluetooth();

    } // onCreate()


    // ------------------------------------------------------------
    // requestCode: N, permissions, grantResults
    //        --> onRequestPermissionsResult()
    //
    // Procesa el resultado de la solicitud de permisos Bluetooth.
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


        boolean concedidos =
                grantResults.length > 0;


        for (
                int resultado
                : grantResults
        ) {

            if (
                    resultado
                            != PackageManager.PERMISSION_GRANTED
            ) {

                concedidos =
                        false;

                break;
            }
        }


        if (concedidos) {

            Log.d(
                    ETIQUETA_LOG,
                    "Permisos concedidos"
            );


            inicializarBluetooth();

        } else {

            Log.d(
                    ETIQUETA_LOG,
                    "Permisos no concedidos"
            );
        }

    } // onRequestPermissionsResult()


    // ------------------------------------------------------------
    // onDestroy()
    //
    // Detiene el escaneo antes de destruir la actividad.
    // ------------------------------------------------------------
    @Override
    protected void onDestroy() {

        detenerBusquedaDispositivosBTLE();

        super.onDestroy();

    } // onDestroy()

} // class MainActivity