package com.cybergod.oyeetaxi.ui.userRegistration.activity


import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.UserRegistrationActivityBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class UserRegistrationActivity : BaseActivity(),  Communicator {


    private lateinit var binding: UserRegistrationActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = UserRegistrationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_user_registration)
        when(NavHostFragment.findNavController(fragment!!).currentDestination?.id) {
            R.id.userRegistrationFragment2byFirebase-> {
                doubleBackToExit()
            }
            R.id.userRegistrationFragment2byTwilio-> {
                doubleBackToExit()
            }
            R.id.userRegistrationFragment3-> {
                doubleBackToExit()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun passProvinceSelected(province: Provincia) {
        val viewModel = ViewModelProvider(this)[UserRegistrationViewModel::class.java]
        viewModel.provincia.postValue( province )

    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        //
    }


}

