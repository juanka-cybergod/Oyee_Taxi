package com.cybergod.oyeetaxi.web_socket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class ChatObject(

     var userName: String? = null,

     val message: String? = null,
):Parcelable
