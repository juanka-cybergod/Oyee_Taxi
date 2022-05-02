package com.cybergod.oyeetaxi.ui.dilogs.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.ItemDescripcionActualizacionBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.UpdateApplicationFragment


class AppUpdateDescriptionListAdapter (
    private val context: Context,
    private val activity: Activity,
    private val view : View,
    private val updateApplicationFragment: UpdateApplicationFragment,
) : RecyclerView.Adapter<AppUpdateDescriptionListAdapter.MyViewHolder>() {


    private var descriptionList: List<String> = emptyList<String>()

    fun setDescriptionList(descriptionListFromData : List<String> ){
        descriptionList = descriptionListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_descripcion_actualizacion,parent,false))
    }

    override fun getItemCount(): Int {
        return descriptionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(descriptionList[position])
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        //Nuevo Metodo Binding para Cargar las Vistas dentro de los Adapters
        val bindig = ItemDescripcionActualizacionBinding.bind(itemView)


        fun bind(description:String) {
                bindig.tvDescripcion.text = description
        }

    }

}
