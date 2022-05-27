package com.cybergod.oyeetaxi.api.futures.province.repositories


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import javax.inject.Inject


class ProvincesRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


    suspend fun getAvailableProvinces():List<Provincia>?   {

        handleRequest {
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

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()?.toList() ?: emptyList()
                } else  null

            } else null

        }

        return null

    }

    suspend fun getAllProvinces():List<Provincia>?   {

        handleRequest {
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

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()?.toList() ?: emptyList()
                } else  null

            } else null

        }

        return null

    }

    suspend fun updateProvince(province: Provincia): Provincia?   {

        handleRequest {
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

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else  null

            } else null

        }

        return null

    }

    suspend fun addProvince(province: Provincia): Provincia?   {

        handleRequest {
            retroServiceInterface.addProvince(province)
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