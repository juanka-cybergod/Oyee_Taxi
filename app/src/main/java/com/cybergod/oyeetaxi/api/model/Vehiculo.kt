package com.cybergod.oyeetaxi.api.model


import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.verification.VehiculoVerificacion
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Vehiculo(
    @SerializedName("ano")
    val ano: String? = null,

    @SerializedName("capacidadPasajeros")
    val capacidadPasajeros: String? = null,

    @SerializedName("capacidadEquipaje")
    val capacidadEquipaje: String? = null,

    @SerializedName("capacidadCarga")
    val capacidadCarga: String? = null,

    @SerializedName("habilitado")
    val habilitado: Boolean? = null,

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("idUsuario")
    val idUsuario: String? = null,

    @SerializedName("marca")
    val marca: String? = null,

    @SerializedName("modelo")
    val modelo: String? = null,

    @SerializedName("disponible")
    val disponible: Boolean? = null,

    @SerializedName("visible")
    val visible: Boolean? = null,

    @SerializedName("tipoVehiculo")
    val tipoVehiculo: String? = null,

    @SerializedName("climatizado")
    var climatizado: Boolean? = null,

    @SerializedName("fechaDeRegistro")
    val fechaDeRegistro: String? = null,

    @SerializedName("imagenFrontalPublicaURL")
    var imagenFrontalPublicaURL:String? = null,

    @SerializedName("vehiculoVerificacion")
    var vehiculoVerificacion: VehiculoVerificacion? = null,

    @SerializedName("activo")
    val activo: Boolean? = null,


    ): Parcelable