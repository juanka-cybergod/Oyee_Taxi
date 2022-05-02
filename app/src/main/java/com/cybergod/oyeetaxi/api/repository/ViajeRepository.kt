package com.cybergod.oyeetaxi.api.repository


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.model.Viaje
import com.cybergod.oyeetaxi.api.utils.UtilsApi
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_CREATED
import javax.inject.Inject

class ViajeRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface) {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


    suspend fun addViaje(viaje: Viaje):Boolean?   {


        UtilsApi.handleRequest {
            retroServiceInterface.addViaje(viaje)
        }?.let { response ->

            return if (response.isSuccessful) {

                UtilsApi.logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                response.code() == RESPONSE_CODE_CREATED

            } else false

        }


        return null


    }






}