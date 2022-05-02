package com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle

import android.annotation.SuppressLint
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
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.Vehiculo
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.VehicleControlPanelFragmentListBinding
import com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle.adapters.VehiculosListAdapter
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.vehicleRegistration.activity.VehicleRegistrationActivity
import com.cybergod.oyeetaxi.utils.GlobalVariables
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
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

        setupVehiclesListObserver()

        setupVehicleUpdatedSussesObserver()

        setupImagenVehiculoURLUpdatedSussessObserver()

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

    private fun setupImagenVehiculoURLUpdatedSussessObserver(){

        viewModel.imagenFrontalVehiculoURL.observe(viewLifecycleOwner, Observer { imagenFrontalVehiculoURL ->
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



    private fun setupVehicleUpdatedSussesObserver(){
        viewModel.vehicleUpdatedSusses.observe(viewLifecycleOwner, Observer { updatedOK ->

            (requireActivity() as BaseActivity).hideProgressDialog()


            if (updatedOK != null){

                if (updatedOK) {

                    viewModel.updateCurrentUserAndActiveVehicleGlobalVariables()

                    viewModel.getAllVehicleFromUserId(currentUserActive.value?.id.orEmpty())


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
    }

    private fun initRecyclerView(){
        // Recyclerview
        recyclerViewAdapter = VehiculosListAdapter(requireContext(),requireActivity(),binding.root,this)
        recyclerView = binding.recylerViewVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

    }

        @SuppressLint("NotifyDataSetChanged")
    private fun setupVehiclesListObserver(){

        //Observar vehicleList
        //Otro Metodo ->viewModel.getAllVehiclesObserver()(this, Observer {
        viewModel.vehiclesList.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE

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
                GlobalVariables.isServerAvailable.postValue(true)

            } else {

                //Alerta al View Model de que hay Errores de Conexion y Servidor
                GlobalVariables.isServerAvailable.postValue(false)

                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )

            }

        })



    }


    override fun onResume() {
        super.onResume()
        updateVehiclesList()
    }

    private fun updateVehiclesList(){
        viewModel.getAllVehicleFromUserId(currentUserActive.value?.id.orEmpty())
    }

    private fun setupOnClickListener() {

        binding.btnOK.setOnClickListener {
            closeThisFragment()
        }

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