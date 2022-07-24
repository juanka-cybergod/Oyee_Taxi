package com.cybergod.oyeetaxi.ui.userRegistration.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment5Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURI
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRegistrationFragment5 : BaseFragment() {


    private var _binding: UserRegistrationFragment5Binding? = null
    private val binding get() = _binding!!


    //Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserRegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment5Binding.inflate(inflater, container, false)


        loadTempDatafromViewModel()

        binding.continueButton.setOnClickListener {
            if (verifyUserData()) {
                goToNextFragment()
            }
        }


        binding.checkboxRememberPassword.setOnClickListener {
            val checkboxCheckState : Boolean = (it as CheckBox).isChecked
            viewModel.recordarPassword.postValue(checkboxCheckState)
        }


        return  binding.root
    }


    private fun loadTempDatafromViewModel() {


        binding.imagePerfil.loadImagePerfilFromURI(viewModel.imagenPerfilURI.value)

        if (viewModel.conductor.value == true) {
            "CONDUCTOR\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
        } else {
            "PASAJERO\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
        }

        binding.checkboxRememberPassword.isChecked = viewModel.recordarPassword.value ?: true


        binding.tvPassword1.editText?.setText(viewModel.password.value ?: "")
        binding.tvPassword2.editText?.setText(viewModel.password.value ?: "")

    }


    private fun verifyUserData(): Boolean {
        val mPassword1 = binding.tvPassword1.editText!!.text.trim().toString()
        val mPassword2 = binding.tvPassword2.editText!!.text.trim().toString()

        binding.tvPassword1.isErrorEnabled=false
        binding.tvPassword2.isErrorEnabled=false

        return when {
            mPassword1.isEmptyTrim() -> {
                binding.tvPassword1.error = "Por favor introduzca una contraseña"
                false
            }
            mPassword1.length < 6 -> {
                binding.tvPassword1.error = "La contraseña deberá tener más de 6 caracteres"
                false
            }
            mPassword2.isEmptyTrim() -> {
                binding.tvPassword2.error = "Por favor verifique su contraseña"
                false
            }
           mPassword1 != mPassword2 -> {
               binding.tvPassword2.error = "La verificación de la contraseña no coincide"
                false
            }

            else -> {
                viewModel.password.postValue(mPassword1)
                true
            }

        }




    }


    private fun goToNextFragment(){
        findNavController().navigate(R.id.action_userRegistrationFragment5_to_userRegistrationFragment6)
    }





}

