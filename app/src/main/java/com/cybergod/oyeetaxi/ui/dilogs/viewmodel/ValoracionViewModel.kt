package com.cybergod.oyeetaxi.ui.dilogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.valoration.model.Valoracion
import com.cybergod.oyeetaxi.api.futures.valoration.repositories.ValorationRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValoracionViewModel @Inject constructor(private val valorationRepository: ValorationRepository) :  BaseViewModel() {

    var currentValoracion: MutableLiveData<Valoracion> = MutableLiveData()

    var idUsuarioValora:String? = null
    var idUsuarioValorado:String? = null
    var valoracion:Float? = null
    var opinion:String? = null
    var usuarioValoradoImagenPerfilURL:String? = null

    var valoracionUpdatedSussefuctly: MutableLiveData<Valoracion> = MutableLiveData()


    fun getValorationByUsersId(){

        if (!idUsuarioValora.isNullOrEmpty() && !idUsuarioValorado.isNullOrEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                currentValoracion.postValue(
                    valorationRepository.getValorationByUsersId(idUsuarioValora!!,idUsuarioValorado!!)
                )


            }

        }

    }


    fun addUpdateValoration(){

        viewModelScope.launch(Dispatchers.IO) {
            delay(200L)
            valoracionUpdatedSussefuctly.postValue(
                valorationRepository.addUpdateValoration(prepareValoration())
            )

        }

    }

    private fun prepareValoration(): Valoracion {

        return Valoracion(
            id = currentValoracion.value?.id,
            idUsuarioValora = idUsuarioValora,
            idUsuarioValorado = idUsuarioValorado,
            valoracion = valoracion,
            opinion = opinion,
        )

    }


}