package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RedSocial(

    var disponible: Boolean? = null,
    var nombre: String? = null,
    var ico: String? = null,
    var url: String? = null,


    ): Parcelable
