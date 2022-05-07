package com.cybergod.oyeetaxi.api.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class EmailConfiguracion(

    //val serviceEmail: String? = null,
    val host: String? = null,
    val port: Int? = null,
    val username: String? = null,
    val password: String? = null,

    val properties_mail_transport_protocol: String? = null,
    val properties_mail_smtp_auth: Boolean? = null,
    val properties_mail_smtp_starttls_enable: Boolean? = null,
    val properties_mail_debug: Boolean? = null,

    ): Parcelable
