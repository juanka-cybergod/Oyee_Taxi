package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.model.verification.VehiculoVerificacion
import com.cybergod.oyeetaxi.databinding.DialogVehicleEditVerificationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.VehiclesAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoCirculacionFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoCirculacionFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE_PARCELABLE
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditVehicleVerificationFragment : BottomSheetDialogFragment() {

    private var _binding: DialogVehicleEditVerificationBinding? = null
    private val binding get() = _binding!!

    val viewModel: VehiclesAdministrationViewModel by activityViewModels()

    private lateinit var vehiculo: VehiculoResponse

    private var removeImageVehicleVerificacion: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogVehicleEditVerificationBinding.inflate(inflater, container, false)

        return binding.root


    }


    override fun onResume() {
        super.onResume()
        binding.cancelButton.isChecked = false

        requireArguments().getParcelable<VehiculoResponse>(KEY_VEHICLE_RESPONSE_PARCELABLE)
            ?.let { vehicle ->

                if (!vehicle.id.isNullOrEmpty()) {
                    vehiculo = vehicle
                    loadVehicleDetails(vehicle)
                }

                setupOnClickListener()

            }


    }


    private fun loadVehicleDetails(vehiculo: VehiculoResponse) {


        with(binding) {

            switchVerificado.isChecked = vehiculo.vehiculoVerificacion?.verificado ?: false

            ivImageCirculacion.loadImageVehiculoCirculacionFromURL(vehiculo.vehiculoVerificacion?.imagenCirculacionURL)

            if (vehiculo.tipoVehiculo != null) {

                if (vehiculo.tipoVehiculo?.requiereVerification!!) {
                    tvMatricula.helperText = "Requerimiento Obligatorio"
                    tvCirculacion.helperText = "Requerimiento Obligatorio"
                    clImagenCirculacionURL.visibility = View.VISIBLE

                } else {
                    tvMatricula.helperText = "Requerimiento Opcional"
                    tvCirculacion.helperText = "Requerimiento Opcional"
                    clImagenCirculacionURL.visibility = View.VISIBLE

                }
            }

            tvMatricula.editText?.setText(vehiculo.vehiculoVerificacion?.matricula ?: "")

            tvCirculacion.editText?.setText(vehiculo.vehiculoVerificacion?.circulacion ?: "")

        }


    }


    private fun setupOnClickListener() {

        binding.ivImageCirculacion.setOnClickListener {
            if (!vehiculo.vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty()) {
                launchImageViewFragment(vehiculo.vehiculoVerificacion?.imagenCirculacionURL)
            }
        }

        binding.buttonRemoveImageVerification.setOnClickListener {
            if (!vehiculo.vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty()) {

                requireContext().showMessageDialogForResult(
                    funResult = { ok ->
                        if (ok) {
                            removeImageVehicleVerificacion = ""
                            binding.ivImageCirculacion.loadImageVehiculoCirculacionFromURLNoCache(
                                relativeURL = "",
                            )
                        }
                    },
                    title = "Requerir fotocopia al usuario",
                    message = "Desea quitar la fotocopia del documento puesto que no cumple con los parámetros y requerir una nueva al usuario",
                    icon = R.drawable.ic_alert_24
                )

            }
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {


            (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_vehicle))



            viewModel.updateVehicleAndGetVehicleResponse(

                Vehiculo(
                    id = vehiculo.id,
                    vehiculoVerificacion = VehiculoVerificacion(
                        verificado = binding.switchVerificado.isChecked,
                        matricula = binding.tvMatricula.editText?.text.toString(),
                        circulacion = binding.tvCirculacion.editText?.text.toString(),
                        imagenCirculacionURL = removeImageVehicleVerificacion

                    )

                ),

                )

            closeThisBottomSheetDialogFragment()


        }

    }


    private fun closeThisBottomSheetDialogFragment() {
        this.dismiss()
    }


    private fun launchImageViewFragment(imageURL: String?) {
        val imageViewFragment = ImageViewFragment()
        val args = Bundle()
        args.putString(Constants.KEY_IMAGE_URL, imageURL)
        imageViewFragment.arguments = args
        imageViewFragment.show(
            requireActivity().supportFragmentManager,
            "imageViewFragment+${UtilsGlobal.getRamdomUUID()}"
        )
    }

}