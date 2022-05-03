package com.cybergod.oyeetaxi.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.cybergod.oyeetaxi.api.model.verification.UsuarioVerificacion
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
    var provincia:Provincia? = null,
    @SerializedName("fechaDeNacimiento")
    var fechaDeNacimiento:String? = null,
    @SerializedName("fechaDeRegistro")
    var fechaDeRegistro:String? = null,
    @SerializedName("habilitado")
    val habilitado:Boolean? = null,
    @SerializedName("administrador")
    val administrador:Boolean? = null,
    @SerializedName("superAdministrador")
    val superAdministrador:Boolean? = null,
    @SerializedName("ubicacion")
    var ubicacion: Ubicacion? = null,
    @SerializedName("usuarioVerificacion")
    val usuarioVerificacion: UsuarioVerificacion? = null,
    @SerializedName("valoracion")
    val valoracion: Float? = null,
    @SerializedName("mensaje")
    val mensaje: String? = null,

    ):Parcelable


