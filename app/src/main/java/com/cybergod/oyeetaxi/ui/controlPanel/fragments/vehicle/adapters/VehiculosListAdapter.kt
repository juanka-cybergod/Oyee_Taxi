package com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.ItemVehiculoBinding
import com.cybergod.oyeetaxi.ui.controlPanel.fragments.vehicle.VehicleControlPanelFragmentList
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVehiculoActivo
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesVehiculos
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoClimatizado
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoMatricula
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoVerificacionButton
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoVerificacionImage


class VehiculosListAdapter (
    private val vehicleControlPanelFragmentList: VehicleControlPanelFragmentList,
) : RecyclerView.Adapter<VehiculosListAdapter.MyViewHolder>() {

    private var vehiculosList: List<VehiculoResponse> = emptyList<VehiculoResponse>()


    fun setVehiculosList(vehiculosListFromData : List<VehiculoResponse> ){
        vehiculosList = vehiculosListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVehiculoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            vehicleControlPanelFragmentList
        )
    }

    override fun getItemCount(): Int {
        return vehiculosList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(vehiculosList[position])
    }




    class MyViewHolder(
        private val binding : ItemVehiculoBinding,
        private val vehicleControlPanelFragmentList: VehicleControlPanelFragmentList
    ): RecyclerView.ViewHolder(binding.root){
        //Nuevo Metodo Binding para Cargar las Vistas dentro de los Adapters



        fun bind(vehiculo:VehiculoResponse ) {

                with(binding) {
                    //Ocultar las Opciones por defecto
                    clVehiculoOpciones.visibility = vehicleControlPanelFragmentList.viewModel.rememberListExpanded[vehiculo.id!!] ?:View.GONE

                    //vehiculo Activo o Inactivo
                    buttonVehiculoActivo.setButtonVehiculoActivo(vehiculo.activo)

                    //Imagen Frontal
                    imageVehiculo.loadImageVehiculoFrontalFromURL(vehiculo.imagenFrontalPublicaURL)

                    //Verificacion
                    imageVehiculoVerificado.setVehiculoVerificacionImage(vehiculo)
                    buttonVehiculoVerificacion.setVehiculoVerificacionButton(vehiculo)

                    //Matricula
                    tvMatricula.setVehiculoMatricula(vehiculo.vehiculoVerificacion?.matricula)

                    //Detalles del Vehiculo
                    tvDetalles.setDetallesVehiculos(vehiculo)

                    //Climatizado
                    imageVehiculoClimatizado.setVehiculoClimatizado(vehiculo)
                }



                //setupOnClickListeners
                setupOnClickListeners(vehiculo)


        }



        private fun setupOnClickListeners(vehiculo: VehiculoResponse) {
            //Btn Climatizado
            binding.imageVehiculoClimatizado.setOnClickListener {
                Toast.makeText(vehicleControlPanelFragmentList.requireContext(),"Climatizado", Toast.LENGTH_LONG).show()
                it.isEnabled=false
            }

            //Mostrar Ocultar Opciones de Vehiculo
            binding.clVehiculoActivo.setOnClickListener {
                when (binding.clVehiculoOpciones.visibility) {
                    View.GONE -> {
                        binding.clVehiculoOpciones.visibility = View.VISIBLE
                    }
                    View.VISIBLE -> {
                        binding.clVehiculoOpciones.visibility = View.GONE
                    }
                    else -> { /*NADA*/ }
                }

                //Guardar para Recordar si la Lista de Vehiculos estaba Expandida o no
                vehicleControlPanelFragmentList.viewModel.rememberListExpanded[vehiculo.id!!] = binding.clVehiculoOpciones.visibility


            }

            //Boton Activar o Desactivar Vehiculo
            binding.buttonVehiculoActivo.setOnClickListener {
                showSetActiveVehicleDialog(vehiculo)
            }

            //Boton Editar Vehiculo
            binding.buttonEditarVehiculo.setOnClickListener {
                findNavController(vehicleControlPanelFragmentList).navigate(R.id.action_vehicleListControlPanelFragment_to_vehicleControlPanelFragmentEdit,
                    Bundle().apply {
                    putParcelable(Constants.KEY_VEHICLE_PARCELABLE, vehiculo)
                })
            }

            //Boton Verificar Vehiculo
            binding.buttonVehiculoVerificacion.setOnClickListener {
                if (vehiculo.vehiculoVerificacion?.verificado!=true) {
                    launchVehicleVerificationFragment(vehiculo)

                }


            }

            //Boton Cambiar Imagen Frontal de Vehiculo
            binding.imageViewSelect2.setOnClickListener {
                vehicleControlPanelFragmentList.openImageChooser(vehiculo)
            }

        }

        private fun launchVehicleVerificationFragment(vehiculoResponse: VehiculoResponse) {
            findNavController(vehicleControlPanelFragmentList).navigate(R.id.action_vehicleListControlPanelFragment_to_vehicleControlPanelFragmentEditVerification,
                Bundle().apply {
                    putParcelable(Constants.KEY_VEHICLE_PARCELABLE, vehiculoResponse)
                })
        }

        private fun setActiveVehicle(vehiculo: VehiculoResponse) {
            //Show Dialog
            (vehicleControlPanelFragmentList.requireActivity() as BaseActivity).
            showProgressDialog(vehicleControlPanelFragmentList.getString(R.string.updating_vehicle))

            //Update Vehicle
            vehicleControlPanelFragmentList.viewModel.activeVehicleToUserId(vehiculo.usuario?.id.orEmpty(),vehiculo.id.orEmpty())

        }

        private fun showSetActiveVehicleDialog(vehiculo: VehiculoResponse) {
            val title  = "Activar este Vehículo"
            val icon = R.drawable.ic_vehicle_active_32
            val text  = "Al activar este vehículo será visible en el mapa para todos los pasajeros mientras que los demás pasarán a inactivos, podrá cambiar su selección en cuaquier momento"

            val builder = AlertDialog.Builder(vehicleControlPanelFragmentList.requireContext())
            builder
                .setTitle(title)
                .setMessage(text)
                .setIcon(icon)
                .setPositiveButton("Activar"){ dialogInterface , _ ->
                    dialogInterface.dismiss()
                    setActiveVehicle(vehiculo)
                }
                .setNegativeButton("Cancelar"){ dialogInterface, _ ->
                    dialogInterface.cancel()
                }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()


        }



    }



}
