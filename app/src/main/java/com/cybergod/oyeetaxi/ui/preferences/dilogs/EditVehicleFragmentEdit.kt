package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.DialogEditVehicleBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.VehiclesAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showDialogYearPicker
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE_PARCELABLE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentYear
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditVehicleFragmentEdit : BottomSheetDialogFragment() {

    private var _binding: DialogEditVehicleBinding? = null
    private val binding get() = _binding!!

    val viewModel: VehiclesAdministrationViewModel by activityViewModels()

    private lateinit var vehiculoResponse: VehiculoResponse

    var removeVehicleImagenFrontal: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogEditVehicleBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onResume() {
        super.onResume()
        binding.cancelButton.isChecked = false

        requireArguments().getParcelable<VehiculoResponse>(KEY_VEHICLE_RESPONSE_PARCELABLE)
            ?.let { vehicleResponse ->

                if (!vehicleResponse.id.isNullOrEmpty()) {
                    vehiculoResponse = vehicleResponse
                    loadVehicleDetails(vehicleResponse)
                }

            }

        setupOnClickListener()

    }


    private fun loadVehicleDetails(vehiculo: VehiculoResponse) {

        with(binding) {

            imageFrontal.loadImageVehiculoFrontalFromURLNoCache(vehiculo.imagenFrontalPublicaURL)

            if (vehiculo.tipoVehiculo != null) {

                if (vehiculo.tipoVehiculo?.transportePasajeros!!) {
                    llCapacidadPasajeros.visibility = View.VISIBLE
                    llCapacidadEquipaje.visibility = View.VISIBLE

                } else {
                    llCapacidadPasajeros.visibility = View.GONE
                    llCapacidadEquipaje.visibility = View.GONE
                }

                if (vehiculo.tipoVehiculo?.transporteCarga!!) {
                    llCapacidadCarga.visibility = View.VISIBLE
                } else {
                    llCapacidadCarga.visibility = View.GONE
                }

            }


            tvMarca.editText?.setText(vehiculo.marca)
            tvModelo.editText?.setText(vehiculo.modelo)
            tvAno.editText?.setText(vehiculo.ano)
            tvCapacidadPasajeros.editText?.setText(vehiculo.capacidadPasajeros)
            tvCapacidadEquipaje.editText?.setText(vehiculo.capacidadEquipaje)
            tvCapacidadCarga.editText?.setText(vehiculo.capacidadCarga)
            rbYes.isChecked = vehiculo.climatizado ?: false


            switchHabilitado.isChecked = vehiculo.habilitado ?: true
            switchVisible.isChecked = vehiculo.visible ?: true
            tvFechaRegistro.text = "Fecha Registro : ${vehiculo.fechaDeRegistro}"


        }


    }


    private fun setupOnClickListener() {


        binding.imageFrontal.setOnClickListener {

            if (!vehiculoResponse.imagenFrontalPublicaURL.isNullOrEmpty()) {

                requireContext().showMessageDialogForResult(
                    funResult = { ok ->
                        if (ok) {
                            removeVehicleImagenFrontal = ""
                            binding.imageFrontal.loadImageVehiculoFrontalFromURLNoCache("")
                        }
                    },
                    title = "Quitar Imágen Vehículo",
                    message = "Desea quitar imágen pública de este vehículo puesto que no cumple con los parámetros requeridos",
                    icon = R.drawable.ic_alert_24
                )

            }


        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_vehicle))

                viewModel.updateVehicleAndGetVehicleResponse(
                    Vehiculo(
                        id = vehiculoResponse.id,
                        marca =binding.tvMarca.editText?.text.toString(),
                        modelo = binding.tvModelo.editText?.text.toString(),
                        ano = binding.tvAno.editText?.text.toString(),
                        capacidadPasajeros = binding.tvCapacidadPasajeros.editText?.text.toString(),
                        capacidadEquipaje = binding.tvCapacidadEquipaje.editText?.text.toString(),
                        capacidadCarga = binding.tvCapacidadCarga.editText?.text.toString(),
                        climatizado = binding.rbYes.isChecked,
                        habilitado = binding.switchHabilitado.isChecked,
                        visible = binding.switchVisible.isChecked,
                        imagenFrontalPublicaURL = removeVehicleImagenFrontal,
                    )
                )


                closeThisBottomSheetDialogFragment()


            }
        }


        binding.etAno.setOnClickListener {
            var initialYear: Int? = null
            try {
                initialYear = binding.tvAno.editText?.text.toString().toInt()
            } catch (e: Exception) {
            }

            requireActivity().showDialogYearPicker(
                funResult = { yearSelected -> binding.tvAno.editText?.setText(yearSelected) },
                initialYear = initialYear
            )
        }


    }


    private fun closeThisBottomSheetDialogFragment() {
        this.isCancelable = true
        this.dismiss()
    }


    private fun verifyData(): Boolean {

        val mTipoVehiculo = currentVehicleActive.value?.tipoVehiculo!!
        val mMarca: String = binding.tvMarca.editText!!.text.trim().toString()
        val mModelo = binding.tvModelo.editText!!.text.trim().toString()
        val mAno = binding.tvAno.editText!!.text.trim().toString()
        val mCapacidadPasajeros = binding.tvCapacidadPasajeros.editText!!.text.trim().toString()
        val mCapacidadEquipaje: String =
            binding.tvCapacidadEquipaje.editText!!.text.trim().toString()
        val mCapacidadCarga = binding.tvCapacidadCarga.editText!!.text.trim().toString()
        //val mClimatizado = binding.rbYes.isChecked


        binding.tvMarca.isErrorEnabled = false
        binding.tvModelo.isErrorEnabled = false
        binding.tvAno.isErrorEnabled = false
        binding.tvCapacidadPasajeros.isErrorEnabled = false
        binding.tvCapacidadEquipaje.isErrorEnabled = false
        binding.tvCapacidadCarga.isErrorEnabled = false



        return when {
            mMarca.isEmptyTrim() -> {
                binding.tvMarca.error = "Introduzca la marca del vehículo"
                false
            }
            mModelo.isEmptyTrim() -> {
                binding.tvModelo.error = "Introduzca el modelo del vehiculo"
                false
            }
            mAno.isEmptyTrim() -> {
                binding.tvAno.error = "Introduzca el año de fabricación del vehículo"
                false
            }
            (mAno.toInt() < 1900) or (mAno.toInt() > getCurrentYear()) -> {
                binding.tvAno.error = "El año de fabricación está incorrecto"
                false
            }
            mTipoVehiculo.transportePasajeros == true && mCapacidadPasajeros.isEmptyTrim() -> {
                binding.tvCapacidadPasajeros.error =
                    "Especifique la capacidad de pasajeros"
                false
            }
            mTipoVehiculo.transportePasajeros == true && mCapacidadEquipaje.isEmptyTrim() -> {
                binding.tvCapacidadEquipaje.error = "Especifique la capacidad de equipaje"
                false
            }
            mTipoVehiculo.transporteCarga== true && mCapacidadCarga.isEmptyTrim() -> {
                binding.tvCapacidadCarga.error = "Especifique la capacidad de carga"
                false
            }

            else -> {
                true
            }

        }


    }


}