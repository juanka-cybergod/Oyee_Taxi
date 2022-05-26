package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Viaje(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("estado")
    var estado:ViajeEstado? = ViajeEstado.PENDIENTE,

    @SerializedName("usuario")
    var usuario : Usuario? = null,

    @SerializedName("paraTiposVehiculos")
    var paraTiposVehiculos : List<TipoVehiculo>? = null,

    @SerializedName("origen")
    var origen:Ubicacion? = null,

    @SerializedName("destino")
    var destino:Ubicacion? = null,

    @SerializedName("distancia")
    var distancia:String? = null,

    @SerializedName("pasajeros")
    var pasajeros:Int? = null,

    @SerializedName("detallesAdicionales")
    var detallesAdicionales:String? = null,

    @SerializedName("motivoCancelacion")
    var motivoCancelacion:String? = null,

    @SerializedName("ofertasRecibidas")
    var ofertasRecibidas:List<ViajeOferta>? = emptyList<ViajeOferta>(),

    @SerializedName("metodoPago")
    var metodoPago:MetodoPago? = MetodoPago.EFECTIVO,

    @SerializedName("soloIda")
    var soloIda:Boolean? = null,

    @SerializedName("equipaje")
    var equipaje:Int? = null,

    @SerializedName("fechaHoraSolicitud")
    var fechaHoraSolicitud: String? = null,

    @SerializedName("fechaHoraViaje")
    var fechaHoraViaje: String? = null,

    @SerializedName("requiereClimatizado")
    var requiereClimatizado:Boolean?=null

): Parcelable

