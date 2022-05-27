package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.province.repositories.ProvincesRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvincesViewModel @Inject constructor(private val repository: ProvincesRepository) :  BaseViewModel() {

    var provincesList: MutableLiveData<List<Provincia>> = MutableLiveData()


    fun getAllProvinces(){

            viewModelScope.launch(Dispatchers.IO) {
                provincesList.postValue(
                    repository.getAvailableProvinces()
                )


        }


    }








}