package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle_type.repositories.VehicleTypeRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleTypeViewModel @Inject constructor(private val vehicleTypeRepository: VehicleTypeRepository) :  BaseViewModel() {

    var tipoVehiculoList: MutableLiveData<List<TipoVehiculo>> = MutableLiveData()


    fun getAvailableVehiclesType(){

            viewModelScope.launch {
                tipoVehiculoList.postValue(
                    vehicleTypeRepository.getAvailableVehiclesType()
                )


        }


    }








}