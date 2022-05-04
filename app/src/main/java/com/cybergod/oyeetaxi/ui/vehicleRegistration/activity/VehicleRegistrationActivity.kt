package com.cybergod.oyeetaxi.ui.vehicleRegistration.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = VehicleRegistrationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun passProvinceSelected(province: Provincia) {
        //
    }

    override fun passVehicleTypeSelected(vehicleType: TipoVehiculo) {
        val viewModel = ViewModelProvider(this)[VehicleRegistrationViewModel::class.java]
        viewModel.tipoVehiculo.postValue( vehicleType )
    }



}