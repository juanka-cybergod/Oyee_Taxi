package com.cybergod.oyeetaxi.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Provincia(
    @SerializedName("alturaMapa")
    val alturaMapa: Int? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("ubicacion")
    val ubicacion: Ubicacion? = null,
    @SerializedName("visible")
    val visible: Boolean? = null,
):Parcelable