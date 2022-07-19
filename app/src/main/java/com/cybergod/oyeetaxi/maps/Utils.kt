package com.cybergod.oyeetaxi.maps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.graphics.Color
import android.location.Location
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat


object Utils {




    fun getMapStyleByName(name:String):TypeAndStyle.MapStyle{

        return when (name){
            "Oye Taxi" -> {
                TypeAndStyle.MapStyle.SILVER
            }
            "Normal" -> {
                TypeAndStyle.MapStyle.NORMAL
            }
            "Obscuro" -> {
                TypeAndStyle.MapStyle.NIGHT
            }
            "Retro" -> {
                TypeAndStyle.MapStyle.RETRO
            }
            "Satélite" -> {
                TypeAndStyle.MapStyle.SATELLITE
            }
            "Terreno" -> {
                TypeAndStyle.MapStyle.TERRAIN
            }
            "Híbrido" -> {
                TypeAndStyle.MapStyle.HYBRID
            }
            else -> {
                TypeAndStyle.MapStyle.SILVER
            }
        }


    }

    fun getStyleNameByMapStyle(style:TypeAndStyle.MapStyle):String{
        return when (style){
            TypeAndStyle.MapStyle.SILVER -> {
                "Oye Taxi"
            }
            TypeAndStyle.MapStyle.NORMAL -> {
                "Normal"
            }
            TypeAndStyle.MapStyle.NIGHT -> {
                "Obscuro"
            }
            TypeAndStyle.MapStyle.RETRO -> {
                "Retro"
            }
            TypeAndStyle.MapStyle.SATELLITE -> {
                "Satélite"
            }
            TypeAndStyle.MapStyle.TERRAIN -> {
                "Terreno"
            }
            TypeAndStyle.MapStyle.HYBRID -> {
                "Híbrido"
            }
            else -> {
                "Oye Taxi"
            }
        }
    }

    fun getCircle(location: Location): CircleOptions {

        val latLng = LatLng(location.latitude, location.longitude)

        return CircleOptions()
            .center(latLng)
            .fillColor(Color.argb(32, 255, 0, 0))
            .strokeColor(Color.argb(255, 255, 0, 0))
            .strokeWidth(4f)
            .radius(location.accuracy.toDouble())

    }

    private fun getIco(tipoVehiculo: String?):BitmapDescriptor {
        if (tipoVehiculo.isNullOrEmpty()) {
            return getDefaultIco()
        }
        return when (tipoVehiculo) {
            //Tipos (Camión/Guagua/Auto/Motor)
            "Auto Móvil" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_taxi)
            "Auto Van" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_van_new)
            "Camión de Carga" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_camion_carga)
            "Camión de Pasaje" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_camion_pasaje)
            "Auto Bús" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_guagua)
            "Motocicleta" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_moto)
            "Motorina" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_motorina)
            "Motoneta" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_top_motoneta)
            else -> getDefaultIco()
        }
    }

    private fun getDefaultIco(): BitmapDescriptor {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_top_user)
    }

    fun getIcoFromResource(tipoVehiculo: String?, modoConductor:Boolean?):BitmapDescriptor{
        return when  (modoConductor) {
            true -> getIco(tipoVehiculo)
            else -> getDefaultIco()
        }
    }


    fun calculateTheDistance(startLocation: Ubicacion?, endLocation: Ubicacion?):String {
       val startLocationLatLng: LatLng = startLocation.toLatLng()
       val endLocationLatLng: LatLng = endLocation.toLatLng()

       val meters = SphericalUtil.computeDistanceBetween(startLocationLatLng,endLocationLatLng)
       val kilometers = meters / 1000
       return DecimalFormat("#.#").format(kilometers)
//       return DecimalFormat("#.##").format(kilometers)
    }

    fun Ubicacion?.toLatLng():LatLng{
        return LatLng(this?.latitud ?:0.0,this?.longitud  ?:0.0)
    }

    fun Location.toLatLng():LatLng{
        return LatLng(this.latitude,this.longitude)
    }

    fun LatLng.toUbicacion():Ubicacion{
        return Ubicacion(latitud =  this.latitude, longitud = this.longitude)
    }


    fun Location.toUbicacion(): Ubicacion {
        return  Ubicacion(
            latitud = this.latitude,
            longitud = this.longitude,
            rotacion = this.bearing.toInt(),
        )
    }


    @SuppressLint("MissingPermission")
    fun showDialogEnableGPS(activity:Activity){
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())



        result.addOnCompleteListener(OnCompleteListener<LocationSettingsResponse?> { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                activity,
                                LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        })

    }




}




/*
    fun fromVectorToBitmapDescriptor(activity: Activity, id:Int, color:Int):BitmapDescriptor{
        val vectorDrawable:Drawable? = ResourcesCompat.getDrawable(activity.resources,id,null)



        if (vectorDrawable == null) {
            Log.d(activity.getString(R.string.apklog),"Recurso Drawable para Marcador no encontrado")
            return BitmapDescriptorFactory.defaultMarker()
        }

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0,0,canvas.width,canvas.height)
        DrawableCompat.setTint(vectorDrawable,color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun fromVectorToBitmap(activity: Activity, id:Int,):Bitmap{
         return  BitmapFactory.decodeResource(activity.resources,id)
    }


    fun getIcoBmpFromVehicle(vehiculo: Vehiculo,activity: Activity):Bitmap {

        var icoBmp : Bitmap = fromVectorToBitmap(activity,R.drawable.ic_unknow)

        when (vehiculo.tipoVehiculo) {
            //Tipos (Camión/Guagua/Auto/Motor)
            "Auto Móvil" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_unknow)
            }
            "Auto Van" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_van)
            }
            "Camión de Carga" -> {
            }
            "Camión de Pasaje" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_camion)
            }
            "Auto Bús" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_guagua)
            }
            "Motocicleta" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_motor)
            }
            "Motorina" -> {
                icoBmp  = fromVectorToBitmap(activity,R.drawable.ic_motorina)
            }
        }

        return icoBmp
    }

    fun getIcoFromVehicle(vehiculo: VehiculoResponse,activity: Activity):BitmapDescriptor {


        var ico: BitmapDescriptor =
            fromVectorToBitmapDescriptor(activity, R.drawable.ic_unknow, Color.parseColor("#FF5722"))

        when (vehiculo.tipoVehiculo?.tipoVehiculo ?: "") {
            //Tipos (Camión/Guagua/Auto/Motor)
            "Auto Móvil" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_taxi,
                    Color.parseColor("#FF5722")
                )
            }
            "Auto Van" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_van,
                    Color.parseColor("#FF5722")
                )
            }
            "Camión de Carga" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_camion,
                    Color.parseColor("#673AB7")
                )
            }
            "Camión de Pasaje" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_camion,
                    Color.parseColor("#673AB7")
                )
            }
            "Auto Bús" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_guagua,
                    Color.parseColor("#673AB7")
                )
            }
            "Motocicleta" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_motor,
                    Color.parseColor("#2962FF")
                )
            }
            "Motorina" -> {
                ico = fromVectorToBitmapDescriptor(
                    activity,
                    R.drawable.ic_motorina,
                    Color.parseColor("#2962FF")
                )
            }




        }

        return ico
    }
    private fun vehicleToLatLng(vehiculo: VehiculoResponse):LatLng{
        return LatLng(vehiculo.ubicacion?.latitud ?:0.0,vehiculo.ubicacion?.longitud  ?:0.0)
    }

    fun provinceToLatLng(provincia: Provincia):LatLng{
        return LatLng(provincia.ubicacion?.latitud!! ,provincia.ubicacion.longitud!!)
    }
 */