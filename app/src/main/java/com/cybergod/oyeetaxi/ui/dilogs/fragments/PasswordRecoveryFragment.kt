package com.cybergod.oyeetaxi.ui.dilogs.fragments


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.databinding.FragmentPasswordRecoveryBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.passwordRecovery.viewmodel.RecoveryPasswordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PasswordRecoveryFragment : BottomSheetDialogFragment() {

        private var _binding: FragmentPasswordRecoveryBinding? = null
        private val binding get() = _binding!!


    val viewModel: RecoveryPasswordViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)


        setupOnClickListener()



        return  binding.root

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
                        id = viewModel.userIdToRestorePassword.value,
                        contrasena = newPassword,
                        otpCode = "",
                    )
                )

                viewModel.saveUserAuthentication(newPassword,viewModel.recordarPassword)

                closeThisBottomSheetDialogFragment()

            }
        }

    }


    private fun verifyData(): Boolean {

        val mPassword1 = binding.tvPassword1.editText!!.text.trim().toString()
        val mPassword2 = binding.tvPassword2.editText!!.text.trim().toString()

        binding.tvPassword1.isErrorEnabled=false
        binding.tvPassword2.isErrorEnabled=false


        return when {

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


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}