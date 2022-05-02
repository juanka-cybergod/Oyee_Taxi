package com.cybergod.oyeetaxi.maps

import android.location.Location
import android.util.Log
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.maps.Utils.getCircle
import com.cybergod.oyeetaxi.maps.Utils.getIcoFromResource
import com.cybergod.oyeetaxi.maps.Utils.ubicacionToLatLng
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkers
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationCircle
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationMarker
import com.google.android.gms.maps.model.*

class MarkerControl {





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
            it?.tag = "USER_LOCATION"
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

        Log.d("CHEAK",latLng.toString()+" "+location.bearing)

        // Clear the current marker before add the new one
        if (userLocationMarker != null) {
            userLocationMarker?.apply {
                setAnchor(0.5f,0.5f)
                position = latLng
                setIcon(getIcoFromResource(currentVehicleActive.value?.tipoVehiculo?.tipoVehiculo,currentUserActive.value?.modoCondutor))
                rotation = location.bearing
                title = ""
                snippet = ""
            }


            userLocationCircle?.apply {
                center = latLng
                radius = location.accuracy.toDouble()
            }
        } else

            addUserLocationMarker(location)







    }


    fun addVehicleMarker(vehiculoResponse: VehiculoResponse){

        val markerOptions = MarkerOptions()
            .anchor(0.5f,0.5f)
            .position(ubicacionToLatLng(vehiculoResponse.usuario?.ubicacion))
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //Cambar el Color al Marcador
            .icon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
            .rotation(vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0F)
            .title(vehiculoResponse.tipoVehiculo?.tipoVehiculo ?: "")
            .snippet("snippet")

        val marker :Marker
        map.addMarker(markerOptions).also {
            it?.tag = vehiculoResponse.id
            marker = it!!

        }


        hashMapMarkers.value?.put(vehiculoResponse.id!!,marker)

    }

    fun updateVehicleMarker(vehiculoResponse: VehiculoResponse){
        val marker: Marker = hashMapMarkers.value?.get(vehiculoResponse.id)!!

        marker.setAnchor(0.5f,0.5f)
        marker.position = ubicacionToLatLng(vehiculoResponse.usuario?.ubicacion)
        marker.setIcon(getIcoFromResource(vehiculoResponse.tipoVehiculo?.tipoVehiculo,vehiculoResponse.usuario?.modoCondutor))
        marker.rotation = vehiculoResponse.usuario?.ubicacion?.rotacion?.toFloat() ?: 0f
    }


    //Buscar y quitar los Marcadores que ya no son vehiculos Disponibles
    fun removeUnaviableVehiclesFromMapMarker(listVehiculoResponse: List<VehiculoResponse>){

        Log.d("PRUEBA",hashMapMarkers.value?.keys?.toList().toString())
        hashMapMarkers.value?.keys?.toList()?.forEach { idMarcador ->
            val vehiculoEncontrado =  listVehiculoResponse.find { item -> item.id.equals(idMarcador,true) }

            if ( vehiculoEncontrado== null) {
                val marker: Marker = hashMapMarkers.value?.get(idMarcador)!!
                marker.remove()
                hashMapMarkers.value?.remove(idMarcador)
                Log.d("PRUEBA","Eliminado $idMarcador")
            }

        }
    }


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