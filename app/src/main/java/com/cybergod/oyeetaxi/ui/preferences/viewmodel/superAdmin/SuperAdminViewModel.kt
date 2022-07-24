package com.cybergod.oyeetaxi.ui.preferences.viewmodel.superAdmin


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.api.futures.configuration.repositories.ConfigurationRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SuperAdminViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : BaseViewModel() {


    val serverConfiguration : MutableLiveData<Configuracion> = MutableLiveData<Configuracion>()
    val updatedConfiguracion : MutableLiveData<Configuracion?> = MutableLiveData<Configuracion?>()





    fun getServerConfiguration(){

        viewModelScope.launch(Dispatchers.IO) {
            serverConfiguration.postValue(
                configurationRepository.getConfiguration()
            )
        }

    }



    private suspend fun refreshThisConfiguration(config: Configuracion?) {


        delay(1000)

        updatedConfiguracion.postValue(
            config
        )
        config?.let {
            serverConfiguration.postValue(it)
        }

    }

    fun setServerActiveForAdmins(active: Boolean, motivo:String?=null) {
        viewModelScope.launch(Dispatchers.IO) {

            refreshThisConfiguration(

                configurationRepository.updateConfiguration(
                    Configuracion(
                        servidorActivoAdministradores = active,
                        motivoServidorInactivoAdministradores =  motivo
                    )
                )

            )
        }
    }




}
