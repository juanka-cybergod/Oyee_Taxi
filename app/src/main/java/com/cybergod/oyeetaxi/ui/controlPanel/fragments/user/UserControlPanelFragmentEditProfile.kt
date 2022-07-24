package com.cybergod.oyeetaxi.ui.controlPanel.fragments.user

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelFragmentEditProfileBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidEmail
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.wordCount

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserControlPanelFragmentEditProfile: BottomSheetDialogFragment()  {

    private var _binding: UserControlPanelFragmentEditProfileBinding? = null
    private val binding get() = _binding!!


    val viewModel: UserControlPanelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserControlPanelFragmentEditProfileBinding.inflate(inflater, container, false)

        loadUserDetails()

        setupOnClickListener()

        return  binding.root

    }

    private fun loadUserDetails() {

        binding.tvNombre.editText?.setText(currentUserActive.value?.nombre)
        binding.tvApellidos.editText?.setText(currentUserActive.value?.apellidos)
        binding.tvCorreo.editText?.setText(currentUserActive.value?.correo)
        binding.tvFechaNacimiento.editText?.setText(currentUserActive.value?.fechaDeNacimiento)

    }

    private fun setupOnClickListener() {


        binding.etFechaNacimiento.setOnClickListener {
            requireView().hideKeyboard()
            val datePicker = DatePickerFragment({ day, month, year -> binding.tvFechaNacimiento.setOnDateSelected(day, month, year)},binding.etFechaNacimiento.text.toString(), setMaxDate = true, setMinDate = false)
            datePicker
                .show(parentFragmentManager,"datePicker")
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) {
                if (verifyData()) {
                    updateUser()
                }
            }

        }




    }

    private fun updateUser() {
        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))

        viewModel.updateUser(
            Usuario(
                nombre = binding.tvNombre.editText?.text?.trim().toString(),
                apellidos = binding.tvApellidos.editText?.text?.trim().toString(),
                correo = binding.tvCorreo.editText?.text?.trim().toString(),
                fechaDeNacimiento = binding.tvFechaNacimiento.editText?.text?.trim().toString(),
            )
        )
        closeThisBottomSheetDialogFragment()
    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }


    private suspend fun verifyData(): Boolean {
        val mNombre = binding.tvNombre.editText!!.text.trim().toString()
        val mApellidos: String = binding.tvApellidos.editText!!.text.trim().toString()
        val mfechaNacimiento = binding.tvFechaNacimiento.editText!!.text.trim().toString()
        val mCorreo = binding.tvCorreo.editText!!.text.trim().toString()

        binding.tvNombre.isErrorEnabled=false
        binding.tvApellidos.isErrorEnabled=false
        binding.tvCorreo.isErrorEnabled=false
        binding.tvFechaNacimiento.isErrorEnabled=false


        var dataOK :Boolean = when {

            //nombre
            mNombre.isEmptyTrim()-> {
                binding.tvNombre.error = "Por favor introduzca su nombre completo"
                false
            }
            //apellidos
            mApellidos.isEmptyTrim()-> {
                binding.tvApellidos.error = "Por favor introduzca sus apellidos"
                false
            }
            //los 2 apellidos
            mApellidos.wordCount() < 2 -> {
                binding.tvApellidos.error = "Por favor introduzca su segundo apellido"
                false
            }
            //correo electronico
            !mCorreo.isValidEmail() -> {
                binding.tvCorreo.error = "Por favor introduzca una dirección válida de correo"
                false
            }
            //fecha de nacimiento
            mfechaNacimiento.isEmptyTrim()-> {
                binding.tvFechaNacimiento.error = "Por favor introduzca su fecha de nacimiento"
                false
            }
            else -> {
                true
            }
        }

        if (dataOK && mCorreo!=currentUserActive.value?.correo) {

            //(requireActivity() as BaseActivity).showProgressDialog(getString(R.string.validating_email))

            //delay(500)

            dataOK = when (viewModel.emailExist(mCorreo)) {
                true -> {
                    binding.tvCorreo.error = "Este correo ya está siendo usado"
                    false
                }
                null -> {
                    requireActivity().showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                    false
                }
                false -> {
                    true
                }

            }


        }

        return dataOK

    }




//    private fun verifyData(): Boolean {
//        val mNombre = binding.tvNombre.editText!!.text.trim().toString()
//        val mApellidos: String = binding.tvApellidos.editText!!.text.trim().toString()
//        val mfechaNacimiento = binding.tvFechaNacimiento.editText!!.text.trim().toString()
//        val mCorreo = binding.tvCorreo.editText!!.text.trim().toString()
//
//        binding.tvNombre.isErrorEnabled=false
//        binding.tvApellidos.isErrorEnabled=false
//        binding.tvCorreo.isErrorEnabled=false
//        binding.tvFechaNacimiento.isErrorEnabled=false
//
//        return when {
//
//            TextUtils.isEmpty(mNombre.trim { it <= ' ' }) -> {
//                binding.tvNombre.error = "Por favor introduzca su nombre completo"
//                false
//
//            }
//            TextUtils.isEmpty(mApellidos.trim { it <= ' ' }) -> {
//                binding.tvApellidos.error = "Por favor introduzca sus apellidos"
//                false
//            }
//            mApellidos.wordCount() < 2 -> {
//                binding.tvApellidos.error = "Por favor introduzca su segundo apellido"
//                false
//            }
//            !mCorreo.isValidEmail() -> {
//                binding.tvCorreo.error = "Por favor introduzca una dirección válida de correo"
//                false
//            }
//            TextUtils.isEmpty(mfechaNacimiento.trim { it <= ' ' }) -> {
//                binding.tvCorreo.error = "Por favor introduzca su fecha de nacimiento"
//                false
//            }
//
//            else -> {
//                true
//            }
//
//        }
//
//    }
//


}