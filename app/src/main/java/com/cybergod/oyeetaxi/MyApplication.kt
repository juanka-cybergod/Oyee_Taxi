package com.cybergod.oyeetaxi

import android.app.Application
import android.content.Context
//import com.cybergod.oyeetaxi.di.NetworkModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {



    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

//
//    override fun onCreate() {
//        super.onCreate()
//        DataStoreModule.getPreferenceDataStoreInstance(applicationContext)
//        //NetworkModule.registerNetworkCallback(applicationContext)
//    }
//
//    override fun onTerminate() {
//        super.onTerminate()
//        //De Registrar el Modulo de Retrofit para Monitorear la red
//        NetworkModule.unregisterNetworkCallback()
//    }







}