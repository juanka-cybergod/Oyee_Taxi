package com.cybergod.oyeetaxi.ui.passwordRecovery.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.model.response.RequestVerificationCodeResponse
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class RecoveryPasswordViewModel @Inject constructor(
    private val dataStoreRepository: DataStorageRepository,
    private val userRepository: UserRepository,
    ) : BaseViewModel() {

    val userIdToRestorePassword = dataStoreRepository.readUserIdToRestorePassword.asLiveData()

    var verificationCodeResponse: MutableLiveData<RequestVerificationCodeResponse> = MutableLiveData()

    var verificationCodeOK: MutableLiveData<Boolean> = MutableLiveData()

    val userUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    var recordarPassword : Boolean = false

    val userUpdated:MutableLiveData<Usuario>  = MutableLiveData<Usuario>()

    fun requestVerificationCode(userEmailOrPhone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            verificationCodeResponse.postValue(
                userRepository.requestVerificationCode(userEmailOrPhone)
            )
        }

    }

    fun saveUserIdToRestorePassword(userId: String?){
        userId?.let {
            viewModelScope.launch {
                dataStoreRepository.saveUserIdToRestorePassword(it)
            }

        }
    }

    fun verifyOTPCodeToRestorePassword(idUsuario: String,otpCode: String){
        viewModelScope.launch {
            verificationCodeOK.postValue(
                userRepository.verifyOTPCodeToRestorePassword(idUsuario,otpCode)
            )

        }
    }

    fun updateUser(usuario: Usuario){

        viewModelScope.launch(Dispatchers.IO) {
            userUpdatedSusses.postValue(
                userRepository.updateUser(usuario,userUpdated)
            )
        }

    }


    fun saveUserAuthentication(newPassword:String,rememberPassword:Boolean){
        viewModelScope.launch {
            dataStoreRepository.saveUserAuthentication(
                DataStorageRepository.UserAuthentication(userUpdated.value?.telefonoMovil,newPassword,rememberPassword)
            )

        }
    }

}
