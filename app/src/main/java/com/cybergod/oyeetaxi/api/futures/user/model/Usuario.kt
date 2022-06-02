package com.cybergod.oyeetaxi.api.futures.user.model


import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.google.gson.annotations.SerializedName
import com.cybergod.oyeetaxi.api.futures.user.model.verification.UsuarioVerificacion
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Usuario(
    @SerializedName("id")
    var id:String? = null,
    @SerializedName("conductor")
    var conductor:Boolean? = null,
    @SerializedName("modoCondutor")
    val modoCondutor: Boolean? = null,
    @SerializedName("nombre")
    var nombre:String? = null,
    @SerializedName("apellidos")
    var apellidos:String? = null,
    @SerializedName("imagenPerfilURL")
    var imagenPerfilURL:String? = null,
    @SerializedName("telefonoMovil")
    var telefonoMovil:String? = null,
    @SerializedName("telefonoFijo")
    var telefonoFijo:String? = null,
    @SerializedName("correo")
    var correo:String? = null,
    @SerializedName("contrasena")
    var contrasena:String? = null,
    @SerializedName("otpCode")
    var otpCode:String?=null,
    @SerializedName("provincia")
    var provincia: Provincia? = null,
    @SerializedName("fechaDeNacimiento")
    var fechaDeNacimiento:String? = null,
    @SerializedName("fechaDeRegistro")
    var fechaDeRegistro:String? = null,
    @SerializedName("habilitado")
    var habilitado:Boolean? = null,
    @SerializedName("administrador")
    var administrador:Boolean? = null,
    @SerializedName("superAdministrador")
    var superAdministrador:Boolean? = null,
    @SerializedName("ubicacion")
    var ubicacion: Ubicacion? = null,
    @SerializedName("usuarioVerificacion")
    var usuarioVerificacion: UsuarioVerificacion? = null,
    @SerializedName("valoracion")
    var valoracion: Float? = null,
    @SerializedName("mensaje")
    var mensaje: String? = null,

    ):Parcelable


