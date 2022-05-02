package com.cybergod.oyeetaxi.services



import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Ubicacion
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.maps.Utils.locationToUbicacion
import com.cybergod.oyeetaxi.utils.Constants.ACTION_SERVICE_START
import com.cybergod.oyeetaxi.utils.Constants.ACTION_SERVICE_STOP
import com.cybergod.oyeetaxi.utils.Constants.LOCATION_UPDATE_INTERFAL_FASTEST
import com.cybergod.oyeetaxi.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.cybergod.oyeetaxi.utils.Constants.NOTIFICATION_CHANEL_ID
import com.cybergod.oyeetaxi.utils.Constants.NOTIFICATION_CHANE_NAME
import com.cybergod.oyeetaxi.utils.Constants.NOTIFICATION_ID
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class TrackerService : LifecycleService() {

    val TAG = "TrackerService"

    @Inject
    lateinit var notification : NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var usuarioRepository: UserRepository

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var ubicacionActualToServer:Ubicacion

    companion object{
        val started = MutableLiveData<Boolean>()
        val currentLocationFromService : MutableLiveData<Location> = MutableLiveData<Location>(null)
        val isGPSEnabled : MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    }



    val locationUpdatedSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>(null)

    private val locationCallback = object : LocationCallback(){


        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.let {
                for (location in it){
                    updateNotificationPeriodically()
                   // if (location.bearing != 0.0f){
                        currentLocationFromService.postValue(location)
                        ubicacionActualToServer = locationToUbicacion(location)
                       // Log.d(TAG,"ubicacionActualToServer=${ubicacionActualToServer}")

                        Log.d(TAG,"currentUser.value?.id!!=${currentUserActive.value?.id}")
                        currentUserActive.value?.id?.let {

                            CoroutineScope(Dispatchers.IO).launch {
                                locationUpdatedSuccess.postValue(
                                    usuarioRepository.updateUserLocationById(it,ubicacionActualToServer)
                                )
                            }

                        }

                }
            }
        }


        override fun onLocationAvailability(locationAvilability: LocationAvailability) {
            checkGpsStatus()
            super.onLocationAvailability(locationAvilability)
        }
    }


    private fun checkGpsStatus(){
        val locationManager = this@TrackerService.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isGPSEnabled.postValue(gpsEnabled)

        Log.d(TAG,"isGpsEnabled=${gpsEnabled}")


    }

    private fun setInitialValues(){
        started.postValue(false)
        checkGpsStatus()
    }

    override fun onCreate() {
        setInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
        Log.d(TAG,"CREATED")
    }

    private fun updateNotificationPeriodically(){
        val bigTextStyle =
            NotificationCompat. BigTextStyle()
                //.setSummaryText(getString(R.string.app_name))
                .setBigContentTitle(getString(R.string.notificationServiceTitle))
                .bigText(getString(R.string.notificationServiceText))


        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)

        notification.apply {
           //setContentTitle(getString(R.string.notificationServiceTitle))
           // setContentText(getString(R.string.notificationServiceText))
            // setContentText("Ubicación "+currentLocation)


                setStyle(bigTextStyle)
        }
            .setSmallIcon(R.drawable.ic_location)
            .setLargeIcon(largeIcon)

        notificationManager.notify(NOTIFICATION_ID,notification.build())
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_SERVICE_START -> {
                    started.postValue(true)
                    //Notify
                    Log.d(TAG,"COMMAND STAR")
                    startForegroundService()
                    startLocationUpdate()
                }
                ACTION_SERVICE_STOP -> {
                    started.postValue(false)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    Log.d(TAG,"COMMAND STOP")
                    stopSelf()
                } else -> {

                }
            }
        }


        return super.onStartCommand(intent, flags, startId)


    }

    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,notification.build())
    }


    lateinit var builder : LocationSettingsRequest.Builder

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate(){
        val locationRequest = com.google.android.gms.location.LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_UPDATE_INTERFAL_FASTEST
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANEL_ID,
                NOTIFICATION_CHANE_NAME,
                IMPORTANCE_LOW

            )
            notificationManager.createNotificationChannel(channel)
        }
    }



    override fun onDestroy() {
        Log.d(TAG,"DESTROY")
        super.onDestroy()
    }
}