package com.cybergod.oyeetaxi.ui.main.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.maps.Utils.toUbicacion
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.getAvailableVehicleInterval
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val dataStorageRepository: DataStorageRepository,
    //private val protoDataStorageRepository: ProtoDataStorageRepository,
    ) : BaseViewModel() {
    //var coroutine : CoroutineScope = CoroutineScope(Dispatchers.IO)
    var aleadyRuned: Boolean = false
    var goToProvince:MutableLiveData<Provincia> = MutableLiveData()
    var isVehiclesAviables: MutableLiveData<Boolean> = MutableLiveData(true)
    var vehicleList: MutableLiveData<List<VehiculoResponse>> = MutableLiveData()
    var gettingData = false
    var stopGettingAvailableVehicleToMap : Boolean = false
    var intervalTimerConfiguracion  = dataStorageRepository.readIntervalTimerConfiguracionFromDataStore.asLiveData()

    init {
        continuouslyUpdateVehiclesInTheMap()
    }

    private fun getServerData(){

        viewModelScope.launch {
            gettingData = true
            vehicleRepository.getData()?.let { dataResponse ->
                dataResponse.vehicleResponseList?.let { vehicleList.postValue(it) }
                dataResponse.intervalTimerConfiguracion?.let {
                    if (intervalTimerConfiguracion.value != it) {
                        dataStorageRepository.saveIntervalTimerConfiguracion(it)
                    }
                }

            }.also {
                gettingData = false
            }
        }

    }

    private fun continuouslyUpdateVehiclesInTheMap(){


        viewModelScope.launch(Dispatchers.IO) {

            while (true) {

                if (!stopGettingAvailableVehicleToMap && !gettingData) {
                    //getAvailableVehicles()
                    getServerData()
                }
                delay(getAvailableVehicleInterval * 1000L)
            }

        }
    }



    fun updateCurrentUserLocation(location:Location){
        currentUserActive.value?.apply {
            this.ubicacion = location.toUbicacion()
        }
    }







//    private fun getAvailableVehicles(){
//
//        viewModelScope.launch {
//            getting = true
//            repository.getAvailableVehicles()?.let {
//                vehicleList.postValue(it)
//            }.also {
//                getting = false
//            }
//        }
//
//    }








}