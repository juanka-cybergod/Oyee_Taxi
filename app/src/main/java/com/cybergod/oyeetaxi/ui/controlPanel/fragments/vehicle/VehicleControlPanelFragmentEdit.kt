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
import com.cybergod.oyeetaxi.api.model.Vehiculo
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.VehicleControlPanelFragmentEditBinding
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_PARCELABLE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal
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
            showDialogYearPicker(initialYear)
        }


    }


    private fun showDialogYearPicker(initialYear:Int? = null) {



        val alertDialog: AlertDialog?
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogStyle)
        val inflater = requireActivity().layoutInflater

        val cal = Calendar.getInstance()

        val dialog = inflater.inflate(R.layout.year_picker_dialog, null)
        val monthPicker = dialog.findViewById(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH) + 1

        val year = initialYear ?: cal.get(Calendar.YEAR)

        yearPicker.minValue = 1900
        yearPicker.maxValue = UtilsGlobal.getCurrentYear()

        yearPicker.value = year

        builder.setView(dialog).setPositiveButton(Html.fromHtml("<font color='#FF4081'>Ok</font>")){ dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            val value = yearPicker.value
            binding.tvAno.editText?.setText(value.toString())
            dialogInterface.cancel()
        }

        builder.setNegativeButton(Html.fromHtml("<font color='#FF4081'>Cancel</font>")){ dialogInterface, which ->
            dialogInterface.cancel()
        }

        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
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
                //Marca
                TextUtils.isEmpty(mMarca.trim { it <= ' ' }) -> {
                    binding.tvMarca.error =  "Por favor introduzca la marca del vehículo"
                    false
                }
                //Modelo
                TextUtils.isEmpty(mModelo.trim { it <= ' ' }) -> {
                    binding.tvModelo.error =   "Por favor introduzca el modelo del vehiculo"
                    false
                }
                //Año vacio
                TextUtils.isEmpty(mAno.trim { it <= ' ' })  -> {
                    binding.tvAno.error =  "Por favor introduzca el año de fabricación del vehículo"
                    false
                }
                //Año incompleto
                ( mAno.length != 4 ) -> {
                    binding.tvAno.error =   "El año de fabricación está incompleto"
                    false
                }
                //Año incorrecto
                (mAno.toInt() < 1900 ) or (mAno.toInt() > UtilsGlobal.getCurrentYear()) -> {
                    binding.tvAno.error = "El año de fabricación está incorrecto"
                    false
                }
                //CapacidadPasajeros
                mTipoVehiculo.transportePasajeros!! && TextUtils.isEmpty(mCapacidadPasajeros.trim { it <= ' ' }) -> {
                    binding.tvCapacidadPasajeros.error =   "Por favor especifique la capacidad de pasajeros"
                    false
                }
                //CapacidadEquipaje
                mTipoVehiculo.transportePasajeros &&  TextUtils.isEmpty(mCapacidadEquipaje.trim { it <= ' ' }) -> {
                    binding.tvCapacidadEquipaje.error =  "Por favor especifique la capacidad de equipaje"
                    false
                }
                //CapacidadCarga
                mTipoVehiculo.transporteCarga!! && TextUtils.isEmpty(mCapacidadCarga.trim { it <= ' ' }) -> {

                    binding.tvCapacidadCarga.error =   "Por favor especifique la capacidad de carga"
                    false
                }

                else -> {
                    true
                }

            }


    }




}