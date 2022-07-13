package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.TwilioConfiguracion
import com.cybergod.oyeetaxi.databinding.FragmentTwillioConfigurationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.AdministrationViewModel
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


        loadData()

        return  binding.root

    }

    private fun loadData() {
        viewModel.serverConfiguration.value?.twilioConfiguracion?.let { twilioConfiguracion ->
            with(binding) {
                tvAccountSid.editText?.setText(twilioConfiguracion.account_sid?:"")
                tvAuthToken.editText?.setText(twilioConfiguracion.auth_token?:"")
                tvTrialNumber.editText?.setText(twilioConfiguracion.trial_number?:"")
            }
        }
    }


    private fun setupOnClickListener() {
        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                setServerTwilioConfiguration()
            }
        }

    }

   lateinit var twilioConfiguracion: TwilioConfiguracion
    private fun setServerTwilioConfiguration() {

        with(binding){
           twilioConfiguracion = TwilioConfiguracion(
                account_sid = tvAccountSid.editText?.text.toString().trim(),
                auth_token = tvAuthToken.editText?.text.toString().trim(),
                trial_number = tvTrialNumber.editText?.text.toString().trim()
            )
        }

        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))

        viewModel.setServerTwilioConfiguration(twilioConfiguracion)
        dismiss()
    }


    private fun verifyData(): Boolean {
        val mAccountSid = binding.tvAccountSid.editText!!.text.trim().toString()
        val mAuthToken: String = binding.tvAuthToken.editText!!.text.trim().toString()
        val mTrialNumber = binding.tvTrialNumber.editText!!.text.trim().toString()

        binding.tvAccountSid.isErrorEnabled=false
        binding.tvAuthToken.isErrorEnabled=false
        binding.tvTrialNumber.isErrorEnabled=false


        return when {

            TextUtils.isEmpty(mAccountSid.trim { it <= ' ' }) -> {
                binding.tvAccountSid.error = "Por favor introduzca el Account Sid "
                false

            }
            TextUtils.isEmpty(mAuthToken.trim { it <= ' ' }) -> {
                binding.tvAuthToken.error = "Por favor introduzca el Token"
                false
            }
            TextUtils.isEmpty(mTrialNumber.trim { it <= ' ' }) -> {
                binding.tvTrialNumber.error = "Por favor introduzca el número de servicio"
                false
            }
            else -> {
                true
            }

        }

    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}