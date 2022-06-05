package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.province.repositories.ProvincesRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvincesAdministrationViewModel @Inject constructor(
    private val provincesRepository: ProvincesRepository
) : BaseViewModel() {


    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var provincesList: MutableLiveData<List<Provincia>> = MutableLiveData()
    var provincesAddedOrUpdated: MutableLiveData<Provincia?> = MutableLiveData()

    init {
        getAllProvinces()
    }

    fun getAllProvinces() {

        isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            provincesList.postValue(
                provincesRepository.getAllProvinces()
            )

            isLoading.postValue(false)

        }


    }

    private suspend fun updateProvince(provincia: Provincia): Provincia? {

        val updatedProvince = provincesRepository.updateProvince(provincia)
        if (updatedProvince != null) {
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


    suspend fun setProvinceVisibility(nombreProvincia: String, visible: Boolean): Provincia? {
        return updateProvince(
            Provincia(
                nombre = nombreProvincia,
                visible = visible
            )
        )
    }


    fun setProvinceLocation(provincia: Provincia) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(500L)

            provincesAddedOrUpdated.postValue(
                updateProvince(
                    provincia
                )
            )
        }
    }

    fun addProvince(provincia: Provincia) {
        viewModelScope.launch(Dispatchers.IO) {

            val addedProvince = provincesRepository.addProvince(provincia)
            provincesAddedOrUpdated.postValue(addedProvince)
            addedProvince?.let {
                getAllProvinces()
            }
        }
    }

}