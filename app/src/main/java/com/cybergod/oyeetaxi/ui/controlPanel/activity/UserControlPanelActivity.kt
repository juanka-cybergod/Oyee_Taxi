package com.cybergod.oyeetaxi.ui.controlPanel.activity



import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelActivityBinding
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserControlPanelActivity : BaseActivity() , Communicator {


    private lateinit var binding: UserControlPanelActivityBinding

    val viewModel: UserControlPanelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UserControlPanelActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_user_control_panel_navegation)
        when(NavHostFragment.findNavController(fragment!!).currentDestination?.id) {
            R.id.userControlPanelFragmentMain-> {
                //doubleBackToExit()
                super.onBackPressed()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


    override fun passProvinceSelected(province: Provincia) {
        //val viewModel = ViewModelProvider(this)[UserControlPanelViewModel::class.java]
        (this as BaseActivity).showProgressDialog(getString(R.string.updating_user))

        viewModel.updateUser(
            Usuario(
                provincia = province
            )
        )

    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        //
    }



}

