package com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration

import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter.VehicleFilterOptions
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculosPaginados
import com.cybergod.oyeetaxi.api.futures.vehicle.repositories.VehicleRepository
import com.cybergod.oyeetaxi.api.futures.vehicle_type.repositories.VehicleTypeRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import com.cybergod.oyeetaxi.utils.Constants.VEHICLE_ANY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesAdministrationViewModel @Inject constructor(
    private val vehicleTypeRepository: VehicleTypeRepository,
    private val vehicleRepository: VehicleRepository,

    ) :  BaseViewModel() {

    var vehiclesList: MutableLiveData<List<VehiculoResponse>> = MutableLiveData()
    var vehiclesPaginadosResponse: MutableLiveData<VehiculosPaginados?> = MutableLiveData<VehiculosPaginados?>()

    var vehicleFilterOptions  = VehicleFilterOptions()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    var totalPages: Int = 0
    var getPage = 1
    var isLastPage = false
    var isScrolling = false

    val vehicleUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    init {
        getVehiclesPaginated()
    }

    fun getVehiclesPaginated(){

            isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {

                delay(1000)

                val response: VehiculosPaginados? = vehicleRepository.searchVehiclesPaginatedWithFilter(
                    page = getPage,
                    vehicleFilterOptions = vehicleFilterOptions,
                )

                vehiclesPaginadosResponse.postValue(response)

                if (response != null) {
                    totalPages = response.totalPages
                    isLastPage = response.last

                    val listaVehiculosObtenidos = response.content


                    if (getPage == 1 ) {
                        vehiclesList.postValue(listaVehiculosObtenidos)
                    } else {
                        val listaVehiculosAnteriores = vehiclesList.value?.toMutableList()
                        listaVehiculosAnteriores?.addAll(listaVehiculosObtenidos ?: emptyList())
                        vehiclesList.postValue(listaVehiculosAnteriores?.toList())
                    }

                    if (listaVehiculosObtenidos.isNotEmpty()) {
                        getPage++
                    }
                }



                isLoading.postValue(false)

            }




    }


    fun updateVehicleAndGetVehicleResponse(vehiculo: Vehiculo){


        viewModelScope.launch(Dispatchers.IO) {

            delay(1000)

            val vehicleResponseUpdated : VehiculoResponse? = vehicleRepository.updateVehicleAndGetVehicleResponse(vehiculo)
            if (vehicleResponseUpdated != null) {
                vehicleUpdatedSusses.postValue(true)
                updateVehicleResponseList(vehicleResponseUpdated)
            }else {
                vehicleUpdatedSusses.postValue(false)
            }


        }

    }


    private fun updateVehicleResponseList(vehicleResponseUpdated: VehiculoResponse) {

        vehiclesList.value?.toMutableList()?.let { listaVehiculosResponse ->
            val nuevaListaVehiculoResponse = ArrayList<VehiculoResponse>()
            listaVehiculosResponse.forEach { vehiculoResponse ->
                if (vehiculoResponse.id.equals(vehicleResponseUpdated.id)) {
                    nuevaListaVehiculoResponse.add(vehicleResponseUpdated)
                } else {
                    nuevaListaVehiculoResponse.add(vehiculoResponse)
                }
            }
            vehiclesList.postValue(nuevaListaVehiculoResponse)
        }

    }



    /////////////////////////////////////////////////////////////////

    val vehicleTypesItems: MutableList<String> = getVehicleTypesList()
    lateinit var arrayAdapter : ArrayAdapter<String>

    private fun getVehicleTypesList(): MutableList<String>{
        val stringArray = mutableListOf<String>(VEHICLE_ANY_TYPE)

        viewModelScope.launch(Dispatchers.IO) {
            vehicleTypeRepository.getAllVehicleTypes()?.let { listaTipoVehiculos ->
                listaTipoVehiculos.forEach { tipoVehiculo ->
                    tipoVehiculo.tipoVehiculo?.let {
                        stringArray.add(it)
                    }

                }
            }
        }

        return  stringArray
    }

}