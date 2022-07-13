package com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration


import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.futures.share.model.RedSocial
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RedesSocialesViewModel @Inject constructor(
   // private val dataStorageRepository: DataStorageRepository,
) : BaseViewModel() {

    var newSocialConfiguracion: SocialConfiguracion = SocialConfiguracion()
    var redSocialSeleccionada:MutableLiveData<RedSocial> = MutableLiveData<RedSocial>()




}
