package com.cybergod.oyeetaxi.api.futures.vehicle_type.repositories

import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import javax.inject.Inject


class VehicleTypeRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS


    suspend fun getAllVehicleTypes():List<TipoVehiculo>?   {

            handleRequest {
                retroServiceInterface.getAllVehiclesType()
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


    suspend fun getAvailableVehiclesType():List<TipoVehiculo>?   {

            handleRequest {
                retroServiceInterface.getAvailableVehiclesType()
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

    suspend fun updateVehicleType(tipoVehiculo: TipoVehiculo): TipoVehiculo?   {

        handleRequest {
            retroServiceInterface.updateVehicleType(tipoVehiculo)
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