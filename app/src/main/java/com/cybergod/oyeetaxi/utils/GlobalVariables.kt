package com.cybergod.oyeetaxi.utils

import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker


object GlobalVariables {

    //NETWORK
    var isServerAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var NetworkErrorFound:MutableLiveData<Boolean> = MutableLiveData()
    var NetworkErrorMessaje : String = ""

    //Maps Objects
    lateinit var map: GoogleMap

    var hashMapMarkers: HashMap<String, Marker> = HashMap()
    var hashMapMarkersObservable: MutableLiveData<HashMap<String, Marker>> = MutableLiveData(HashMap())

    var userLocationMarker : Marker? = null
    var userLocationCircle : Circle? = null

    //CURRENT CACHE
    val currentUserActive: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
    val currentVehicleActive: MutableLiveData<VehiculoResponse> = MutableLiveData<VehiculoResponse>()
    var currentMapStyle: TypeAndStyle.MapStyle = TypeAndStyle.MapStyle.SILVER

    var hashMapMarkersChangingPosition: HashMap<Any?, Boolean> = HashMap()

}