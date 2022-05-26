package com.cybergod.oyeetaxi.ui.controlPanel.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.api.repository.*
import com.cybergod.oyeetaxi.api.utils.UploadRequestBody
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UserControlPanelViewModel @Inject constructor(
    //private val configurationRepository: ConfigurationRepository,
    private val userRepository: UserRepository,
    private val filesRepository: FilesRepository,
    private val dataStoreRepository: DataStorageRepository


) : BaseViewModel(), UploadRequestBody.UploadCallback {


    val userUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    var imagenPerfilURL : MutableLiveData<String> = MutableLiveData<String>()
    var recordarPassword : Boolean = false

    var identificaion : String = ""
    var imagenVerificacionURL : MutableLiveData<String> = MutableLiveData<String>()
    var imagenVerificacionURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)
    var imagenVerificacionFile : MutableLiveData<File?> = MutableLiveData<File?>( null)

    val userAUTH = dataStoreRepository.readUserAuthenticationFromDataStore.asLiveData()


    fun updateUser(usuario: Usuario){

        usuario.id=currentUserActive.value?.id

        viewModelScope.launch(Dispatchers.IO) {
            userUpdatedSusses.postValue(
                userRepository.updateUser(usuario,currentUserActive)
            )
        }


    }


    fun uploadFile(file: File?,id:String,tipoFichero: TipoFichero){
        file?.let { fileToUplod ->

            viewModelScope.launch(Dispatchers.IO) {

                if (tipoFichero == TipoFichero.USUARIO_PERFIL) {

                    imagenPerfilURL.postValue(
                        filesRepository.uploadSingleFile(
                            file = fileToUplod,
                            context = this@UserControlPanelViewModel,
                            id = id,
                            tipoFichero = tipoFichero,
                        )
                    )

                }
                if (tipoFichero == TipoFichero.USUARIO_VERIFICACION) {

                    imagenVerificacionURL.postValue(
                        filesRepository.uploadSingleFile(
                            file = fileToUplod,
                            context = this@UserControlPanelViewModel,
                            id = id,
                            tipoFichero = tipoFichero,
                        )
                    )


                }

            }






        }

    }




    fun saveUserAuthentication(newPassword:String,rememberPassword:Boolean){
        viewModelScope.launch {
           dataStoreRepository.saveUserAuthentication(
               DataStorageRepository.UserAuthentication(currentUserActive.value?.telefonoMovil,newPassword,rememberPassword)
           )

        }
    }

    override fun onProggressUpdate(porcentage: Int) {

    }



}
