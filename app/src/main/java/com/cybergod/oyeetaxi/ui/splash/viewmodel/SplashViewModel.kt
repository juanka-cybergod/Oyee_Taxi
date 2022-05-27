package com.cybergod.oyeetaxi.ui.splash.viewmodel

import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.UpdateConfiguracion
import com.cybergod.oyeetaxi.api.futures.configuration.repositories.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStorageRepository: DataStorageRepository,
    private val configurationRepository: ConfigurationRepository
    ) : ViewModel() {


    val userRegistered = dataStorageRepository.readUserRegistred.asLiveData()
    val rememberAppUpdateAfterDate = dataStorageRepository.readRememberAppUpdateAfterDate.asLiveData()
    val readMapStyle = dataStorageRepository.readMapStyle.asLiveData()


    var omitActualization:Boolean = false

    var updateConfiguration : MutableLiveData<UpdateConfiguracion?>  = MutableLiveData()

    val continueNow : MutableLiveData<Boolean> = MutableLiveData<Boolean>( null)


    suspend fun getAvailableUpdate(): UpdateConfiguracion?{
        val updateConfig = configurationRepository.getUpdateConfiguration()
        updateConfiguration.postValue(updateConfig)
        return updateConfig
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