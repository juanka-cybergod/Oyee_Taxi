package com.cybergod.oyeetaxi.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TipoVehiculo(
    @SerializedName("tipoVehiculo")
    val tipoVehiculo: String? = null,
    @SerializedName("descripcion")
    val descripcion: String? = null,
    @SerializedName("cuotaMensual")
    val cuotaMensual: Int? = null,
    @SerializedName("seleccionable")
    val seleccionable: Boolean? = null,
    @SerializedName("prioridadEnMapa")
    val prioridadEnMapa: Int? = null,
    @SerializedName("transportePasajeros")
    val transportePasajeros: Boolean? = null,
    @SerializedName("transporteCarga")
    val transporteCarga: Boolean? = null,
    @SerializedName("requiereVerification")
    val requiereVerification: Boolean? = null,
    @SerializedName("imagenVehiculoURL")
    var imagenVehiculoURL:String? = null,


): Parcelable

