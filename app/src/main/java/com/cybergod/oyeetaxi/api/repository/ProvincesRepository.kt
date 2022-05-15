package com.cybergod.oyeetaxi.api.repository


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.utils.UtilsApi
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants
import javax.inject.Inject


class ProvincesRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


    suspend fun getAvailableProvinces():List<Provincia>?   {

        UtilsApi.handleRequest {
            retroServiceInterface.getAvailableProvinces()
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
                    response.body()?.toList() ?: emptyList()
                } else  null

            } else null

        }

        return null

    }



    suspend fun getAllProvinces():List<Provincia>?   {

        UtilsApi.handleRequest {
            retroServiceInterface.getAllProvinces()
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
                    response.body()?.toList() ?: emptyList()
                } else  null

            } else null

        }

        return null

    }

    suspend fun updateProvince(province:Provincia):Provincia?   {

        UtilsApi.handleRequest {
            retroServiceInterface.updateProvince(province)
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