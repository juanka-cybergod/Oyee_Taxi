package com.cybergod.oyeetaxi.api.futures.vehicle.repositories

import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter.VehicleFilterOptions
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculosPaginados
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_CREATED
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import com.cybergod.oyeetaxi.utils.GlobalVariables.isServerAvailable
import javax.inject.Inject


class VehicleRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface)  {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"


        suspend fun searchVehiclesPaginatedWithFilter(page:Int=1, vehicleFilterOptions: VehicleFilterOptions?) : VehiculosPaginados?  {

        handleRequest {
            retroServiceInterface.searchVehiclesPaginatedWithFilter(
                page = page - 1,
                vehicleFilterOptions = vehicleFilterOptions,
            )
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
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



    suspend fun addVehicle(vehiculo: Vehiculo) : Boolean?  {

        handleRequest {
            retroServiceInterface.addVehicle(vehiculo)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
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


    suspend fun getActiveVehicleByUserId(userId: String):VehiculoResponse?   {

        handleRequest {
            retroServiceInterface.getActiveVehicleByUserId(userId)
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


    suspend fun getAvailableVehicles() :List<VehiculoResponse>?   {

        handleRequest {
            retroServiceInterface.getAvailableVehicles()
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
                    isServerAvailable.postValue(true)
                    response.body()?.toList()
                } else  null
            } else null

        }

        isServerAvailable.postValue(false)
        return null




    }


    suspend fun updateVehicle(vehiculo: Vehiculo) :Boolean?  {

        handleRequest {
            retroServiceInterface.updateVehicle(vehiculo)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                response.code() == RESPONSE_CODE_OK

            } else false

        }

        return null
    }


    suspend fun updateVehicleAndGetVehicleResponse(vehiculo: Vehiculo) :VehiculoResponse?  {

        handleRequest {
            retroServiceInterface.updateVehicleAndGetVehicleResponse(vehiculo)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                response.body()

            } else null

        }

        return null
    }

    suspend fun getAllVehiclesFromUserId(userId: String):List<VehiculoResponse>?  {

        handleRequest {
            retroServiceInterface.getAllVehiclesFromUserId(userId)
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
                    response.body()?.toList()
                } else  null
            } else null

        }

        return null
    }


    suspend fun setActiveVehicleToUserId( userId: String,vehicleId: String):Boolean?   {

        handleRequest {
            retroServiceInterface.setActiveVehicleToUserId(userId,vehicleId)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                response.code() == RESPONSE_CODE_OK
            } else false

        }

        return null

    }




}