package com.cybergod.oyeetaxi.api.futures.vehicle.model.response

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.IntervalTimerConfiguracion
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class DataResponse(

    @SerializedName("vehicleResponseList")
    var vehicleResponseList: List<VehiculoResponse>? = null,

    @SerializedName("intervalTimerConfiguracion")
    var intervalTimerConfiguracion: IntervalTimerConfiguracion? = null,


    ): Parcelable


