package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.FragmentVehiclesTypeBinding
import com.cybergod.oyeetaxi.ui.dilogs.adapters.VehicleTypeListAdapter
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.VehicleTypeViewModel
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleTypeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentVehiclesTypeBinding? = null
    private val binding get() = _binding!!

    //viewModel
    lateinit var viewModel: VehicleTypeViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: VehicleTypeListAdapter


    val vehicleTypeSelected : MutableLiveData<TipoVehiculo> = MutableLiveData()


    val userRegistrationViewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentVehiclesTypeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[VehicleTypeViewModel::class.java]

        initRecyclerView()

        setupVehicleTypesObserver()

        binding.btnOK.setOnClickListener {
            dismiss()
        }





        return  binding.root
    }



    private fun initRecyclerView(){
        recyclerViewAdapter = VehicleTypeListAdapter(this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

    }


    private fun setupVehicleTypesObserver(){

        viewModel.tipoVehiculoList.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE

                if (it.isNotEmpty()) {

                    it.plus(it)
                    recyclerViewAdapter.setTipoVehiculosList(it)
                    recyclerViewAdapter.notifyDataSetChanged()


                } else {

                    dismiss()

                    requireActivity().showSnackBar(
                        getString(R.string.no_vehicles_type_available),
                        false
                    )

                }


            } else {
                dismiss()

                requireActivity().showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
            }

        })

        viewModel.getAvailableVehiclesType()


        vehicleTypeSelected.observe(this, Observer {

            (requireActivity() as Communicator).passVehicleTypeSelected(vehicleTypeSelected.value!!)

            dismiss()

        })




    }


}