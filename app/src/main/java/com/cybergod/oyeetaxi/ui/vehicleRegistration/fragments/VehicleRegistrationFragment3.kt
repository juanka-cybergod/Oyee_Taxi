package com.cybergod.oyeetaxi.ui.vehicleRegistration.fragments


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.VehicleRegistrationFragment3Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.vehicleRegistration.viewmodel.VehicleRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoCirculacionFromURI
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VehicleRegistrationFragment3 : BaseFragment() {


    private var _binding: VehicleRegistrationFragment3Binding? = null
    private val binding get() = _binding!!

    val viewModel: VehicleRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VehicleRegistrationFragment3Binding.inflate(inflater, container, false)

        loadTempDataFromViewModel()

        setupOnClickListeners()


        return binding.root
    }

    private fun setupOnClickListeners() {
        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                addNewVehicle()
            }
        }


        binding.buttonSelectImageCirculacion.setOnClickListener {
            openImageChooser()
        }
    }


    private fun closeActivity(){
        lifecycleScope.launch {
            delay(500)
            requireActivity().finish()
        }
    }

    private fun openImageChooser() {
        requireView().hideKeyboard()
        startActivityForResult.launch("image/jpeg")
    }

    private fun loadTempDataFromViewModel() {

        binding.ivImageCirculacion.loadImageVehiculoCirculacionFromURI(viewModel.imagenCirculacionURI.value)

        if (viewModel.tipoVehiculo.value != null) {

            if (viewModel.tipoVehiculo.value?.requiereVerification!!) {
                binding.tvMatricula.helperText = "Requerimiento Obligatorio"
                binding.tvCirculacion.helperText = "Requerimiento Obligatorio"
                binding.clImagenCirculacionURL.visibility = View.VISIBLE

            } else {
                binding.tvMatricula.helperText = "Requerimiento Opcional"
                binding.tvCirculacion.helperText = "Requerimiento Opcional"
                binding.clImagenCirculacionURL.visibility = View.VISIBLE

            }
        }


        binding.tvMatricula.editText?.setText(viewModel.matricula.value ?: "")

        binding.tvCirculacion.editText?.setText(viewModel.circulacion.value ?: "")


    }

    private fun addNewVehicle() {

        showProgressDialog(getString(R.string.finishing_vahicle_registration))

        lifecycleScope.launch (Dispatchers.IO){
            delay(500L)


            when (viewModel.addNewVehicle()) {
                true -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.vehicle_sussefuctly_registered),
                        false,
                    )

                    viewModel.updateCurrentUserAndActiveVehicleGlobalVariables()

                    closeActivity()

                }

                false -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.vehicle_register_fail),
                        true
                    )
                }

                else -> {
                    hideProgressDialog()
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true,
                    )

                }

            }


        }

    }

    private fun verifyData(): Boolean {


        val mMatricula = binding.tvMatricula.editText!!.text.trim().toString()
        val mCirculacion: String = binding.tvCirculacion.editText!!.text.trim().toString()




        return when {

            //mMatricula
            viewModel.tipoVehiculo.value == null -> {
                false
            }
            TextUtils.isEmpty(mMatricula.trim { it <= ' ' }) && viewModel.tipoVehiculo.value?.requiereVerification!! -> {
                showSnackBar(
                    "Por favor especifique la matrícula del vehículo",
                    true,
                )
                false
            }
            //mMatricula
            mMatricula.trim().length < 7 && viewModel.tipoVehiculo.value?.requiereVerification!! -> {
                showSnackBar(
                    "La matrícula del vehículo está incompleta",
                    true,
                )
                false
            }
            //mCirculacion
            TextUtils.isEmpty(mCirculacion.trim { it <= ' ' }) && viewModel.tipoVehiculo.value?.requiereVerification!! -> {
                showSnackBar(
                    "Por favor especifique la circulación del vehiculo",
                    true,
                )
                false
            }
            //foto ImageCirculacion del vehiculo si la requiere
            viewModel.imagenCirculacionFile.value == null   && viewModel.tipoVehiculo.value?.requiereVerification!! -> {
                showSnackBar(
                    "Por favor seleccione la fotocopia de la circulación del vehículo",
                    true,
                )
                false
            }

            else -> {
                viewModel.matricula.postValue(mMatricula)
                viewModel.circulacion.postValue(mCirculacion)
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


//                showSnackBar(
//                    getString(R.string.image_sussefuctly_selected),
//                    false
//                )


            }

        })



}