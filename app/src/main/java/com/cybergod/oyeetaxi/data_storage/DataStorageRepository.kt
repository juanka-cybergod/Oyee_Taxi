package com.cybergod.oyeetaxi.data_storage


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import com.cybergod.oyeetaxi.utils.UtilsGlobal.logGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.passwordDecode
import com.cybergod.oyeetaxi.utils.UtilsGlobal.passwordEncode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class DataStorageRepository @Inject constructor(private val preferenceDataStoreInstance : DataStore<Preferences>) {

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS


    suspend fun saveMapStyle(mapStyle: TypeAndStyle.MapStyle) {
        logGlobal(
            className = className,
            metodo = object {}.javaClass.enclosingMethod!!,
            mapStyle.name
        )
        preferenceDataStoreInstance.edit { preference ->
            preference[MAP_STYLE_KEY] = mapStyle.name
        }
    }

    val readMapStyle : Flow<TypeAndStyle.MapStyle> = preferenceDataStoreInstance.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preference ->
            val mapStyleString = preference[MAP_STYLE_KEY] ?: "SILVER"
            val mapStyle = getMapStyleByStringStyleName(mapStyleString)
            Log.d(className, "readMapStyleFromDataStore=${mapStyle.name}")
            mapStyle

        }



    suspend fun saveUserAuthentication(userAuthentication:UserAuthentication) {

        logGlobal(
            className = className,
            metodo = object {}.javaClass.enclosingMethod!!,
            userAuthentication.toString()
        )

        preferenceDataStoreInstance.edit { preference ->

            userAuthentication.userNumber?.let {
                preference[USER_NUMBER_KEY] = it
            }
            userAuthentication.password?.let {
                preference[USER_PASSWORD_KEY] = passwordEncode(it)
            }
            preference[USER_PASSWORD_REMEMBER_KEY] = userAuthentication.rememberPassword
        }
    }

    val readUserAuthenticationFromDataStore : Flow<UserAuthentication?> = preferenceDataStoreInstance.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preference ->
            val userID = preference[USER_NUMBER_KEY] ?: ""
            val userPasword = preference[USER_PASSWORD_KEY] ?: ""
            val userPaswordRemember = preference[USER_PASSWORD_REMEMBER_KEY] ?: false

            if (userID != "" || userPasword != "" ) {
                val userAuthentication = UserAuthentication(userID,passwordDecode(userPasword),userPaswordRemember)
                Log.d(className, "readUserAuthenticationFromDataStore=$userAuthentication")
                userAuthentication
            } else {
                null
            }


        }


    suspend fun saveUserRegistred(isRegistred:Boolean) {
        logGlobal(
            className = className,
            metodo = object {}.javaClass.enclosingMethod!!,
            isRegistred.toString()
        )
        preferenceDataStoreInstance.edit { preference ->
            preference[USER_REGISTERED_KEY] = isRegistred
        }
    }



    val readUserRegistred : Flow<Boolean?> = preferenceDataStoreInstance.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preference ->
            val userRegistred = preference[USER_REGISTERED_KEY] ?: false
            Log.d(className, "readUserRegistred=$userRegistred")
            userRegistred
        }





    suspend fun saveUserIdToRestorePassword(userId: String) {
        preferenceDataStoreInstance.edit { preference ->
            logGlobal(
                className = className,
                metodo = object {}.javaClass.enclosingMethod!!,
                userId.toString()
            )
            preference[USER_ID_KEY] = userId
        }
    }

    val readUserIdToRestorePassword : Flow<String?> = preferenceDataStoreInstance.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preference ->
            val userId = preference[USER_ID_KEY]
            Log.d(className, "userId=$userId")
            userId
        }





    suspend fun saveRememberAppUpdateAfterDate(date: String) {
        preferenceDataStoreInstance.edit { preference ->
            logGlobal(
                className = className,
                metodo = object {}.javaClass.enclosingMethod!!,
                date.toString()
            )
            preference[APP_REMEMBER_UPDATE_AFTER_DATE] = date
        }
    }
    suspend fun removeRememberAppUpdateAfterDate() {
        preferenceDataStoreInstance.edit { preference ->
            logGlobal(
                className = className,
                metodo = object {}.javaClass.enclosingMethod!!,
                "RemoveKey"
            )
            preference.remove(APP_REMEMBER_UPDATE_AFTER_DATE)
        }
    }


    val readRememberAppUpdateAfterDate : Flow<String?> = preferenceDataStoreInstance.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preference ->
            val date = preference[APP_REMEMBER_UPDATE_AFTER_DATE]
            Log.d(className, "date=$date")
            date
        }






    companion object {
        val USER_REGISTERED_KEY = booleanPreferencesKey("USER_REGISTERED_KEY")

        val USER_NUMBER_KEY = stringPreferencesKey("USER_NUMBER_KEY")
        val USER_PASSWORD_KEY = stringPreferencesKey("USER_PASSWORD_KEY")
        val USER_PASSWORD_REMEMBER_KEY = booleanPreferencesKey("USER_PASSWORD_REMEMBER_KEY")
        val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
        val APP_REMEMBER_UPDATE_AFTER_DATE = stringPreferencesKey("APP_REMEMBER_UPDATE_AFTER_DATE")

        val MAP_STYLE_KEY = stringPreferencesKey("MAP_STYLE_KEY")

    }

    data class UserAuthentication(
        val userNumber: String?,
        val password: String?,
        val rememberPassword:Boolean,
    )



    private fun getMapStyleByStringStyleName(name:String):TypeAndStyle.MapStyle{

        val mapStyle = when (name){
            "SILVER" -> {
                TypeAndStyle.MapStyle.SILVER
            }
            "NORMAL" -> {
                TypeAndStyle.MapStyle.NORMAL
            }
            "NIGHT" -> {
                TypeAndStyle.MapStyle.NIGHT
            }
            "RETRO" -> {
                TypeAndStyle.MapStyle.RETRO
            }
            "SATELLITE" -> {
                TypeAndStyle.MapStyle.SATELLITE
            }
            "TERRAIN" -> {
                TypeAndStyle.MapStyle.TERRAIN
            }
            "HYBRID" -> {
                TypeAndStyle.MapStyle.HYBRID
            }
            else -> {
                TypeAndStyle.MapStyle.SILVER
            }
        }

        return mapStyle
    }



}