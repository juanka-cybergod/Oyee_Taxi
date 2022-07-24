package com.cybergod.oyeetaxi.ui.userRegistration.fragments


import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment2Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.broadcast.SMSReceiver
import com.cybergod.oyeetaxi.ui.userRegistration.utils.AppSignatureHashHelper
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.utils.Constants.CONTRY_CODE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.pasteFromClipBoard
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserRegistrationFragment2byTwilio : BaseFragment() {

    val TAG_CLASS_NAME = "Fragment2byTwilio"

    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null

    private var _binding: UserRegistrationFragment2Binding? = null
    private val binding get() = _binding!!

    private lateinit var phoneVerifiedOK:String
    private lateinit var phoneNumber:String

    val viewModel: UserRegistrationViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment2Binding.inflate(inflater, container, false)

        binding.llPhone.visibility = View.VISIBLE
        binding.llCode.visibility = View.INVISIBLE


        binding.sendCodeButton.setOnClickListener {
            if (verifyData()) {
                sendPhoneNumberVerification()
            }
        }

        binding.continueButton.setOnClickListener {

            val verificationCode: String = binding.tvCode.editText!!.text.trim().toString()
            val phoneNumber:String = binding.tvPhone.editText!!.text.toString()

            binding.tvPhone.isErrorEnabled=false
            binding.tvCode.isErrorEnabled=false

            if (phoneNumber.length != 8) {
                binding.tvPhone.error = "Por favor introduzca su número de teléfono"
            } else {

                if (verificationCode.isEmptyTrim()) {
                    binding.tvCode.error = "Por favor introduzca el código de verificación"
                } else {

                    if (verificationCode.length != 6) {
                        binding.tvCode.error = "El código de verificación está incompleto"
                   } else {

                        lifecycleScope.launch {
                            showProgressDialog(getString(R.string.cheking_your_code))
                            delay(1000L)
                            verifyPhoneNumberWithCode(viewModel.otpCode.value,verificationCode)
                        }


                    }

                }

            }



        }



        binding.tvCode.setEndIconOnClickListener {
            requireView().hideKeyboard()
            binding.tvCode.editText?.setText(requireActivity().pasteFromClipBoard())

        }

        binding.tvPhone.setEndIconOnClickListener {
            requireView().hideKeyboard()
            binding.tvPhone.editText?.setText("")
        }

        setupObserver()

        //Preparar el la Api que leera los Mensajes para Autocompletar el Codigo OTP
        setupReadSmsCodeVerificationApi()

        return  binding.root
    }


    private fun verifyData(): Boolean {

        requireView().hideKeyboard()

        val mPhoneNumber = binding.tvPhone.editText!!.text.trim().toString()

        binding.tvPhone.isErrorEnabled=false

        return when {
            //mPhoneNumber
            mPhoneNumber.isEmptyTrim() -> {
                binding.tvPhone.error = "Por favor introduzca su número de teléfono"
                false
            }
            //mPhoneNumber
            mPhoneNumber.length != 8 -> {
                binding.tvPhone.error = "Por favor introduzca su número de teléfono"
                false
            }
            else -> {
                phoneNumber = CONTRY_CODE + mPhoneNumber
                true
            }

        }

    }

    private fun setupObserver() {
        //observar el resultado de si es creado satisfactoriamente el usuario
        viewModel.otpCode.observe( viewLifecycleOwner, Observer { otpCode ->

                hideProgressDialog()

                   if (!otpCode.isNullOrEmpty()) {

                       onCodeSent()

                   } else {
                       showSnackBar(
                           getString(R.string.fail_server_comunication),
                           true,
                       )
                   }

        })

        viewModel.userAlreadyExistWithPhone.observe(viewLifecycleOwner, Observer { exist ->

            hideProgressDialog()

            if (exist != null) {
                if (exist == true) {
                    binding.tvPhone.error = getString(R.string.user_already_exist)
                } else {

                    //si no existe comenzar la verificacion
                    viewModel.twilioPhoneAuthentication(phoneNumber)

                }

            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }

        })


    }

    private fun onCodeSent() {

        phoneVerifiedOK = phoneNumber

        // Toast.makeText(requireContext(),"Código de Verificación Enviado",Toast.LENGTH_LONG).show()
        Log.d(TAG_CLASS_NAME,"onCodeSent : Código de Verificación Enviado (verificationId=${viewModel.otpCode.value})")

        //Habilitar Boton Continuar
        binding.continueButton.isEnabled = true

        //Deshabilitar el Campo de Telefono
        binding.tvPhone.isEnabled = false

        //Mostrar el Helper
        binding.tvPhone.helperText  = getString(R.string.code_sent)

        //Modtrar el Campo para ingresar el Codigo de Verificacion
        binding.llCode.visibility = View.VISIBLE


        //Corutina de Conteo Regresivo para Re Encviar el Codigo
        val coroutine : CoroutineScope = CoroutineScope(Dispatchers.IO)
        coroutine.launch(Dispatchers.Main) {

            binding.sendCodeButton.isEnabled = false
            binding.sendCodeButton.isChecked = false

            var i = 120
            while (i > 0) {
                binding.sendCodeButton.text = "Volver a solicitar código en $i segundos"
                delay(1000L)
                i--
            }

            binding.sendCodeButton.text = "Solicitar código"
            binding.sendCodeButton.isEnabled = true
            //binding.tvPhone.isEnabled = true
            binding.sendCodeButton.isChecked = true
        }

    }

    private fun goToNextFragment(){
            findNavController().navigate(R.id.action_userRegistrationFragment2byTwilio_to_userRegistrationFragment3)
    }

    private fun verifyPhoneNumberWithCode(verificationId:String?, code:String){


        hideProgressDialog()


        if (verificationId == code) {
            viewModel.telefonoMovil.postValue(phoneVerifiedOK)
            Log.d(TAG_CLASS_NAME,"El Código de Verificación es Correcto")
            goToNextFragment()
        } else {
            binding.tvCode.error = getString(R.string.verification_code_is_incorrect)
            Log.d(TAG_CLASS_NAME,getString(R.string.verification_code_is_incorrect))

        }




    }


    private fun sendPhoneNumberVerification() {
        showProgressDialog( getString(R.string.verify_number),)
        viewModel.isUserExistByPhone(phoneNumber)
    }



    private fun setupReadSmsCodeVerificationApi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d("Hash" , AppSignatureHashHelper(requireContext()).appSignatures.toString())
        }




        startUserConsent()


        // Init Sms Retriever >>>>
        initSmsListener()
        initBroadCast()

    }

    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                showToast("OTP Received: $otp")
            }
        })
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(requireContext())
        client.startSmsRetriever()
    }

    override fun onResume() {
        super.onResume()

        //registerReceiver(smsReceiver, intentFilter)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(smsReceiver!!,IntentFilter(intentFilter))



    }

    override fun onPause() {
        super.onPause()
        //unregisterReceiver(smsReceiver)
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(smsReceiver!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun startUserConsent(){
        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsUserConsent(null)

    }



}