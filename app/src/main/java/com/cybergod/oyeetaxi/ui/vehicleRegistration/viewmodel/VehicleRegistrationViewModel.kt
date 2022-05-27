package com.cybergod.oyeetaxi.ui.vehicleRegistration.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.futures.vahicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vahicle.model.verification.VehiculoVerificacion
import com.cybergod.oyeetaxi.api.futures.file.repositories.FilesRepository
import com.cybergod.oyeetaxi.api.futures.file.request_body.UploadRequestBody
import com.cybergod.oyeetaxi.api.futures.vahicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getRamdomUUID
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
class VehicleRegistrationViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val filesRepository: FilesRepository,
    ) : BaseViewModel(), UploadRequestBody.UploadCallback {



    val idVehiculo = getRamdomUUID()
    var idUsuario = GlobalVariables.currentUserActive.value?.id.orEmpty()

    var imagenFrontalPublicaURL : String? = null
    var imagenCirculacionURL: String? = null

    var imagenFrontalVehiculoFile : MutableLiveData<File?> = MutableLiveData<File?>( null)
    var imagenFrontalVehiculoURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)

    var tipoVehiculo : MutableLiveData<TipoVehiculo?> = MutableLiveData<TipoVehiculo?>( null)
    var marca : MutableLiveData<String> = MutableLiveData<String>()
    var modelo : MutableLiveData<String> = MutableLiveData<String>()
    var ano : MutableLiveData<String> = MutableLiveData<String>()

    var capacidadPasajeros: MutableLiveData<String> = MutableLiveData<String>()
    var capacidadEquipaje: MutableLiveData<String> = MutableLiveData<String>()
    var capacidadCarga: MutableLiveData<String> = MutableLiveData<String>()
    var climatizado: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    var matricula: MutableLiveData<String> = MutableLiveData<String>()
    var circulacion: MutableLiveData<String> = MutableLiveData<String>()


    var imagenCirculacionFile : MutableLiveData<File?> = MutableLiveData<File?>( null)
    var imagenCirculacionURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)


    suspend fun addNewVehicle():Boolean? {

        imagenFrontalPublicaURL = uploadSingleFile(imagenFrontalVehiculoFile.value,idVehiculo,
            TipoFichero.VEHICULO_FRONTAL)
        imagenCirculacionURL = uploadSingleFile(imagenCirculacionFile.value,idVehiculo, TipoFichero.VEHICULO_CIRCULACION)

        return if (!imagenFrontalPublicaURL.isNullOrEmpty())   {

            if (tipoVehiculo.value?.requiereVerification == true &&  imagenCirculacionURL.isNullOrEmpty()) {
                return null
            }
            vehicleRepository.addVehicle(getVehicleReady())

        } else  null


    }


    private suspend fun uploadSingleFile(file: File?,id:String,tipoFichero: TipoFichero):String?{
        return filesRepository.uploadSingleFile(
            file = file,
            context = this,
            id = id,
            tipoFichero = tipoFichero,
        )
    }



    private fun getVehicleReady(): Vehiculo {


        return Vehiculo(
            id = idVehiculo,
            idUsuario = idUsuario,
            tipoVehiculo = tipoVehiculo.value?.tipoVehiculo.orEmpty() ,
            marca = marca.value.orEmpty(),
            modelo = modelo.value.orEmpty(),
            ano = ano.value.orEmpty(),
            capacidadPasajeros = capacidadPasajeros.value.orEmpty(),
            capacidadEquipaje = capacidadEquipaje.value.orEmpty(),
            capacidadCarga = capacidadCarga.value.orEmpty(),
            activo = false,
            visible = true,
            habilitado = true,
            disponible = true,
            climatizado = climatizado.value ?: false,
            fechaDeRegistro = null,
            imagenFrontalPublicaURL = imagenFrontalPublicaURL.orEmpty(),
            vehiculoVerificacion = VehiculoVerificacion(
                verificado = false,
                matricula = matricula.value.orEmpty(),
                circulacion = circulacion.value.orEmpty(),
                imagenCirculacionURL = imagenCirculacionURL.orEmpty()
            ),

            )
    }

    override fun onProggressUpdate(porcentage: Int) {

    }




}