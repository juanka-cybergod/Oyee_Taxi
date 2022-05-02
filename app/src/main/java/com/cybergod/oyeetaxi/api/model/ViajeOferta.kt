package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class ViajeOferta(
  var conductorVehiculo: VehiculoResponse? = null,
  var precio:Long? = 0L,
  var ofertaAceptada: Boolean? = null,




  ): Parcelable
