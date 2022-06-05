package com.cybergod.oyeetaxi.ui.controlPanel.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.file.repositories.FilesRepository
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.vahicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vahicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vahicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.api.futures.file.request_body.UploadRequestBody
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject



@HiltViewModel
open class VehicleControlPanelViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val filesRepository: FilesRepository,

    ) : BaseViewModel(), UploadRequestBody.UploadCallback {

    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val vehicleUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    var imagenFrontalVehiculoURL : MutableLiveData<String> = MutableLiveData<String>()


    var vehicleSelectedToChangeImagen : VehiculoResponse? = null

    var imagenCirculacionURL : MutableLiveData<String> = MutableLiveData<String>()
    var imagenCirculacionURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)
    var imagenCirculacionFile : MutableLiveData<File?> = MutableLiveData<File?>( null)

    var vehiclesList: MutableLiveData<List<VehiculoResponse>> = MutableLiveData()

    var rememberListExpanded: HashMap<String, Int> = HashMap()

    init {
        getAllVehicleFromUserId()
    }


    fun getAllVehicleFromUserId(){

        isLoading.postValue(true)

        viewModelScope.launch {

            delay(500)

            vehiclesList.postValue(
                vehicleRepository.getAllVehiclesFromUserId((currentUserActive.value?.id.orEmpty()))
            )

            isLoading.postValue(false)
        }

    }


    fun updateVehicleById(vehiculo: Vehiculo, vehiculoId:String){

        vehiculo.id= vehiculoId

        viewModelScope.launch(Dispatchers.IO) {
            vehicleUpdatedSusses.postValue(
                vehicleRepository.updateVehicle(vehiculo)
            )
        }

    }



    fun uploadFile(file: File?, id:String, fileType: TipoFichero){


        file?.let { fileToUpload ->

            viewModelScope.launch(Dispatchers.IO) {

                if (fileType == TipoFichero.VEHICULO_FRONTAL) {
                    imagenFrontalVehiculoURL.postValue(
                        filesRepository.uploadSingleFile(
                            file = fileToUpload,
                            context = this@VehicleControlPanelViewModel,
                            id = id,
                            tipoFichero = fileType,
                        )
                    )

                }
                if (fileType == TipoFichero.VEHICULO_CIRCULACION) {
                    imagenCirculacionURL.postValue(
                        filesRepository.uploadSingleFile(
                            file = fileToUpload,
                            context = this@VehicleControlPanelViewModel,
                            id = id,
                            tipoFichero = fileType,
                        )
                    )

                }

            }







        }

    }





    fun activeVehicleToUserId(userId: String, vehicleId:String,){

        viewModelScope.launch(Dispatchers.IO) {
            vehicleUpdatedSusses.postValue(
                vehicleRepository.setActiveVehicleToUserId(userId,vehicleId)
            )
        }

    }

    override fun onProggressUpdate(porcentage: Int) {

    }



}
