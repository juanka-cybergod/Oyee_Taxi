package com.cybergod.oyeetaxi.ui.interfaces

import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo

interface Communicator {
    fun passProvinceSelected(province: Provincia)
    fun passVehicleTypeSelected(vehicleType: TipoVehiculo)
}