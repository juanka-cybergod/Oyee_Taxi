package com.cybergod.oyeetaxi.maps

import android.content.Context
import android.util.Log
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import java.lang.Exception

class TypeAndStyle(context: Context) {

    private val thisContext = context

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS


    private fun setMapStyle(rawStyle: Int = R.raw.styledefault):Boolean{

        return try {
            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisContext, rawStyle))
            if (!success) {
                Log.d(className,"Error al Cargar y Añadir el estilo Predefinido al Mapa")
            }
            return success
        } catch (e: Exception){
            Log.d(className,"Error al Cargar y Añadir el estilo Predefinido al Mapa ${e}")
            false
        }

    }


    fun setMapType(mapStyle:MapStyle):Boolean{


        val map: GoogleMap = map

        return when(mapStyle){
            MapStyle.NORMAL -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle()
            }
            MapStyle.HYBRID -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
                setMapStyle()
            }
            MapStyle.TERRAIN -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                setMapStyle()
            }
            MapStyle.SATELLITE -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                setMapStyle()
            }
            MapStyle.NONE -> {
                map.mapType = GoogleMap.MAP_TYPE_NONE
                setMapStyle()
            }
            MapStyle.RETRO -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle(R.raw.style_retro)
            }
            MapStyle.SILVER -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle(R.raw.style_silver)
            }
            MapStyle.NIGHT -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle(R.raw.style_night)
            }

        }
    }

    enum class MapStyle{
        NORMAL,HYBRID,TERRAIN,SATELLITE,NONE,RETRO,SILVER,NIGHT
    }


}