package com.cybergod.oyeetaxi.ui.preferences.fragments.administration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentProvincesAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.ProvincesEditListAdapter
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.ProvincesAdministrationViewModel
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

        return  binding.root
    }


    private fun initRecyclerView(){
        recyclerViewAdapter = ProvincesEditListAdapter(this)
        recyclerView = binding.recylerViewProcincias
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }


    private fun setupObservers() {

        with(viewModel) {


            isLoading.observe(viewLifecycleOwner, Observer {
                val visibility = if (it == true) {
                    View.VISIBLE
                } else (View.GONE)
                binding.isLoadingAnimation.visibility = visibility
            })

            provincesAddedOrUpdated.observe(viewLifecycleOwner, Observer {

                (requireActivity() as BaseActivity).hideProgressDialog()
                if (it == null) {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                }
            })

            provincesList.observe(viewLifecycleOwner, Observer {



                if (it != null) {

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