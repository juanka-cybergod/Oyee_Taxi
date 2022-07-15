package com.cybergod.oyeetaxi.ui.main.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.maps.Utils.toUbicacion
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: VehicleRepository,
    //private val protoDataStorageRepository: ProtoDataStorageRepository,

    ) : BaseViewModel() {


    fun updateCurrentUserLocation(location:Location){
        currentUserActive.value?.apply {
            this.ubicacion = location.toUbicacion()
        }
    }




    //Variables para Controlar la Actualizacion de los Vehiculos en el Mapa
    var stopMapVehicleUpdate : Boolean = false
    var coroutine : CoroutineScope = CoroutineScope(Dispatchers.IO)


    var isVehiclesAviables: MutableLiveData<Boolean> = MutableLiveData(true)

    var goToProvince:MutableLiveData<Provincia> = MutableLiveData()

    var vehicleList: MutableLiveData<List<VehiculoResponse>> = MutableLiveData()

    //variable para que no se vuelva a ir a la Provincia al entrar al fragment
    var aleadyRuned: Boolean = false



        fun getAvailableVehicles(){

            viewModelScope.launch {
                repository.getAvailableVehicles()?.let {
                    vehicleList.postValue(it)
                }
            }

        }




}