package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.databinding.ItemProvinciaEditBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.fragments.administration.ProvincesAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.utils.Constants.KEY_PROVINCE_PARCELABLE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProvincesEditListAdapter (
    private val provincesAdministrationFragment: ProvincesAdministrationFragment,
) : RecyclerView.Adapter<ProvincesEditListAdapter.MyViewHolder>() {

    private var provincesList: List<Provincia> = emptyList<Provincia>()


    fun setProvincesList(provincesListFromData : List<Provincia> ){
        provincesList = provincesListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_provincia_edit,parent,false),provincesAdministrationFragment)
    }

    override fun getItemCount(): Int {
        return provincesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(provincesList[position])
    }




    class MyViewHolder(itemView: View,private val provincesAdministrationFragment: ProvincesAdministrationFragment): RecyclerView.ViewHolder(itemView){

        val binding = ItemProvinciaEditBinding.bind(itemView)
        private lateinit var provinceSelected: Provincia


        @SuppressLint("SetTextI18n")
        fun bind(provincia: Provincia) {

            with(binding) {

                tvProvinciaNombreID.text = provincia.nombre
                tvDetalles.text = "Latidud: ${provincia.ubicacion?.latitud}\n" +
                                  "Longitud: ${provincia.ubicacion?.longitud}\n" +
                                  "Altura: ${provincia.ubicacion?.alturaMapa}"

                btnVisible.setButtonVisibilityIcon(provincia.visible?:true)

                btnVisible.setOnClickListener {

                    provinceSelected = provincia

                    val title:String
                    val message:String
                    val ico:Int
                    if (provincia.visible == true) {
                        title="Ocultar Provincia"
                        message="Desea ocultar Provincia ${provincia.nombre} para que no sea seleccionable por los clientes por el momento"
                        ico=R.drawable.ic_visibility_off_24
                    } else {
                        title="Mostrar Provincia"
                        message="Desea volver a mostrar Provincia ${provincia.nombre} para que sea visible y seleccionable por los clientes"
                        ico=R.drawable.ic_visibility_on_24
                    }
                    provincesAdministrationFragment.requireContext().showMessageDialogForResult(
                        funResult = {ok -> changeProvinceVisibilityContinue(ok)},
                        title = title,
                        message = message,
                        icon = ico
                    )


                }


                btnEditarUbicacion.setOnClickListener {
                    launchAddUpdateProvinceFragment(provincia)
                }

            }



        }


        private fun launchAddUpdateProvinceFragment(provincia: Provincia) {
            findNavController(provincesAdministrationFragment).navigate(R.id.action_go_to_addUpdateProvinceFragment,Bundle().apply {
                    putParcelable(KEY_PROVINCE_PARCELABLE,provincia)
                }
            )
        }


        private fun changeProvinceVisibilityContinue(ok:Boolean){
            if (ok) {
                changeProvinceVisibility()
            }
        }

        private fun changeProvinceVisibility() {

            provincesAdministrationFragment.lifecycleScope.launch {

                (provincesAdministrationFragment.requireActivity() as BaseActivity).showProgressDialog("Actualizando provincia ...")

                delay(1000L)

                val provinciaChanged = provincesAdministrationFragment.viewModel.setProvinceVisibility(
                    nombreProvincia = provinceSelected.nombre?:"",
                    visible = !provinceSelected.visible!!
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


    }



}
