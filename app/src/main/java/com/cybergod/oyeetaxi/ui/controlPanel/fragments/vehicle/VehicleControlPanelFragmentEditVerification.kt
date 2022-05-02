package com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.Vehiculo
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.model.verification.VehiculoVerificacion
import com.cybergod.oyeetaxi.databinding.VehicleControlPanelFragmentEditVerificationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoCirculacionFromURI
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoCirculacionFromURL
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VehicleControlPanelFragmentEditVerification: BottomSheetDialogFragment()  {

    private var _binding: VehicleControlPanelFragmentEditVerificationBinding? = null
    private val binding get() = _binding!!

    val viewModel: VehicleControlPanelViewModel by activityViewModels()

    private lateinit var vehiculo: VehiculoResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VehicleControlPanelFragmentEditVerificationBinding.inflate(inflater, container, false)

        //Obtener el Vehiculo pasado por Argumentos para Editarlo
        requireArguments().getParcelable<VehiculoResponse>(Constants.KEY_VEHICLE_PARCELABLE)?.let { vehicle ->

            if (!vehicle.id.isNullOrEmpty()) {
                vehiculo = vehicle
                loadVehicleDetails(vehicle)
            }

        }


        setupObservers()

        setupOnClickListener()

        return  binding.root


    }


    private fun loadVehicleDetails(vehiculo: VehiculoResponse) {



        binding.ivImageCirculacion.loadImageVehiculoCirculacionFromURL(vehiculo.vehiculoVerificacion?.imagenCirculacionURL)

        if (vehiculo.tipoVehiculo != null) {

            if (vehiculo.tipoVehiculo?.requiereVerification!!) {
                binding.tvMatricula.helperText = "Requerimiento Obligatorio"
                binding.tvCirculacion.helperText = "Requerimiento Obligatorio"
                binding.clImagenCirculacionURL.visibility = View.VISIBLE

            } else {
                binding.tvMatricula.helperText = "Requerimiento Opcional"
                binding.tvCirculacion.helperText = "Requerimiento Opcional"
                binding.clImagenCirculacionURL.visibility = View.VISIBLE

            }
        }


        binding.tvMatricula.editText?.setText(vehiculo.vehiculoVerificacion?.matricula ?: "")

        binding.tvCirculacion.editText?.setText(vehiculo.vehiculoVerificacion?.circulacion ?: "")





    }


    private fun setupObservers() {
        //observer si la imagen de verificacion fue subida satisfactoriamente
        viewModel.imagenCirculacionURL.observe(viewLifecycleOwner, Observer { imagenCirculacionURL ->
            if (imagenCirculacionURL != null) {

                if (imagenCirculacionURL.isNotEmpty()) {

                    viewModel.updateVehicleById(

                        Vehiculo(
                            vehiculoVerificacion = VehiculoVerificacion(
                                  matricula = binding.tvMatricula.editText?.text.toString(),
                                  circulacion =  binding.tvCirculacion.editText?.text.toString(),
                                  imagenCirculacionURL = imagenCirculacionURL,
                            )

                        ),
                        vehiculoId = vehiculo.id!!
                    )
                    closeThisBottomSheetDialogFragment()

                }


            } else {

                closeThisBottomSheetDialogFragment()

                (requireActivity() as BaseActivity).hideProgressDialog()

                requireActivity().showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )


            }

        })
    }



    private fun setupOnClickListener() {


        binding.buttonSelectImageCirculacion.setOnClickListener {
            requireView().hideKeyboard()
            openImageChooser()
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_vehicle))


                if (viewModel.imagenCirculacionFile.value != null ) {

                    viewModel.uploadFile(
                        file = viewModel.imagenCirculacionFile.value,
                        id = vehiculo.id!!,
                        fileType = TipoFichero.VEHICULO_CIRCULACION
                    )



                } else {
                    viewModel.updateVehicleById(

                        Vehiculo(
                            vehiculoVerificacion = VehiculoVerificacion(
                                matricula = binding.tvMatricula.editText?.text.toString(),
                                circulacion =  binding.tvCirculacion.editText?.text.toString(),
                            )

                        ),
                        vehiculoId = vehiculo.id!!
                    )

                    closeThisBottomSheetDialogFragment()
                }




            }
        }

    }

    private fun openImageChooser() {
        startActivityForResult.launch("image/jpeg")
    }

    private fun closeThisBottomSheetDialogFragment(){


        this.dismiss()

        viewModel.imagenCirculacionURL.value=""
        viewModel.imagenCirculacionURI.value=null
        viewModel.imagenCirculacionFile.value=null



    }


    private fun verifyData(): Boolean {


        val mMatricula = binding.tvMatricula.editText!!.text.trim().toString()
        val mCirculacion = binding.tvCirculacion.editText!!.text.trim().toString()

        binding.tvMatricula.isErrorEnabled=false
        binding.tvCirculacion.isErrorEnabled=false


        return when {

            //mMatricula
            TextUtils.isEmpty(mMatricula.trim { it <= ' ' }) && vehiculo.tipoVehiculo?.requiereVerification!! -> {
                binding.tvMatricula.error =  "Por favor especifique la matrícula del vehículo"
                false
            }
            //mMatricula
            mMatricula.trim().length < 7 && vehiculo.tipoVehiculo?.requiereVerification!! -> {
                binding.tvMatricula.error =  "La matrícula del vehículo está incompleta"
                false
            }
            //mCirculacion
            TextUtils.isEmpty(mCirculacion.trim { it <= ' ' }) && vehiculo.tipoVehiculo?.requiereVerification!! -> {
                binding.tvCirculacion.error =   "Por favor especifique la circulación del vehiculo"
                false
            }
            //foto ImageCirculacion del vehiculo si la requiere
            (viewModel.imagenCirculacionFile.value == null) &&
                    vehiculo.tipoVehiculo?.requiereVerification!! &&
                    vehiculo.vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty() -> {
                binding.tvCirculacion.error = "Por favor seleccione la fotocopia de la circulación del vehículo"
                false
            }
            else -> {
                true
            }

        }


    }


    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri ->

            if (uri != null ) {

                viewModel.imagenCirculacionURI.value = uri

                binding.ivImageCirculacion.loadImageVehiculoCirculacionFromURI(uri)

                viewModel.imagenCirculacionFile.postValue(
                    requireContext().prepareImageCompressAndGetFile(uri)
                )

                //Toast.makeText(requireContext(),getString(R.string.image_sussefuctly_selected),Toast.LENGTH_SHORT).show()

            }


        })

}