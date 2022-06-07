package com.cybergod.oyeetaxi.api.futures.vehicle.model.verification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class VehiculoVerificacion(

    var verificado:Boolean? = null,
    var matricula:String? = null,
    var circulacion:String? = null,
    var imagenCirculacionURL:String? = null,



): Parcelable

