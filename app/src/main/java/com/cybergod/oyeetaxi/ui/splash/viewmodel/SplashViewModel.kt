package com.cybergod.oyeetaxi.ui.splash.viewmodel

import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.futures.app_update.model.Actualizacion
import com.cybergod.oyeetaxi.api.futures.app_update.repositories.ActualizacionRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStorageRepository: DataStorageRepository,
    private val actualizacionRepository: ActualizacionRepository,
    ) : ViewModel() {


    val userRegistered = dataStorageRepository.readUserRegistred.asLiveData()
    val rememberAppUpdateAfterDate = dataStorageRepository.readRememberAppUpdateAfterDate.asLiveData()
    val readMapStyle = dataStorageRepository.readMapStyle.asLiveData()


    var omitActualization:Boolean = false

    var appUpdateLiveData : MutableLiveData<Actualizacion?>  = MutableLiveData()

    val continueNow : MutableLiveData<Boolean> = MutableLiveData<Boolean>( null)


    suspend fun getCurrentAppUpdate(): Actualizacion?{
        val actualizacion = actualizacionRepository.getAppUpdate()
        if (actualizacion?.errorResponse.isNullOrEmpty()) {
            appUpdateLiveData.postValue(actualizacion)
        }
        return actualizacion
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