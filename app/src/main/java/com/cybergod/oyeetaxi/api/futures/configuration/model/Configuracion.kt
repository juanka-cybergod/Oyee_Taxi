package com.cybergod.oyeetaxi.api.futures.configuration.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.EmailConfiguracion
import com.google.gson.annotations.SerializedName
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.TwilioConfiguracion
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.UpdateConfiguracion
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
    @SerializedName("motivoServidorInactivoClientes")
    val motivoServidorInactivoClientes: String? = null,
    @SerializedName("motivoServidorInactivoAdministradores")
    val motivoServidorInactivoAdministradores: String? = null,
    @SerializedName("twilioConfiguracion")
    val twilioConfiguracion: TwilioConfiguracion?= null,
    @SerializedName("smsProvider")
    val smsProvider: SmsProvider?=null,
    @SerializedName("emailConfiguracion")
    val emailConfiguracion: EmailConfiguracion? = null,
    @SerializedName("updateConfiguracion")
    val updateConfiguracion: UpdateConfiguracion? = null,
    @SerializedName("socialConfiguracion")
    val socialConfiguracion: SocialConfiguracion? = null,


    ): Parcelable



