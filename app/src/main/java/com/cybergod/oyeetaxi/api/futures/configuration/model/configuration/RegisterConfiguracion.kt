package com.cybergod.oyeetaxi.api.futures.configuration.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RegisterConfiguracion(

    var smsProvider: SmsProvider? = null,
    var habilitadoRegistroConductores: Boolean? = null,
    var habilitadoRegistroPasajeros: Boolean? = null,


    ): Parcelable
