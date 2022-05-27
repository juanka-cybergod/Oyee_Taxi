package com.cybergod.oyeetaxi.api.futures.user.model.verification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class UsuarioVerificacion(

    var verificado:Boolean? = null,
    var identificacion:String? = null,
    var imagenIdentificaionURL:String? = null,

): Parcelable

