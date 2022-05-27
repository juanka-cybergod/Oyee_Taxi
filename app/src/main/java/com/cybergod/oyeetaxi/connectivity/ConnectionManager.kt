package com.cybergod.oyeetaxi.connectivity

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConnectionManager @Inject constructor(application: Application) {


    private val connectionLiveData = ConnectionLiveData(application)

    var isNetworkAvilableObserver = MutableLiveData<Boolean>()
    var isNetworkAvilable : Boolean = true


    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
         connectionLiveData.observe(lifecycleOwner) {
             isNetworkAvilableObserver.postValue(it)
             isNetworkAvilable = it

         }

        connectionLiveData.checkValidNetworks()
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner) {
            connectionLiveData.removeObservers(lifecycleOwner)
        }
    }

}

