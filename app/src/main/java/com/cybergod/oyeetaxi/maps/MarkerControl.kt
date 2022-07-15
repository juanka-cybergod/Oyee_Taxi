package com.cybergod.oyeetaxi.maps

import android.location.Location
import android.os.Handler
import android.os.SystemClock
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.maps.Constants.USER_LOCATION
import com.cybergod.oyeetaxi.maps.Utils.getCircle
import com.cybergod.oyeetaxi.maps.Utils.getIcoFromResource
import com.cybergod.oyeetaxi.maps.Utils.toLatLng
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.DELAY_VEHICLES_UPDATE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkers
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkersObservable
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationCircle
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationMarker
import com.cybergod.oyeetaxi.utils.UtilsGlobal.logGlobal
import com.google.android.gms.maps.model.*


class MarkerControl {

    private val className = this.javaClass.simpleName?: Constants.UNKNOWN_CLASS


    fun addUserLocationMarker(location : Location){
        val latLng = LatLng(location.latitude,location.longitude)

        val markerOptions = MarkerOptions()
            .anchor(0.5f,0.5f)
            .position(latLng)
            .icon(getIcoFromResource(currentVehicleActive.value?.tipoVehiculo?.tipoVehiculo,currentUserActive.value?.modoCondutor))
            .rotation(location.bearing)
            .title( "Auto Móvil")
            .snippet("snippet")

        val marker :Marker
        map.addMarker(markerOptions).also {
            it?.tag = USER_LOCATION
            marker = it!!
        }

        userLocationMarker = marker



        val circle :Circle
        map.addCircle(getCircle(location)).also {
            circle = it
        }

        userLocationCircle = circle

    }

    fun updateUserLocationMarker( location : Location){

        val latLng = LatLng(location.latitude,location.longitude)

        if (userLocationMarker != null) {

            userLocationMarker?.apply {
                setAnchor(0.5f,0.5f)
                position = latLng
                setIcon(getIcoFromResource(currentVehicleActive.value?.tipoVehiculo?.tipoVehiculo,currentUserActive.value?.modoCondutor))
                rotation = location.bearing
                title = ""
                snippet = ""

                logGlobal(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    logText = "$USER_LOCATION -> $position ($rotation)"
                )
            }

            userLocationCircle?.apply {
                center = latLng
                radius = location.accuracy.toDouble()
            }

        } else {
            addUserLocationMarker(location)
        }

    }


    fun removeUnavailableVehiclesFromMapMarker(listVehiculoResponse: List<VehiculoResponse>){

//        logGlobal(
//            className = className,
//            metodo = object {}.javaClass.enclosingMethod!!,
//            logText = hashMapMarkers.value?.keys?.toList().toString()
//        )

        hashMapMarkers.keys.toList().forEach { idMarcador ->
            val vehiculoEncontrado =  listVehiculoResponse.find { item -> item.id.equals(idMarcador,true) }

            if ( vehiculoEncontrado== null) {
                val marker: Marker = hashMapMarkers[idMarcador]!!
                marker.remove()
                hashMapMarkers.remove(idMarcador)

                if (hashMapMarkers[idMarcador]?.position != hashMapMarkersObservable.value?.get(idMarcador)?.position) {
                    hashMapMarkersObservable.postValue(hashMapMarkers)
                }


                logGlobal(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    logText = "Eliminado $idMarcador"
                )

            }

        }



    }

    fun addVehicleMarker(vehiculoResponse: VehiculoResponse){

        val markerOptions = MarkerOptions()
            .anchor(0.5f,0.5f)
            .position(vehiculoResponse.usuario?.ubicacion.toLatLng())
            .icon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
            .rotation(vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0F)
            .title(vehiculoResponse.tipoVehiculo?.tipoVehiculo ?: "")
            .snippet("snippet")

        map.addMarker(markerOptions)?.also { marker ->
            marker.tag = vehiculoResponse.id
            hashMapMarkers[vehiculoResponse.id!!] = marker

            if (hashMapMarkers[vehiculoResponse.id!!]?.position != hashMapMarkersObservable.value?.get(vehiculoResponse.id!!)?.position) {
                hashMapMarkersObservable.postValue(hashMapMarkers)
            }
        }





    }

    fun updateVehicleMarker(vehiculoResponse: VehiculoResponse){

        hashMapMarkers[vehiculoResponse.id]?.apply {
//            setAnchor(0.5f,0.5f)
//            setIcon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
//            position = ubicacionToLatLng(vehiculoResponse.usuario?.ubicacion)
//            rotation = vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0f

            if (vehiculoResponse.id=="id_van")
            logGlobal(
                className = className,
                metodo = object {}.javaClass.enclosingMethod!!,
                logText = "${vehiculoResponse.id} -> $position ($rotation)"
            )


            this.changePositionSmoothly(vehiculoResponse)


        }





    }


    fun Marker.changePositionSmoothly(vehiculoResponse: VehiculoResponse) {
        val startPosition:LatLng = this.position
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = DELAY_VEHICLES_UPDATE.toFloat()
        val hideMarker = false
        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {
                this@changePositionSmoothly.rotation = vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: this@changePositionSmoothly.rotation
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                val currentPosition = LatLng(
                    startPosition.latitude * (1 - t) + vehiculoResponse.usuario?.ubicacion?.latitud!! * t,
                    startPosition.longitude * (1 - t) + vehiculoResponse.usuario?.ubicacion?.longitud!! * t
                )
                this@changePositionSmoothly.position = currentPosition

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    this@changePositionSmoothly.isVisible = !hideMarker
                }


                hashMapMarkersObservable.postValue(hashMapMarkers)



            }
        })
    }




//
//    fun addVehicleMarker(vehiculoResponse: VehiculoResponse){
//
//        val markerOptions = MarkerOptions()
//            .anchor(0.5f,0.5f)
//            .position(ubicacionToLatLng(vehiculoResponse.usuario?.ubicacion))
//            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //Cambar el Color al Marcador
//            .icon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
//            .rotation(vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0F)
//            .title(vehiculoResponse.tipoVehiculo?.tipoVehiculo ?: "")
//            .snippet("snippet")
//
//        val marker :Marker
//        map.addMarker(markerOptions).also {
//            it?.tag = vehiculoResponse.id
//            marker = it!!
//
//        }
//
//
//        hashMapMarkers.value?.put(vehiculoResponse.id!!,marker)
//
//    }
//
//    fun updateVehicleMarker(vehiculoResponse: VehiculoResponse){
//        val marker: Marker = hashMapMarkers.value?.get(vehiculoResponse.id)!!
//
//        marker.setAnchor(0.5f,0.5f)
//        marker.position = ubicacionToLatLng(vehiculoResponse.usuario?.ubicacion)
//        marker.setIcon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
//        marker.rotation = vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0f
//    }


//    fun addVehicleMarkerOld(title:String,map: GoogleMap,vehiculo: VehiculoResponse,activity: Activity){
//
//        val location: LatLng = vehicleToLatLng(vehiculo)
//
//        map.addMarker(
//            MarkerOptions()
//                .anchor(0.5f,0.5f)
//                .position(location)
//                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //Cambar el Color al Marcador
////            .icon(Utils().fromVectorToBitmapDescriptor(
////                activity,
////                R.drawable.ic_taxi,
////                Color.parseColor("#FF5722")))
//                .icon(Utils().getIcoFromVehicle(vehiculo,activity))
//                .rotation(vehiculo.ubicacion?.rotacion ?: 0f)
//                .title(title))
//            ?.tag = vehiculo.id
//
//    }




//    fun addCityMarker(cityName:String,map: GoogleMap,markerLocation: LatLng,activity: Activity){
//
//        map.addMarker(
//            MarkerOptions()
//                .position(markerLocation)
//                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //Cambar el Color al Marcador
//                .icon(Utils().fromVectorToBitmapDescriptor(activity, R.drawable.ic_city_32, Color.parseColor("#FF3700B3")))
//                .title(cityName)
//                .snippet("Provincia ${cityName}")
//
//
//        )
//            ?.tag ="TAG_PROVINCE"
//
//    }

    fun addDefaultMarker(title:String,markerLocation: LatLng){
       // map.addMarker(MarkerOptions().position(santa_clara).title("Marcador en Santa Clara"))
        map.addMarker(
            MarkerOptions()
                .position(markerLocation)
                //.icon(getIcoFromResource("",false))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) //Cambar el Color al Marcador
                //.icon(Utils().fromVectorToBitmap(activity, R.drawable.ic_city_32, Color.parseColor("#FF3700B3")))
                .title(title))
            ?.tag =title
    }

//    fun removeUserLocationMarker(homeViewModel:HomeViewModel){
//
//        if (homeViewModel.userLocationMarker != null) {
//            homeViewModel.userLocationMarker?.remove()
//        }
//
//
//    }


}