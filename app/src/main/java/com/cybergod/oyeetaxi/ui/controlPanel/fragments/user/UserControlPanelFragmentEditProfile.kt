package com.cybergod.oyeetaxi.ui.controlPanel.fragments.user

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelFragmentEditProfileBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidEmail
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.wordCount

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


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
            if (verifyData()) {

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
        }




    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }


    private fun verifyData(): Boolean {
        val mNombre = binding.tvNombre.editText!!.text.trim().toString()
        val mApellidos: String = binding.tvApellidos.editText!!.text.trim().toString()
        val mfechaNacimiento = binding.tvFechaNacimiento.editText!!.text.trim().toString()
        val mCorreo = binding.tvCorreo.editText!!.text.trim().toString()

        binding.tvNombre.isErrorEnabled=false
        binding.tvApellidos.isErrorEnabled=false
        binding.tvCorreo.isErrorEnabled=false
        binding.tvFechaNacimiento.isErrorEnabled=false

        return when {

            TextUtils.isEmpty(mNombre.trim { it <= ' ' }) -> {
                binding.tvNombre.error = "Por favor introduzca su nombre completo"
                false

            }
            TextUtils.isEmpty(mApellidos.trim { it <= ' ' }) -> {
                binding.tvApellidos.error = "Por favor introduzca sus apellidos"
                false
            }
            mApellidos.wordCount() < 2 -> {
                binding.tvApellidos.error = "Por favor introduzca su segundo apellido"
                false
            }
            !mCorreo.isValidEmail() -> {
                binding.tvCorreo.error = "Por favor introduzca una dirección válida de correo"
                false
            }
            TextUtils.isEmpty(mfechaNacimiento.trim { it <= ' ' }) -> {
                binding.tvCorreo.error = "Por favor introduzca su fecha de nacimiento"
                false
            }

            else -> {
                true
            }

        }

    }



}