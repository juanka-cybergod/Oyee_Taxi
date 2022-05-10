package com.cybergod.oyeetaxi.ui.preferences.viewmodel


import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.model.RedSocial
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RedesSocialesViewModel @Inject constructor(
   // private val dataStorageRepository: DataStorageRepository,
) : BaseViewModel() {

    lateinit var newSocialConfiguracion: SocialConfiguracion
    var redSocialSeleccionada:MutableLiveData<RedSocial> = MutableLiveData<RedSocial>()




}
