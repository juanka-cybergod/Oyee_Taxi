package com.cybergod.oyeetaxi.maps


import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


class MapControl {




    fun setupUISettings(){

        //Aplicar las Restricciones al Mapa para que se ajuste a las necesidades del Momento
        map.uiSettings.apply {
            //isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isTiltGesturesEnabled = true
            isRotateGesturesEnabled = false //Deshabilita la Rotacion por Gesto
            isCompassEnabled = true //Icono q reatablece la rotacion solo si esta habilitada
            isMapToolbarEnabled = false //Muestra o es esconde la barra de herramientas para las localizaciones cuando son marcadas


        }
        map.setMinZoomPreference(7f) //Zoom Minimo *que tanto se aleja la camara Min 1
        map.setMaxZoomPreference(19f) //Zoom Maximo *que tanto se acerca la camara al suelo Max 20


        //Establece Marge para las Herramientas dentro del Mapa **No Por ahora
        map.setPadding(0,200,0,200)


        //Establecer los Limites del Mapa
        val cubaBounders = LatLngBounds(
            LatLng(18.728592772286778, -86.11689885912554),
            LatLng(24.24608907488078, -73.29585543788363)

        )
        map.setLatLngBoundsForCameraTarget(cubaBounders)


        //Establecer el Adaptador de InfoWindowsAdapter Personalizado
        //map.setInfoWindowAdapter(InfoWindowsAdapter(this.requireActivity(),this.requireActivity(),viewModel))
    }


}