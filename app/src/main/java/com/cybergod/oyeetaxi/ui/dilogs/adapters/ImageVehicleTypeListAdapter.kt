package com.cybergod.oyeetaxi.ui.dilogs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.TipoVehiculo
import com.cybergod.oyeetaxi.databinding.ItemImagenTipoVehiculoBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ViajeFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.itemRecyclerViewSeleccionado


class ImageVehicleTypeListAdapter (
//    private val context: Context,
//    private val activity: Activity,
//    private val view : View,
    private val viajeFragment: ViajeFragment,
) : RecyclerView.Adapter<ImageVehicleTypeListAdapter.MyViewHolder>() {


    private var vehicleTypeList: List<TipoVehiculo> = emptyList<TipoVehiculo>()

    fun setTipoVehiculosList(tipoVehiculoListFromData : List<TipoVehiculo> ){
        vehicleTypeList = tipoVehiculoListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_imagen_tipo_vehiculo,parent,false))
    }

    override fun getItemCount(): Int {
        return vehicleTypeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(vehicleTypeList[position],viajeFragment)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val binding = ItemImagenTipoVehiculoBinding.bind(itemView)

        fun bind(tipoVehiculo:TipoVehiculo, viajeFragment: ViajeFragment) {



                //Seleccionado desde el viewModel
                if (viajeFragment.viewModel.tiposVehiculoSeleccionados.value?.containsKey(tipoVehiculo.tipoVehiculo.toString()) == true) {
                    binding.llTipoVehiculo.itemRecyclerViewSeleccionado(true)
                } else {
                    binding.llTipoVehiculo.itemRecyclerViewSeleccionado(false)
                }

                //Imagen
                binding.imageVehiculo.loadImageVehiculoFrontalFromURL(tipoVehiculo.imagenVehiculoURL)


                //Tipo
                binding.tvTipoVehiculo.text = tipoVehiculo.tipoVehiculo


                //onClick
                binding.llTipoVehiculo.setOnClickListener {

                    if (viajeFragment.viewModel.tiposVehiculoSeleccionados.value?.containsKey(tipoVehiculo.tipoVehiculo.toString()) == false) {
                        viajeFragment.viewModel.tiposVehiculoSeleccionados.value?.put(tipoVehiculo.tipoVehiculo.toString(),tipoVehiculo)
                        binding.llTipoVehiculo.itemRecyclerViewSeleccionado(true)
                    } else {
                        viajeFragment.viewModel.tiposVehiculoSeleccionados.value?.remove(tipoVehiculo.tipoVehiculo.toString())
                        binding.llTipoVehiculo.itemRecyclerViewSeleccionado(false)
                    }

                }







        }

    }

}
