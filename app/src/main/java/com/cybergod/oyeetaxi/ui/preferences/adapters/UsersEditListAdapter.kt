package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.databinding.ItemUserEditBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.fragments.UsersAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL


class UsersEditListAdapter (
    private val usersAdministrationFragment: UsersAdministrationFragment,
) : RecyclerView.Adapter<UsersEditListAdapter.MyViewHolder>() {

    private var usuarioList: List<Usuario> = emptyList<Usuario>()


    fun setUsersList(usuarioListFromData : List<Usuario> ){
        usuarioList = usuarioListFromData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_edit,parent,false),usersAdministrationFragment)
    }

    override fun getItemCount(): Int {
        return usuarioList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(usuarioList[position])
    }




    class MyViewHolder(itemView: View,private val usersAdministrationFragment:UsersAdministrationFragment): RecyclerView.ViewHolder(itemView){

        val binding = ItemUserEditBinding.bind(itemView)
        private lateinit var usuarioSelected: Usuario


        @SuppressLint("SetTextI18n")
        fun bind(usuario: Usuario) {

            with(binding) {

                tvNombreUsuario.text = "${usuario.nombre} ${usuario.apellidos}"

                imageUsuario.loadImagePerfilFromURL(usuario.imagenPerfilURL)

                imageUsuario.setOnClickListener {
                    launchImageViewFragment(usuario.imagenPerfilURL)
                }
//
//                tvTipoVehiculoNombreID.text = tipoVehiculo.tipoVehiculo
//                tvCuotaMensualActual.text = "Cuota mensual : ${tipoVehiculo.cuotaMensual} CUP"
//
//                btnRequireVerification.visibility =  if (tipoVehiculo.requiereVerification==true) {View.VISIBLE} else {View.GONE}
//                btnTypeCarga.visibility =  if (tipoVehiculo.transporteCarga==true) {View.VISIBLE} else {View.GONE}
//                btnTypePasajeros.visibility =  if (tipoVehiculo.transportePasajeros==true) {View.VISIBLE} else {View.GONE}
//
//                btnVisibleForClients.setButtonVisibilityIcon(tipoVehiculo.seleccionable?:true)
//                //btnVisibleForClients.visibility =  if (tipoVehiculo.seleccionable==true) {View.VISIBLE} else {View.GONE}
//
//
//                btnInfo.setOnClickListener {
//                    vehiclesTypeAdministrationFragment.requireContext().simpleAlertDialog("Descripción",tipoVehiculo.descripcion?:"")
//                }
//
//
//                btnEdit.setOnClickListener {
//                    launchAddUpdateVehicleTypeFragment(tipoVehiculo)
//                }


            }



        }


        private val imageViewFragment = ImageViewFragment()
        private fun launchImageViewFragment(imageURL:String?) {

            imageURL?.let {

                if (!imageViewFragment.isVisible) {
                    val args = Bundle()
                    args.putString("imageURL", imageURL)
                    imageViewFragment.arguments = args
                    imageViewFragment.show(usersAdministrationFragment.requireActivity().supportFragmentManager,"imageViewFragment")
                }

            }



//            findNavController(usersAdministrationFragment).navigate(R.id.action_vehicleDetailFragment_to_valoracionFragment,Bundle().apply {
//                putParcelable(KEY_VEHICLE_RESPONSE_PARCELABLE,it)
//            })
        }


//        private fun launchAddUpdateVehicleTypeFragment(tipoVehiculo: TipoVehiculo) {
//            findNavController(vehiclesTypeAdministrationFragment).navigate(R.id.action_go_to_addUpdateVehiclesTypesFragment,
//                Bundle().apply {
//                    putParcelable(KEY_VEHICLE_TYPE_PARCELABLE,tipoVehiculo)
//                }
//            )
//        }


    }



}
