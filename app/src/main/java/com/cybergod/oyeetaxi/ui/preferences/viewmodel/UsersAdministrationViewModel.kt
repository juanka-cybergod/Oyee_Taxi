package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersAdministrationViewModel @Inject constructor(
    private val UserRepository: UserRepository
    ) :  BaseViewModel() {

    var usersList: MutableLiveData<List<Usuario>> = MutableLiveData()
    var vehicleTypeAddedOrUpdated: MutableLiveData<Usuario?> = MutableLiveData()

    var usuariosCurrentPage = 1

    fun getUsersPaginated(text:String=""){

            viewModelScope.launch(Dispatchers.IO) {
                usersList.postValue(
                   // UserRepository.getAllUsers()
                    UserRepository.searchUsersPaginated(
                        nombre_apellidos_correo_telefono=text,
                        page = usuariosCurrentPage
                    )
                )


        }


    }
//
//    private suspend fun updateVehicleType(tipoVehiculo: TipoVehiculo):TipoVehiculo?{
//
//        val updatedVehicleType = vehicleTypeRepository.updateVehicleType(tipoVehiculo)
//        if (updatedVehicleType!=null) {
//            updateVehiclesTypesList(updatedVehicleType)
//        }
//        return updatedVehicleType
//
//    }
//
//    private fun updateVehiclesTypesList(updatedVehicleType: TipoVehiculo) {
//
//        vehiclesTypesList.value?.toMutableList()?.let { listaTiposVehiculos ->
//            val nuevaListaTiposVehiculos = ArrayList<TipoVehiculo>()
//            listaTiposVehiculos.forEach { tipoVehiculo ->
//                if (tipoVehiculo.tipoVehiculo.equals(updatedVehicleType.tipoVehiculo)) {
//                    nuevaListaTiposVehiculos.add(updatedVehicleType)
//                } else {
//                    nuevaListaTiposVehiculos.add(tipoVehiculo)
//                }
//            }
//            vehiclesTypesList.postValue(nuevaListaTiposVehiculos)
//        }
//
//    }
//
//
//
//    fun updateThisVehicleType(tipoVehiculo: TipoVehiculo) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            delay(1000L)
//
//            vehicleTypeAddedOrUpdated.postValue(
//                updateVehicleType(
//                    tipoVehiculo
//                )
//            )
//        }
//    }


}