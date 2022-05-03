package com.cybergod.oyeetaxi.ui.splash.viewmodel

import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.model.configuration.UpdateConfiguracion
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStorageRepository: DataStorageRepository,
    private val configurationRepository: ConfigurationRepository
    ) : ViewModel() {


    val userRegistred = dataStorageRepository.readUserRegistred.asLiveData()
    val rememberAppUpdateAfterDate = dataStorageRepository.readRememberAppUpdateAfterDate.asLiveData()
    val readMapStyle = dataStorageRepository.readMapStyle.asLiveData()



    var omitirActualizacion:Boolean = false

    val updateConfiguration : MutableLiveData<UpdateConfiguracion> = MutableLiveData()

    val continueNow : MutableLiveData<Boolean> = MutableLiveData<Boolean>( null)


//    fun getUpdateConfiguration(){
//
//        viewModelScope.launch(Dispatchers.IO) {
//            updateConfiguration.postValue(
//                configurationRepository.getUpdateConfiguration()
//            )
//        }
//
//    }


    fun getUpdateConfiguration(){

        viewModelScope.launch(Dispatchers.IO) {
            updateConfiguration.postValue(
                configurationRepository.getUpdateConfiguration()
            )
        }

    }



    fun saveRememberAppUpdateAfterDate(remember:Boolean){
        viewModelScope.launch {
            if (remember) {
                dataStorageRepository.saveRememberAppUpdateAfterDate(getCurrentDate())
            } else {
                dataStorageRepository.removeRememberAppUpdateAfterDate()
            }

        }
    }


}