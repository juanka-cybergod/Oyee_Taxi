package com.cybergod.oyeetaxi.api.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UpdateConfiguracion(


    val available: Boolean? = true,
    val version: Int? = null,
    val versionString: String? = null,
    val fileSize: String? = null,
    val appURL: String? = null,
    val packageName: String? = null,
    val forceUpdate: Boolean? = null,
    val description: List<String>? = null,

    ): Parcelable
