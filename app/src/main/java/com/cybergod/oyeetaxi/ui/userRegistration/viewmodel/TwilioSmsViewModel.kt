package com.cybergod.oyeetaxi.ui.userRegistration.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TwilioSmsViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val dataStoreRepository: DataStorageRepository,
) : BaseViewModel(){


    val otpCode: MutableLiveData<String> = MutableLiveData<String>()


    fun twilioPhoneAuthentication(userPhone: String){

        viewModelScope.launch(Dispatchers.IO) {
            otpCode.postValue(
                configurationRepository.sendSMS(userPhone)
            )
        }


    }






//    fun vehicleRegisterSuccess(success:Boolean){
//        viewModelScope.launch {
//            //TODO Guardar en DataStore el ID del Vehiculo Activo que tiene el usuario para que actualice su ubicacion
//            dataStoreRepository.saveCurrentVehicle(idVehiculo)
//        }
//    }






}