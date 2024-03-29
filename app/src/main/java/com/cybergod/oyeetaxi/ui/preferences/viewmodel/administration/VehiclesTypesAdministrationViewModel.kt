package com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle_type.repositories.VehicleTypeRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesTypesAdministrationViewModel @Inject constructor(
    private val vehicleTypeRepository: VehicleTypeRepository
) : BaseViewModel() {

    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var vehiclesTypesList: MutableLiveData<List<TipoVehiculo>> = MutableLiveData()
    var vehicleTypeAddedOrUpdated: MutableLiveData<TipoVehiculo?> = MutableLiveData()


    init {
        getAllVehicleTypes()

    }

    private fun getAllVehicleTypes() {


        isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            vehiclesTypesList.postValue(
                vehicleTypeRepository.getAllVehicleTypes()
            )

            isLoading.postValue(false)
        }


    }

    private suspend fun updateVehicleType(tipoVehiculo: TipoVehiculo): TipoVehiculo? {

        val updatedVehicleType = vehicleTypeRepository.updateVehicleType(tipoVehiculo)
        if (updatedVehicleType != null) {
            updateVehiclesTypesList(updatedVehicleType)
        }
        return updatedVehicleType

    }

    private fun updateVehiclesTypesList(updatedVehicleType: TipoVehiculo) {

        vehiclesTypesList.value?.toMutableList()?.let { listaTiposVehiculos ->
            val nuevaListaTiposVehiculos = ArrayList<TipoVehiculo>()
            listaTiposVehiculos.forEach { tipoVehiculo ->
                if (tipoVehiculo.tipoVehiculo.equals(updatedVehicleType.tipoVehiculo)) {
                    nuevaListaTiposVehiculos.add(updatedVehicleType)
                } else {
                    nuevaListaTiposVehiculos.add(tipoVehiculo)
                }
            }
            vehiclesTypesList.postValue(nuevaListaTiposVehiculos)
        }

    }


    fun updateThisVehicleType(tipoVehiculo: TipoVehiculo) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000L)
            vehicleTypeAddedOrUpdated.postValue(
                updateVehicleType(
                    tipoVehiculo
                )
            )
        }
    }


//    fun addVehicleType(provincia: Provincia) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            delay(1000L)
//
//            val addedProvince = vehicleTypeRepository.addProvince(provincia)
//            vehicleTypeAddedOrUpdated.postValue(addedProvince)
//            addedProvince?.let {
//                getAllVehicleTypes()
//            }
//        }
//    }

}