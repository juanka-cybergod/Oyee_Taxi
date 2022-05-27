package com.cybergod.oyeetaxi.ui.dilogs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.ItemTipoVehiculoBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.VehicleTypeFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL


class VehicleTypeListAdapter (
//    private val context: Context,
//    private val activity: Activity,
//    private val view : View,
    private val vehicleTypeFragment: VehicleTypeFragment,
) : RecyclerView.Adapter<VehicleTypeListAdapter.MyViewHolder>() {


    private var vehicleTypeList: List<TipoVehiculo> = emptyList<TipoVehiculo>()

    fun setTipoVehiculosList(tipoVehiculoListFromData : List<TipoVehiculo> ){
        vehicleTypeList = tipoVehiculoListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tipo_vehiculo,parent,false))
    }

    override fun getItemCount(): Int {
        return vehicleTypeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(vehicleTypeList[position],vehicleTypeFragment)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        private val bindig = ItemTipoVehiculoBinding.bind(itemView)


        fun bind(tipoVehiculo: TipoVehiculo, vehicleTypeFragment: VehicleTypeFragment) {

            if (tipoVehiculo.seleccionable!!) {

                bindig.tvTipoVehiculo.text = tipoVehiculo.tipoVehiculo
                bindig.tvDescripcionTipoVehiculo.text = tipoVehiculo.descripcion


                bindig.imageTipoVehiculo.loadImageVehiculoFrontalFromURL(tipoVehiculo.imagenVehiculoURL)

                bindig.llTipoVehiculo.setOnClickListener {
                    vehicleTypeFragment.vehicleTypeSelected.postValue(tipoVehiculo)
                }
            }



        }

    }

}
