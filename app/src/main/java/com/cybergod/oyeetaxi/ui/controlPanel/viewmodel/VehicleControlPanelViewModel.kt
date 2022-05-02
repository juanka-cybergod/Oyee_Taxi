package com.cybergod.oyeetaxi.ui.controlPanel.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.Vehiculo
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.repository.*
import com.cybergod.oyeetaxi.api.utils.UploadRequestBody
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject



@HiltViewModel
open class VehicleControlPanelViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val filesRepository: FilesRepository,

) : BaseViewModel(), UploadRequestBody.UploadCallback {


    val vehicleUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    var imagenFrontalVehiculoURL : MutableLiveData<String> = MutableLiveData<String>()
    //var imagenFrontalCurrentVehiculoURL : MutableLiveData<String> = MutableLiveData<String>()


    var vehicleSelectedToChangeImagen : VehiculoResponse? = null

    var imagenCirculacionURL : MutableLiveData<String> = MutableLiveData<String>()
    var imagenCirculacionURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)
    var imagenCirculacionFile : MutableLiveData<File?> = MutableLiveData<File?>( null)

    var vehiclesList: MutableLiveData<List<VehiculoResponse>> = MutableLiveData()

    var rememberListExpanded: HashMap<String, Int> = HashMap()



    fun updateVehicleById(vehiculo: Vehiculo,vehiculoId:String){

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


//    fun uploadFileImagenFrontalCurrentVehiculo(file: File?, id:String, fileType: TipoFichero){
//
//
//        file?.let { fileToUpload ->
//
//                filesRepository.uploadSingleFile(
//                    file = fileToUpload,
//                    URL = imagenFrontalCurrentVehiculoURL,
//                    context = this,
//                    id = id,
//                    tipoFichero = fileType,
//                    created = nothing
//                )
//
//
//
//
//
//        }
//
//    }



    fun getAllVehicleFromUserId(userId:String){

        viewModelScope.launch {
            vehiclesList.postValue(
                vehicleRepository.getAllVehiclesFromUserId(userId)
            )
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
