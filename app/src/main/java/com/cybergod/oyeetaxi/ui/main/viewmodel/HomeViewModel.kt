package com.cybergod.oyeetaxi.ui.main.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.repository.VehicleRepository
import com.cybergod.oyeetaxi.maps.Utils.locationToUbicacion
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
            this.ubicacion = locationToUbicacion(location)
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
                    vehicleList.postValue(
                        repository.getAvailableVehicles()
                    )


            }

        }




}