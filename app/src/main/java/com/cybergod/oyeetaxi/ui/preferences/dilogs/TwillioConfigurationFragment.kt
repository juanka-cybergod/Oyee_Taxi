package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.databinding.FragmentTwillioConfigurationBinding
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TwillioConfigurationFragment : BottomSheetDialogFragment() {

        private var _binding: FragmentTwillioConfigurationBinding? = null
        private val binding get() = _binding!!


    val viewModel: AdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTwillioConfigurationBinding.inflate(inflater, container, false)


        setupOnClickListener()



        return  binding.root

    }






    private fun setupOnClickListener() {
        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                closeThisBottomSheetDialogFragment()

            }
        }

    }


    private fun verifyData(): Boolean {
        return true

    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}