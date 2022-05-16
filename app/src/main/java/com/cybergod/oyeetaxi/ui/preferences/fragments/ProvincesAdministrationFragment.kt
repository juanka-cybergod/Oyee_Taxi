package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.databinding.FragmentProvincesAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.ProvincesEditListAdapter
import com.cybergod.oyeetaxi.ui.preferences.dilogs.TwillioConfigurationFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.ProvincesAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProvincesAdministrationFragment : BaseFragment() {


    private var _binding: FragmentProvincesAdministrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter : ProvincesEditListAdapter


    val viewModel: ProvincesAdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProvincesAdministrationBinding.inflate(inflater, container, false)


        initRecyclerView()

        setupOnClickListener()

        setupObservers()

        getProvinces1stTime()


        return  binding.root
    }


    private fun initRecyclerView(){
        recyclerViewAdapter = ProvincesEditListAdapter(this)
        recyclerView = binding.recylerViewProcincias
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }


    private fun setupObservers() {

        setupProvincesListObserver()

        setupProvinceAddedOrUpdated()

    }

    private fun setupProvinceAddedOrUpdated() {
        viewModel.provincesAddedOrUpdated.observe(viewLifecycleOwner, Observer {

            (requireActivity() as BaseActivity).hideProgressDialog()
            if (it == null) {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
            } else {

            }
        })
    }


    private fun setupProvincesListObserver(){
        viewModel.provincesList.observe(viewLifecycleOwner, Observer {



            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE
                binding.scrollView2.visibility = View.VISIBLE


                if (it.isNotEmpty()) {

                    it.plus(it)
                    recyclerViewAdapter.setProvincesList(it)
                    recyclerViewAdapter.notifyDataSetChanged()


                } else {

                    showSnackBar(
                        getString(R.string.no_provinces_availables),
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

        updateProvincesList()
    }




    private fun setupOnClickListener() {

        with (binding) {
            buttonAddProvince.setOnClickListener {
                launchAddUpdateProvinceFragment()
            }
        }

    }


    private fun launchAddUpdateProvinceFragment() {
        findNavController().navigate(R.id.action_go_to_addUpdateProvinceFragment,Bundle().apply {

        })
    }

    override fun onResume() {
        super.onResume()
        updateProvincesList()

    }

    private fun updateProvincesList(){
        viewModel.getAllProvinces()
    }




}
















/*

        binding.switchServerActive.setOnClickListener {
            binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked

        }

                binding.switchServerActive.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                if (viewModel.serverConfiguration.value?.servidorActivoClientes != binding.switchServerActive.isChecked) {
                    binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked
                }

            }
            true
        }
 */