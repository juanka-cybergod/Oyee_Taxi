package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.DialogEditUserProfileBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.wordCount

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditUserProfileFragment: BottomSheetDialogFragment()  {

    private var _binding: DialogEditUserProfileBinding? = null
    private val binding get() = _binding!!


    val viewModel: UsersAdministrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogEditUserProfileBinding.inflate(inflater, container, false)


        requireArguments().getParcelable<Usuario>(Constants.KEY_USER_PARCELABLE)?.let { usuario ->
            loadUserDetails(usuario)
        }


        setupOnClickListener()

        return  binding.root

    }

    private fun loadUserDetails(usuario: Usuario) {

        binding.tvNombre.editText?.setText(usuario.nombre)
        binding.tvApellidos.editText?.setText(usuario.apellidos)
        binding.tvFechaNacimiento.editText?.setText(usuario.fechaDeNacimiento)

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


        binding.tvNombre.isErrorEnabled=false
        binding.tvApellidos.isErrorEnabled=false
        binding.tvFechaNacimiento.isErrorEnabled=false

        return when {

            mNombre.isEmptyTrim() -> {
                binding.tvNombre.error = "Por favor introduzca el nombre completo"
                false
            }
            mApellidos.isEmptyTrim() -> {
                binding.tvApellidos.error = "Por favor introduzca los apellidos"
                false
            }
            mApellidos.wordCount() < 2 -> {
                binding.tvApellidos.error = "Por favor introduzca el segundo apellido"
                false
            }
            else -> {
                true
            }

        }

    }



}