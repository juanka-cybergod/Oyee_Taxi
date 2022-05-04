package com.cybergod.oyeetaxi.ui.main.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.ActivityMainBinding
import com.cybergod.oyeetaxi.maps.Utils.showDialogEnableGPS
import com.cybergod.oyeetaxi.services.ServiceController.startLocationService
import com.cybergod.oyeetaxi.services.TrackerService
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.main.viewmodel.HomeViewModel
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.permissions.activity.PermissionsActivity
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasBackgroundLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasStoragePermission
import com.cybergod.oyeetaxi.utils.GlobalVariables.NetworkErrorFound
import com.cybergod.oyeetaxi.utils.GlobalVariables.NetworkErrorMessaje
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(), Communicator {


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        //Preparar los Observers para los problemas de Conexion
        setupNetworkConnectionObservers()

        //Preparar Observer para que esté habilitado el GPS
        setupGPSEnabledObserver()

        //Cambiar Color del Status Bar en Tiempo de ejecucion
        window.statusBarColor = ContextCompat.getColor(this, R.color.casiBlanco);

    }


    private fun setupGPSEnabledObserver(){

        TrackerService.isGPSEnabled.observe(this, Observer {
            it?.let { gpsEnabled->


                if (!gpsEnabled) {
                    showDialogEnableGPS(this)

                }



            }
        })

    }

    private fun setupNetworkConnectionObservers(){

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.registerConectionsObservers(this,this)

        NetworkErrorFound.observe(this) {
            if (it) {
                binding.llNetworkError.visibility = View.VISIBLE
                binding.tvNetworkErrorMessage.text = NetworkErrorMessaje
                binding.llNetworkError.setBackgroundColor(Color.RED)
                window.statusBarColor = ContextCompat.getColor(this,  R.color.colorSnackBarError);
            } else {
                binding.llNetworkError.visibility = View.GONE
                window.statusBarColor = ContextCompat.getColor(this,  R.color.yellow_light);
            }

        }

    }


    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        when(NavHostFragment.findNavController(fragment!!).currentDestination?.id) {
            R.id.navigation_home-> {
                doubleBackToExit()

            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun passProvinceSelected(province: Provincia) {
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.goToProvince.postValue( province )
    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        //
    }


    override fun onResume() {

        //Chequea los Permisos que deben estar Siempre Activados
        if (!hasLocationPermission(this) || !hasStoragePermission(this) || !hasBackgroundLocationPermission(this) ) {
            launchPermissionsActivity()
        }else {

            if (hasLocationPermission(this) && hasBackgroundLocationPermission(this)){

                if (TrackerService.started.value != true) {
                    this.startLocationService()
                }



                TrackerService.isGPSEnabled.value?.let { gpsEnabled->
                    if (!gpsEnabled) {
                        showDialogEnableGPS(this)

                    }
                }

            }



        }

        super.onResume()
    }


    private fun launchPermissionsActivity(){

        startActivity(
            Intent(this,
                PermissionsActivity::class.java)
        )


    }

}