package com.cybergod.oyeetaxi.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.api.repository.VehicleRepository
import com.cybergod.oyeetaxi.connectivity.ConnectionManager
import com.cybergod.oyeetaxi.utils.GlobalVariables.NetworkErrorFound
import com.cybergod.oyeetaxi.utils.GlobalVariables.NetworkErrorMessaje
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.isServerAvailable
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch


import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var vehicleRepo: VehicleRepository

    //Desde ConnectivityManager



    fun registerConectionsObservers(lifecycleOwner: LifecycleOwner,context:Context){

        //Observar el Estado de la Conexion
        connectionManager.isNetworkAvilableObserver.observe(lifecycleOwner, Observer { networkAviable ->
            if (!networkAviable) {
                NetworkErrorFound.postValue(true)
                NetworkErrorMessaje = context.getString(R.string.fail_internet_conection)
            } else {
                NetworkErrorFound.postValue(false)
            }
        })

        isServerAvailable.observe(lifecycleOwner, Observer { serverAviable ->
            if (!serverAviable) {
                NetworkErrorFound.postValue(true)
                NetworkErrorMessaje = context.getString(R.string.fail_server_conection)
             } else {
                NetworkErrorFound.postValue(false)
            }


        })

    }



    fun updateCurrentUserAndActiveVehicleGlobalVariables(){

        viewModelScope.launch(Dispatchers.IO) {

            val currentUser = userRepo.getUserById(currentUserActive.value?.id!!)
            currentUser?.let {
                currentUserActive.postValue(it)
            }

        }

        viewModelScope.launch(Dispatchers.IO) {

            val activeVehicle = vehicleRepo.getActiveVehicleByUserId(currentUserActive.value?.id!!)
            activeVehicle?.let {
                currentVehicleActive.postValue(it)
            }

        }


    }


}