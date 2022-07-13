package com.cybergod.oyeetaxi.api.futures.app_update.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actualizacion(

    @SerializedName("id")
    var id:String? = null,
    @SerializedName("active")
    var active: Boolean? = null,
    @SerializedName("version")
    var version: Int? = null,
    @SerializedName("versionString")
    var versionString: String? = null,
    @SerializedName("fileSize")
    var fileSize: String? = null,
    @SerializedName("appURL")
    var appURL: String? = null,
    @SerializedName("playStorePackageName")
    var playStorePackageName: String? = null,
    @SerializedName("forceUpdate")
    var forceUpdate: Boolean? = null,
    @SerializedName("description")
    var description: List<String>? = null,


    //Usado solo por cliente y se define en caso de que el servidor devualva
    //el Header ERROR_RESPONSE
    var errorResponse: String? = null,
    ): Parcelable

