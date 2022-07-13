package com.cybergod.oyeetaxi.api.utils

import android.util.Log
import java.lang.reflect.Method


object UtilsApi {


    suspend fun <T: Any> handleRequest(requestFunc: suspend () -> T): T? {
        return try {
            //Result.success(requestFunc.invoke())
            requestFunc.invoke()

        } catch (e: Exception) {
            null
        }
    }


    fun logResponse(className:String, metodo: Method?, responseCode :Int, responseHeaders:String, responseBody:String){
        Log.d(className, "${metodo?.name?: "MetodoDesconocido"}: R_Code : $responseCode")
        //Log.d(className, "${metodo?.name?: "MetodoDesconocido"}: R_Headers : $responseHeaders")
        Log.d(className, "${metodo?.name?: "MetodoDesconocido"}: R_Body : $responseBody")
    }
}