package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.FragmentVehicleDetailBinding
import com.cybergod.oyeetaxi.maps.Utils.calculateTheDistance
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.VehicleDetailsViewModel
import com.cybergod.oyeetaxi.ui.main.viewmodel.HomeViewModel
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_ID
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE_PARCELABLE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_CALL_PHONE_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_SEND_SMS_REQUEST_CODE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesUsuario
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesVehiculos
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setUsuarioVerificacionImage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoClimatizado
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoMatricula
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoVerificacionImage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showInputTextMessage
import com.cybergod.oyeetaxi.utils.Intents.launchIntentCallPhone
import com.cybergod.oyeetaxi.utils.UtilsGlobal.logD
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VehicleDetailFragment : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentVehicleDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: VehicleDetailsViewModel
    private val viewModelHome: HomeViewModel by activityViewModels()

    private var vehicleOK: MutableLiveData<VehiculoResponse> = MutableLiveData<VehiculoResponse>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVehicleDetailBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(this)[VehicleDetailsViewModel::class.java]

        //Obtener el ID pasado por Argumentos
        requireArguments().getString(KEY_VEHICLE_ID,"").let { vehicleId->

            if (vehicleId.isNotEmpty()){

                setupVehicleDetailObserverFromHomeViewModel(vehicleId)

                setupCalculateDistancesObserver()

                setupOnClickListener()

            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return  binding.root
    }



    //Pintar la Vista
    private fun paintVehicle(){

        with(binding) {

            //Imagen Perfil
            imageUsuario.loadImagePerfilFromURL(vehicleOK.value?.usuario?.imagenPerfilURL)

            //Imagen Frontal
            imageVehiculo.loadImageVehiculoFrontalFromURL(vehicleOK.value?.imagenFrontalPublicaURL)

            //Verificacion Vehiculo
            imageVehiculoVerificado.setVehiculoVerificacionImage(vehicleOK.value!!)

            //Verificacion Usuario
            imageUsuarioVerificado.setUsuarioVerificacionImage(vehicleOK.value?.usuario!!)

            //Matricula
            tvMatricula.setVehiculoMatricula(vehicleOK.value?.vehiculoVerificacion?.matricula)

            //Detalles del Vehiculo
            tvDetalles.setDetallesVehiculos(vehicleOK.value!!)

            //Climatizado
            imageVehiculoClimatizado.setVehiculoClimatizado(vehicleOK.value!!)

            //Nombre Condutor
            tvNombreCondutor.text = vehicleOK.value?.usuario?.nombre

            //Datos Condutor
            tvDetallesUsuario.setDetallesUsuario(vehicleOK.value!!)

            //Valoracion
            vehicleOK.value!!.usuario!!.valoracion?.let {
                userRatingBar.rating = it
            }

        }



    }



    private fun setupOnClickListener() {
        //Btn Enviar SMS
        binding.btnSms.setOnClickListener {
            if (Permissions.hasSendSmsPermission(this.requireContext())){
                showDialogSendSMS()
            } else {
                Permissions.requestSendSmsPermission(this)
            }
        }

        //Btn Climatizado
        binding.imageVehiculoClimatizado.setOnClickListener {
            Toast.makeText(requireContext(),"Climatizado",Toast.LENGTH_LONG).show()
            it.isEnabled=false
        }

        //Btn Call
        binding.btnCall.setOnClickListener {

            if (Permissions.hasCallPhonePermission(this.requireContext())){
                requireContext().launchIntentCallPhone(vehicleOK.value?.usuario?.telefonoMovil!!)
            } else {
                Permissions.requestCallPhonePermission(this)
            }

        }

        //Valorar
        binding.imageUsuario.setOnClickListener {
            vehicleOK.value?.let {
                findNavController().navigate(R.id.action_vehicleDetailFragment_to_valoracionFragment,Bundle().apply {
                    putParcelable(KEY_VEHICLE_RESPONSE_PARCELABLE,it)
                })

            }
        }

        //Viajar con este Conductor
        binding.btnViajar.setOnClickListener {
            crearViaje()
        }

        //
    }


    private fun crearViaje(){
        findNavController().navigate(R.id.action_vehicleDetailFragment_to_viajeFragment,Bundle().apply {
            //putString(KEY_VEHICLE_ID,vehicleId)
        })
        //dismiss()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendSmsToPhone(send:Boolean, message:String?){
        if (send) {

            if (message.isNullOrEmpty()) {
                Toast.makeText(requireContext(),"Mensaje no enviado porque estaba en blanco",Toast.LENGTH_LONG).show()
            } else {

                val phoneNumber:String = vehicleOK.value?.usuario?.telefonoMovil!!
                val sentPI: PendingIntent = PendingIntent.getBroadcast(requireContext(), 0, Intent("SMS_SENT"), 0)

                try {


                    SmsManager.getDefault().sendTextMessage(phoneNumber, null,  "Cliente de " + requireContext().getString(R.string.app_name) + " dice : $message", sentPI, null)
                    Toast.makeText(requireContext(),"Mensaje Enviado a ${vehicleOK.value?.usuario?.nombre}",Toast.LENGTH_LONG).show()
                }catch (e: Exception){


                    try {
                        val smsManager: SmsManager? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requireContext().getSystemService(SmsManager::class.java)}  else null

                        smsManager?.sendTextMessage(phoneNumber, null,  "Cliente de " + requireContext().getString(R.string.app_name) + " dice : $message", sentPI, null)
                        Toast.makeText(requireContext(),"Mensaje Enviado a ${vehicleOK.value?.usuario?.nombre}",Toast.LENGTH_LONG).show()



                    } catch (e: Exception) {

                        Toast.makeText(requireContext(),"Error al intentar enviar el mensaje de texto",Toast.LENGTH_LONG).show()
                        logD(requireActivity(),"sendSmsToPhoneError=$e")

                    }




                }
            }

        }


    }





    private fun showDialogSendSMS(){

        requireActivity().showInputTextMessage(
            funResult =  {ok,texto, -> sendSmsToPhone(ok,texto)},
            title = "SMS a Conductor",
            hint = "Introduzca su mensaje de texto",
            helperText = "Es posible que se apliquen cargos a su cuenta móvil por valor de un mensaje",
            message = "",
            icon = R.drawable.ic_sms,
            buttonConfirmText = "Enviar"
        )


    }

    private fun setupCalculateDistancesObserver() {

        vehicleOK.observe(viewLifecycleOwner, Observer {
            it?.let {
                reCalculateDistances()
            }

        })
        currentUserActive.observe(viewLifecycleOwner, Observer {
            it?.let {
               reCalculateDistances()
            }
        })

    }

    private fun reCalculateDistances(){
        if (currentUserActive.value?.ubicacion != null && vehicleOK.value?.usuario?.ubicacion != null) {
            "Vehículo a ${calculateTheDistance(currentUserActive.value?.ubicacion,vehicleOK.value?.usuario?.ubicacion)  } Km de su posición".also { binding.tvDistanceBetween.text = it }

        } else binding.tvDistanceBetween.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun setupVehicleDetailObserverFromHomeViewModel(vehicleId: String){


        viewModelHome.vehicleList.value?.find {  item -> item.id.equals(vehicleId,true) }?.let { vehiculoResponse ->
            vehicleOK.value = vehiculoResponse
            paintVehicle()
        }


        viewModelHome.vehicleList.observe(viewLifecycleOwner, Observer { listaVehiculos ->

            if (!listaVehiculos.isNullOrEmpty()) {

                listaVehiculos.find {  item -> item.id.equals(vehicleId,true) }?.let { vehiculoResponse ->
                    if ( vehiculoResponse != vehicleOK.value) {
                        vehicleOK.value = vehiculoResponse
                        paintVehicle()
                    }

                }

            }


        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CALL_PHONE_REQUEST_CODE -> {
                EasyPermissions.onRequestPermissionsResult(PERMISSION_CALL_PHONE_REQUEST_CODE,permissions,grantResults,this)
            }
            PERMISSION_SEND_SMS_REQUEST_CODE -> {
                EasyPermissions.onRequestPermissionsResult(PERMISSION_SEND_SMS_REQUEST_CODE,permissions,grantResults,this)
            }
        }

    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        val a:  List<String> = perms

        when (requestCode) {
            PERMISSION_CALL_PHONE_REQUEST_CODE -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this.requireActivity(),a)){
                    SettingsDialog.Builder(requireActivity()).build().show()
                } else {
                    Permissions.requestCallPhonePermission(this)
                }
            }
            PERMISSION_SEND_SMS_REQUEST_CODE -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this.requireActivity(),a)){
                    SettingsDialog.Builder(requireActivity()).build().show()
                } else {
                    Permissions.requestSendSmsPermission(this)
                }
            }
        }


    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        when (requestCode) {
            PERMISSION_CALL_PHONE_REQUEST_CODE -> {
                requireContext().launchIntentCallPhone(vehicleOK.value?.usuario?.telefonoMovil!!)
            }
            PERMISSION_SEND_SMS_REQUEST_CODE -> {
                showDialogSendSMS()
            }
        }


    }




}