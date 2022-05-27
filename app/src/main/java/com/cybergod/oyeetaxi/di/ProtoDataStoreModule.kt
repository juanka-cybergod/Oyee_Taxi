package com.cybergod.oyeetaxi.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.cybergod.oyeetaxi.api.futures.user.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.di.utils.LoginRespuestaSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton



@InstallIn(SingletonComponent::class)
@Module
object ProtoDataStoreModule {

    const val LOGIN_RESPUESTA_PROTO_DATA_STORAGE_FILE = "LoginRespuestaProtoDataStorage.pb"

    @Singleton
    @Provides
    fun getLoginResponseProtoDataStorageInstance(@ApplicationContext appContext:Context):DataStore<LoginRespuesta> {
        return DataStoreFactory.create(
            produceFile = {appContext.dataStoreFile(LOGIN_RESPUESTA_PROTO_DATA_STORAGE_FILE)},
            serializer = LoginRespuestaSerializer,
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )

    }





}

