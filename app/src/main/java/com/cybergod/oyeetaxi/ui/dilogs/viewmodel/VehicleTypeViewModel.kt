package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.repository.VehicleTypeRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleTypeViewModel @Inject constructor(private val vehicleTypeRepository: VehicleTypeRepository) :  BaseViewModel() {

    var tipoVehiculoList: MutableLiveData<List<TipoVehiculo>> = MutableLiveData()


    fun getAllVehicleTypes(){

            viewModelScope.launch {
                tipoVehiculoList.postValue(
                    vehicleTypeRepository.getAllVehicleTypes()
                )


        }


    }








}