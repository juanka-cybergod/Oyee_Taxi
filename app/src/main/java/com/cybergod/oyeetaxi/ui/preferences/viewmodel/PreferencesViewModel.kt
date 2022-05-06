package com.cybergod.oyeetaxi.ui.preferences.viewmodel


import android.widget.ArrayAdapter
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val dataStorageRepository: DataStorageRepository,
) : BaseViewModel() {


    val readMapStyle = dataStorageRepository.readMapStyle.asLiveData()


    lateinit var mapsStylesItems:Array<String>
    lateinit var arrayAdapter : ArrayAdapter<String>



    fun saveMapStyle(newStyle:TypeAndStyle.MapStyle){
        viewModelScope.launch (Dispatchers.Main){
            dataStorageRepository.saveMapStyle(newStyle)
        }

    }



}
