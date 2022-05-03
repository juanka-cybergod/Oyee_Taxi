package com.cybergod.oyeetaxi.ui.splash.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.databinding.ActivitySplashBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.UpdateApplicationFragment
import com.cybergod.oyeetaxi.ui.login.activity.LoginActivity
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.splash.viewmodel.SplashViewModel
import com.cybergod.oyeetaxi.ui.userRegistration.activity.UserRegistrationActivity
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionInt
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isGooglePlayServicesAvailable
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            getAvailableUpdates()
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

        viewModel.userRegistered.observe(this, Observer {
        })

        viewModel.rememberAppUpdateAfterDate.observe(this, Observer { lastDate ->
            if (!lastDate.isNullOrEmpty()) {
                viewModel.omitActualization = lastDate == getCurrentDate()
            } else
                viewModel.omitActualization = false
        })

        viewModel.continueNow.observe(this, Observer {
            if (it == true) {
                noUpdateAndContinue()
            }
        })


    }

    private fun getAvailableUpdates() {


        lifecycleScope.launch (Dispatchers.Main){

            val updateConfiguration = viewModel.getAvailableUpdate()
            if (updateConfiguration != null ){
                if (updateConfiguration.available == true) {

                    if (getAppVersionInt() < updateConfiguration.version?:1){

                        if (updateConfiguration.forceUpdate == false && viewModel.omitActualization) {
                            noUpdateAndContinue()
                        } else {
                            launchUpdateApplicationFragment()
                        }

                    } else {
                        noUpdateAndContinue()
                        //Toast.makeText(requireContext(),"Su aplicación está actualizada", Toast.LENGTH_SHORT).show()
                    }



                } else {
                    noUpdateAndContinue()
                    //Toast.makeText(requireContext(),"Actualizaciones desactivadas temporalmente",Toast.LENGTH_SHORT).show()

                }

            }  else {
                noUpdateAndContinue()
                //Toast.makeText(requireContext(),"Falló la conexion con el Servidor para obtener actualizacions", Toast.LENGTH_SHORT).show()
            }




        }
    }


    private fun noUpdateAndContinue(){

        val userAlreadyRegitered = viewModel.userRegistered.value

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