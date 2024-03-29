package com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.VehicleControlPanelFragmentEditBinding
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showDialogYearPicker
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_PARCELABLE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentYear
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*


@AndroidEntryPoint
class VehicleControlPanelFragmentEdit: BottomSheetDialogFragment()  {

    private var _binding: VehicleControlPanelFragmentEditBinding? = null
    private val binding get() = _binding!!

    val viewModel: VehicleControlPanelViewModel by activityViewModels()

    private lateinit var vehiculoId:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VehicleControlPanelFragmentEditBinding.inflate(inflater, container, false)

        //Obtener el Vehiculo pasado por Argumentos para Editarlo
        requireArguments().getParcelable<VehiculoResponse>(KEY_VEHICLE_PARCELABLE)?.let { vehicle->

            if (!vehicle.id.isNullOrEmpty()) {
                vehiculoId = vehicle.id!!
                loadVehicleDetails(vehicle)
            }

        }


        setupOnClickListener()


        return  binding.root


    }


    private fun loadVehicleDetails(vehiculo: VehiculoResponse) {

        if (vehiculo.tipoVehiculo != null) {

            if (vehiculo.tipoVehiculo?.transportePasajeros!!) {
                binding.llCapacidadPasajeros.visibility = View.VISIBLE
                binding.llCapacidadEquipaje.visibility = View.VISIBLE

            } else {
                binding.llCapacidadPasajeros.visibility = View.GONE
                binding.llCapacidadEquipaje.visibility = View.GONE
            }

            if (vehiculo.tipoVehiculo?.transporteCarga!!) {
                binding.llCapacidadCarga.visibility = View.VISIBLE
            } else {
                binding.llCapacidadCarga.visibility = View.GONE
            }

        }


        binding.tvMarca.editText?.setText(vehiculo.marca)
        binding.tvModelo.editText?.setText(vehiculo.modelo)
        binding.tvAno.editText?.setText(vehiculo.ano)
        binding.tvCapacidadPasajeros.editText?.setText(vehiculo.capacidadPasajeros)
        binding.tvCapacidadEquipaje.editText?.setText(vehiculo.capacidadEquipaje)
        binding.tvCapacidadCarga.editText?.setText(vehiculo.capacidadCarga)
        binding.rbYes.isChecked = vehiculo.climatizado ?: false








    }


    private fun setupOnClickListener() {

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_vehicle))

                viewModel.updateVehicleById(
                    Vehiculo(
                        marca =binding.tvMarca.editText?.text.toString(),
                        modelo = binding.tvModelo.editText?.text.toString(),
                        ano = binding.tvAno.editText?.text.toString(),
                        capacidadPasajeros = binding.tvCapacidadPasajeros.editText?.text.toString(),
                        capacidadEquipaje = binding.tvCapacidadEquipaje.editText?.text.toString(),
                        capacidadCarga = binding.tvCapacidadCarga.editText?.text.toString(),
                        climatizado = binding.rbYes.isChecked,
                        ),
                    vehiculoId = vehiculoId
                )
                closeThisBottomSheetDialogFragment()


            }
        }


        binding.etAno.setOnClickListener {
            var initialYear : Int? = null
            try {
                initialYear = binding.tvAno.editText?.text.toString().toInt()
            } catch (e: Exception){}

            requireActivity().showDialogYearPicker(
                funResult = { yearSelected -> binding.tvAno.editText?.setText(yearSelected) },
                initialYear = initialYear
            )
        }


    }



    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }


    private fun verifyData(): Boolean {

        val mTipoVehiculo = currentVehicleActive.value?.tipoVehiculo!!
        val mMarca: String = binding.tvMarca.editText!!.text.trim().toString()
        val mModelo = binding.tvModelo.editText!!.text.trim().toString()
        val mAno = binding.tvAno.editText!!.text.trim().toString()
        val mCapacidadPasajeros = binding.tvCapacidadPasajeros.editText!!.text.trim().toString()
        val mCapacidadEquipaje: String = binding.tvCapacidadEquipaje.editText!!.text.trim().toString()
        val mCapacidadCarga = binding.tvCapacidadCarga.editText!!.text.trim().toString()
        //val mClimatizado = binding.rbYes.isChecked


        binding.tvMarca.isErrorEnabled=false
        binding.tvModelo.isErrorEnabled=false
        binding.tvAno.isErrorEnabled=false
        binding.tvCapacidadPasajeros.isErrorEnabled=false
        binding.tvCapacidadEquipaje.isErrorEnabled=false
        binding.tvCapacidadCarga.isErrorEnabled=false


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