package com.cybergod.oyeetaxi.ui.userRegistration.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TwilioSmsViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : BaseViewModel(){


    val otpCode: MutableLiveData<String> = MutableLiveData<String>()


    fun twilioPhoneAuthentication(userPhone: String){

        viewModelScope.launch(Dispatchers.IO) {
            otpCode.postValue(
                configurationRepository.sendSMS(userPhone)
            )
        }


    }






}