package com.cybergod.oyeetaxi.ui.login.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.api.repository.UserRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStorageRepository,
    private val userRepository: UserRepository,
   // private val protoDataStorageRepository: ProtoDataStorageRepository,

    ) : BaseViewModel() {

    val userAUTH = dataStoreRepository.readUserAuthenticationFromDataStore.asLiveData()

    var loginRespuesta: MutableLiveData<LoginRespuesta> = MutableLiveData()

    fun login(userPhone: String,password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            loginRespuesta.postValue(
                userRepository.loginUser(userPhone, password)
            )

        }
    }

    fun userLoginSuccess(userAuthentication:DataStorageRepository.UserAuthentication){
        viewModelScope.launch {
            dataStoreRepository.saveUserAuthentication(userAuthentication)
            dataStoreRepository.saveUserRegistred(true)
        }
    }


}
