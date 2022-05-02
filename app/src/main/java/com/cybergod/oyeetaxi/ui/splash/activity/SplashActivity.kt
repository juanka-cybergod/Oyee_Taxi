package com.cybergod.oyeetaxi.ui.splash.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.databinding.ActivitySplashBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.UpdateApplicationFragment
import com.cybergod.oyeetaxi.ui.login.activity.LoginActivity
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.splash.viewmodel.SplashViewModel
import com.cybergod.oyeetaxi.ui.userRegistration.activity.UserRegistrationActivity
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isGooglePlayServicesAvailable
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionInt
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {


    private lateinit var binding: ActivitySplashBinding

    val delay : Long = 100// 1200

    private val dialogUpdateApplication = UpdateApplicationFragment()

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (isGooglePlayServicesAvailable(this) ) {
            setupObservers()
        } else {
            //finish()
        }


    }



    private fun setupObservers() {

        viewModel.readMapStyle.observe(this, Observer {
            it?.let {
                currentMapStyle = it
            }

        })

        viewModel.userRegistred.observe(this, Observer {
        })

        viewModel.rememberAppUpdateAfterDate.observe(this, Observer { lastDate ->
            if (!lastDate.isNullOrEmpty()) {
                viewModel.omitirActualizacion = lastDate == getCurrentDate()
            } else
                viewModel.omitirActualizacion = false
        })

        viewModel.continueNow.observe(this, Observer {
            if (it == true) {
                noUpdateAndContinue()
            }
        })

        viewModel.updateConfiguration.observe(this, Observer { updateConfiguration ->
            if (updateConfiguration != null ){
                if (updateConfiguration.available == true) {

                    if (getAppVersionInt() < updateConfiguration.version?:1){

                        if (updateConfiguration.forceUpdate == false && viewModel.omitirActualizacion) {

                            noUpdateAndContinue()

                        } else {

                            launchUpdateApplicationFragment()

                        }




                    } else {
                        //Toast.makeText(this,"Aplicación Actualizada",Toast.LENGTH_SHORT).show()
                        noUpdateAndContinue()
                    }



                } else {
                    //Toast.makeText(this,"Actualizaciones Desactivadas Temporalmente",Toast.LENGTH_SHORT).show()
                    noUpdateAndContinue()
                }

            }  else {
                //Toast.makeText(this,"Falló la conexion con el Servidor para obtener actualizacions",Toast.LENGTH_SHORT).show()
                noUpdateAndContinue()
            }

        })

        viewModel.getUpdateConfiguration()


    }






    private fun noUpdateAndContinue(){

        val userAlreadyRegitered = viewModel.userRegistred.value

        if (userAlreadyRegitered == false) {
            launchUserRegistrationActivity()
        } else {
            launchLoginActivity()
        }


    }

    private fun launchLoginActivity(){
        startActivity(
            Intent(this@SplashActivity,
                LoginActivity::class.java)
        )

        finish()


    }

    private fun launchUserRegistrationActivity(){
        startActivity(
             Intent(this@SplashActivity,
                 UserRegistrationActivity::class.java)
        )
        finish()
    }

    private fun launchUpdateApplicationFragment() {

        if (!dialogUpdateApplication.isVisible) {
            dialogUpdateApplication.show(supportFragmentManager,"updateApplicationFragment")
        }

    }



}