package com.cybergod.oyeetaxi.ui.permissions.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_CALL_PHONE_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_SEND_SMS_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_STORAGE_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_WRITE_EXTERNAL_STORAGE
import com.vmadalin.easypermissions.EasyPermissions


object Permissions {
    //TODO LocationPermission
    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    fun requestLocationPermissions(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Oye Taxi no puede funcionar sin acceso a la Ubicación",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    //TODO StoragePermission
    fun hasStoragePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    fun requestStoragePermissions(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Oye Taxi no puede funcionar sin acceso a guardar los datos de la aplicación",
            PERMISSION_STORAGE_REQUEST_CODE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    //TODO CallPermission
    fun hasCallPhonePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.CALL_PHONE
        )
    fun requestCallPhonePermission(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Para realizar llamadas directamente debe permitir el acceso a Llamadas para Oyee Taxi",
            PERMISSION_CALL_PHONE_REQUEST_CODE,
            Manifest.permission.CALL_PHONE
        )
    }


    //TODO SMSPermission
    fun hasSendSmsPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.SEND_SMS
        )
    fun requestSendSmsPermission(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Para enviar mensajes de texto directamente debe permitir el acceso a Mensajes para Oyee Taxi",
            PERMISSION_SEND_SMS_REQUEST_CODE,
            Manifest.permission.SEND_SMS
        )
    }



    //TODO WHRITE EXTERNAL StoragePermission
    fun hasWriteExternalStoragePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    fun requestWriteExternalStoragePermissions(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Se requiere acceso al almacenamiento del dispositivo para comenzar la descarga de la aplicación",
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }





    /**TODO
     * Requerir este permiso para obtener la ubicacion permanente cuando se use la aplicacion en modo Taxista Solo si lo Desea el Usuario
     */
    //TODO BackgroundLocationPermission
    fun hasBackgroundLocationPermission(context: Context):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
            return EasyPermissions.hasPermissions(
                    context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        return true

    }
    fun requestBackgroundLocationPermission(fragment: Fragment){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
            EasyPermissions.requestPermissions(
                fragment,
                "Oye Taxi no puede funcionar sin acceso a la Ubicación en Segundo Plano. Debe activar la opción \"Permitir Siempre\" ",
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }


}