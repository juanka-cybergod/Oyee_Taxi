package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.IntervalTimerConfiguracion
import com.cybergod.oyeetaxi.databinding.DialogIntervalTimerConfigurationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.AdministrationViewModel


class IntervalTimerConfiguracionFragment : DialogFragment() {

    private var _binding: DialogIntervalTimerConfigurationBinding? = null
    private val binding get() = _binding!!


    val viewModel: AdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogIntervalTimerConfigurationBinding.inflate(inflater, container, false)

        with (binding) {

//            npAvailableVehicleInterval.setOnValueChangedListener { numberPicker, oldValue, newValue ->
//                Toast.makeText(requireContext(),newValue.toString(),Toast.LENGTH_SHORT).show()
//
//            }
            npAvailableVehicleInterval.minValue=1
            npAvailableVehicleInterval.maxValue=300
            npDriversLocationInterval.minValue=1
            npDriversLocationInterval.maxValue=300
        }


        setupOnClickListener()

        setupObservers()

        return  binding.root




    }

    private fun setupObservers() {

        viewModel.serverConfiguration.observe(viewLifecycleOwner, Observer { configuration ->
            configuration?.intervalTimerConfiguracion?.let {
                loadData(it)
            }
        })

    }


    private fun loadData(intervalTimerConfiguracion: IntervalTimerConfiguracion) {
            with (binding) {
                npAvailableVehicleInterval.value=intervalTimerConfiguracion.getAvailableVehicleInterval?.toInt() ?: 10
                npDriversLocationInterval.value=intervalTimerConfiguracion.setDriversLocationInterval?.toInt() ?: 10
            }
    }


    private fun setupOnClickListener() {

        with(binding) {

            btnClose.setOnClickListener {
                closethisfragment()
            }


            btnApply.setOnClickListener {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))

                viewModel.setIntervalTimerConfiguration(
                    IntervalTimerConfiguracion(
                        getAvailableVehicleInterval = binding.npAvailableVehicleInterval.value.toLong(),
                        setDriversLocationInterval = binding.npDriversLocationInterval.value.toLong(),
                    )
                )

                closethisfragment()
            }


        }

    }



    private fun closethisfragment(){
        this.dismiss()
    }




}