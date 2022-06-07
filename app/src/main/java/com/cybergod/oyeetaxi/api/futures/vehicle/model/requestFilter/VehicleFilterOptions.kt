package com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleFilterOptions(
    var texto:String="",
    var tipoVehiculo:String?=null,
    var noVisibles:Boolean?=null,
//    var activos:Boolean?=null, //Not Use Yet
    var deshabilitados:Boolean?=null,
    var verificacionesPendientes:Boolean?=null,
): Parcelable
