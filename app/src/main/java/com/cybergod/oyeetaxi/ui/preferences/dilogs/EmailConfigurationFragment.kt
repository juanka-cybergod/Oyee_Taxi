package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.EmailConfiguracion
import com.cybergod.oyeetaxi.databinding.DialogConfigurationEmailBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.showDropDownMenuFix
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmailConfigurationFragment : BottomSheetDialogFragment() {

        private var _binding: DialogConfigurationEmailBinding? = null
        private val binding get() = _binding!!


    val viewModel: AdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogConfigurationEmailBinding.inflate(inflater, container, false)


        setupProtocolListAdapter()

        setupOnClickListener()


        loadData()

        return  binding.root

    }

    private fun loadData() {
        viewModel.serverConfiguration.value?.emailConfiguracion?.let { emailConfiguracion ->
            with(binding) {
                tvHost.editText?.setText(emailConfiguracion.host?:"")
                tvPort.editText?.setText(emailConfiguracion.port?.toString()?:"")
                tvEmailUsername.editText?.setText(emailConfiguracion.username?:"")
                tvEmailPassword.editText?.setText(emailConfiguracion.password?:"")

                tvTansportProtocol.setText(emailConfiguracion.properties_mail_transport_protocol?.uppercase()?:"" , false)

                switchSmtpAuth.isChecked = emailConfiguracion.properties_mail_smtp_auth ?: true
                switchStarttls.isChecked = emailConfiguracion.properties_mail_smtp_starttls_enable ?: true
                switchDebug.isChecked = emailConfiguracion.properties_mail_debug ?: true

            }
        }


        fillTexViewProtocolList()
    }


    private fun fillTexViewProtocolList() {

        binding.tvTansportProtocol.setAdapter(
            viewModel.protocolArrayAdapter
        )
        binding.tfTansportProtocol.clearFocus()
    }

    private fun setupOnClickListener() {



        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                setServerEmailConfiguration()
            }
        }

        binding.tvTansportProtocol.setOnClickListener {
            binding.tvTansportProtocol.showDropDownMenuFix(viewModel.protocolArrayAdapter)
        }

    }

   lateinit var emailConfiguracion: EmailConfiguracion
    private fun setServerEmailConfiguration() {

        with(binding){
           emailConfiguracion = EmailConfiguracion(
                host = tvHost.editText?.text.toString().trim().lowercase(),
                port = tvPort.editText?.text.toString().trim().toInt(),
                username = tvEmailUsername.editText?.text.toString().trim().lowercase(),
                password = tvEmailPassword.editText?.text.toString().trim(),

                properties_mail_transport_protocol = tvTansportProtocol.text.toString().trim().lowercase(),
                properties_mail_smtp_auth = switchSmtpAuth.isChecked,
                properties_mail_smtp_starttls_enable = switchStarttls.isChecked,
                properties_mail_debug = switchDebug.isChecked,

            )
        }

        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))

        viewModel.setServerEmailConfiguration(emailConfiguracion)
        dismiss()
    }



    private fun verifyData(): Boolean {
        val mHost = binding.tvHost.editText!!.text.trim().toString()
        val mPort = binding.tvPort.editText!!.text.trim().toString()
        val mUsername = binding.tvEmailUsername.editText!!.text.trim().toString()
        val mPassword = binding.tvEmailPassword.editText!!.text.trim().toString()
        val mTansportProtocol = binding.tvTansportProtocol.text.trim().toString()


        binding.tvHost.isErrorEnabled=false
        binding.tvPort.isErrorEnabled=false
        binding.tvEmailUsername.isErrorEnabled=false
        binding.tvEmailPassword.isErrorEnabled=false
        binding.tfTansportProtocol.isErrorEnabled=false



        return when {

            mHost.isEmptyTrim() -> {
                binding.tvHost.error = " "
                false
            }
            mPort.isEmptyTrim() -> {
                binding.tvPort.error = " "
                false
            }
            mUsername.isEmptyTrim() -> {
                binding.tvEmailUsername.error = "Por favor introduzca el email de servicio"
                false
            }
            mPassword.isEmptyTrim() -> {
                binding.tvEmailPassword.error = "Por favor introduzca el token de acceso al servicio"
                false
            }
            mTansportProtocol.isEmptyTrim() -> {
                binding.tfTansportProtocol.error = "Por favor introduzca el protocolo"
                false
            }
            else -> {
                true
            }

        }

    }



    private fun setupProtocolListAdapter(){
        viewModel.protocolItems = resources.getStringArray(R.array.protocol_items)
        viewModel.protocolArrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_map_style,
            viewModel.protocolItems
        )
    }




    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}