package com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.VehicleControlPanelFragmentListBinding
import com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle.adapters.VehiculosListAdapter
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.vehicleRegistration.activity.VehicleRegistrationActivity
import com.cybergod.oyeetaxi.utils.GlobalVariables
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.cybergod.oyeetaxi.utils.GlobalVariables.isServerAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VehicleControlPanelFragmentList: BaseFragment()  {

    private var _binding: VehicleControlPanelFragmentListBinding? = null
    private val binding get() = _binding!!


    val viewModel: VehicleControlPanelViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: VehiculosListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = VehicleControlPanelFragmentListBinding.inflate(inflater, container, false)

        initRecyclerView()

        setupObservers()

        setupOnClickListener()

        return  binding.root


    }


    private fun setupObservers(){

        with(viewModel) {

            isLoading.observe(viewLifecycleOwner, Observer {
                val visibility = if (it == true) {
                    View.VISIBLE
                } else (View.GONE)
                binding.isLoadingAnimation.visibility = visibility
            })

            vehiclesList.observe(viewLifecycleOwner, Observer {

                if (it != null) {

                    if (it.isNotEmpty()) {

                        it.plus(it)
                        recyclerViewAdapter.setVehiculosList(it)
                        recyclerViewAdapter.notifyDataSetChanged()


                    } else {

                        showSnackBar(
                            getString(R.string.found_no_vehicles),
                            false
                        )


                    }


                    //Alerta al View Model de que se restableció la Conexion y Servidor
                    isServerAvailable.postValue(true)

                } else {

                    //Alerta al View Model de que hay Errores de Conexion y Servidor
                    isServerAvailable.postValue(false)

                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )

                }

            })

            vehicleUpdatedSusses.observe(viewLifecycleOwner, Observer { updatedOK ->

                (requireActivity() as BaseActivity).hideProgressDialog()


                if (updatedOK != null){

                    if (updatedOK) {

                        viewModel.updateCurrentUserAndActiveVehicleGlobalVariables()

                        viewModel.getAllVehicleFromUserId()


                    } else {
                        showSnackBar(
                            getString(R.string.error_al_actualizar_vehiculo),
                            true
                        )
                    }

                } else {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true,
                    )
                }



            })

            imagenFrontalVehiculoURL.observe(viewLifecycleOwner, Observer { imagenFrontalVehiculoURL ->
                if (!imagenFrontalVehiculoURL.isNullOrEmpty()) {

                    if (!viewModel.vehicleSelectedToChangeImagen?.id.isNullOrEmpty()) {

                        viewModel.updateVehicleById(
                            Vehiculo(
                                imagenFrontalPublicaURL = imagenFrontalVehiculoURL
                            ),
                            vehiculoId = viewModel.vehicleSelectedToChangeImagen?.id!!
                        )

                        viewModel.vehicleSelectedToChangeImagen = null
                    }



                } else {

                    (requireActivity() as BaseActivity).hideProgressDialog()

                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )


                }

            })

        }




    }






    fun openImageChooser(forVehicle : VehiculoResponse) {
        viewModel.vehicleSelectedToChangeImagen = forVehicle
        startActivityForResult.launch("image/jpeg")
    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            it?.let { uri ->

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_vehicle))


                if (!viewModel.vehicleSelectedToChangeImagen?.id.isNullOrEmpty()) {

                    viewModel.uploadFile(
                        file = requireContext().prepareImageCompressAndGetFile(uri),
                        id = viewModel.vehicleSelectedToChangeImagen?.id!!,
                        fileType = TipoFichero.VEHICULO_FRONTAL
                    )

                }


            }
        }
    )



    private fun initRecyclerView(){
        recyclerViewAdapter = VehiculosListAdapter(this)
        recyclerView = binding.recylerViewVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

    }


    private fun setupOnClickListener() {

        binding.buttonAddVehicle.setOnClickListener {
            launchVehicleRegisterActivity()
        }


    }



    private fun launchVehicleRegisterActivity() {
        lifecycleScope.launch {
            delay(100)
            startActivity(
                Intent(requireActivity(),
                    VehicleRegistrationActivity::class.java)
            )
        }
    }



    private fun closeThisFragment(){

        findNavController().popBackStack()


    }






}