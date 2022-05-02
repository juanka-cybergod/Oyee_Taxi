package com.cybergod.oyeetaxi.api.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TwilioConfiguracion(
     val account_sid: String? = null,
     val auth_token: String? = null,
     val trial_number: String? = null,
     val remaningCredit: Double? = 0.0,
     val smsCost: Double? = 0.0,
    ): Parcelable
