package com.cybergod.oyeetaxi.ui.preferences.fragments.superAdmin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentAppUpdatesAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.AppUpdatesListAdapter
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.superAdmin.ActualizacionesViewModel
import com.cybergod.oyeetaxi.utils.Constants.KEY_APP_UPDATE_PARCELABLE
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class AppUpdateAdminFragment : BaseFragment() {


    private var _binding: FragmentAppUpdatesAdministrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: AppUpdatesListAdapter

    val viewModel: ActualizacionesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAppUpdatesAdministrationBinding.inflate(inflater, container, false)

        initRecyclerView()

        (requireActivity() as BaseActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        setupObservers()

        setupOnclickListener()


        return binding.root
    }

    private fun setupOnclickListener() {
        with(binding) {

            switchAppUpdateEnable.setOnClickListener {
               (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))
               viewModel.setAppUpdatesEnabled(switchAppUpdateEnable.isChecked)
            }


            buttonAddAppUpdate.setOnClickListener {
                launchAddEditAppUpdateFragment()
            }

        }

    }



    private fun initRecyclerView() {
        recyclerViewAdapter = AppUpdatesListAdapter(this)
        recyclerView = binding.recylerViewUsers
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }


    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        with(viewModel) {


            actualizacionesList.observe(viewLifecycleOwner, Observer { listaActualizaciones ->


                (requireActivity() as BaseActivity).hideProgressDialog()

                if (listaActualizaciones!=null) {

                    recyclerViewAdapter.differ.submitList(listaActualizaciones)

                    if (listaActualizaciones.isEmpty()) {
                            showSnackBar(
                                getString(R.string.no_app_updates_list_available),
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


            actualizacionHabilitada.observe(viewLifecycleOwner, Observer {

                (requireActivity() as BaseActivity).hideProgressDialog()

                if (it != null) {
                    binding.switchAppUpdateEnable.isChecked=it
                } else {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                }


            })


            isLoading.observe(viewLifecycleOwner, Observer {
                val visibility = if (it == true) {
                    View.VISIBLE
                } else (View.GONE)
                binding.isLoadingAnimation.visibility = visibility
            })

            viewModel.success.observe(viewLifecycleOwner, Observer {

                (requireActivity() as BaseActivity).hideProgressDialog()

                if (it == false) {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                }
            })


        }
    }


    private fun launchAddEditAppUpdateFragment() {
        findNavController().navigate(R.id.action_go_to_addEditAppUpdateFragment,Bundle().apply {
            putParcelable(KEY_APP_UPDATE_PARCELABLE,null)
        }
        )
    }

    fun deleteAppUpdateById(idActualizacion: String) {

        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.deleting_app_update))
        viewModel.deleteAppUpdateById(idActualizacion)


    }


}




