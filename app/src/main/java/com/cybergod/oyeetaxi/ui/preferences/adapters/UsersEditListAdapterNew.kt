package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.databinding.ItemUserEditBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.fragments.UsersAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setTipoClienteConductor
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVerificacionEstado
import com.cybergod.oyeetaxi.utils.UtilsGlobal


class UsersEditListAdapterNew (
    private val usersAdministrationFragment: UsersAdministrationFragment,
) : RecyclerView.Adapter<UsersEditListAdapterNew.MyViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Usuario>(){
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    val differ = AsyncListDiffer(this,differCallback)


//    private var currentUserList:List<Usuario> = emptyList<Usuario>()
//
//    fun setData(newUserList:List<Usuario>) {
//        val diffUtil = myDiffUtil(currentUserList,newUserList)
//        val diffResult = DiffUtil.calculateDiff(diffUtil)
//        diffResult.dispatchUpdatesTo(this)
//        currentUserList = newUserList
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_edit,parent,false),usersAdministrationFragment)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
//        return  currentUserList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.bind(currentUserList[position])
        holder.bind(differ.currentList[position])
    }




    class MyViewHolder(itemView: View,private val usersAdministrationFragment:UsersAdministrationFragment): RecyclerView.ViewHolder(itemView){

        private val binding = ItemUserEditBinding.bind(itemView)
        private lateinit var usuarioSelected:Usuario


        @SuppressLint("SetTextI18n")
        fun bind(usuario:Usuario) {

            with(binding) {

                tvNombreUsuario.text = "${usuario.nombre} ${usuario.apellidos}"
                tvCorreo.text = "${usuario.correo}"
                tvTelefonoMovil.text = "${usuario.telefonoMovil}"
                //tvTipoUsuario.text = if (usuario.conductor==true) {"Conductor"} else {"Pasajero"}
                btnCondutor.setTipoClienteConductor(usuario.conductor)
                btnDisabledUser.visibility = if (usuario.habilitado==false) {View.VISIBLE} else {View.GONE}
                btnAdminOrSuperAdmin.visibility = if (usuario.administrador==true || usuario.superAdministrador==true  ) {View.VISIBLE} else {View.GONE}
                btnVerificado.setVerificacionEstado(usuario.usuarioVerificacion)



                imageUsuario.loadImagePerfilFromURLNoCache(usuario.imagenPerfilURL)

                if (!usuario.imagenPerfilURL.isNullOrEmpty()) {
                    imageUsuario.setOnClickListener {
                        launchImageViewFragment(usuario.imagenPerfilURL)
                    }
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

            if (!imageViewFragment.isVisible) {
                val args = Bundle()
                args.putString("imageURL", imageURL)
                imageViewFragment.arguments = args
                imageViewFragment.show(usersAdministrationFragment.requireActivity().supportFragmentManager,"imageViewFragment")
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
