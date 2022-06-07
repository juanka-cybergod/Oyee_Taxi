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
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.ItemVehicleEditBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditUserProfileFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditUserVerificationFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.UserOverviewFragment
import com.cybergod.oyeetaxi.ui.preferences.fragments.VehiclesAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesVehiculos
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoMatricula
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setEstadoVerificacionUsuario
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setEstadoVerificacionVehiculo
import com.cybergod.oyeetaxi.utils.Constants.KEY_IMAGE_URL
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getRamdomUUID


class VehiclesEditListAdapter (
    private val vehiclesAdministrationFragment: VehiclesAdministrationFragment,
) : RecyclerView.Adapter<VehiclesEditListAdapter.MyViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<VehiculoResponse>(){
        override fun areItemsTheSame(oldItem: VehiculoResponse, newItem: VehiculoResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VehiculoResponse, newItem: VehiculoResponse): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVehicleEditBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            vehiclesAdministrationFragment
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    class MyViewHolder(private val binding : ItemVehicleEditBinding,
                       private val vehiclesAdministrationFragment:VehiclesAdministrationFragment
                       ): RecyclerView.ViewHolder(binding.root){


        @SuppressLint("SetTextI18n")
        fun bind(vehiculoResponse: VehiculoResponse) {

            with(binding) {

                tvMatricula.setVehiculoMatricula(vehiculoResponse.vehiculoVerificacion?.matricula)
                tvDetalles.setDetallesVehiculos(vehiculoResponse)

                btnDisabled.visibility = if (vehiculoResponse.habilitado==false) {View.VISIBLE} else {View.GONE}
//                btnAdminOrSuperAdmin.visibility = if (vehiculo.administrador==true || vehiculo.superAdministrador==true  ) {View.VISIBLE} else {View.GONE}
//                btnCondutor.setTipoClienteConductor(vehiculo.conductor)
                btnVisible.setButtonVisibilityIcon(vehiculoResponse.visible?:true)
                btnVerificado.setEstadoVerificacionVehiculo(vehiculoResponse.vehiculoVerificacion)


                imageVehiculo.loadImageVehiculoFrontalFromURLNoCache(vehiculoResponse.imagenFrontalPublicaURL)
                if (!vehiculoResponse.imagenFrontalPublicaURL.isNullOrEmpty()) {
                    imageVehiculo.setOnClickListener {
                        launchImageViewFragment(vehiculoResponse.imagenFrontalPublicaURL)
                    }
                }



//                btnEdit.setOnClickListener {
//                    launchEditUserProfileFragment(vehiculoResponse)
//                }
//                btnVerificado.setOnClickListener {
//                    launchEditUserVerificationFragment(vehiculoResponse)
//                }
//                btnCondutor.setOnClickListener {
//                    val text = if (vehiculoResponse.conductor==true) {"Conductor"} else {"Pasajero"}
//                    Toast.makeText(vehiclesAdministrationFragment.requireContext(),text,Toast.LENGTH_SHORT).show()
//                }
//                btnAdminOrSuperAdmin.setOnClickListener {
//                    Toast.makeText(vehiclesAdministrationFragment.requireContext(),"Administrador",Toast.LENGTH_SHORT).show()
//                }
//                btnDisabledUser.setOnClickListener {
//                    Toast.makeText(vehiclesAdministrationFragment.requireContext(),"Usuario Deshabilitado",Toast.LENGTH_SHORT).show()
//                }
//                binding.clUsuario.setOnClickListener {
//                    launchUserOverviewFragment(vehiculoResponse)
//                }


            }



        }



        private fun launchImageViewFragment(imageURL:String?) {
            val imageViewFragment = ImageViewFragment()
            val args = Bundle()
            args.putString(KEY_IMAGE_URL, imageURL)
            imageViewFragment.arguments = args
            imageViewFragment.show(vehiclesAdministrationFragment.requireActivity().supportFragmentManager,"imageViewFragment+${getRamdomUUID()}")
        }

        private fun launchEditUserProfileFragment(usuario: Usuario) {
            val editUserProfileFragment = EditUserProfileFragment()
            val args = Bundle()
            args.putParcelable(KEY_USER_PARCELABLE, usuario)
            editUserProfileFragment.arguments = args
            editUserProfileFragment.show(vehiclesAdministrationFragment.requireActivity().supportFragmentManager,"editUserProfileFragment+${getRamdomUUID()}")

        }

        private fun launchEditUserVerificationFragment(usuario: Usuario) {
            val editUserVerificationFragment = EditUserVerificationFragment()
            val args = Bundle()
            args.putParcelable(KEY_USER_PARCELABLE, usuario)
            editUserVerificationFragment.arguments = args
            editUserVerificationFragment.show(vehiclesAdministrationFragment.requireActivity().supportFragmentManager,"editUserVerificationFragment+${getRamdomUUID()}")

        }


        private fun launchUserOverviewFragment(usuario: Usuario) {
            val userOverviewFragment = UserOverviewFragment()
            val args = Bundle()
            args.putParcelable(KEY_USER_PARCELABLE, usuario)
            userOverviewFragment.arguments = args
            userOverviewFragment.show(vehiclesAdministrationFragment.requireActivity().supportFragmentManager,"userOverviewFragment+${getRamdomUUID()}")

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
