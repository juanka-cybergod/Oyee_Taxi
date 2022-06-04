package com.cybergod.oyeetaxi.ui.userRegistration.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment6Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.splash.activity.SplashActivity
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURI
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageUserVerificacionFromURI
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.cybergod.oyeetaxi.utils.GlobalVariables
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserRegistrationFragment6 : BaseFragment() {


    private var _binding: UserRegistrationFragment6Binding? = null
    private val binding get() = _binding!!


    val viewModel: UserRegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment6Binding.inflate(inflater, container, false)

        loadTempDatafromViewModel()


        setupOnclickListener()


        return  binding.root
    }

    private fun setupOnclickListener() {
        binding.continueButton.setOnClickListener {
            if (verifyUserData()) {
                addNewUser()
            }
        }

        binding.buttonSelectImageVerification.setOnClickListener {
            openImageChooser()
        }
    }


    private fun openImageChooser() {
        requireView().hideKeyboard()
        startActivityForResult.launch("image/jpeg")
    }


    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {uri ->

            if (uri != null ) {

                viewModel.imagenVerificacionURI.value = uri

                binding.ivImageVerificacion.loadImageUserVerificacionFromURI(uri,viewModel.conductor.value)

                viewModel.imagenVerificacionFile.postValue(
                    requireContext().prepareImageCompressAndGetFile(uri)
                )

            }



        })



    private fun loadTempDatafromViewModel() {

        binding.imagePerfil.loadImagePerfilFromURI(viewModel.imagenPerfilURI.value)

        binding.ivImageVerificacion.loadImageUserVerificacionFromURI(viewModel.imagenVerificacionURI.value,viewModel.conductor.value)


        binding.tvIdentificacion.editText?.setText(viewModel.identificaion.value ?: "")

        binding.tvIdentificacion.hint =
            if (viewModel.conductor.value == true) {
                "CONDUCTOR\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
                "Licencia de Conducción"
            } else {
                "PASAJERO\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
                "Número de Identidad"
            }





    }



    private fun verifyUserData(): Boolean {

        val mNumeroIdentificacion = binding.tvIdentificacion.editText!!.text.trim().toString()

        binding.tvIdentificacion.isErrorEnabled = false

        return when {

            //mNumeroCarnetIdentidad
            (mNumeroIdentificacion.length < 11) && (GlobalVariables.currentUserActive.value?.conductor == true) -> {
                binding.tvIdentificacion.error =
                    "Por favor introduzca su licencia de conducir completa"
                false
            }
            //mNumeroLicenciaConduccion
            (mNumeroIdentificacion.length < 11) && (GlobalVariables.currentUserActive.value?.conductor == false) -> {
                binding.tvIdentificacion.error =
                    "Por favor introduzca su número de identidad completo"
                false
            }
            //foto Verificacion
            (viewModel.imagenVerificacionFile.value == null) && (GlobalVariables.currentUserActive.value?.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty()) -> {
                binding.tvIdentificacion.error =
                    "Por favor adjunte fotocopia del documento requerido"
                false
            }
            else -> {

                viewModel.identificaion.value = mNumeroIdentificacion
                true
            }

        }

    }


    private fun addNewUser() {

        showProgressDialog("Finalizando Registro de Usuario ...")

        lifecycleScope.launch(Dispatchers.IO) {

            delay(200L)

            when (viewModel.addUser()) {
                true -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.user_registered_secces),
                        false,
                    )
                    viewModel.userRegisterSuccess(true)
                    launchSplashActivity()

                }
                false -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.user_register_fail),
                        true
                    )

                }
                null -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true,
                    )
                }
            }

        }

    }


    private fun launchSplashActivity(){
        lifecycleScope.launch(Dispatchers.Main) {

            delay(600)

            startActivity(
                Intent(requireActivity(),
                    SplashActivity::class.java)
            )
            requireActivity().finish() //cerrar esta actividad
        }
    }



}