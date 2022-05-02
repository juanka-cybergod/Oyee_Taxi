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
import com.cybergod.oyeetaxi.ui.main.viewmodel.HomeViewModel
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



    val viewModelHome: HomeViewModel by activityViewModels()
    val UserRegistrationViewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //TODO Inflar la Vista
        _binding = FragmentVehiclesTypeBinding.inflate(inflater, container, false)

        //TODO Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[VehicleTypeViewModel::class.java]

        //TODO Inicializa el ReciclerView
        initRecyclerView()

        //TODO Preparar el ViewModel para Escuchar los Vehiculos
        setupVehicleTypesObserver()

        //TODO Escucha el Click sobre el Boton Para Cerrar el Dialogo
        binding.btnOK.setOnClickListener {
            dismiss()
        }





        return  binding.root
    }



    //TODO Inicializa el RecylreyView
    private fun initRecyclerView(){

        // Recyclerview
        recyclerViewAdapter = VehicleTypeListAdapter(requireContext(),requireActivity(),binding.root,this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

    }

    //TODO Preparar el ViewModel para Escuchar los Vehiculos
    private fun setupVehicleTypesObserver(){

        //Observar vehicleList
        //Otro Metodo ->viewModel.getAllVehiclesObserver()(this, Observer {
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

        viewModel.getAllVehicleTypes()







        vehicleTypeSelected.observe(this, Observer {

            (requireActivity() as Communicator).passVehicleTypeSelected(vehicleTypeSelected.value!!)

            dismiss()

        })







    }


    override fun dismiss() {
        super.dismiss()
    }
}