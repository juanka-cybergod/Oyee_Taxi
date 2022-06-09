package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.ItemVehicleEditBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditUserProfileFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditUserVerificationFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.EditVehicleFragmentEdit
import com.cybergod.oyeetaxi.ui.preferences.dilogs.UserOverviewFragment
import com.cybergod.oyeetaxi.ui.preferences.fragments.VehiclesAdministrationFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesVehiculos
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoMatricula
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setEstadoVerificacionVehiculo
import com.cybergod.oyeetaxi.utils.Constants.KEY_IMAGE_URL
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_PARCELABLE
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE_PARCELABLE
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
                btnVisible.setButtonVisibilityIcon(vehiculoResponse.visible?:true)
                btnVerificado.setEstadoVerificacionVehiculo(vehiculoResponse.vehiculoVerificacion)


                imageVehiculo.loadImageVehiculoFrontalFromURLNoCache(vehiculoResponse.imagenFrontalPublicaURL)
                if (!vehiculoResponse.imagenFrontalPublicaURL.isNullOrEmpty()) {
                    imageVehiculo.setOnClickListener {
                        launchImageViewFragment(vehiculoResponse.imagenFrontalPublicaURL)
                    }
                }


                btnDisabled.setOnClickListener {
                    Toast.makeText(vehiclesAdministrationFragment.requireContext(),"Vehiculo Deshabilitado",Toast.LENGTH_SHORT).show()
                }

                btnEdit.setOnClickListener {
                    launchEditVehicleFragmentEdit(vehiculoResponse)
                }
//                btnVerificado.setOnClickListener {
//                    launchEditUserVerificationFragment(vehiculoResponse)
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

        private fun launchEditVehicleFragmentEdit(vehiculoResponse: VehiculoResponse) {
            val editVehicleFragmentEdit = EditVehicleFragmentEdit()
            val args = Bundle()
            args.putParcelable(KEY_VEHICLE_RESPONSE_PARCELABLE, vehiculoResponse)
            editVehicleFragmentEdit.arguments = args
            editVehicleFragmentEdit.show(vehiclesAdministrationFragment.requireActivity().supportFragmentManager,"editVehicleFragmentEdit+${getRamdomUUID()}")

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
