package com.cybergod.oyeetaxi.ui.controlPanel.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.file.repositories.FilesRepository
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.user.repositories.UserRepository
import com.cybergod.oyeetaxi.api.futures.file.request_body.UploadRequestBody
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

            val userUpdated : Usuario? = userRepository.updateUser(usuario)
            if (userUpdated != null) {
                currentUserActive.postValue(userUpdated)
                userUpdatedSusses.postValue(true)
            }else {
                userUpdatedSusses.postValue(false)
            }

        }


    }


    fun uploadFile(file: File?,id:String,tipoFichero: TipoFichero){
        file?.let { fileToUplod ->

            viewModelScope.launch(Dispatchers.IO) {

                if (tipoFichero == TipoFichero.USUARIO_PERFIL) {

                    imagenPerfilURL.postValue(
                        filesRepository.uploadSingleFileByType(
                            file = fileToUplod,
                            context = this@UserControlPanelViewModel,
                            id = id,
                            tipoFichero = tipoFichero,
                        )
                    )

                }
                if (tipoFichero == TipoFichero.USUARIO_VERIFICACION) {

                    imagenVerificacionURL.postValue(
                        filesRepository.uploadSingleFileByType(
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
