package com.cybergod.oyeetaxi.api.model

import android.os.Parcelable
import com.cybergod.oyeetaxi.api.futures.share.model.RedSocial
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialConfiguracion(

    var disponible: Boolean? = null,
    var phone: String? = null,
    var email: String? = null,
    var web: String? = null,
    var redesSociales: List<RedSocial>? = null,


//    @Nullable var whatsapp: String? = null,
//    @Nullable var facebook: String? = null,
//    @Nullable var instagram: String? = null,
//    @Nullable var linkedin: String? = null,
//    @Nullable var snapchat: String? = null,
//    @Nullable var tiktok: String? = null,
//    @Nullable var like: String? = null,

    ): Parcelable
