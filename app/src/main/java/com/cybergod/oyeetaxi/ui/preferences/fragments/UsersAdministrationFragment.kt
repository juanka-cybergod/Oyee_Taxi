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
import com.cybergod.oyeetaxi.databinding.FragmentUsersAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.UsersEditListAdapter
import com.cybergod.oyeetaxi.ui.preferences.adapters.UsersEditListAdapterNew
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UsersAdministrationFragment : BaseFragment() {


    private var _binding: FragmentUsersAdministrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView:RecyclerView
//    private lateinit var recyclerViewAdapter : UsersEditListAdapter
private lateinit var recyclerViewAdapter : UsersEditListAdapterNew

    val viewModel: UsersAdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersAdministrationBinding.inflate(inflater, container, false)


        initRecyclerView()


        setupObservers()

        getUsers1stTime()


        return  binding.root
    }


    private fun initRecyclerView(){
//        recyclerViewAdapter = UsersEditListAdapter(this)
        recyclerViewAdapter = UsersEditListAdapterNew(this)
        recyclerView = binding.recylerViewUsers
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }


    private fun setupObservers() {

        setupVehiclesTypesListObserver()

        //setupProvinceAddedOrUpdated()

    }
//
//    private fun setupProvinceAddedOrUpdated() {
//        viewModel.vehicleTypeAddedOrUpdated.observe(viewLifecycleOwner, Observer {
//
//            (requireActivity() as BaseActivity).hideProgressDialog()
//            if (it == null) {
//                requireActivity().showSnackBar(
//                    getString(R.string.fail_server_comunication),
//                    true
//                )
//            }
//
//        })
//
//    }
//
//
    private fun setupVehiclesTypesListObserver(){
        viewModel.usersList.observe(viewLifecycleOwner, Observer {



            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE
                binding.scrollView2.visibility = View.VISIBLE


                if (it.isNotEmpty()) {

                    it.plus(it)
//                    recyclerViewAdapter.setUsersList(it)
//                    recyclerViewAdapter.notifyDataSetChanged()

                    recyclerViewAdapter.differ.submitList(it)


                } else {

                    showSnackBar(
                        getString(R.string.no_users_available),
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


    private fun getUsers1stTime(){
        binding.animationView.visibility = View.VISIBLE
        binding.scrollView2.visibility = View.INVISIBLE

        updateUsersList()
    }




    override fun onResume() {
        super.onResume()
        updateUsersList()

    }

    private fun updateUsersList(){
        viewModel.getAllVehicleTypes()
    }




}




