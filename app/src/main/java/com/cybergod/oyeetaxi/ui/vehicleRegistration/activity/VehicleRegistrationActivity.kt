package com.cybergod.oyeetaxi.ui.vehicleRegistration.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.VehicleRegistrationActivityBinding
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.vehicleRegistration.viewmodel.VehicleRegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class VehicleRegistrationActivity: BaseActivity(), Communicator {

    private lateinit var binding:VehicleRegistrationActivityBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = VehicleRegistrationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navController = findNavController(R.id.nav_host_fragment_activity_vehicle_registration)




    }

    override fun passProvinceSelected(province: Provincia) {
        TODO("Not yet implemented")
    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        val viewModel = ViewModelProvider(this)[VehicleRegistrationViewModel::class.java]
        viewModel.tipoVehiculo.postValue( vehicleType )
    }


//    // TODO Step 10: Override the onBackPressed function and call the double back press function created in the base activity.
//    override fun onBackPressed() {
//
//        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_vehicle_registration)
//        when(NavHostFragment.findNavController(fragment!!).currentDestination?.id) {
//            R.id.userRegistrationFragment1-> {
//                super.onBackPressed()
//            }
//            else -> {
//                doubleBackToExit()
//            }
//        }
//    }

}