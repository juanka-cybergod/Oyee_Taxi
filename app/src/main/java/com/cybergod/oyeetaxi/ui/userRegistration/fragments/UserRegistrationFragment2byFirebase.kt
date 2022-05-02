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
import com.cybergod.oyeetaxi.utils.UtilsGlobal.pasteFromClipBoard
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class UserRegistrationFragment2byFirebase : BaseFragment() {


    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null


    private var _binding: UserRegistrationFragment2Binding? = null
    private val binding get() = _binding!!

    //si falla el envio del Codigo Forzar Re Envio
    private var forceResendToken : PhoneAuthProvider.ForceResendingToken? = null

    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId:String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG_CLASS_NAME = "Fragment2byFirebase"

    private lateinit var phoneVerifiedOK:String
    private lateinit var phoneNumber:String



    //TODO Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //TODO Prepara el Binding
        _binding = UserRegistrationFragment2Binding.inflate(inflater, container, false)


        //TODO Preparar la Vista Inicial
        binding.llPhone.visibility = View.VISIBLE
        binding.llCode.visibility = View.INVISIBLE


        //TODO Crear una Instancia para Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()


        //TODO Setup sPhoneAuthProvider Callback
        setupPhoneAuthProvider()

        // TODO Preparar el Observer 1 Sola Vez
        setupObserver()

        //TODO Boton Enviar y Re Enviar Codigo setOnClickListener
        binding.sendCodeButton.setOnClickListener {

        val requiereVerifyIfUserAlreadyExist  = true


            if (verifyData()) {

                if (requiereVerifyIfUserAlreadyExist) {
                    //Verificar que no exista ya un usuario con ese numero de telefono en el Servidor

                    showProgressDialog(getString(R.string.verify_number))


                    viewModel.isUserExistByPhone(phoneNumber)

                } else {
                    sendPhoneNumberVerification(forceResendToken)
                }

            }

        }

        //TODO Boton Verificar Codigo y Continuar setOnClickListener
        binding.continueButton.setOnClickListener {

            val verificationCode: String = binding.tvCode.editText!!.text.trim().toString()
            val phoneNumber:String = binding.tvPhone.editText!!.text.toString()

            if (phoneNumber.length != 8) {
                showSnackBar(
                    "Por favor introduzca su número de teléfono",
                    true,
                )
            } else {

                if (verificationCode.isEmpty()) {
                        showSnackBar(
                            "Por favor introduzca el código de verificación",
                            true,
                        )
                } else {

                    if (verificationCode.length != 6) {
                        showSnackBar(
                            "El código de verificación está incompleto",
                            true,
                        )
                   } else {
                        verifyPhoneNumberWithCode(mVerificationId,verificationCode)
                    }

                }

            }



        }

        //TODO Quitar esta Linea Solo para Pruebas
        binding.continueButton.setOnLongClickListener{
            viewModel.telefonoMovil.postValue("+5353208579")
            goToNextFragment()
            true
        }

        binding.tvCode.setEndIconOnClickListener {
            requireView().hideKeyboard()
            binding.tvCode.editText?.setText(requireActivity().pasteFromClipBoard())

        }

        binding.tvPhone.setEndIconOnClickListener {
            requireView().hideKeyboard()
            binding.tvPhone.editText?.setText("")
        }


        //TODO Preparar el la Api que leera los Mensajes para Autocompletar el Codigo OTP
        setupReadSmsCodeVerificationApi()


        //TODO Retornar la Vista
        return  binding.root
    }



    private fun verifyData(): Boolean {

        requireView().hideKeyboard()

        val mPhoneNumber = binding.tvPhone.editText!!.text.trim().toString()

        return when {
            //mPhoneNumber
            TextUtils.isEmpty(mPhoneNumber.trim { it <= ' ' }) -> {
                showSnackBar(
                    "Por favor introduzca su su número de teléfono",
                    true,
                )
                false
            }
            //mPhoneNumber
            mPhoneNumber.length != 8 -> {
                showSnackBar(
                    "Por favor introduzca un número de teléfono válido",
                    true,
                )
                false
            }
            else -> {

                phoneNumber = CONTRY_CODE + mPhoneNumber
                true
            }

        }












    }

    private fun setupObserver() {

        viewModel.userAlreadyExistWithPhone.observe(viewLifecycleOwner, Observer { exist ->

            if (exist != null) {
                if (exist == true) {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.user_already_exist),
                        true,
                    )
                } else {

                    //si no existe comenzar la verificacion
                    sendPhoneNumberVerification(forceResendToken)


                }

            } else {
                hideProgressDialog()
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }

        })


    }

    private fun goToNextFragment(){

            findNavController().navigate(R.id.action_userRegistrationFragment2_to_userRegistrationFragment3)

    }

    //TODO Preparar el PhoneAuthProvider Callback
    private fun setupPhoneAuthProvider() {
        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                singInwithPhoneAuthCredention(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                hideProgressDialog()

                //TODO VER LO DEL PROBLEMA CON LA VPN AQUI
                showMessage(
                    "Servicio Inaccesible",
                    "Para verificar su número deberá usar una VPN en su dispositivo y luego intente nuevamente"
                )


                Log.d(TAG_CLASS_NAME,"VerificationFailed causa : ${e.message} \n codigo : ${e.hashCode()}")


            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                hideProgressDialog()
                showSnackBar(
                    "Tiempo de espera agotado, intentelo mas tarde",
                    true
                )

                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)

                mVerificationId = verificationId
                forceResendToken = token

                hideProgressDialog()

                // Toast.makeText(requireContext(),"Código de Verificación Enviado",Toast.LENGTH_LONG).show()
                Log.d(TAG_CLASS_NAME,"onCodeSent : Código de Verificación Enviado (verificationId=$verificationId)")

                //Deshabilitar el Campo de Telefono
                binding.tvPhone.isEnabled = false

                //Mostrar el Helper
                binding.tvPhone.helperText  = "Código de verificación enviado satisfactoriamente"

                //Modtrar el Campo para ingresar el Codigo de Verificacion
                binding.llCode.visibility = View.VISIBLE


                //Corutina de Conteo Regresivo para Re Encviar el Codigo
                val coroutine : CoroutineScope = CoroutineScope(Dispatchers.IO)
                coroutine.launch(Dispatchers.Main) {

                    binding.sendCodeButton.isEnabled = false

                    var i:Int = 10
                    while (i > 0) {
                        delay(1000L)
                        i--
                        binding.sendCodeButton.setText("Volver a solicitar código en $i segundos")

                    }
                    binding.sendCodeButton.text = "Solicitar código"
                    binding.sendCodeButton.isEnabled = true
                    binding.tvPhone.isEnabled = true
                }


            }


        }
    }

    private fun sendPhoneNumberVerification(forceResendToken :PhoneAuthProvider.ForceResendingToken?) {

            showProgressDialog("Verificando su número de teléfono ...")

            if (forceResendToken != null) {
                resendPhoneNumberToVerification(phoneNumber ,forceResendToken)
            } else {
                startPhoneNumberVerification(phoneNumber)
            }

    }

    private fun startPhoneNumberVerification(phone: String ) {

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallBacks!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun resendPhoneNumberToVerification(phone: String, token: PhoneAuthProvider.ForceResendingToken? ){


        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallBacks!!)
            .setForceResendingToken(token!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId:String?, code:String){

        showProgressDialog("Verificando su código ...")

        val credential = PhoneAuthProvider.getCredential(verificationId!!,code)

        singInwithPhoneAuthCredention(credential)

    }

    private fun singInwithPhoneAuthCredention(credential: PhoneAuthCredential) {


        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {

                hideProgressDialog()

                val phone = firebaseAuth.currentUser?.phoneNumber

                phoneVerifiedOK = "$phone"

                viewModel.telefonoMovil.postValue(phoneVerifiedOK)

               // Toast.makeText(requireContext(), "Sesion Iniciada con el Número : $phone",Toast.LENGTH_LONG).show()
                Log.d(TAG_CLASS_NAME,"Sesion Iniciada con el Número : $phone".toString())

                //TODO Guardar Su Numero de Telefono para que no tenga que Hacer la Verificacion si Cierra la App
                // y Continuar con el Proceso de Registro

                goToNextFragment()


                //Cerrar Sesion
                firebaseAuth.signOut()



            }
            .addOnFailureListener{e->
                hideProgressDialog()

                if (e.message.toString().contains("is invalid",true)) {
                    showSnackBar(
                        "El Código de Verificación es Incorrecto",
                        true
                    )
                    //Toast.makeText(requireContext(),"El Código de Verificación es Incorrecto",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
                }



                Log.d(TAG_CLASS_NAME,e.message.toString())
            }

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





//    // TODO Nuevo petodo para StartActivityForResult
//    var someActivityResultLauncher = registerForActivityResult(
//        StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK && result.resultCode == REQUEST_USER_CONSENT && result.data != null ) {
//           //doSomeOperations()
//            val message = result.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
//            getOtpFromMessage(message)
//
//
//        }
//    }









}