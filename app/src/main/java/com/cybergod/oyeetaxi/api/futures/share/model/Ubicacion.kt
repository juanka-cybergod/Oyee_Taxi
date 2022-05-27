package com.cybergod.oyeetaxi.api.futures.share.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Ubicacion(
    @SerializedName("latitud")
    val latitud: Double? = null,
    @SerializedName("longitud")
    val longitud: Double? = null,
    @SerializedName("rotacion")
    val rotacion:Int? = 0,
    @SerializedName("direccion")
    val direccion:String? = null,
    @SerializedName("alturaMapa")
    var alturaMapa:Int? = null
):Parcelable

