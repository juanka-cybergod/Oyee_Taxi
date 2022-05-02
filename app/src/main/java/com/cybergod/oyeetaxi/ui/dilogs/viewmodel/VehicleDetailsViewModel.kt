package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Vehiculo
import com.cybergod.oyeetaxi.api.repository.VehicleRepository
import com.cybergod.oyeetaxi.data_storage.ProtoDataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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