package com.cybergod.oyeetaxi.web_socket


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

import javax.inject.Singleton



@InstallIn(SingletonComponent::class)
@Module
object WebSocketModule {


    @Singleton
    @Provides
    fun getSocket(): Socket {

        val uri: URI = URI.create("http://192.168.0.100/")
        val options = IO.Options.builder()
            .setPort(9092)
            .build()

        return IO.socket(uri, options)
    }



}

