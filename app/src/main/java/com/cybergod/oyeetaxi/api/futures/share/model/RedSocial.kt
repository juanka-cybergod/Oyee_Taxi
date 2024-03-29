package com.cybergod.oyeetaxi.api.futures.share.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RedSocial(

    var disponible: Boolean? = null,
    var nombre: String? = null,
    var ico: String? = null,
    var url: String? = null,
    var ayuda: String? = null,

    ): Parcelable
