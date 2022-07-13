package com.cybergod.oyeetaxi.ui.preferences.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.app_update.model.Actualizacion
import com.cybergod.oyeetaxi.databinding.ItemAppUpdateBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.fragments.superAdmin.AppUpdateAdminFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setActualizacionActiva
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setActualizacionForceUpdate
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setActualizacionFileExist
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants.KEY_APP_UPDATE_PARCELABLE


class AppUpdatesListAdapter (
    private val appUpdateAdminFragment: AppUpdateAdminFragment,
) : RecyclerView.Adapter<AppUpdatesListAdapter.MyViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Actualizacion>(){
        override fun areItemsTheSame(oldItem: Actualizacion, newItem: Actualizacion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actualizacion, newItem: Actualizacion): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAppUpdateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            appUpdateAdminFragment
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    class MyViewHolder(private val binding : ItemAppUpdateBinding,
                       private val appUpdateAdminFragment: AppUpdateAdminFragment
                       ): RecyclerView.ViewHolder(binding.root){


        private lateinit var actualizacionSelected: Actualizacion


        @SuppressLint("SetTextI18n")
        fun bind(actualizacion: Actualizacion) {

            with(binding) {


                tvVersionString.text = "${appUpdateAdminFragment.getString(R.string.app_name)} ${actualizacion.versionString}"
                tvVersionInt.text = "Versión ${actualizacion.version}"
                actualizacion.fileSize?.let { tvFileSize.text = "Tamaño ${it}" }

                btnActualizacionActiva.setActualizacionActiva(actualizacion.active?:false)

                btnDescription.visibility = if (actualizacion.description.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                btnForceUpdate.setActualizacionForceUpdate(actualizacion.forceUpdate)

                val fileExist = !actualizacion.fileSize.isNullOrEmpty()

                btnFileExist.setActualizacionFileExist(fileExist)

                btnPlayStore.visibility = if (actualizacion.playStorePackageName.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }


                btnDescription.setOnClickListener {
                    Toast.makeText(appUpdateAdminFragment.requireContext(),actualizacion.description.toString(),Toast.LENGTH_LONG).show()
                }

                btnForceUpdate.setOnClickListener {
                    val text = if (actualizacion.forceUpdate==true) {"Actualización Requerida"} else {"Actualización Opcional"}
                    Toast.makeText(appUpdateAdminFragment.requireContext(),text,Toast.LENGTH_SHORT).show()
                }

                btnFileExist.setOnClickListener {
                    val text = if (fileExist==true) {"Fichero Correcto"} else {"No se encuentra Fichero en Servidor "}
                    Toast.makeText(appUpdateAdminFragment.requireContext(),text,Toast.LENGTH_SHORT).show()
                }


                btnPlayStore.setOnClickListener {
                    Toast.makeText(appUpdateAdminFragment.requireContext(),"Versión en PlayStore",Toast.LENGTH_LONG).show()
                }

                btnActualizacionActiva.setOnClickListener {
                    val currentActiveState : Boolean = actualizacion.active?:false
                    (appUpdateAdminFragment.requireActivity() as BaseActivity).showProgressDialog(appUpdateAdminFragment.getString(R.string.appling_server_configuration))
                    appUpdateAdminFragment.viewModel.setAppUpdateActiveById(actualizacion.id.orEmpty(),!currentActiveState)
                }


                btnDelete.setOnClickListener {

                    actualizacion.id?.let { idActualizacion ->
                        appUpdateAdminFragment.requireActivity().showMessageDialogForResult(
                            funResult = { ok ->
                                if (ok) {
                                    appUpdateAdminFragment.deleteAppUpdateById(idActualizacion)
                                }
                            },
                            title = "Eliminar esta Actualizacion",
                            message = "Desea eliminar esta actualización de la aplicación en el servidor?",
                            icon = R.drawable.ic_alert_24,
                        )
                    }



                }

                btnEdit.setOnClickListener {
                    launchAddEditAppUpdateFragment(actualizacion)
                }

            }



        }




        private fun launchAddEditAppUpdateFragment(actualizacion: Actualizacion?) {
            NavHostFragment.findNavController(appUpdateAdminFragment).navigate(R.id.action_go_to_addEditAppUpdateFragment, Bundle().apply {
                    putParcelable(KEY_APP_UPDATE_PARCELABLE,actualizacion)
                }
                )
        }




    }



}
