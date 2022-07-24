package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.RegisterConfiguracion
import com.cybergod.oyeetaxi.databinding.DialogAdminUsersRegistrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.AdministrationViewModel


class RegisterNewUsersFragment : DialogFragment() {

    private var _binding: DialogAdminUsersRegistrationBinding? = null
    private val binding get() = _binding!!


    val viewModel: AdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogAdminUsersRegistrationBinding.inflate(inflater, container, false)

        setupOnClickListener()

        setupObservers()

        return  binding.root

    }

    private fun setupObservers() {

        viewModel.serverConfiguration.observe(viewLifecycleOwner, Observer { configuration ->
            configuration?.registerConfiguracion?.let {
                loadData(it)
            }
        })

    }


    private fun loadData(registerConfiguracion: RegisterConfiguracion) {
            with (binding) {
                switchConductores.isChecked = registerConfiguracion.habilitadoRegistroConductores?:true
                switchPasajeros.isChecked = registerConfiguracion.habilitadoRegistroPasajeros?:true
            }
    }


    private fun setupOnClickListener() {

        with(binding) {

            btnClose.setOnClickListener {
                closethisfragment()
            }


            btnApply.setOnClickListener {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))

                viewModel.setServerRegisterConfiguration(
                    RegisterConfiguracion(
                        habilitadoRegistroConductores = binding.switchConductores.isChecked,
                        habilitadoRegistroPasajeros = binding.switchPasajeros.isChecked,
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