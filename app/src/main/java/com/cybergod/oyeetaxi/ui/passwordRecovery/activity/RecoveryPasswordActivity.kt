package com.cybergod.oyeetaxi.ui.passwordRecovery.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.ActivityRecoveryPasswordBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.PasswordRecoveryFragment
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.passwordRecovery.viewmodel.RecoveryPasswordViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidEmail
import com.cybergod.oyeetaxi.utils.UtilsGlobal.pasteFromClipBoard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecoveryPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityRecoveryPasswordBinding

    val viewModel: RecoveryPasswordViewModel by viewModels()

    lateinit var mCorreoPhone :String
    lateinit var mVerificationCode :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)





        setupOnClickListeners()


        setupObservers()



    }

    private fun setupOnClickListeners(){

        binding.btnSolicitarCodigo.setOnClickListener {
            if (verifyUserData()) {
                requestVerificationCode()
            }
        }

        //escucha el Click en boton continar
        binding.btnRestorePassword.setOnClickListener {

            if (viewModel.verificationCodeOK.value == true) {
                //Ya esta verivicado lanzar el fragment de cambiar la contraseña
                launchPasswordRecoveryFragment()

            } else {
                //iniciar verificacion
                if (verifyOTPData()){
                    verifyOTPCode()
                }

            }



        }


        binding.tvCode.setEndIconOnClickListener {
            View(this).hideKeyboard()
            binding.tvCode.editText?.setText(pasteFromClipBoard())

        }



    }

    private fun verifyOTPCode() {
        showProgressDialog("Verificación en curso ...")

        lifecycleScope.launch {

            delay(200)
            viewModel.verifyOTPCodeToRestorePassword(viewModel.userIdToRestorePassword.value!!,mVerificationCode)

        }

    }


    private fun requestVerificationCode() {
        showProgressDialog("Solicitud en curso ...")

        lifecycleScope.launch {

            delay(200)
            viewModel.requestVerificationCode(mCorreoPhone)

        }
    }

    private fun setupObservers() {

        viewModel.userIdToRestorePassword.observe(this, Observer { userId ->
            userId?.let {
//                showSnackBar(
//                    it,
//                    false
//                )
            }

        })

        viewModel.verificationCodeOK.observe(this, Observer { verificationCodeOK ->

            hideProgressDialog()

            if (verificationCodeOK != null) {

                if (verificationCodeOK == true) {
                    //codigo correcto mostrar la ventana para cambiar la contraseña

                    binding.tvPhoneOrEmail.isEnabled = false
                    binding.tvCode.isEnabled = false
                    binding.tvCode.helperText = "El código de verificación es correcto"

                    launchPasswordRecoveryFragment()

                } else {
                    //codigo incorrecto
                    binding.tvCode.error = "Código de verificación incorrecto o expirado"
                }



            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }

        })

        viewModel.verificationCodeResponse.observe(this, Observer { requestVerificationCodeResponse ->

            hideProgressDialog()

            if (requestVerificationCodeResponse != null) {

                if (!requestVerificationCodeResponse.usuarioId.isNullOrEmpty()) {

                    if (requestVerificationCodeResponse.codigoEnviado == true) {
                        //codigo enviado
                        binding.tvPhoneOrEmail.isEnabled = false
                        binding.btnSolicitarCodigo.isEnabled=false
                        binding.btnSolicitarCodigo.setText("Código Enviado Satisfactoriamente")

                        viewModel.saveUserIdToRestorePassword(requestVerificationCodeResponse.usuarioId)

                        showMensaje(getString(R.string.message),getString(R.string.verification_code_sent),errorType = false)

                    } else {
                        //error de envio por parte del servidor intentarlo mas tarde
                        showMensaje(getString(R.string.message),getString(R.string.server_fail_to_sent_email),errorType = true)
                    }

                } else {
                    //usuario no encontrado
                    binding.tvPhoneOrEmail.error = "El usuario no existe o este dato es incorrecto"

                }

            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }


        })

        viewModel.userUpdatedSusses.observe(this, Observer { updatedOK ->

            (this as BaseActivity).hideProgressDialog()

            if (updatedOK != null){

                if (updatedOK) {


                    binding.btnRestorePassword.isEnabled = false
                    showMensaje(getString(R.string.message),"Su contraseña ha sido restablecida satisfactoriamente. Vuelva a la pantalla anterior para iniciar sesión",false)

                } else {
                    showSnackBar(
                        getString(R.string.user_update_fail),
                        true
                    )
                }

            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }



        })

    }





    private fun showMensaje(title: String, text: String,errorType:Boolean) {
            val ico = if (errorType) {
                R.drawable.ic_error_24
            } else {
                R.drawable.ic_check_circle_24
            }

            val builder = AlertDialog.Builder(this)
            builder
                .setTitle(title)
                .setMessage(text)
                .setIcon(ico)
                .setPositiveButton("OK"){ dialogInterface , _ ->
                    dialogInterface.dismiss()



                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
    }



    private fun verifyOTPData(): Boolean {

        mVerificationCode = binding.tvCode.editText!!.text.trim().toString()

        binding.tvPhoneOrEmail.isErrorEnabled = false
        binding.tvCode.isErrorEnabled = false


        return when {
            viewModel.userIdToRestorePassword.value.isNullOrEmpty() -> {
                binding.tvPhoneOrEmail.error = "Por favor solicite el código de verificación"
                false
            }

            TextUtils.isEmpty(mVerificationCode.trim { it <= ' ' }) -> {
                binding.tvCode.error = "Por favor introduzca el código de verificación"
                false
            }
            mVerificationCode.length != 6 ->{
                binding.tvCode.error = "El código de verificación está incompleto"
                false
            }
            else -> {
                true
            }

        }

    }

    private fun verifyUserData(): Boolean {

        mCorreoPhone = binding.tvPhoneOrEmail.editText!!.text.trim().toString()

        binding.tvPhoneOrEmail.isErrorEnabled=false
        binding.tvCode.isErrorEnabled = false



        return when {
            //Usuario vacio
            mCorreoPhone.isEmptyTrim() -> {
                binding.tvPhoneOrEmail.error = "Introduzca su correo o su teléfono"
                false
            }
            //correo electronico
            mCorreoPhone.contains("@",true) &&  (!mCorreoPhone.isValidEmail()) -> {
                binding.tvPhoneOrEmail.error = "Introduzca una dirección válida de correo o su teléfono"
                false
            }

            else -> {
                true
            }

        }


    }

    private fun launchPasswordRecoveryFragment(){
            val dialogPersonas = PasswordRecoveryFragment()
            dialogPersonas.show(this.supportFragmentManager,"passwordRecoveryFragment")
    }



}