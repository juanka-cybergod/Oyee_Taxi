package com.cybergod.oyeetaxi.ui.userRegistration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment3Binding

import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRegistrationFragment3 : BaseFragment() {

    private var _binding: UserRegistrationFragment3Binding? = null
    private val binding get() = _binding!!


    val viewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment3Binding.inflate(inflater, container, false)



        viewModel.conductor.observe(viewLifecycleOwner, Observer { conductor ->

            if (conductor) {
                binding.imageView.setImageResource(R.drawable.ic_driver_selected)
                binding.tvUserTypeSelectedDescription.text = getString(R.string.conductor_type_message)
            } else {
                binding.imageView.setImageResource(R.drawable.ic_client_selected)
                binding.tvUserTypeSelectedDescription.text = getString(R.string.cliente_type_message)
            }

            binding.continueButton.isEnabled = true

        })


        binding.rbConductor.setOnClickListener {
            setUserTypeSelected()
        }
        binding.rbPasajero.setOnClickListener {
            setUserTypeSelected()
        }



        binding.continueButton.setOnClickListener {
             findNavController().navigate(R.id.action_userRegistrationFragment3_to_userRegistrationFragment4)
        }


        loadTempDatafromViewModel()


        return  binding.root
    }




    private fun loadTempDatafromViewModel() {

        if (viewModel.conductor.value != null) {

            if (viewModel.conductor.value!!) {
                binding.rbConductor.isChecked = true
            } else {
                binding.rbPasajero.isChecked = true
            }

        }

    }


    private fun setUserTypeSelected() {
        viewModel.conductor.postValue(binding.rbConductor.isChecked)
    }



}