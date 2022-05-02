package com.cybergod.oyeetaxi.ui.userRegistration.activity


import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
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



    // TODO Step 10: Override the onBackPressed function and call the double back press function created in the base activity.
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
        //Toast.makeText(this,data, Toast.LENGTH_SHORT).show()
    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        TODO("Not yet implemented")
    }


}


/*
    //progress Dialog
    private lateinit var progressDialog: ProgressDialog
    private fun setupProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Subiendo imagen a Servidor")

    }

    override fun onProggressUpdate(porcentage: Int) {
        val viewModel = ViewModelProvider(this)[UserRegistrationViewModel::class.java]

        if (porcentage == 100) {
            binding.pv.visibility = View.INVISIBLE
           // Toast.makeText(this,"llego a $porcentage",Toast.LENGTH_SHORT).show()
        } else {
            binding.pv.visibility = View.VISIBLE
        }

        binding.pv.progress = porcentage


    }
 */