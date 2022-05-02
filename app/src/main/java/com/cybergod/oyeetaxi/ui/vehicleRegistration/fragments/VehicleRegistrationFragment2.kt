package com.cybergod.oyeetaxi.ui.vehicleRegistration.fragments


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.VehicleRegistrationFragment2Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.vehicleRegistration.viewmodel.VehicleRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleRegistrationFragment2 : BaseFragment() {


    private var _binding: VehicleRegistrationFragment2Binding? = null
    private val binding get() = _binding!!


    //Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: VehicleRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VehicleRegistrationFragment2Binding.inflate(inflater, container, false)

        loadTempDataFromViewModel()

        setupOnClickListeners()


        return  binding.root
    }

    private fun setupOnClickListeners() {
        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                goToNextFragment()
            }
        }
    }


    private fun loadTempDataFromViewModel() {

        binding.imageFrontal.loadImageVehiculoFrontalFromURI(viewModel.imagenFrontalVehiculoURI.value)


        if (viewModel.tipoVehiculo.value != null) {


            if (viewModel.tipoVehiculo.value?.transportePasajeros!!) {
                binding.llCapacidadPasajeros.visibility = View.VISIBLE
                binding.llCapacidadEquipaje.visibility = View.VISIBLE

            } else {
                binding.llCapacidadPasajeros.visibility = View.GONE
                binding.llCapacidadEquipaje.visibility = View.GONE
            }

            if (viewModel.tipoVehiculo.value?.transporteCarga!!) {
                binding.llCapacidadCarga.visibility = View.VISIBLE
            } else {
                binding.llCapacidadCarga.visibility = View.GONE
            }

        }


        binding.tvCapacidadPasajeros.editText?.setText(viewModel.capacidadPasajeros.value ?: "")

        binding.tvCapacidadEquipaje.editText?.setText(viewModel.capacidadEquipaje.value ?: "")

        binding.tvCapacidadCarga.editText?.setText(viewModel.capacidadCarga.value ?: "")

        binding.rbYes.isChecked = viewModel.climatizado.value ?: false



    }


    private fun goToNextFragment(){
        findNavController().navigate(R.id.action_vehicleRegistrationFragment2_to_vehicleRegistrationFragment3)
    }


    private fun verifyData(): Boolean {

        val mCapacidadPasajeros = binding.tvCapacidadPasajeros.editText!!.text.trim().toString()
        val mCapacidadEquipaje: String = binding.tvCapacidadEquipaje.editText!!.text.trim().toString()
        val mCapacidadCarga = binding.tvCapacidadCarga.editText!!.text.trim().toString()
        val mClimatizado = binding.rbYes.isChecked

        viewModel.tipoVehiculo.value?.let { tipoVehiculo ->

            return when {

                //mCapacidadPasajeros
                tipoVehiculo.transportePasajeros!! && TextUtils.isEmpty(mCapacidadPasajeros.trim { it <= ' ' }) -> {
                    showSnackBar(
                        "Por favor especifique la capacidad de pasajeros",
                        true,
                    )
                    false
                }
                //mCapacidadEquipaje
                tipoVehiculo.transportePasajeros &&  TextUtils.isEmpty(mCapacidadEquipaje.trim { it <= ' ' }) -> {
                    showSnackBar(
                        "Por favor especifique la capacidad de equipaje",
                        true,
                    )
                    false
                }
                //mCapacidadCarga
                tipoVehiculo.transporteCarga!! && TextUtils.isEmpty(mCapacidadCarga.trim { it <= ' ' }) -> {
                    showSnackBar(
                        "Por favor especifique la capacidad de carga",
                        true,
                    )
                    false
                }

                else -> {


                    viewModel.capacidadPasajeros.postValue(mCapacidadPasajeros)
                    viewModel.capacidadEquipaje.postValue(mCapacidadEquipaje)
                    viewModel.capacidadCarga.postValue(mCapacidadCarga)
                    viewModel.climatizado.postValue(mClimatizado)
                    true
                }

            }


        } ?:
            showSnackBar(
                getString(R.string.please_come_back_and_select_vahicle_type),
                true,
            )
            return false
















    }


}