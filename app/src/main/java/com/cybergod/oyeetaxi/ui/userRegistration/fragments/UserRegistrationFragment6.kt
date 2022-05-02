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

        if (viewModel.conductor.value == true) {
            "CONDUCTOR\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
            binding.tvCarnetIdentidad.visibility = View.GONE
            binding.tvLicenciaConduccion.visibility = View.VISIBLE
            binding.tvLicenciaConduccion.editText?.setText(viewModel.identificaion.value ?: "")


        } else {
            "PASAJERO\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
            binding.tvLicenciaConduccion.visibility = View.GONE
            binding.tvCarnetIdentidad.visibility = View.VISIBLE
            binding.tvCarnetIdentidad.editText?.setText(viewModel.identificaion.value ?: "")
        }



    }



    private fun verifyUserData(): Boolean {

        val mNumeroCarnetIdentidad = binding.tvCarnetIdentidad.editText!!.text.trim().toString()
        val mNumeroLicenciaConduccion = binding.tvLicenciaConduccion.editText!!.text.trim().toString()
        val identificacion :String


        return when {

            //mNumeroCarnetIdentidad
            (mNumeroLicenciaConduccion.length < 11) && (viewModel.conductor.value == true) -> {
                showSnackBar(
                    "Por favor introduzca su licencia de conducir completa",
                    true,
                )
                false
            }
            //mNumeroLicenciaConduccion
            (mNumeroCarnetIdentidad.length < 11) && (viewModel.conductor.value == false) -> {
                    showSnackBar(
                        "Por favor introduzca su número de identidad completo",
                    true,
                )
                false
            }
            //foto Verificacion
            viewModel.imagenVerificacionFile.value == null  -> {
                showSnackBar(
                    "Por favor seleccione la fotocopia del documento requerido",
                    true,
                )
                false
            }

            else -> {

                if (viewModel.conductor.value == true) {
                    identificacion = mNumeroLicenciaConduccion
                } else {
                    identificacion = mNumeroCarnetIdentidad
                }
                viewModel.identificaion.value = identificacion


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