package com.cybergod.oyeetaxi.api.model.response


import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.model.verification.VehiculoVerificacion
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class VehiculoResponse(
    var id:String? = null,
    var usuario: Usuario? = null,
    var tipoVehiculo: TipoVehiculo? = null,
    var marca:String? = null,
    var modelo:String? = null,
    var ano:String? = null,
    var capacidadPasajeros: String? = null,
    var capacidadEquipaje: String? = null,
    var capacidadCarga: String? = null,
    var visible: Boolean? = null,
    var activo: Boolean? = null,
    var habilitado: Boolean? = null,
    var disponible: Boolean? = null,
    var climatizado: Boolean? = null,
    var fechaDeRegistro: String? = null,
    var imagenFrontalPublicaURL:String? = null,
    var vehiculoVerificacion: VehiculoVerificacion? = null,

    ): Parcelable


