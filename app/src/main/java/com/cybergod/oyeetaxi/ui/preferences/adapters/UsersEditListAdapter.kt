package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.ItemUserEditBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditUserProfileFragment
import com.cybergod.oyeetaxi.ui.preferences.fragments.UsersAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setTipoClienteConductor
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVerificacionEstado
import com.cybergod.oyeetaxi.utils.Constants.KEY_IMAGE_URL
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE


class UsersEditListAdapter (
    private val usersAdministrationFragment: UsersAdministrationFragment,
) : RecyclerView.Adapter<UsersEditListAdapter.MyViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Usuario>(){
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemUserEditBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            usersAdministrationFragment
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    class MyViewHolder(private val binding : ItemUserEditBinding,
                       private val usersAdministrationFragment:UsersAdministrationFragment
                       ): RecyclerView.ViewHolder(binding.root){


        private lateinit var usuarioSelected: Usuario


        @SuppressLint("SetTextI18n")
        fun bind(usuario: Usuario) {

            with(binding) {

                tvNombreUsuario.text = "${usuario.nombre} ${usuario.apellidos}"
                tvCorreo.text = "${usuario.correo}"
                tvTelefonoMovil.text = "${usuario.telefonoMovil}"
                btnDisabledUser.visibility = if (usuario.habilitado==false) {View.VISIBLE} else {View.GONE}
                btnAdminOrSuperAdmin.visibility = if (usuario.administrador==true || usuario.superAdministrador==true  ) {View.VISIBLE} else {View.GONE}
                btnCondutor.setTipoClienteConductor(usuario.conductor)
                btnVerificado.setVerificacionEstado(usuario.usuarioVerificacion)

                imageUsuario.loadImagePerfilFromURLNoCache(usuario.imagenPerfilURL)
                if (!usuario.imagenPerfilURL.isNullOrEmpty()) {
                    imageUsuario.setOnClickListener {
                        launchImageViewFragment(usuario.imagenPerfilURL)
                    }
                }

                btnEdit.setOnClickListener {
                    launchEditUserProfileFragment(usuario)
                }


            }



        }


        private val imageViewFragment = ImageViewFragment()
        private fun launchImageViewFragment(imageURL:String?) {
            if (!imageViewFragment.isVisible) {
                val args = Bundle()
                args.putString(KEY_IMAGE_URL, imageURL)
                imageViewFragment.arguments = args
                imageViewFragment.show(usersAdministrationFragment.requireActivity().supportFragmentManager,"imageViewFragment")
            }
        }

        private val editUserProfileFragment = EditUserProfileFragment()
        private fun launchEditUserProfileFragment(usuario: Usuario) {
            if (!editUserProfileFragment.isVisible) {
                val args = Bundle()
                args.putParcelable(KEY_USER_PARCELABLE, usuario)
                editUserProfileFragment.arguments = args
                editUserProfileFragment.show(usersAdministrationFragment.requireActivity().supportFragmentManager,"editUserProfileFragment")
            }
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
