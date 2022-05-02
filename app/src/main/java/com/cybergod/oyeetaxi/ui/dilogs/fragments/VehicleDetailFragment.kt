package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.FragmentVehicleDetailBinding
import com.cybergod.oyeetaxi.maps.Utils.calculateTheDistance
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.VehicleDetailsViewModel
import com.cybergod.oyeetaxi.ui.main.viewmodel.HomeViewModel
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_ID
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE
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
import com.cybergod.oyeetaxi.utils.UtilsGlobal.logD
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VehicleDetailFragment : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentVehicleDetailBinding? = null
    private val binding get() = _binding!!

    //viewModels
    lateinit var viewModel: VehicleDetailsViewModel
    private val viewModelHome: HomeViewModel by activityViewModels()

    //private lateinit var vehicleOK: VehiculoResponse
    private var vehicleOK: MutableLiveData<VehiculoResponse> = MutableLiveData<VehiculoResponse>()
    private var smsText = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVehicleDetailBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(this)[VehicleDetailsViewModel::class.java]

        //Obtener el ID pasado por Argumentos
        requireArguments().getString(KEY_VEHICLE_ID,"").let { vehicleId->

            if (vehicleId.isNotEmpty()){

                //Preparar el Observer para Escuchar los Detalles de Vehiculos
                setupVehicleDetailObserverFromHomeViewModel(vehicleId)

                //Prepara el Observer para Actualizar las Distancias entre vehiculos
                setupCalculateDistancesObserver()

            }
        }



        //Escucha el Click sobre el Boton Para Cerrar el Dialogo
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return  binding.root
    }



    //TODO Pintar la Vista
    private fun paintVehicle(){

        //Imagen Perfil
        binding.imageUsuario.loadImagePerfilFromURL(vehicleOK.value?.usuario?.imagenPerfilURL)

        //Imagen Frontal
        binding.imageVehiculo.loadImageVehiculoFrontalFromURL(vehicleOK.value?.imagenFrontalPublicaURL)

        //Verificacion Vehiculo
        binding.imageVehiculoVerificado.setVehiculoVerificacionImage(vehicleOK.value!!)

        //Verificacion Usuario
        binding.imageUsuarioVerificado.setUsuarioVerificacionImage(vehicleOK.value?.usuario!!)

        //Matricula
        binding.tvMatricula.setVehiculoMatricula(vehicleOK.value!!)

        //Detalles del Vehiculo
        binding.tvDetalles.setDetallesVehiculos(vehicleOK.value!!)

        //Climatizado
        binding.imageVehiculoClimatizado.setVehiculoClimatizado(vehicleOK.value!!)

        //Nombre Condutor
        binding.tvNombreCondutor.text = vehicleOK.value?.usuario?.nombre

        //Datos Condutor
        binding.tvDetallesUsuario.setDetallesUsuario(vehicleOK.value!!)

        //Valoracion
        vehicleOK.value!!.usuario!!.valoracion?.let {
            binding.userRatingBar.rating = it
        }



        //setupOnClickListener
        setupOnClickListener()

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
                callPhone(vehicleOK.value?.usuario?.telefonoMovil!!)
            } else {
                Permissions.requestCallPhonePermission(this)
            }

        }

        //Valorar
        binding.imageUsuario.setOnClickListener {
            vehicleOK.value?.let {
                findNavController().navigate(R.id.action_vehicleDetailFragment_to_valoracionFragment,Bundle().apply {
                    putParcelable(KEY_VEHICLE_RESPONSE,it)
                })

            }
        }
    }

    private fun sendSmsToPhone(phoneNumber:String,message:String){
        try {
            val sentPI: PendingIntent = PendingIntent.getBroadcast(requireContext(), 0, Intent("SMS_SENT"), 0)
            SmsManager.getDefault().sendTextMessage(phoneNumber, null,  "Cliente de " + requireContext().getString(R.string.app_name) + " dice : $message", sentPI, null)
            Toast.makeText(requireContext(),"Mensaje Enviado a ${vehicleOK.value?.usuario?.nombre}",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(requireContext(),"Error al intentar enviar el mensaje de texto",Toast.LENGTH_LONG).show()
            logD(requireActivity(),"sendSmsToPhoneError=$e")
        }

    }

    private fun callPhone(phoneNumber:String) {
        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:${phoneNumber}")
        startActivity(intent)
    }

    private fun showDialogSendSMS(){
        val builder: AlertDialog.Builder =AlertDialog.Builder(requireContext())
        builder.setTitle("SMS a Conductor")

        val input = TextInputEditText(requireContext())

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Introduzca su Mensaje")

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        // Set up the buttons
        //builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { dialog, which ->
        builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            smsText = input.text.toString()
            if (!smsText.trim().isEmpty()) {
                sendSmsToPhone(vehicleOK.value?.usuario?.telefonoMovil!!,smsText)
            } else {
                Toast.makeText(requireContext(),"Mensaje no enviado porque estaba en blanco",Toast.LENGTH_LONG).show()
            }


        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.show()
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
        //Distancia entre usuario y vehiculo
        if (currentUserActive.value?.ubicacion != null && vehicleOK.value?.usuario?.ubicacion != null) {
            binding.tvDistanceBetween.text = "Vehículo a ${calculateTheDistance(currentUserActive.value?.ubicacion,vehicleOK.value?.usuario?.ubicacion)  } Km de su posición"

        } else binding.tvDistanceBetween.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun setupVehicleDetailObserverFromHomeViewModel(vehicleId: String){


        //TODO Cargar los valores de ese vehiculo inmediatamente  desde el 1er momento
        viewModelHome.vehicleList.value?.find {  item -> item.id.equals(vehicleId,true) }?.let { vehiculoResponse ->
            vehicleOK.value = vehiculoResponse
            paintVehicle()
        }


        //TODO Observar para Actualizar si hay cambios OnLine
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
                callPhone(vehicleOK.value?.usuario?.telefonoMovil!!)
            }
            PERMISSION_SEND_SMS_REQUEST_CODE -> {
                showDialogSendSMS()
            }
        }


    }




}