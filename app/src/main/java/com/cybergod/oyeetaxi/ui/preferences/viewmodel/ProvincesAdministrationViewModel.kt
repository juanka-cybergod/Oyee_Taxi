package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.repository.ProvincesRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvincesAdministrationViewModel @Inject constructor(
    private val provincesRepository: ProvincesRepository
    ) :  BaseViewModel() {

    var provincesList: MutableLiveData<List<Provincia>> = MutableLiveData()


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
            listaProvincia.forEach { provincia ->
                val nuevaListaProvinces = ArrayList<Provincia>()
                if (provincia.nombre.equals(updatedProvince.nombre)) {
                    nuevaListaProvinces.add(updatedProvince)
                } else {
                    nuevaListaProvinces.add(provincia)
                }
                provincesList.postValue(nuevaListaProvinces.sortedBy { provincia.nombre  })
            }
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







}