package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.configuration.EmailConfiguracion
import com.google.gson.annotations.SerializedName
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import com.cybergod.oyeetaxi.api.model.configuration.TwilioConfiguracion
import com.cybergod.oyeetaxi.api.model.configuration.UpdateConfiguracion
import com.cybergod.oyeetaxi.utils.Constants.DEFAULT_CONFIG
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configuracion(
    @SerializedName("id")
    val id:String? = DEFAULT_CONFIG,
    @SerializedName("servidorActivoClientes")
    val servidorActivoClientes: Boolean? = null,
    @SerializedName("servidorActivoAdministradores")
    val servidorActivoAdministradores: Boolean? = null,
    @SerializedName("twilioConfiguracion")
    val twilioConfiguracion: TwilioConfiguracion?= null,
    @SerializedName("smsProvider")
    val smsProvider: SmsProvider?=null,
    @SerializedName("emailConfiguracion")
    val emailConfiguracion: EmailConfiguracion? = null,
    @SerializedName("updateConfiguracion")
    val updateConfiguracion: UpdateConfiguracion? = null,


    ): Parcelable



