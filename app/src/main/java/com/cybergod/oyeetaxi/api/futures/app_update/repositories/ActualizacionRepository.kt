package com.cybergod.oyeetaxi.api.futures.app_update.repositories

import com.cybergod.oyeetaxi.api.futures.app_update.model.Actualizacion
import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.ERROR_RESPONSE
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_NOT_FOUND
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import javax.inject.Inject



class ActualizacionRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName?:UNKNOWN_CLASS

    suspend fun getAppUpdate(): Actualizacion?   {

        handleRequest {
            retroServiceInterface.getAppUpdate()
        }?.let { response ->

            //return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                return when (response.code()) {
                    RESPONSE_CODE_OK -> {
                        response.body()
                    }
                    RESPONSE_CODE_NOT_FOUND -> {
                        Actualizacion(
                            errorResponse = response.headers()[ERROR_RESPONSE]
                        )
                    }
                    else -> null
                }

//                if (response.code() == Constants.RESPONSE_CODE_OK) {
//                    response.body()
//                } else  null

            //} else null

        }


        return null

    }

    suspend fun setAppUpdateActiveById(idActualizacion:String, active:Boolean): Boolean?   {

        handleRequest {
            retroServiceInterface.setAppUpdateActiveById(idActualizacion,active)
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

    suspend fun getAllAppUpdates(): List<Actualizacion>?   {

        handleRequest {
            retroServiceInterface.getAllAppUpdates()
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
                    response.body()?.toList()
                } else  null

            } else null

        }


        return null

    }

    suspend fun addAppUpdate(actualizacion: Actualizacion):Actualizacion? {

        handleRequest {
            retroServiceInterface.addAppUpdate(actualizacion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null

    }

    suspend fun editAppUpdate(actualizacion: Actualizacion):Actualizacion? {

        handleRequest {
            retroServiceInterface.editAppUpdate(actualizacion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null

    }


    suspend fun deleteAppUpdateById(idActualizacion: String):Boolean? {

        handleRequest {
            retroServiceInterface.deleteAppUpdateById(idActualizacion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }


        return null

    }



}