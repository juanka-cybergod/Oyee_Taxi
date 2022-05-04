package com.cybergod.oyeetaxi.maps

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.BuildConfig.MAPS_API_KEY
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class PlacesControl  {

    private val cameraControl by lazy { CameraControl() }
    private val markerControl by lazy { MarkerControl() }

    private val TAG = "PlacesControl"

    private lateinit var placesClient:PlacesClient
    private lateinit var token : AutocompleteSessionToken
    private lateinit var findAutocompletePredictionsRequest: FindAutocompletePredictionsRequest.Builder
    lateinit var predictionListPlaces : List<AutocompletePrediction>
    val sugestionListPlaces : MutableLiveData<List<String>> = MutableLiveData<List<String>>(emptyList())

    fun Context.initializePlacesLocation(){

        Places.initialize(this,MAPS_API_KEY)
        placesClient = Places.createClient(this)
        token = AutocompleteSessionToken.newInstance()

        findAutocompletePredictionsRequest = FindAutocompletePredictionsRequest.builder()
        .setCountry("cu")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)

    }


    fun searchLocation(address:String, context: Context ){
        context.initializePlacesLocation()

        val request : FindAutocompletePredictionsRequest  = findAutocompletePredictionsRequest
            .setQuery(address)
            .build()

        placesClient.findAutocompletePredictions(request).addOnCompleteListener { task->

            try {
                Log.d(TAG,task.result.toString())
            } catch (e: Exception) {
                Log.e(TAG,"ERROR AL ACCEDER A LA API DE GOOGLE PALCES = $e")
            }


            if (task.isSuccessful) {
                val predictionsResponse:  FindAutocompletePredictionsResponse = task.result
                predictionsResponse.let {

                    predictionListPlaces = predictionsResponse.autocompletePredictions

                    val listSugestions : MutableList<String> = ArrayList()

                    predictionListPlaces.forEach {

                        Log.d(TAG,it.getFullText(null).toString())

                        listSugestions.add(it.getFullText(null).toString())
                    }

                    sugestionListPlaces.postValue(listSugestions)
                }
            }


        }

    }

    fun getLocationFromPlace(placeId: String){
        val placeFields : List<Place.Field> = listOf(Place.Field.LAT_LNG)
        val fetchPlaceRequest : FetchPlaceRequest = FetchPlaceRequest.builder(placeId,placeFields).build()
        placesClient.fetchPlace(fetchPlaceRequest)
            .addOnSuccessListener {  fetchPlaceResponse ->
                Log.d(TAG,"Place Found = $fetchPlaceResponse")

                fetchPlaceResponse.place.latLng?.let { location ->
                    //Añadir Marcador Mobible
                    markerControl.addDefaultMarker(""+fetchPlaceResponse.place.address,location)
                    //Ir a Lugar
                    cameraControl.animateCameraTo(location)
                }

        }
            .addOnFailureListener { e : Exception->
                Log.e(TAG,"ERROR AL ACCEDER A LA API DE GOOGLE PALCES = $e")
        }



    }





}


