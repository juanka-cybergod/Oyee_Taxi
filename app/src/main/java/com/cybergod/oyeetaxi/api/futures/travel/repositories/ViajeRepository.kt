package com.cybergod.oyeetaxi.api.futures.travel.repositories


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.travel.model.Viaje
import com.cybergod.oyeetaxi.api.utils.UtilsApi
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_CREATED
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import javax.inject.Inject

class ViajeRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface) {

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS


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