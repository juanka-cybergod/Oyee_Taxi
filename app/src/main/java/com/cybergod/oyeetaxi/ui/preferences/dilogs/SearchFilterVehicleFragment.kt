package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.SmsProvider
import com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter.VehicleFilterOptions
import com.cybergod.oyeetaxi.databinding.DialogFilterVehicleBinding
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.VehiclesAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_FILTER_OPTIONS
import com.cybergod.oyeetaxi.utils.Constants.VEHICLE_ANY_TYPE
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.showDropDownMenuFix

class SearchFilterVehicleFragment : DialogFragment() {

    private var _binding: DialogFilterVehicleBinding? = null
    private val binding get() = _binding!!


    val viewModel: VehiclesAdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogFilterVehicleBinding.inflate(inflater, container, false)




        requireArguments().getParcelable<VehicleFilterOptions>(KEY_VEHICLE_FILTER_OPTIONS)?.let { filterOptions ->
            loadData(filterOptions)
        }


        setupOnClickListener()


        return  binding.root

    }




    private fun loadData(filterOptions: VehicleFilterOptions) {

        setupVehicleTypesListAdapter()

            with (binding) {
                tvTipoVehiculo.setText(filterOptions.tipoVehiculo ?: VEHICLE_ANY_TYPE,false)
                switchNoVisibles.isChecked = filterOptions.noVisibles?:false
                switchDeshabilitados.isChecked = filterOptions.deshabilitados?:false
                switchVerificacionPendiente.isChecked = filterOptions.verificacionesPendientes?:false
            }

        fillTexViewVehicleTypesList()

    }

    private fun setupVehicleTypesListAdapter(){
        viewModel.arrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_map_style,
            viewModel.vehicleTypesItems
        )
    }

    private fun fillTexViewVehicleTypesList() {

        binding.tvTipoVehiculo.setAdapter(
            viewModel.arrayAdapter
        )
        binding.tfTipoVehiculo.clearFocus()
    }


    private var selectedTipoVehiculo : String? = null

    private fun setupOnClickListener() {

        with(binding) {
            btnClose.setOnClickListener {
                closethisfragment()
            }


            btnApply.setOnClickListener {

                viewModel.vehicleFilterOptions.apply {
                    tipoVehiculo = selectedTipoVehiculo
                    noVisibles = if (switchNoVisibles.isChecked) {true} else {null}
                    deshabilitados = if (switchDeshabilitados.isChecked) {true} else {null}
                    verificacionesPendientes =if (switchVerificacionPendiente.isChecked) {true} else {null}
                }

                search()

                closethisfragment()
            }




            tvTipoVehiculo.setOnClickListener {
                binding.tvTipoVehiculo.showDropDownMenuFix(viewModel.arrayAdapter)
            }


            tvTipoVehiculo.setOnItemClickListener { adapterView, view, position, id ->

                val selection = adapterView.getItemAtPosition(position).toString()

                selectedTipoVehiculo = if (selection == VEHICLE_ANY_TYPE || selection.isEmpty()){
                    null
                } else {
                    selection
                }

            }



        }

    }

    private fun search() {
        viewModel.getPage=1
        viewModel.getVehiclesPaginated()
    }


    private fun closethisfragment(){
        this.dismiss()
    }




}