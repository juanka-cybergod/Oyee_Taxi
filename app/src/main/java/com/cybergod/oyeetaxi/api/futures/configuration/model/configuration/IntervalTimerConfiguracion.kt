package com.cybergod.oyeetaxi.api.futures.configuration.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class IntervalTimerConfiguracion(

    var getAvailableVehicleInterval: Long? = null,
    var setDriversLocationInterval: Long? = null,



    ): Parcelable
