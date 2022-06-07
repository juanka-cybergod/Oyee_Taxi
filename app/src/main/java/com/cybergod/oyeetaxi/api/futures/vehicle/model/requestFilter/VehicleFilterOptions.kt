package com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleFilterOptions(
    var texto:String="",
    var tipoVehiculo:String?=null,
    var visibles:Boolean?=null,
    var activos:Boolean?=null,
    var deshabilitados:Boolean?=null,
    var verificacionesPendientes:Boolean?=null,
): Parcelable
