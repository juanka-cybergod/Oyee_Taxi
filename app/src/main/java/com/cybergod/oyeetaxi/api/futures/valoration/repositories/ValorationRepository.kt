package com.cybergod.oyeetaxi.api.futures.valoration.repositories


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.valoration.model.Valoracion
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import javax.inject.Inject

class ValorationRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface) {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


    suspend fun getValorationByUsersId(idUsuarioValora: String, idUsuarioValorado: String): Valoracion?   {

        handleRequest {
            retroServiceInterface.getValorationByUsersId(idUsuarioValora,idUsuarioValorado)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                return if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else {
                    null
                }

            } else null

        }

        return null

    }


    suspend fun addUpdateValoration(valoracion: Valoracion): Valoracion?   {


        handleRequest {
            retroServiceInterface.addUpdateValoration(valoracion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                return if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else {
                    null
                }

            } else null

        }

        return null

    }



}