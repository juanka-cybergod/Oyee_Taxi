package com.cybergod.oyeetaxi.ui.controlPanel.fragments.user

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelFragmentEditSecurityBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal.passwordDecode


import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserControlPanelFragmentEditSecurity: BottomSheetDialogFragment()  {

    private var _binding: UserControlPanelFragmentEditSecurityBinding? = null
    private val binding get() = _binding!!

    //Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserControlPanelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserControlPanelFragmentEditSecurityBinding.inflate(inflater, container, false)

        //this.isCancelable=false

        loadUserDetails()

        setupOnClickListener()

        return  binding.root


    }

    private fun loadUserDetails() {
        viewModel.userAUTH.observe(this, Observer { authData ->
            if (authData != null) {
                binding.checkboxRememberPassword.isChecked = authData.rememberPassword
            }
        })

    }

    private fun setupOnClickListener() {

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))

                val newPassword =  binding.tvPassword1.editText?.text?.trim().toString()

                viewModel.updateUser(
                    Usuario(
                        contrasena = newPassword,
                    )
                )

                viewModel.saveUserAuthentication(newPassword,viewModel.recordarPassword)

                closeThisBottomSheetDialogFragment()

            }
        }


    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }

    private fun verifyData(): Boolean {

        val mPassword0 = binding.tvPassword0.editText!!.text.trim().toString()
        val mPassword1 = binding.tvPassword1.editText!!.text.trim().toString()
        val mPassword2 = binding.tvPassword2.editText!!.text.trim().toString()

        binding.tvPassword0.isErrorEnabled=false
        binding.tvPassword1.isErrorEnabled=false
        binding.tvPassword2.isErrorEnabled=false


        return when {

            //contaseña actual
            TextUtils.isEmpty(mPassword0.trim { it <= ' ' }) -> {
                binding.tvPassword0.error = "Por favor introduzca su contraseña actual"
                false
            }
            //contraseña actual incorrecta
            mPassword0 != passwordDecode(currentUserActive.value?.contrasena?:"")  -> {
                binding.tvPassword0.error = "Contraseña incorrecta"
                false
            }

            //contaseña vacia
            TextUtils.isEmpty(mPassword1.trim { it <= ' ' }) -> {
                binding.tvPassword1.error = "Por favor introduzca una nueva contraseña"
                false
            }
            //contaseña incomleta
            mPassword1.length < 6 -> {
                binding.tvPassword1.error = "La contraseña deberá tener más de 6 caracteres"
                false
            }
            //verificacion de contaseña vacia
            TextUtils.isEmpty(mPassword2.trim { it <= ' ' }) -> {
                binding.tvPassword2.error = "Por favor verifique su nueva contraseña"
                false
            }
            //no coinciden las contraseñas
            mPassword1 != mPassword2 -> {
                binding.tvPassword2.error = "La verificación de la contraseña no coincide"
                false
            }

            else -> {
                viewModel.recordarPassword = binding.checkboxRememberPassword.isChecked
                true
            }

        }












    }



}