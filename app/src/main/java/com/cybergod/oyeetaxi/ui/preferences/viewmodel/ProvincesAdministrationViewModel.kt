package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.repository.ProvincesRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvincesAdministrationViewModel @Inject constructor(
    private val provincesRepository: ProvincesRepository
    ) :  BaseViewModel() {

    var provincesList: MutableLiveData<List<Provincia>> = MutableLiveData()
    var provincesAddedOrUpdated: MutableLiveData<Provincia?> = MutableLiveData()

    fun getAllProvinces(){

            viewModelScope.launch(Dispatchers.IO) {
                provincesList.postValue(
                    provincesRepository.getAllProvinces()
                )


        }


    }

    private suspend fun updateProvince(provincia: Provincia):Provincia?{

        val updatedProvince = provincesRepository.updateProvince(provincia)
        if (updatedProvince!=null) {
            updateProvincesList(updatedProvince)
        }
        return updatedProvince

    }

    private fun updateProvincesList(updatedProvince: Provincia) {

        provincesList.value?.toMutableList()?.let { listaProvincia ->
            val nuevaListaProvinces = ArrayList<Provincia>()
            listaProvincia.forEach { provincia ->
                if (provincia.nombre.equals(updatedProvince.nombre)) {
                    nuevaListaProvinces.add(updatedProvince)
                } else {
                    nuevaListaProvinces.add(provincia)
                }
            }
            provincesList.postValue(nuevaListaProvinces)
        }

    }


    suspend fun setProvinceVisibility(nombreProvincia: String,visible:Boolean) :Provincia?{
        return updateProvince(
            Provincia(
                nombre = nombreProvincia,
                visible = visible
            )
        )
    }



    fun setProvinceLocation(provincia: Provincia) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000L)

            provincesAddedOrUpdated.postValue(
                updateProvince(
                    provincia
                )
            )
        }
    }

    fun addProvince(provincia: Provincia) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000L)

            val addedProvince = provincesRepository.addProvince(provincia)
            provincesAddedOrUpdated.postValue(addedProvince)
            addedProvince?.let {
                getAllProvinces()
            }
        }
    }

}