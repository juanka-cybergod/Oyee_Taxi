package com.cybergod.oyeetaxi.ui.preferences.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Configuracion
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdministrationViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val dataStorageRepository: DataStorageRepository,
) : BaseViewModel() {


    val serverConfiguration : MutableLiveData<Configuracion> = MutableLiveData<Configuracion>()

    fun getServerConfiguration(){

        viewModelScope.launch(Dispatchers.IO) {
            serverConfiguration.postValue(
                configurationRepository.getConfiguration()
            )
        }

    }

    fun setServerActiveForClients(active: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000)

            val updatedConfiguracion =  configurationRepository.updateConfiguration(
                Configuracion(
                    servidorActivoClientes = active
                )
            )
            updatedConfiguracion?.let {
                serverConfiguration.postValue(it)
            }

        }
    }

}
