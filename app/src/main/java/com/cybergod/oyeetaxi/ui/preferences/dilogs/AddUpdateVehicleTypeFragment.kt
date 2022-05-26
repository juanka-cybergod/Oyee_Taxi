package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.DialogAddUpdateVehicleTypeBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.VehiclesTypesAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_TYPE_PARCELABLE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddUpdateVehicleTypeFragment : BottomSheetDialogFragment() {

    private var _binding: DialogAddUpdateVehicleTypeBinding? = null
    private val binding get() = _binding!!


    val viewModel: VehiclesTypesAdministrationViewModel by activityViewModels()

    private var tipoVehiculo:TipoVehiculo?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogAddUpdateVehicleTypeBinding.inflate(inflater, container, false)

        requireArguments().getParcelable<TipoVehiculo>(KEY_VEHICLE_TYPE_PARCELABLE)?.let { tipo ->
            tipoVehiculo = tipo
            loadData(tipo)
        }


        setupOnClickListener()


        return  binding.root

    }


    private fun loadData(tipoVehiculo: TipoVehiculo) {

            with(binding) {
                imageTipoVehiculo.loadImageVehiculoFrontalFromURL(tipoVehiculo.imagenVehiculoURL)
                continueButton.text = getString(R.string.apply)
                tvTipoVehiculo.editText?.setText(tipoVehiculo.tipoVehiculo?:"")
                tvTipoVehiculo.isEnabled = false
                tvCuotaMensualActual.editText?.setText("${tipoVehiculo.cuotaMensual?:""}")
                tvDescripcionTipoVehiculo.editText?.setText(tipoVehiculo.descripcion?:"")
                switchSeleccionable.isChecked = tipoVehiculo.seleccionable?:true
                switchTransportePasajeros.isChecked = tipoVehiculo.transportePasajeros?:true
                switchTransporteCarga.isChecked = tipoVehiculo.transporteCarga?:false
                switchRequiereVerificacion.isChecked = tipoVehiculo.requiereVerification?:true

            }

    }


    private fun setupOnClickListener() {
        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                addUpdateVehicleType()

            }
        }

    }

    private fun addUpdateVehicleType() {

        if (tipoVehiculo != null) {
            (requireActivity() as BaseActivity).showProgressDialog("Actualizando tipo de vehículo ...")

                viewModel.updateThisVehicleType(
                    TipoVehiculo(
                         tipoVehiculo  = tipoVehiculo?.tipoVehiculo,
                         descripcion  = binding.tvDescripcionTipoVehiculo.editText?.text.toString(),
                         cuotaMensual  = binding.tvCuotaMensualActual.editText?.text.toString().toInt() ?: 0,
                         seleccionable  = binding.switchSeleccionable.isChecked,
                         prioridadEnMapa  = 1,
                         transportePasajeros = binding.switchTransportePasajeros.isChecked,
                         transporteCarga  = binding.switchTransporteCarga.isChecked,
                         requiereVerification = binding.switchRequiereVerificacion.isChecked,
                         imagenVehiculoURL  = null,
                    )
                )



        } else {
//            (requireActivity() as BaseActivity).showProgressDialog("Añadiendo tipo de vehículo ...")
//
//                viewModel.addProvince(
//                    Provincia(
//                        nombre = binding.tvProvinceName.editText?.text.toString().trim(),
//                        visible = true,
//                        ubicacion = Ubicacion(
//                            latitud = newLatitude,
//                            longitud = newLongitude,
//                            alturaMapa = 8,
//                        )
//
//                    )
//                )

        }




        closeThisBottomSheetDialogFragment()

    }


    private fun verifyData(): Boolean {

        with (binding) {
            val mTipoVehiculo: String = tvTipoVehiculo.editText!!.text.trim().toString()
            val mCuotaMensualActual: String = tvCuotaMensualActual.editText!!.text.trim().toString()
            val mDescripcionTipoVehiculo: String = tvDescripcionTipoVehiculo.editText!!.text.trim().toString()


            tvTipoVehiculo.isErrorEnabled=false
            tvCuotaMensualActual.isErrorEnabled=false
            tvDescripcionTipoVehiculo.isErrorEnabled=false


            return when {
                mTipoVehiculo.isEmptyTrim() -> {
                    tvTipoVehiculo.error = "Por favor introduzca el tipo de vehículo"
                    false
                }
                vehicleTypeExist(mTipoVehiculo) && tvTipoVehiculo.isEnabled -> {
                    tvTipoVehiculo.error = "Ya existe este tipo de vehículo"
                    false
                }
                mCuotaMensualActual.isEmptyTrim() -> {
                    tvCuotaMensualActual.error = "Por favor introduzca un cantidad"
                    false
                }
                mDescripcionTipoVehiculo.isEmptyTrim() -> {
                    tvDescripcionTipoVehiculo.error = "Por favor introduzca una descripción"
                    false
                }
                !binding.switchTransportePasajeros.isChecked && !binding.switchTransporteCarga.isChecked -> {
                    lifecycleScope.launch {
                        binding.switchTransportePasajeros.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                        binding.switchTransporteCarga.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                        Toast.makeText(requireContext(),"Habilite al menos un tipo de transporte",Toast.LENGTH_LONG).show()
                        delay(2500)
                        binding.switchTransportePasajeros.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        binding.switchTransporteCarga.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))


                    }
                    false
                }
                else -> {
                    true
                }

            }

        }



    }





    private fun vehicleTypeExist(tipoVehiculo:String):Boolean {
        var found = false
        viewModel.vehiclesTypesList.value?.let { listaTiposVehiculos ->
            listaTiposVehiculos.find { tipo -> tipo.tipoVehiculo!!.contains(tipoVehiculo,true) }
        }?.let {
            found = true
        }
        return found
    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}