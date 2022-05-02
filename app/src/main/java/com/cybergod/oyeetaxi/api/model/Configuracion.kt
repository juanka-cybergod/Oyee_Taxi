package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.configuration.EmailConfiguracion
import com.google.gson.annotations.SerializedName
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import com.cybergod.oyeetaxi.api.model.configuration.TwilioConfiguracion
import com.cybergod.oyeetaxi.api.model.configuration.UpdateConfiguracion
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configuracion(
    @SerializedName("id")
    val id:String? = "default",
    @SerializedName("servidorActivoClientes")
    val servidorActivoClientes: Boolean? = true,
    @SerializedName("servidorActivoAdministradores")
    val servidorActivoAdministradores: Boolean? = true,
    @SerializedName("versionMinimaAppCliente")
    val versionMinimaAppCliente:Int? = 0,
    @SerializedName("twilioConfiguracion")
    val twilioConfiguracion: TwilioConfiguracion?= null,
    @SerializedName("smsProvider")
    val smsProvider: SmsProvider?=SmsProvider.DISABLE,
    @SerializedName("emailConfiguracion")
    val emailConfiguracion: EmailConfiguracion? = null,
    @SerializedName("updateConfiguracion")
    val updateConfiguracion: UpdateConfiguracion? = null,


    ): Parcelable



