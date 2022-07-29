package com.cybergod.oyeetaxi.api.futures.configuration.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class IntervalTimerConfiguracion(

    var getAvailableVehicleInterval: Long? = null,
    var setDriversLocationInterval: Long? = null,



    ): Parcelable
