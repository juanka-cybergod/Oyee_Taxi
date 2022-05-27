package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.share.model.RedSocial
import com.cybergod.oyeetaxi.databinding.ItemRedSocialBinding
import com.cybergod.oyeetaxi.ui.preferences.dilogs.SocialConfigurationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.itemRecyclerViewSeleccionado
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageRedSocialFromURL


class RedesSocialesListAdapter (
    private val socialConfigurationFragment: SocialConfigurationFragment,
) : RecyclerView.Adapter<RedesSocialesListAdapter.MyViewHolder>() {


    private var redesSocialesList: List<RedSocial> = emptyList<RedSocial>()

    fun setRedSocialList(redesSocialesListFromData : List<RedSocial> ){
        redesSocialesList = redesSocialesListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_red_social,parent,false))
    }

    override fun getItemCount(): Int {
        return redesSocialesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(redesSocialesList[position],socialConfigurationFragment)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val binding = ItemRedSocialBinding.bind(itemView)

        fun bind(redSocial: RedSocial, socialConfigurationFragment: SocialConfigurationFragment) {

                binding.icoRedSocial.loadImageRedSocialFromURL(redSocial.ico)

                binding.container.setOnClickListener {
                    socialConfigurationFragment.viewModelRedesSociales.redSocialSeleccionada.postValue(redSocial)
                }

                binding.container.setOnLongClickListener {
                    true
                }

                socialConfigurationFragment.viewModelRedesSociales.redSocialSeleccionada.observe(socialConfigurationFragment.viewLifecycleOwner,
                    Observer {
                        binding.llRedSocial.itemRecyclerViewSeleccionado(redSocial == it)
                    })

        }

    }

}
