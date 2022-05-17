package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentVehiclesTypesAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.VehiclesTypesEditListAdapter
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.VehiclesTypesAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VehiclesTypeAdministrationFragment : BaseFragment() {


    private var _binding: FragmentVehiclesTypesAdministrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter : VehiclesTypesEditListAdapter


    val viewModel: VehiclesTypesAdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVehiclesTypesAdministrationBinding.inflate(inflater, container, false)


        initRecyclerView()


        setupObservers()

        getProvinces1stTime()


        return  binding.root
    }


    private fun initRecyclerView(){
        recyclerViewAdapter = VehiclesTypesEditListAdapter(this)
        recyclerView = binding.recylerViewVehiclesTypes
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }


    private fun setupObservers() {

        setupVehiclesTypesListObserver()

        setupProvinceAddedOrUpdated()

    }

    private fun setupProvinceAddedOrUpdated() {
        viewModel.vehicleTypeAddedOrUpdated.observe(viewLifecycleOwner, Observer {

            (requireActivity() as BaseActivity).hideProgressDialog()
            if (it == null) {
                requireActivity().showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
            }

        })

    }


    private fun setupVehiclesTypesListObserver(){
        viewModel.vehiclesTypesList.observe(viewLifecycleOwner, Observer {



            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE
                binding.scrollView2.visibility = View.VISIBLE


                if (it.isNotEmpty()) {

                    it.plus(it)
                    recyclerViewAdapter.setVehiclesTypesList(it)
                    recyclerViewAdapter.notifyDataSetChanged()


                } else {

                    showSnackBar(
                        getString(R.string.no_vehicles_type_available),
                        false
                    )


                }


            } else {

                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )

            }

        })



    }


    private fun getProvinces1stTime(){
        binding.animationView.visibility = View.VISIBLE
        binding.scrollView2.visibility = View.INVISIBLE

        updateVehiclesTypesList()
    }




    override fun onResume() {
        super.onResume()
        updateVehiclesTypesList()

    }

    private fun updateVehiclesTypesList(){
        viewModel.getAllVehicleTypes()
    }




}




