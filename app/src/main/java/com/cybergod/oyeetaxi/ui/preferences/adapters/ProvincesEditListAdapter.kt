package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Provincia
import com.cybergod.oyeetaxi.databinding.ItemProvinciaEditBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.fragments.ProvincesAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import kotlinx.coroutines.launch


class ProvincesEditListAdapter (
    private val provincesAdministrationFragment: ProvincesAdministrationFragment,
) : RecyclerView.Adapter<ProvincesEditListAdapter.MyViewHolder>() {

    private var provincesList: List<Provincia> = emptyList<Provincia>()


    fun setProvincesList(provincesListFromData : List<Provincia> ){
        provincesList = provincesListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_provincia_edit,parent,false))
    }

    override fun getItemCount(): Int {
        return provincesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(provincesList[position],provincesAdministrationFragment)
    }




    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemProvinciaEditBinding.bind(itemView)


        fun bind(provincia:Provincia, provincesAdministrationFragment: ProvincesAdministrationFragment) {

            with(binding) {

                tvProvinciaNombreID.text = provincia.nombre

                btnVisible.setButtonVisibilityIcon(provincia.visible?:true)

                btnVisible.setOnClickListener {
                    changeProvinceVisibility(provincesAdministrationFragment,provincia)

                }


            }



        }

        private fun changeProvinceVisibility(provincesAdministrationFragment:ProvincesAdministrationFragment,provincia: Provincia) {

            provincesAdministrationFragment.lifecycleScope.launch {

                (provincesAdministrationFragment.requireActivity() as BaseActivity).showProgressDialog("Actualizando provincia ...")

                val provinciaChanged = provincesAdministrationFragment.viewModel.setProvinceVisibility(
                    nombreProvincia = provincia.nombre?:"",
                    visible = !provincia.visible!!
                )

                (provincesAdministrationFragment.requireActivity() as BaseActivity).hideProgressDialog()

                if (provinciaChanged == null) {
                    (provincesAdministrationFragment.requireActivity() as BaseActivity).showSnackBar(
                        provincesAdministrationFragment.getString(R.string.fail_server_comunication),
                        true
                    )
                }

            }

        }



//        private fun setupOnClickListeners(vehiculo: VehiculoResponse, vehicleControlPanelFragmentList: VehicleControlPanelFragmentList) {
//            //Btn Climatizado
//            binding.imageVehiculoClimatizado.setOnClickListener {
//                Toast.makeText(vehicleControlPanelFragmentList.requireContext(),"Climatizado", Toast.LENGTH_LONG).show()
//                it.isEnabled=false
//            }
//
//            //Mostrar Ocultar Opciones de Vehiculo
//            binding.clVehiculoActivo.setOnClickListener {
//                when (binding.clVehiculoOpciones.visibility) {
//                    View.GONE -> {
//                        binding.clVehiculoOpciones.visibility = View.VISIBLE
//                    }
//                    View.VISIBLE -> {
//                        binding.clVehiculoOpciones.visibility = View.GONE
//                    }
//                    else -> { /*NADA*/ }
//                }
//
//                //Guardar para Recordar si la Lista de Vehiculos estaba Expandida o no
//                vehicleControlPanelFragmentList.viewModel.rememberListExpanded[vehiculo.id!!] = binding.clVehiculoOpciones.visibility
//
//
//            }
//
//            //Boton Activar o Desactivar Vehiculo
//            binding.buttonVehiculoActivo.setOnClickListener {
//                showSetActiveVehicleDialog(vehiculo,vehicleControlPanelFragmentList)
//            }
//
//            //Boton Editar Vehiculo
//            binding.buttonEditarVehiculo.setOnClickListener {
//                findNavController(vehicleControlPanelFragmentList).navigate(R.id.action_vehicleListControlPanelFragment_to_vehicleControlPanelFragmentEdit,
//                    Bundle().apply {
//                    putParcelable(Constants.KEY_VEHICLE_PARCELABLE, vehiculo)
//                })
//            }
//
//            //Boton Verificar Vehiculo
//            binding.buttonVehiculoVerificacion.setOnClickListener {
//                findNavController(vehicleControlPanelFragmentList).navigate(R.id.action_vehicleListControlPanelFragment_to_vehicleControlPanelFragmentEditVerification,
//                    Bundle().apply {
//                        putParcelable(Constants.KEY_VEHICLE_PARCELABLE, vehiculo)
//                    })
//            }
//
//            //Boton Cambiar Imagen Frontal de Vehiculo
//            binding.imageViewSelect2.setOnClickListener {
//                vehicleControlPanelFragmentList.openImageChooser(vehiculo)
//            }
//
//        }
//
//        private fun setActiveVehicle(vehiculo: VehiculoResponse, vehicleControlPanelFragmentList: VehicleControlPanelFragmentList) {
//            //Show Dialog
//            (vehicleControlPanelFragmentList.requireActivity() as BaseActivity).
//            showProgressDialog(vehicleControlPanelFragmentList.getString(R.string.updating_vehicle))
//
//            //Update Vehicle
//            vehicleControlPanelFragmentList.viewModel.activeVehicleToUserId(vehiculo.usuario?.id.orEmpty(),vehiculo.id.orEmpty())
//
//        }
//
//        private fun showSetActiveVehicleDialog(vehiculo: VehiculoResponse, vehicleControlPanelFragmentList: VehicleControlPanelFragmentList) {
//            val title  = "Activar este Vehículo"
//            val icon = R.drawable.ic_vehicle_active_32
//            val text  = "Al activar este vehículo será visible en el mapa para todos los pasajeros mientras que los demás pasarán a inactivos, podrá cambiar su selección en cuaquier momento"
//
//            val builder = AlertDialog.Builder(vehicleControlPanelFragmentList.requireContext())
//            builder
//                .setTitle(title)
//                .setMessage(text)
//                .setIcon(icon)
//                .setPositiveButton("Activar"){ dialogInterface , _ ->
//                    dialogInterface.dismiss()
//                    setActiveVehicle(vehiculo,vehicleControlPanelFragmentList)
//                }
//                .setNegativeButton("Cancelar"){ dialogInterface, _ ->
//                    dialogInterface.cancel()
//                }
//
//            val alertDialog: AlertDialog = builder.create()
//            alertDialog.setCancelable(false)
//            alertDialog.show()
//
//
//        }



    }



}
