package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.ItemVehicleTypeEditBinding
import com.cybergod.oyeetaxi.ui.preferences.fragments.VehiclesTypeAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.simpleAlertDialog
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_TYPE_PARCELABLE


class VehiclesTypesEditListAdapter (
    private val vehiclesTypeAdministrationFragment: VehiclesTypeAdministrationFragment,
) : RecyclerView.Adapter<VehiclesTypesEditListAdapter.MyViewHolder>() {

    private var vehiclesTypesList: List<TipoVehiculo> = emptyList<TipoVehiculo>()


    fun setVehiclesTypesList(vehiclesTypesListFromData : List<TipoVehiculo> ){
        vehiclesTypesList = vehiclesTypesListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_type_edit,parent,false),vehiclesTypeAdministrationFragment)
    }

    override fun getItemCount(): Int {
        return vehiclesTypesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(vehiclesTypesList[position])
    }




    class MyViewHolder(itemView: View,private val vehiclesTypeAdministrationFragment:VehiclesTypeAdministrationFragment): RecyclerView.ViewHolder(itemView){

        val binding = ItemVehicleTypeEditBinding.bind(itemView)
        private lateinit var vehicleTypeSelected: TipoVehiculo


        @SuppressLint("SetTextI18n")
        fun bind(tipoVehiculo: TipoVehiculo) {

            with(binding) {

                imageTipoVehiculo.loadImageVehiculoFrontalFromURL(tipoVehiculo.imagenVehiculoURL)

                tvTipoVehiculoNombreID.text = tipoVehiculo.tipoVehiculo
                tvCuotaMensualActual.text = "Cuota mensual : ${tipoVehiculo.cuotaMensual} CUP"

                btnRequireVerification.visibility =  if (tipoVehiculo.requiereVerification==true) {View.VISIBLE} else {View.GONE}
                btnTypeCarga.visibility =  if (tipoVehiculo.transporteCarga==true) {View.VISIBLE} else {View.GONE}
                btnTypePasajeros.visibility =  if (tipoVehiculo.transportePasajeros==true) {View.VISIBLE} else {View.GONE}

                btnVisibleForClients.setButtonVisibilityIcon(tipoVehiculo.seleccionable?:true)
                //btnVisibleForClients.visibility =  if (tipoVehiculo.seleccionable==true) {View.VISIBLE} else {View.GONE}


                btnInfo.setOnClickListener {
                    vehiclesTypeAdministrationFragment.requireContext().simpleAlertDialog("Descripción",tipoVehiculo.descripcion?:"")
                }


                btnEdit.setOnClickListener {
                    launchAddUpdateVehicleTypeFragment(tipoVehiculo)
                }


            }



        }


        private fun launchAddUpdateVehicleTypeFragment(tipoVehiculo: TipoVehiculo) {
            findNavController(vehiclesTypeAdministrationFragment).navigate(R.id.action_go_to_addUpdateVehiclesTypesFragment,
                Bundle().apply {
                    putParcelable(KEY_VEHICLE_TYPE_PARCELABLE,tipoVehiculo)
                }
            )
        }


    }



}
