package com.cybergod.oyeetaxi.api.futures.configuration.repositories

import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.api.futures.configuration.model.SmsProvider
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import javax.inject.Inject



class ConfigurationRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS

    suspend fun isServerActive():Boolean?{

        val response = retroServiceInterface.isServerActive()

        return if (response.isSuccessful) {
            response.body()
        } else null

    }


    suspend fun getSmsProvider(): SmsProvider?   {

        handleRequest {
            retroServiceInterface.getSmsProvider()
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == Constants.RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null

    }



    suspend fun getConfiguration(): Configuracion? {

        handleRequest {
            retroServiceInterface.getConfiguration()
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == Constants.RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null


    }

    
    suspend fun updateConfiguration(configuracion: Configuracion): Configuracion? {


        handleRequest {
            retroServiceInterface.updateConfiguration(configuracion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == Constants.RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null


    }


}