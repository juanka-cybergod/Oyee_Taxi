package com.cybergod.oyeetaxi.ui.preferences.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.user.model.pagination.UsuariosPaginados
import com.cybergod.oyeetaxi.api.futures.user.model.requestFilter.UserFilterOptions
import com.cybergod.oyeetaxi.api.futures.user.repositories.UserRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersAdministrationViewModel @Inject constructor(
    private val userRepository: UserRepository
    ) :  BaseViewModel() {

    var usersList: MutableLiveData<List<Usuario>> = MutableLiveData()
    var usuariosPaginadosResponse: MutableLiveData<UsuariosPaginados?> = MutableLiveData<UsuariosPaginados?>()

    var userFilterOptions  = UserFilterOptions()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    var totalPages: Int = 0
    var getPage = 1
    var isLastPage = false
    var isScrolling = false

    val userUpdatedSusses :MutableLiveData<Boolean>  = MutableLiveData<Boolean>()

    init {
        getUsersPaginated()
    }

    fun getUsersPaginated(){

            isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {

                delay(1000)

                val response: UsuariosPaginados? = userRepository.searchUsersPaginatedWithFilter(
                    page = getPage,
                    userFilterOptions = userFilterOptions,
                )

                usuariosPaginadosResponse.postValue(response)

                if (response != null) {
                    totalPages = response.totalPages
                    isLastPage = response.last

                    val listaUsuariosObtenidos = response.content


                    if (getPage == 1 ) {
                        usersList.postValue(listaUsuariosObtenidos)
                    } else {
                        val listaUsuariosAnteriores = usersList.value?.toMutableList()
                        listaUsuariosAnteriores?.addAll(listaUsuariosObtenidos ?: emptyList())
                        usersList.postValue(listaUsuariosAnteriores?.toList())
                    }

                    if (listaUsuariosObtenidos.isNotEmpty()) {
                        getPage++
                    }
                }



                isLoading.postValue(false)

            }




    }


    fun updateUser(usuario: Usuario){


        viewModelScope.launch(Dispatchers.IO) {

            delay(1000)

            val userUpdated : Usuario? = userRepository.updateUser(usuario)
            if (userUpdated != null) {
                currentUserActive.postValue(userUpdated)
                userUpdatedSusses.postValue(true)
                updateUsersList(userUpdated)
            }else {
                userUpdatedSusses.postValue(false)
            }
        }

    }


    private fun updateUsersList(userUpdated: Usuario) {

        usersList.value?.toMutableList()?.let { listaUsuarios ->
            val nuevaListaUsuarios = ArrayList<Usuario>()
            listaUsuarios.forEach { usuario ->
                if (usuario.id.equals(userUpdated.id)) {
                    nuevaListaUsuarios.add(userUpdated)
                } else {
                    nuevaListaUsuarios.add(usuario)
                }
            }
            usersList.postValue(nuevaListaUsuarios)
        }

    }





}