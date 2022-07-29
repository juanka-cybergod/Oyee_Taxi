package com.cybergod.oyeetaxi.api.futures.configuration.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.*
import com.google.gson.annotations.SerializedName
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
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
    @SerializedName("emailConfiguracion")
    val emailConfiguracion: EmailConfiguracion? = null,
    @SerializedName("actualizacionHabilita")
    val actualizacionHabilita: Boolean? = null,
    @SerializedName("socialConfiguracion")
    val socialConfiguracion: SocialConfiguracion? = null,
    @SerializedName("intervalTimerConfiguracion")
    val intervalTimerConfiguracion: IntervalTimerConfiguracion? = null,//
    @SerializedName("registerConfiguracion")
    val registerConfiguracion: RegisterConfiguracion? = null,

    ): Parcelable



