package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersAdministrationViewModel @Inject constructor(
    private val UserRepository: UserRepository
    ) :  BaseViewModel() {


    var usersList: MutableLiveData<List<Usuario>?> = MutableLiveData()
    var totalPages: MutableLiveData<Int> = MutableLiveData()

    var getPage = 1
    var textSearch = ""

    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isLastPage = false
    var isScrolling = false


    init {
        getUsersPaginated()
    }

    fun getUsersPaginated(){

            isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {

                delay(1000)

                val listaUsuariosObtenidos = UserRepository.searchUsersPaginated(
                        nombre_apellidos_correo_telefono=textSearch,
                        page = getPage,
                        totalPages = totalPages
                    )
                    //.also {it.let {getPage++}}



                if (getPage == 1 ) {
                    usersList.postValue(listaUsuariosObtenidos)
                } else {
                    val listaUsuariosAnteriores = usersList.value?.toMutableList()
                    listaUsuariosAnteriores?.addAll(listaUsuariosObtenidos ?: emptyList())
                    usersList.postValue(listaUsuariosAnteriores?.toList())
                }

                if (!listaUsuariosObtenidos.isNullOrEmpty()) {
                    getPage++
                }

//                if (usersList.value == null || usersList.value?.isEmpty() == true ) {
//                    usersList.postValue(listaUsuariosObtenidos)
//                } else {
//                    val listaUsuariosAnteriores = usersList.value?.toMutableList()
//                    listaUsuariosAnteriores?.addAll(listaUsuariosObtenidos ?: emptyList())
//                    usersList.postValue(listaUsuariosAnteriores?.toList())
//                }

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