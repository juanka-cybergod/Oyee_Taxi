package com.cybergod.oyeetaxi.api.repository

import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.utils.UtilsApi
import com.cybergod.oyeetaxi.utils.Constants
import javax.inject.Inject


class VehicleTypeRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


    suspend fun getAllVehicleTypes():List<TipoVehiculo>?   {

            UtilsApi.handleRequest {
                retroServiceInterface.getAllVehiclesType()
            }?.let { response ->

                return if (response.isSuccessful) {

                    UtilsApi.logResponse(
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




}