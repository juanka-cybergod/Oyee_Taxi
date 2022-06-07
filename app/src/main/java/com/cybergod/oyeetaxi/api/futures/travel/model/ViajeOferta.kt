package com.cybergod.oyeetaxi.api.futures.travel.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class ViajeOferta(
  var conductorVehiculo: VehiculoResponse? = null,
  var precio:Long? = 0L,
  var ofertaAceptada: Boolean? = null,




  ): Parcelable
