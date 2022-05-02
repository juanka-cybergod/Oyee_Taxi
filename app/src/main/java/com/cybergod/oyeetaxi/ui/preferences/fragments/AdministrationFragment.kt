package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.databinding.FragmentAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class AdministrationFragment : BaseFragment() {


    private var _binding: FragmentAdministrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdministrationViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdministrationBinding.inflate(inflater, container, false)



        setupOnClickListener()

        setupObservers()




        return  binding.root
    }




    private fun setupObservers() {


    }


    private fun setupOnClickListener() {





    }



}