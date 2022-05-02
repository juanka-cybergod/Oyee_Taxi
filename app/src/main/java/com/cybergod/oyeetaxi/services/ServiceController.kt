package com.cybergod.oyeetaxi.services

import android.content.Context
import android.content.Intent
import com.cybergod.oyeetaxi.utils.Constants

object ServiceController {

    private fun sendActionCommandToService(action:String, context: Context){
        Intent(context, TrackerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    fun Context.startLocationService(){
        sendActionCommandToService(
            Constants.ACTION_SERVICE_START,
            this
        )
    }

    fun Context.stopLocationService(){
        sendActionCommandToService(
            Constants.ACTION_SERVICE_STOP,
            this
        )
    }

}