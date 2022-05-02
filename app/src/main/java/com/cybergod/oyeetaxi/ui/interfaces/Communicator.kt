package com.cybergod.oyeetaxi.ui.interfaces

import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoVehiculo

interface Communicator {
    fun passProvinceSelected(province: Provincia)
    fun passVehicleTypeSelected(vehicleType: TipoVehiculo)
}