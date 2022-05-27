package com.cybergod.oyeetaxi.api.futures.province.model


import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Provincia(
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("visible")
    val visible: Boolean? = null,
    @SerializedName("ubicacion")
    val ubicacion: Ubicacion? = null,
):Parcelable