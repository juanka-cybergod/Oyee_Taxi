package com.cybergod.oyeetaxi.ui.preferences.viewmodel.superAdmin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.app_update.model.Actualizacion
import com.cybergod.oyeetaxi.api.futures.app_update.repositories.ActualizacionRepository
import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.api.futures.configuration.repositories.ConfigurationRepository
import com.cybergod.oyeetaxi.api.futures.file.repositories.FilesRepository
import com.cybergod.oyeetaxi.api.futures.file.request_body.UploadRequestBody
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ActualizacionesViewModel @Inject constructor(
    private val actualizacionRepository: ActualizacionRepository,
    private val configuracionRepository: ConfigurationRepository,
    private val filesRepository: FilesRepository
    ) :  BaseViewModel(),UploadRequestBody.UploadCallback {

    var actualizacionFile : MutableLiveData<File?> = MutableLiveData()

    var actualizacionesList: MutableLiveData<List<Actualizacion>> = MutableLiveData()
    var actualizacionHabilitada : MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        getAllAppUpdates()

        viewModelScope.launch(Dispatchers.IO) {
            configuracionRepository.getConfiguration()?.let {
                actualizacionHabilitada.postValue(it.actualizacionHabilita?:true)
            }
        }

    }


    fun setAppUpdatesEnabled(enabled :Boolean) {

        viewModelScope.launch (Dispatchers.IO){

            delay(1000)
            actualizacionHabilitada.postValue(
                configuracionRepository.updateConfiguration(
                    Configuracion(
                        actualizacionHabilita = enabled
                    )
                )?.actualizacionHabilita
            )

        }

    }

    private fun getAllAppUpdates(){

            isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {

                delay(1000)
                actualizacionRepository.getAllAppUpdates()?.let {
                    actualizacionesList.postValue(it)
                }

                isLoading.postValue(false)
            }




    }


    fun setAppUpdateActiveById(idActualizacion:String, active:Boolean){

        viewModelScope.launch(Dispatchers.IO) {
            actualizacionRepository.setAppUpdateActiveById(idActualizacion,active)
            getAllAppUpdates()
        }

    }


    ////





    var porcentageSubida: MutableLiveData<Int?> = MutableLiveData()
    var doing :String = "Analizando"
    var count :Int = 0
    override fun onProggressUpdate(porcentage: Int) {

        porcentageSubida.postValue(porcentage)

        if (porcentage==100) {
            doing = "Completado"
            count++
        }

        if (porcentage!=100 && count == 0) {
            doing = "Analizando"
        }

        if (porcentage!=100 && count > 0) {
            doing = "Subiendo"
        }

    }

    suspend fun uploadAppUpdateFile():String? {
       count=0

        return filesRepository.uploadAppUpdateFile(actualizacionFile.value!!,this@ActualizacionesViewModel,actualizacionFile.value!!.name )?.also {
            try {
                if (actualizacionFile.value!!.exists()) {
                    actualizacionFile.value!!.delete()
                }
            } catch (e:Exception) {
                println("Fail to delete $e")
            }
        }
    }

    val success = MutableLiveData<Boolean>()

    fun addAppUpdate(actualizacion: Actualizacion) {

        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)

            val actualizacionAdded = actualizacionRepository.addAppUpdate(actualizacion)
            if (actualizacionAdded != null) {
                getAllAppUpdates()
                success.postValue(true)
            } else {
                success.postValue(false)
            }

        }


    }


    fun deleteAppUpdateById(idActualizacion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            val actualizacionDeleted = actualizacionRepository.deleteAppUpdateById(idActualizacion)
            if (actualizacionDeleted == true) {
                getAllAppUpdates()
                success.postValue(true)
            } else {
                success.postValue(false)
            }


        }

    }


}