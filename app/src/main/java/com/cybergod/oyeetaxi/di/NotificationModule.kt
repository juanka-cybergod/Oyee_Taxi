package com.cybergod.oyeetaxi.di

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.ui.main.activity.MainActivity
import com.cybergod.oyeetaxi.utils.Constants.NOTIFICATION_CHANEL_ID
import com.cybergod.oyeetaxi.utils.Constants.PENDING_INTENT_REQUEST_CODE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @SuppressLint("UnspecifiedImmutableFlag")
    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificatioBuider(
        @ApplicationContext context: Context,
        //pendingIntent: PendingIntent
    ): NotificationCompat.Builder{

        //val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)

        return NotificationCompat.Builder(context,NOTIFICATION_CHANEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
//            .setSmallIcon(R.drawable.ic_location)
//            .setLargeIcon(largeIcon)
          //  .setSmallIcon(R.drawable.ic_location)
            //.setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) : NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


}