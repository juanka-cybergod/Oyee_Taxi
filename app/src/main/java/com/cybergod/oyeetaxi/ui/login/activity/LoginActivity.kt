package com.cybergod.oyeetaxi.ui.login.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.databinding.ActivityLoginBinding
import com.cybergod.oyeetaxi.ui.login.viewmodel.LoginViewModel
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.main.activity.MainActivity
import com.cybergod.oyeetaxi.ui.passwordRecovery.activity.RecoveryPasswordActivity
import com.cybergod.oyeetaxi.ui.userRegistration.activity.UserRegistrationActivity
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.simpleAlertDialog
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    val viewModel: LoginViewModel by viewModels()

    lateinit var mUsuario :String
    lateinit var mPassword :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadAuthentication()


        setupOnClickListeners()


        setupObservers()



    }

    private fun setupOnClickListeners(){


        //escucha el Click en boton continar
        binding.continueButton.setOnClickListener {
            if (verifyUserData()) {
                login()
            }
        }

        //escucha el Click en boton registrar nuevo usuario
        binding.tvRegister.setOnClickListener {
            launchUserRegistrationActivity()
        }

        binding.tvForgetPassword.setOnClickListener{
            launchRecoveryPaswwordActivity()
        }

        binding.tvPassword.editText?.addTextChangedListener {
            if (it.toString().trim().isEmpty()){
                //binding.tvPassword.isEndIconCheckable = true
                        binding.tvPassword.endIconMode = END_ICON_PASSWORD_TOGGLE
            }

        }



    }

    private fun setupObservers() {

        viewModel.loginRespuesta.observe(this, Observer { loginRespusta ->

            hideProgressDialog()

            if (loginRespusta != null) {

                if (loginRespusta.usuarioEncontrado != false) {
                    if (loginRespusta.contrasenaCorrecta != false) {
                        if (loginRespusta.usuario?.habilitado!= false) {

                            if (!loginRespusta.mensajeBienvenida.isNullOrEmpty()) {
                                showMensajeBienvenida(getString(R.string.message),loginRespusta.mensajeBienvenida)
                            } else {
                                iniciarSesion()
                            }

                        } else {

                            if (!loginRespusta.mensajeBienvenida.isNullOrEmpty()) {
                                simpleAlertDialog(
                                    getString(R.string.user_disabled),
                                    getString(R.string.reason) + loginRespusta.mensajeBienvenida)
                            } else {
                                showSnackBar(
                                    getString(R.string.user_temporary_disabled),
                                    true,
                                )
                            }

                        }
                    } else {
                        binding.tvPassword.error = getString(R.string.incorrect_password)
                        binding.tvForgetPassword.visibility = View.VISIBLE
                    }

                } else {
                    binding.tvPhone.error = getString(R.string.user_not_found)
                }


            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }


        })
    }

    private fun iniciarSesion() {

        currentUserActive.value = viewModel.loginRespuesta.value?.usuario
        currentVehicleActive.value = viewModel.loginRespuesta.value?.vehiculoActivo

        viewModel.userLoginSuccess(
            DataStorageRepository.UserAuthentication(mUsuario,mPassword,binding.checkboxRememberPassword.isChecked),
        )

        launchMainActivity()
    }

    private fun showMensajeBienvenida(title: String, text: String) {
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle(title)
                .setMessage(text)
                .setIcon(R.mipmap.ic_launcher_foreground)
                .setPositiveButton("OK"){ dialogInterface , _ ->
                    dialogInterface.dismiss()
                    iniciarSesion()
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
    }

    private fun launchMainActivity() {
        lifecycleScope.launch {

            delay(100)

            startActivity(
                Intent(this@LoginActivity,
                    MainActivity::class.java).apply {
                       // putExtra(EXTRA_LOGIN_RESPONSE,viewModel.loginRespuesta.value)
                }
            )
            this@LoginActivity.finish() //cerrar esta actividad
        }
    }

    private fun launchRecoveryPaswwordActivity() {
        lifecycleScope.launch {

            delay(100)

            startActivity(
                Intent(this@LoginActivity,
                    RecoveryPasswordActivity::class.java).apply {
                }
            )
        }
    }

    private fun launchUserRegistrationActivity(){
        lifecycleScope.launch {

            delay(100)

            startActivity(
                Intent(this@LoginActivity,
                    UserRegistrationActivity::class.java)
            )

        }

    }

    private fun login() {

        showProgressDialog("Iniciando sesión ...")



         lifecycleScope.launch {

            delay(1500)
             viewModel.login(mUsuario,mPassword)

        }

    }

    private fun verifyUserData(): Boolean {

        mUsuario = binding.tvPhone.editText!!.text.trim().toString()
        mPassword = binding.tvPassword.editText!!.text.trim().toString()

        binding.tvPhone.isErrorEnabled=false
        binding.tvPassword.isErrorEnabled=false

        binding.tvForgetPassword.visibility = View.INVISIBLE

        return when {
            //Usuario vacio
            TextUtils.isEmpty(mUsuario.trim { it <= ' ' }) -> {
                binding.tvPhone.error = "Por favor introduzca su número de teléfono"
                false
            }
            //contaseña vacia
            TextUtils.isEmpty(mPassword.trim { it <= ' ' }) -> {
                binding.tvPassword.error = "Por favor introduzca su contraseña"
                false
            }
            else -> {
                true
            }

        }


    }

    private fun loadAuthentication() {
        viewModel.userAUTH.observe(this, Observer { authData ->
            if (authData != null) {

                if (authData.rememberPassword) {

                    binding.checkboxRememberPassword.isChecked = true
                    binding.tvPhone.editText!!.setText(authData.userNumber)
                    binding.tvPassword.editText!!.setText(authData.password)

                } else {
                    binding.checkboxRememberPassword.isChecked = false
                }

            } else {
               // Toast.makeText(this,"NO DATA",Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onBackPressed() {
                doubleBackToExit()
    }





}