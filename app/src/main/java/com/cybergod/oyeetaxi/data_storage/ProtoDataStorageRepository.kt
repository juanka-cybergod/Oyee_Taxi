package com.cybergod.oyeetaxi.data_storage

import android.util.Log
import androidx.datastore.core.DataStore
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.api.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


class ProtoDataStorageRepository @Inject constructor(
    private var loginRespuestaProtoDataStorageInstance :DataStore<LoginRespuesta>,
    )
{


    val readLoginRespuestaFromProtoDataStore : Flow<LoginRespuesta> = loginRespuestaProtoDataStorageInstance.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("ProtoDataStorage", "Error : $exception")
                emit(LoginRespuesta())
            } else {
                throw exception
            }
        }

    private suspend fun writeLoginRespuestaToProtoDataStore(loginRespuesta: LoginRespuesta){
        loginRespuestaProtoDataStorageInstance.updateData {  loginRespuestaProtoDataStore->
            loginRespuestaProtoDataStore.copy(
                usuarioEncontrado = loginRespuesta.usuarioEncontrado ?: false,
                contrasenaCorrecta = loginRespuesta.contrasenaCorrecta ?: false,
                mensaje  = loginRespuesta.mensaje ?: "",
                usuario = loginRespuesta.usuario ?: Usuario(),
                vehiculoActivo = loginRespuesta.vehiculoActivo ?: VehiculoResponse(),
            )
        }.let {
            Log.d("ProtoDataStorage", "writeLoginRespuestaToProtoDataStore : $it")

        }
    }

    private suspend fun writeUserDataToProtoDataStore(usuario: Usuario){
        loginRespuestaProtoDataStorageInstance.updateData {  loginRespuestaProtoDataStore->
            loginRespuestaProtoDataStore.copy(
                usuario = usuario
            )
        }.let {
            Log.d("ProtoDataStorage", "writeUserLocationToProtoDataStore : $it")

        }
    }


    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun setLoginRespuestaToProtoDataStore(loginRespuesta: LoginRespuesta){
        scope.launch {
            writeLoginRespuestaToProtoDataStore(loginRespuesta)
        }

    }
    fun setUserDataToProtoDataStore(usuario: Usuario){
        scope.launch {
            writeUserDataToProtoDataStore(usuario)
        }

    }


}


