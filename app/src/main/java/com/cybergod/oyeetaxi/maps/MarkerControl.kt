package com.cybergod.oyeetaxi.maps

import android.location.Location
import android.os.Handler
import android.os.SystemClock
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.maps.Constants.USER_CIRCLE_LOCATION
import com.cybergod.oyeetaxi.maps.Constants.USER_LOCATION
import com.cybergod.oyeetaxi.maps.Utils.getCircle
import com.cybergod.oyeetaxi.maps.Utils.getIcoFromResource
import com.cybergod.oyeetaxi.maps.Utils.toLatLng
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.LOCATION_UPDATE_INTERFAL_FASTEST
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.getAvailableVehicleInterval
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkers
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkersChangingPosition
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkersObservable
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationCircle
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationMarker
import com.cybergod.oyeetaxi.utils.UtilsGlobal.logGlobal
import com.google.android.gms.maps.model.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class MarkerControl {

    private val className = this.javaClass.simpleName?: Constants.UNKNOWN_CLASS

    fun addUserLocationMarker(location : Location){
        val latLng = LatLng(location.latitude,location.longitude)

        val markerOptions = MarkerOptions()
            .anchor(0.5f,0.5f)
            .position(latLng)
            .icon(getIcoFromResource(currentVehicleActive.value?.tipoVehiculo?.tipoVehiculo,currentUserActive.value?.modoCondutor))
            .rotation(location.bearing)
            .title( "title")
            .snippet("snippet")

        val marker :Marker
        map.addMarker(markerOptions).also {
            it?.tag = USER_LOCATION
            marker = it!!
        }

        userLocationMarker = marker



        val circle :Circle
        map.addCircle(getCircle(location)).also {
            it.tag = USER_CIRCLE_LOCATION
            circle = it
        }

        userLocationCircle = circle

    }

    fun updateUserLocationMarker( location : Location){

        //val latLng = LatLng(location.latitude,location.longitude)

        if (userLocationMarker != null) {

            userLocationMarker?.apply {
                setAnchor(0.5f,0.5f)
                setIcon(getIcoFromResource(currentVehicleActive.value?.tipoVehiculo?.tipoVehiculo,currentUserActive.value?.modoCondutor))
//                position = location.toLatLng()
//                rotation = location.bearing
                title = "title"
                snippet = "title"

                logGlobal(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    logText = "$USER_LOCATION -> ${location.toLatLng()} (${location.bearing})"
                )

//                rotateMarkerTo(location.bearing)

                if ( (position != location.toLatLng() || rotation != location.bearing)  && !hashMapMarkersChangingPosition.containsKey(tag)) {
                    changePositionSmoothlyNew(location.toLatLng(),LOCATION_UPDATE_INTERFAL_FASTEST.toFloat()-250)
                }

            }

            with (userLocationCircle) {
                if (this!=null) {
                    if ( (this.center != location.toLatLng() || this.radius != location.accuracy.toDouble())  && !hashMapMarkersChangingPosition.containsKey(tag)) {
                        userLocationCircle?.changeCirclePositionSmoothly(location.toLatLng(),location.accuracy.toDouble(),LOCATION_UPDATE_INTERFAL_FASTEST.toFloat()-250)
                    }
                }
            }



//            userLocationCircle?.apply {
//                center = location.toLatLng()
//                radius = location.accuracy.toDouble()
//            }

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
            setAnchor(0.5f,0.5f)
            setIcon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))

//            position = vehiculoResponse.usuario?.ubicacion.toLatLng()
//            rotation = vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0f


//            logGlobal(
//                className = className,
//                metodo = object {}.javaClass.enclosingMethod!!,
//                logText = "${vehiculoResponse.tipoVehiculo?.tipoVehiculo} -> $position ($rotation)"
//            )


            logGlobal(
                className = className,
                metodo = object {}.javaClass.enclosingMethod!!,
                logText = "$position != ${vehiculoResponse.usuario?.ubicacion.toLatLng()}"
            )

            if (position != vehiculoResponse.usuario?.ubicacion.toLatLng() && !hashMapMarkersChangingPosition.containsKey(tag)) {

                //changePositionSmoothlyOld(vehiculoResponse)
                //animateMarkerNew(vehiculoResponse.usuario?.ubicacion.toLatLng())
                changePositionSmoothlyNew(vehiculoResponse.usuario?.ubicacion.toLatLng())
            }






        }





    }



    fun Marker.changePositionSmoothlyNew( position: LatLng, passed_durationInMs:Float?=null) {

        //Al Iniciar Animacion de Movimiento añadir al hashmap para que no se le aplique otra animacion mientras se ejecuta esta
        hashMapMarkersChangingPosition[tag] = true

        val startPosition:LatLng = this.position
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = passed_durationInMs ?: ((getAvailableVehicleInterval * 1000).toFloat() - 500)
//        val durationInMs = passed_durationInMs ?: (DELAY_VEHICLES_UPDATE.toFloat() - 500)
        val hideMarker = false

        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            var rotated = false
            override fun run() {

                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                val currentPosition = LatLng(
                    startPosition.latitude * (1 - t) + position.latitude * t,
                    startPosition.longitude * (1 - t) + position.longitude * t
                )

                if (!rotated) {
                    this@changePositionSmoothlyNew.rotateMarkerTo(
                        bearingBetweenLocations( startPosition,position)
                    ).also {
                        rotated = true
                    }
                }

                this@changePositionSmoothlyNew.position = currentPosition



                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)

                } else {
                    this@changePositionSmoothlyNew.isVisible = !hideMarker

                    //Al Terminar Animacion de Movimiento quitar del hashmap para que se pueda vover a mover
                    hashMapMarkersChangingPosition.remove(this@changePositionSmoothlyNew.tag).also {
                        //println("PASSSSS -> ${this@changePositionSmoothlyNew.title}")
                    }

                }


                hashMapMarkersObservable.postValue(hashMapMarkers)



            }


        })
    }

    private fun Marker.rotateMarkerTo(rotation: Float) {
        //if (!isMarkerRotating) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val startRotation = this.rotation
            val duration: Long = 1000 // 1000
            val interpolator: Interpolator = LinearInterpolator()
            handler.post(object : Runnable {
                override fun run() {

                    //isMarkerRotating = true
                    val elapsed = SystemClock.uptimeMillis() - start
                    val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                    val rot = t * rotation + (1 - t) * startRotation
                    this@rotateMarkerTo.rotation = if (-rot > 180) rot / 2 else rot
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16)
                    } else {
                        //isMarkerRotating = false
                    }
                }
            })

    }

    private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Float {
        val pi = 3.14159
        val lat1 = latLng1.latitude * pi / 180
        val long1 = latLng1.longitude * pi / 180
        val lat2 = latLng2.latitude * pi / 180
        val long2 = latLng2.longitude * pi / 180
        val dLon = long2 - long1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - (sin(lat1)
                * cos(lat2) * cos(dLon))
        var brng = atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng.toFloat()
    }




    fun Circle.changeCirclePositionSmoothly( position: LatLng,radius: Double, passed_durationInMs:Float) {

        //Al Iniciar Animacion de Movimiento añadir al hashmap para que no se le aplique otra animacion mientras se ejecuta esta
        hashMapMarkersChangingPosition[tag] = true

        val startPosition : LatLng = this.center
        val startRadius : Double = this.radius
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val hideMarker = false

        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {

                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / passed_durationInMs
                v = interpolator.getInterpolation(t)
                val currentPosition = LatLng(
                    startPosition.latitude * (1 - t) + position.latitude * t,
                    startPosition.longitude * (1 - t) + position.longitude * t
                )
                val currentRadius: Double = startRadius * (1 - t) + radius * t

                this@changeCirclePositionSmoothly.center = currentPosition

                this@changeCirclePositionSmoothly.radius = currentRadius

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)

                } else {
                    this@changeCirclePositionSmoothly.isVisible = !hideMarker

                    //Al Terminar Animacion de Movimiento quitar del hashmap para que se pueda vover a mover
                    hashMapMarkersChangingPosition.remove(this@changeCirclePositionSmoothly.tag)
                }


                hashMapMarkersObservable.postValue(hashMapMarkers)


            }


        })
    }







































































//    private fun Marker.animateMarkerNew(latLng: LatLng) {
//        val startPosition = this.position
//        val endPosition = LatLng(latLng.latitude, latLng.longitude)
//        val startRotation = this.rotation
//        val latLngInterpolator: LatLngInterpolatorNew = LatLngInterpolatorNew.LinearFixed()
//        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
//        valueAnimator.duration = DELAY_VEHICLES_UPDATE -500// duration 3 second
//        valueAnimator.interpolator = LinearInterpolator()
//        valueAnimator.addUpdateListener { animation ->
//
//
//            hashMapMarkersObservable.postValue(hashMapMarkers)
//
//            try {
//                val v = animation.animatedFraction
//                val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
//                this.position = newPosition
//                this.position = newPosition
//                this.rotation =
//                    bearingBetweenLocations(
//                        startPosition,
//                        latLng
//                    )
//            } catch (ex: Exception) {
//                //I don't care atm..
//            }
//        }
//        valueAnimator.addListener(object : AnimatorListenerAdapter() {
//
//            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
//                super.onAnimationStart(animation, isReverse)
//
//
//
//            }
//
//            override fun onAnimationEnd(animation: Animator?) {
//
//                //hashMapMarkersObservable.postValue(hashMapMarkers)
//
//
//                super.onAnimationEnd(animation)
//
//
//                // if (mMarker != null) {
//                // mMarker.remove();
//                // }
//                // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
//            }
//        })
//        valueAnimator.start()
//
//    }
//
//    private interface LatLngInterpolatorNew {
//        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng
//        class LinearFixed : LatLngInterpolatorNew {
//            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
//                val lat = (b.latitude - a.latitude) * fraction + a.latitude
//                var lngDelta = b.longitude - a.longitude
//                // Take the shortest path across the 180th meridian.
//                if (Math.abs(lngDelta) > 180) {
//                    lngDelta -= Math.signum(lngDelta) * 360
//                }
//                val lng = lngDelta * fraction + a.longitude
//                return LatLng(lat, lng)
//            }
//        }
//    }



//    fun Marker.changePositionSmoothlyOld(vehiculoResponse: VehiculoResponse) {
//        val startPosition:LatLng = this.position
//        val handler = Handler()
//        val start: Long = SystemClock.uptimeMillis()
//        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
//        val durationInMs = DELAY_VEHICLES_UPDATE.toFloat()-500
//        val hideMarker = false
//        handler.post(object : Runnable {
//            var elapsed: Long = 0
//            var t = 0f
//            var v = 0f
//            override fun run() {
//                this@changePositionSmoothlyOld.rotation = getBearing(this@changePositionSmoothlyOld.position,vehiculoResponse.usuario?.ubicacion.toLatLng())
//
//                // Calculate progress using interpolator
//                elapsed = SystemClock.uptimeMillis() - start
//                t = elapsed / durationInMs
//                v = interpolator.getInterpolation(t)
//                val currentPosition = LatLng(
//                    startPosition.latitude * (1 - t) + vehiculoResponse.usuario?.ubicacion?.latitud!! * t,
//                    startPosition.longitude * (1 - t) + vehiculoResponse.usuario?.ubicacion?.longitud!! * t
//                )
//                this@changePositionSmoothlyOld.position = currentPosition
//
//                // Repeat till progress is complete.
//                if (t < 1) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16)
//                } else {
//                    this@changePositionSmoothlyOld.isVisible = !hideMarker
//                }
//
//
//                hashMapMarkersObservable.postValue(hashMapMarkers)
//
//
//
//            }
//        })
//    }








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