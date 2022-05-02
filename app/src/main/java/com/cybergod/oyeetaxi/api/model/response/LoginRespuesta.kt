package com.cybergod.oyeetaxi.api.model.response

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.model.Usuario
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class LoginRespuesta(

    val usuarioEncontrado :Boolean? = null,
    val contrasenaCorrecta: Boolean? = null,
    val mensajeBienvenida : String? = null,
    val usuario: Usuario? = null,
    val vehiculoActivo: VehiculoResponse? = null,


    ):Parcelable
