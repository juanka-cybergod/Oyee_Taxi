package com.cybergod.oyeetaxi.utils


object Constants {

    //API
    //Telefono 192.168.137.1
    //EMulador
    fun getHttpProtocol(useSSL: Boolean): String {
        return when {
            useSSL -> {
                "https"
            }
            else -> {
                "http"
            }
        }
    }

    fun getPort(useSSL: Boolean): Int {
        return when {
            useSSL -> {
                URL_PORT_SSL
            }
            else -> {
                URL_PORT
            }
        }
    }

    val URL_USE_SSL: Boolean = false
    val URL_IP = "192.168.0.100"
    val URL_PORT_SSL = 8443
    val URL_PORT = 80
    val URL_BASE = "${getHttpProtocol(URL_USE_SSL)}://$URL_IP:${getPort(URL_USE_SSL)}/"
    const val URL_BASE_USUARIOS = "usuarios/"
    const val URL_BASE_VEHICULOS = "vehiculos/"
    const val URL_BASE_TIPO_VEHICULOS = "tipo_vehiculos/"
    const val URL_BASE_PROVINCIAS = "provincias/"
    const val URL_BASE_VIAJES = "viajes/"
    const val URL_BASE_CONFIGURACION = "configuracion/"
    const val URL_BASE_ACTUALIZACION="actualizacion/"
    const val URL_BASE_FICHEROS = "ficheros/"
    const val URL_BASE_VALORACION = "valoraciones/"

    const val DEFAULT_CONFIG = "default"
    const val QUERRY_PAGE_SIZE = 10


    //RESPONSE_CODES
    const val RESPONSE_CODE_CREATED = 201
    const val RESPONSE_CODE_OK = 200
    const val RESPONSE_CODE_NOT_FOUND = 404
    const val ERROR_RESPONSE="ERROR_RESPONSE"

    //API
    const val Authorization = "Authorization"

    //Permission
    const val PERMISSION_LOCATION_REQUEST_CODE = 1
    const val PERMISSION_STORAGE_REQUEST_CODE = 2
    const val PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 3
    const val PERMISSION_CALL_PHONE_REQUEST_CODE = 4
    const val PERMISSION_SEND_SMS_REQUEST_CODE = 5
    const val PERMISSION_WRITE_EXTERNAL_STORAGE = 6


    //IntervalTimerConfiguracion
//    const val DELAY_VEHICLES_UPDATE = 10000L
    const val DEFAULT_GetAvailableVehicleInterval = 10L
    const val DEFAULT_SetDriversLocationInterval = 5L


    //General
    const val KEY_VEHICLE_ID = "KEY_VEHICLE_ID"
    const val KEY_VEHICLE_RESPONSE_PARCELABLE = "KEY_VEHICLE_RESPONSE_PARCELABLE"
    const val KEY_VEHICLE_PARCELABLE = "KEY_VEHICLE_PARCELABLE"
    const val KEY_USER_PARCELABLE = "KEY_USER_PARCELABLE"
    const val KEY_PROVINCE_PARCELABLE = "KEY_PROVINCE_PARCELABLE"
    const val KEY_VEHICLE_TYPE_PARCELABLE = "KEY_VEHICLE_TYPE_PARCELABLE"
    const val KEY_USER_FILTER_OPTIONS = "KEY_USER_FILTER_OPTIONS"
    const val KEY_VEHICLE_FILTER_OPTIONS = "KEY_VEHICLE_FILTER_OPTIONS"
    const val KEY_APP_UPDATE_PARCELABLE = "KEY_APP_UPDATE_PARCELABLE"
    const val KEY_IMAGE_URL = "KEY_IMAGE_URL"


    const val UNKNOWN_CLASS = "ClaseDesconocida"


    const val CONTRY_CODE = "+53"

    const val VEHICLE_ANY_TYPE = "Cualquier Tipo"


    //TRACKER SERVICE
    const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
    const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"
    const val NOTIFICATION_CHANEL_ID = "TRACKER_NOTIFICATION_ID"
    const val NOTIFICATION_CHANE_NAME = "Canal de Localizacion"
    const val NOTIFICATION_ID = 100
    const val PENDING_INTENT_REQUEST_CODE = 101
    const val LOCATION_UPDATE_INTERVAL = 4000L
    const val LOCATION_UPDATE_INTERFAL_FASTEST = 2000L


}