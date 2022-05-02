package com.cybergod.oyeetaxi.ui.userRegistration.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.Ubicacion
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.api.repository.FilesRepository
import com.cybergod.oyeetaxi.api.utils.UploadRequestBody
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import com.cybergod.oyeetaxi.api.model.verification.UsuarioVerificacion
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getRamdomUUID

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val userRepository: UserRepository,
    private val filesRepository: FilesRepository,
    private val dataStoreRepository: DataStorageRepository) : ViewModel(), UploadRequestBody.UploadCallback {

    val userRegistered = dataStoreRepository.readUserRegistred.asLiveData()

    var smsProvider : MutableLiveData<SmsProvider> = MutableLiveData<SmsProvider>(SmsProvider.FIREBASE)
    init {

        viewModelScope.launch(Dispatchers.IO) {
            smsProvider.postValue(
                configurationRepository.getSmsProvider()
            )
        }

    }


    var imagenPerfilURL : String? = null
    var imagenVerificacionURL : String? = null


    var imagenPerfilFile : MutableLiveData<File?> = MutableLiveData<File?>( null)
    var imagenVerificacionFile : MutableLiveData<File?> = MutableLiveData<File?>( null)

    var imagenPerfilURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)
    var imagenVerificacionURI : MutableLiveData<Uri?> = MutableLiveData<Uri?>(null)

    val idUsuario = getRamdomUUID()
    var conductor : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var telefonoMovil : MutableLiveData<String> = MutableLiveData<String>()
    var userAlreadyExistWithPhone : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nombre : MutableLiveData<String> = MutableLiveData<String>()
    var apellidos : MutableLiveData<String> = MutableLiveData<String>()
    var fechaNacimiento : MutableLiveData<String> = MutableLiveData<String>()
    var correo : MutableLiveData<String> = MutableLiveData<String>()
    var provincia : MutableLiveData<Provincia> = MutableLiveData<Provincia>()
    var password : MutableLiveData<String> = MutableLiveData<String>()
    var recordarPassword : MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    var identificaion : MutableLiveData<String> = MutableLiveData<String>()


    suspend fun addUser():Boolean? {

        imagenPerfilURL = uploadSingleFile(imagenPerfilFile.value,idUsuario,TipoFichero.USUARIO_PERFIL)
        imagenVerificacionURL = uploadSingleFile(imagenVerificacionFile.value,idUsuario,TipoFichero.USUARIO_VERIFICACION)

        return if (!imagenPerfilURL.isNullOrEmpty()  &&  !imagenVerificacionURL.isNullOrEmpty() )   {


            userRepository.addUser(userReady())
        } else  null


    }

    private suspend fun uploadSingleFile(file: File?,id:String,tipoFichero: TipoFichero):String?{
        return filesRepository.uploadSingleFile(
            file = file,
            context = this,
            id = id,
            tipoFichero = tipoFichero,
        )
    }




    fun isUserExistByPhone(phone: String){
        viewModelScope.launch(Dispatchers.IO) {
            userAlreadyExistWithPhone.postValue(
                userRepository.userExistByPhone(phone)
            )
        }
    }




    fun userRegisterSuccess(success:Boolean){
        viewModelScope.launch {
            if (!telefonoMovil.value.isNullOrEmpty()) {
                dataStoreRepository.saveUserAuthentication(DataStorageRepository.UserAuthentication(telefonoMovil.value?:"",password.value?:"",recordarPassword.value ?: true))
                dataStoreRepository.saveUserRegistred(success)
            } else {
                dataStoreRepository.saveUserAuthentication(DataStorageRepository.UserAuthentication("","",true))
                dataStoreRepository.saveUserRegistred(true)
            }


        }
    }

    private fun userReady(): Usuario {

        return Usuario(
            id = idUsuario,
            nombre = nombre.value,
            apellidos = apellidos.value,
            contrasena = password.value,
            correo = correo.value,
            fechaDeNacimiento = fechaNacimiento.value,
            fechaDeRegistro = null,
            habilitado = true,
            imagenPerfilURL = imagenPerfilURL.orEmpty(),
            provincia = provincia.value,
            telefonoFijo = "",
            telefonoMovil = telefonoMovil.value,
            conductor = conductor.value,
            modoCondutor = false,
            administrador = false,
            superAdministrador = false,
            usuarioVerificacion = UsuarioVerificacion(
                verificado = false,
                identificacion = identificaion.value?: "",
                imagenIdentificaionURL = imagenVerificacionURL.orEmpty(),
            ),
            ubicacion = Ubicacion(0.0,0.0,0)
            )
    }




    private var fileProgress : MutableLiveData<Int> = MutableLiveData<Int>()
    override fun onProggressUpdate(porcentage: Int) {
        fileProgress.postValue(porcentage)

    }


}