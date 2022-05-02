package com.cybergod.oyeetaxi.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import com.cybergod.oyeetaxi.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory


class ConnectionLiveData(context: Context) : LiveData<Boolean>()   {

    val TAG_Class_Name = "ConnectionLiveData"

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NET_CAPABILITY_INTERNET)
        .build()

    private val validNetwork : MutableSet<Network> = HashSet()

    init {
        postValue(true)
    }


    fun checkValidNetworks():Boolean{

        Log.d(TAG_Class_Name,"ValidNetworks = ${validNetwork.size}")

        if (validNetwork.size > 0) {
            postValue(true) //Significa que al menos 1 de las redes Tiene Internet
            return true
        } else {
            Log.d(TAG_Class_Name,"No hay Redes Disponibles con Internet")
            postValue(false) //Significa que no hay redes o que ninguna Tiene Internet
            return false
        }


    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()

        networkCallback = createNetworkCallback()
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)
    }


    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


    private fun createNetworkCallback()  = object  : ConnectivityManager.NetworkCallback() {



        @SuppressLint("MissingPermission")
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG_Class_Name,"onAvailable network = $network")

            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            if (hasInternetCapability == true) {
                CoroutineScope(Dispatchers.IO).launch {

                    if ( doesNetworHasInternet(network.socketFactory) ) {

                        withContext(Dispatchers.Main){

                            Log.d(TAG_Class_Name,"La red $network Tiene INTERNET")
                            validNetwork.add(network)
                            checkValidNetworks()

                        }

                    } else {
                        Log.d(TAG_Class_Name,"La red $network NO Tiene INTERNET")
                        checkValidNetworks()
                    }



                }
            }

        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(TAG_Class_Name,"onLost network = $network")
            validNetwork.remove(network)
            checkValidNetworks()
        }


    }



    fun doesNetworHasInternet(socketFactory: SocketFactory) :Boolean {
        var internet :Boolean

        try {
            Log.d(TAG_Class_Name,"Ping to ${Constants.URL_IP}") //Google para internet Real es 8.8.8.8 port 53
            val socket = socketFactory.createSocket()
            //val socket = Socket()
            socket.connect(InetSocketAddress(Constants.URL_IP, Constants.URL_PORT),1500)
            socket.close()
            Log.d(TAG_Class_Name,"Ping Success")
            internet = true

        } catch (e: IOException){
            Log.d(TAG_Class_Name,"doesNetworHasInternet = Exception: $e")
            internet = false
        }

        //cm.unregisterNetworkCallback(networkCallback)

        Log.d(TAG_Class_Name,"doesNetworHasInternet = $internet")
        return internet

    }




}