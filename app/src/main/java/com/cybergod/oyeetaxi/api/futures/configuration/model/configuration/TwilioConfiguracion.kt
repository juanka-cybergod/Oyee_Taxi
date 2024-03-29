package com.cybergod.oyeetaxi.api.futures.configuration.model.configuration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TwilioConfiguracion(
     var account_sid: String? = null,
     var auth_token: String? = null,
     var trial_number: String? = null,
     var remainingCredit: Double? = null,//0.0
     var smsCost: Double? = null,//0.0
    ): Parcelable
