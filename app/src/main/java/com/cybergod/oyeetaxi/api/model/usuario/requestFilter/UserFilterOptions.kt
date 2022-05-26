package com.cybergod.oyeetaxi.api.model.usuario.requestFilter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFilterOptions(
    var condutores:Boolean?=null,
    var deshabilitados:Boolean?=null,
    var administradores:Boolean?=null,
    var verificacionesPendientes:Boolean?=null,
): Parcelable
