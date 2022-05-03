package com.cybergod.oyeetaxi.api.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TwilioConfiguracion(
     var account_sid: String? = null,
     var auth_token: String? = null,
     var trial_number: String? = null,
     var remaningCredit: Double? = null,//0.0
     var smsCost: Double? = null,//0.0
    ): Parcelable
