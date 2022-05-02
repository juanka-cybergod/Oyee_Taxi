package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Valoracion(

    @SerializedName("id")
    var id:String? = null,

    @SerializedName("idUsuarioValora")
    var idUsuarioValora:String? = null,

    @SerializedName("idUsuarioValorado")
    var idUsuarioValorado:String? = null,

    @SerializedName("valoracion")
    var valoracion:Float? = null,

    @SerializedName("opinion")
    var opinion:String? = null,

): Parcelable
