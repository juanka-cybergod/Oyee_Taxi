package com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration


import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.*
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
import com.cybergod.oyeetaxi.api.futures.configuration.repositories.ConfigurationRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdministrationViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : BaseViewModel() {


    val serverConfiguration : MutableLiveData<Configuracion> = MutableLiveData<Configuracion>()
    val updatedConfiguracion : MutableLiveData<Configuracion?> = MutableLiveData<Configuracion?>()


    val smsProviderItems : MutableList<String> = getSmsProviderList()
    lateinit var smsArrayAdapter : ArrayAdapter<String>

    lateinit var protocolItems:Array<String>
    lateinit var protocolArrayAdapter : ArrayAdapter<String>


    private fun getSmsProviderList(): MutableList<String>{
        val stringArray = mutableListOf<String>()
        SmsProvider.values().forEach {
            stringArray.add(it.name)
        }
       return  stringArray
    }


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

    fun setServerActiveForClients(active: Boolean,motivo:String?=null) {
        viewModelScope.launch(Dispatchers.IO) {

            refreshThisConfiguration(

                configurationRepository.updateConfiguration(
                    Configuracion(
                        servidorActivoClientes = active,
                        motivoServidorInactivoClientes = motivo
                    )
                )

            )
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

    fun setServerSmsProvider(selectedSmsProvider: SmsProvider) {
        viewModelScope.launch(Dispatchers.IO) {

            refreshThisConfiguration(

                configurationRepository.updateConfiguration(
                    Configuracion(
                        registerConfiguracion =  RegisterConfiguracion(
                            smsProvider = selectedSmsProvider,
                        )

                    )
                )

            )
        }
    }

    fun setServerTwilioConfiguration(newTwilioConfiguracion: TwilioConfiguracion) {

        viewModelScope.launch(Dispatchers.IO) {
            refreshThisConfiguration(
                configurationRepository.updateConfiguration(
                    Configuracion(
                        twilioConfiguracion = newTwilioConfiguracion,
                    )
                )
            )
        }



    }

    fun setServerEmailConfiguration(emailConfiguracion: EmailConfiguracion) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshThisConfiguration(
                configurationRepository.updateConfiguration(
                    Configuracion(
                        emailConfiguracion = emailConfiguracion,
                    )
                )
            )
        }
    }

    fun setServerRegisterConfiguration(registerConfiguracion: RegisterConfiguracion) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshThisConfiguration(
                configurationRepository.updateConfiguration(
                    Configuracion(
                        registerConfiguracion = registerConfiguracion
                    )
                )
            )
        }
    }




    fun setServerSocialConfiguration(socialConfiguracion: SocialConfiguracion) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshThisConfiguration(
                configurationRepository.updateConfiguration(
                    Configuracion(
                        socialConfiguracion = socialConfiguracion,
                    )
                )
            )
        }
    }


    fun setIntervalTimerConfiguration(intervalTimerConfiguracion: IntervalTimerConfiguracion) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshThisConfiguration(
                configurationRepository.updateConfiguration(
                    Configuracion(
                        intervalTimerConfiguracion = intervalTimerConfiguracion
                    )
                )
            )
        }
    }




}