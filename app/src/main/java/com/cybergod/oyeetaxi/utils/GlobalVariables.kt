package com.cybergod.oyeetaxi.utils

import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker


object GlobalVariables {

    //TODO NETWORK
    var isServerAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var NetworkErrorFound:MutableLiveData<Boolean> = MutableLiveData()
    var NetworkErrorMessaje : String = ""

    //TODO Maps
    lateinit var map: GoogleMap

    //TODO Maps Markers
    var hashMapMarkers: MutableLiveData<HashMap<String, Marker>> = MutableLiveData(HashMap())

    //TODO Maps Objects

    var userLocationMarker : Marker? = null
    var userLocationCircle : Circle? = null

    //TODO CURRENT CACHE
    val currentUserActive: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
    val currentVehicleActive: MutableLiveData<VehiculoResponse> = MutableLiveData<VehiculoResponse>()
    var currentMapStyle: TypeAndStyle.MapStyle = TypeAndStyle.MapStyle.SILVER






}