package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
        private val repository: VehicleRepository,
       // private val protoDataStorageRepository: ProtoDataStorageRepository,
        ) : BaseViewModel() {

       // val loginResponseFromProtoDataStorage = protoDataStorageRepository.readLoginRespuestaFromProtoDataStore.asLiveData()

        var vehicleDetails : MutableLiveData<Vehiculo> = MutableLiveData<Vehiculo>()

//        fun getVehicleById(vahicleId:String){
//
//                viewModelScope.launch {
//                    repository.getVehicleById(vahicleId,vehicleDetails)
//                }
//        }



}