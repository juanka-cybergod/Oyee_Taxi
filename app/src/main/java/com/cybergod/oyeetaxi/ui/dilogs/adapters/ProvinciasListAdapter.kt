package com.cybergod.oyeetaxi.ui.dilogs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.databinding.ItemProvinciaBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ProvincesFragment


class ProvinciasListAdapter (
//    private val context: Context,
//    private val activity: Activity,
//    private val view : View,
    private val provincesFragment: ProvincesFragment,
) : RecyclerView.Adapter<ProvinciasListAdapter.MyViewHolder>() {


    private var provinciasList: List<Provincia> = emptyList<Provincia>()

    fun setProvinciasList(provinciasListFromData : List<Provincia> ){
        provinciasList = provinciasListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_provincia,parent,false))
    }

    override fun getItemCount(): Int {
        return provinciasList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(provinciasList[position],provincesFragment)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        private val bindig = ItemProvinciaBinding.bind(itemView)

        fun bind(provincia: Provincia, provincesFragment: ProvincesFragment) {
            bindig.tvProvinciaNombre.text = provincia.nombre

            bindig.tvProvinciaNombre.setOnClickListener {
                provincesFragment.provinceSelected.postValue(provincia)

            }


        }

    }

}
