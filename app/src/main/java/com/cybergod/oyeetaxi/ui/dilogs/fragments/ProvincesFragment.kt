package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.databinding.FragmentProvincesBinding
import com.cybergod.oyeetaxi.ui.dilogs.adapters.ProvinciasListAdapter
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.ProvincesViewModel
import com.cybergod.oyeetaxi.ui.interfaces.Communicator
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProvincesFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProvincesBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: ProvincesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ProvinciasListAdapter


    val provinceSelected : MutableLiveData<Provincia> = MutableLiveData()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentProvincesBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(this)[ProvincesViewModel::class.java]


        initRecyclerView()


        setupProvincesObserver()


        binding.btnOK.setOnClickListener {
            dismiss()
        }





        return  binding.root
    }


    private fun initRecyclerView(){

        recyclerViewAdapter = ProvinciasListAdapter(this)
        recyclerView = binding.recylerViewProvincias
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

    }


    private fun setupProvincesObserver(){

        viewModel.provincesList.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE

                if (it.isNotEmpty()) {

                    it.plus(it)
                    recyclerViewAdapter.setProvinciasList(it)
                    recyclerViewAdapter.notifyDataSetChanged()


                } else {

                    dismiss()

                    requireActivity().showSnackBar(
                        getString(R.string.no_provinces_availables),
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

        viewModel.getAllProvinces()



        provinceSelected.observe(this, Observer {

            (requireActivity() as Communicator).passProvinceSelected(provinceSelected.value!!)

            dismiss()

        })


    }

}