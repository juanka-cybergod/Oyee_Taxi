package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.travel.model.MetodoPago
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle_type.repositories.VehicleTypeRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViajeViewModel  @Inject constructor(private val repository: VehicleTypeRepository) :  BaseViewModel() {



    var tipoVehiculoList: MutableLiveData<List<TipoVehiculo>> = MutableLiveData()

    val tiposVehiculoSeleccionados: MutableLiveData<HashMap<String, TipoVehiculo>> = MutableLiveData(HashMap())

    var viajeAgendado: MutableLiveData<String> = MutableLiveData(null)

    var metodoPago: MutableLiveData<MetodoPago> = MutableLiveData(MetodoPago.EFECTIVO)

    var requiereClimatizado: MutableLiveData<Boolean> = MutableLiveData(false)

    var viajeSoloIda: MutableLiveData<Boolean> = MutableLiveData(true)

    var fecha : MutableLiveData<String> = MutableLiveData(null)
    var hora : MutableLiveData<String> = MutableLiveData(null)

    var cantidadPasajeros : MutableLiveData<Int> = MutableLiveData(1)

    var detallesAdicionales: MutableLiveData<String> = MutableLiveData(null)

    var pesoEquipaje: MutableLiveData<String> = MutableLiveData(null)


    fun getAllVehicleTypes(){

        viewModelScope.launch(Dispatchers.IO) {
            tipoVehiculoList.postValue(
                repository.getAllVehicleTypes()
            )

        }


    }








}