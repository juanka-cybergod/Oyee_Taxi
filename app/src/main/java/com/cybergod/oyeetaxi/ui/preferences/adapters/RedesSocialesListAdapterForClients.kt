package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.share.model.RedSocial
import com.cybergod.oyeetaxi.databinding.ItemRedSocialBinding
import com.cybergod.oyeetaxi.ui.preferences.dilogs.SocialSupportFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageRedSocialFromURL
import com.cybergod.oyeetaxi.utils.Intents.launchRedSocialIntent


class RedesSocialesListAdapterForClients (
    private val socialSupportFragment: SocialSupportFragment,
) : RecyclerView.Adapter<RedesSocialesListAdapterForClients.MyViewHolder>() {


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
        holder.bind(redesSocialesList[position],socialSupportFragment)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val binding = ItemRedSocialBinding.bind(itemView)

        fun bind(redSocial: RedSocial, socialSupportFragment: SocialSupportFragment) {


            if (redSocial.disponible == true && redSocial.url?.isNotEmpty() == true) {

                binding.icoRedSocial.loadImageRedSocialFromURL(redSocial.ico)

                redSocial.url?.let {  link ->

                    binding.container.setOnClickListener {
                        socialSupportFragment.requireContext().launchRedSocialIntent(
                            redSocial.nombre,
                            link
                        )
                    }

                }




            }




        }

    }

}
