package com.cybergod.oyeetaxi.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
object DataStoreModule {

    const val PREFERENCES_NAME = "OyeeTaxiPreferencias"

    @Singleton
    @Provides
    fun getPreferenceDataStoreInstance(@ApplicationContext appContext:Context):DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {appContext.preferencesDataStoreFile(PREFERENCES_NAME)},
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )

    }



}

