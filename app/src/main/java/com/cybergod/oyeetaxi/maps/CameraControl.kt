package com.cybergod.oyeetaxi.maps

import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.Ubicacion
import com.cybergod.oyeetaxi.maps.Utils.ubicacionToLatLng
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CameraControl {

    //Corutina de Conteo Regresivo para Re Encviar el Codigo
    val coroutine : CoroutineScope = CoroutineScope(Dispatchers.IO)


    fun animateCameraZoomIn(markerLocation: LatLng){

        coroutine.launch(Dispatchers.Main) {
            map.animateCamera(CameraUpdateFactory.newLatLng(markerLocation),500,null) //default
            delay(500L)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation,17f) , 2000, object: GoogleMap.CancelableCallback{
                override fun onCancel() {
                    // activity.toastText("Cancel")
                }
                override fun onFinish() {
                    //  activity.toastText(province.toString())
                }
            })
        }



    }




    fun animateCameraTo(ubicacion: LatLng){


        coroutine.launch(Dispatchers.Main) {
            //delay(100L)
            map.animateCamera(CameraUpdateFactory.newLatLng(ubicacion) , 600, object: GoogleMap.CancelableCallback{
                override fun onCancel() {
                    //activity.toastText("Cancel")
                }

                override fun onFinish() {
                    //activity.toastText(province.toString())
                }
            })

        }

    }


    fun moveCameraToProvince(province:Provincia){

        coroutine.launch(Dispatchers.Main) {
            delay(100L)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionToLatLng(province.ubicacion),10f) , 1500, object: GoogleMap.CancelableCallback{
                override fun onCancel() {
                    //activity.toastText("Cancel")
                }

                override fun onFinish() {
                    // activity.toastText(province.toString())
                    //map.animateCamera(CameraUpdateFactory.newLatLngZoom(getProvinceLatLng(Provinces.VillaClara),10f) , 1500,null)
                }
            })
        }


    }


    fun goToUbicacionInicialProvincia(ubicacion:Ubicacion){

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(ubicacion.latitud!!,ubicacion.longitud!!),8f))
        coroutine.launch(Dispatchers.Main) {
            delay(1000L)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(ubicacion.latitud,ubicacion.longitud),9f),1500,null)
        }


    }

//UTILES
//map.moveCamera(CameraUpdateFactory.newLatLngZoom(getProvinceLatLng(province),9f))
//map.animateCamera(CameraUpdateFactory.newLatLngZoom(getProvinceLatLng(province),10f) , 1500,null)
//map.moveCamera(CameraUpdateFactory.newLatLng(santa_clara)) //default
// map.moveCamera(CameraUpdateFactory.newLatLngZoom(santa_clara,12f)) //poner el zoom predefinido desde 1..20


}