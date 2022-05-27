package com.cybergod.oyeetaxi.api.futures.user.model.response

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.vahicle.model.response.VehiculoResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class LoginRespuesta(

    val usuarioEncontrado :Boolean? = null,
    val contrasenaCorrecta: Boolean? = null,
    var servidorActivo: Boolean? = null,
    val mensaje : String? = null,
    val usuario: Usuario? = null,
    val vehiculoActivo: VehiculoResponse? = null,


    ):Parcelable
