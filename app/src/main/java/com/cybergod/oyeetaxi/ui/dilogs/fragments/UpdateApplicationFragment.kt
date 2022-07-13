package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentUpdateApplicationBinding
import com.cybergod.oyeetaxi.download.FileDownloadManager
import com.cybergod.oyeetaxi.ui.dilogs.adapters.AppUpdateDescriptionListAdapter
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions
import com.cybergod.oyeetaxi.ui.splash.viewmodel.SplashViewModel
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_WRITE_EXTERNAL_STORAGE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionString
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getFullURL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UpdateApplicationFragment : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentUpdateApplicationBinding? = null
    private val binding get() = _binding!!

    val viewModel: SplashViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: AppUpdateDescriptionListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateApplicationBinding.inflate(inflater, container, false)

        setupObservers()


        return  binding.root
    }

    private fun setupObservers() {

        viewModel.appUpdateLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { actualizacion ->

                this.isCancelable = false

                binding.tvVersion.text = "Versión\n${getAppVersionString()} --> ${actualizacion.versionString}"

                //Actualizacion Forzada o No
                if (actualizacion.forceUpdate == true) {
                    binding.tvTipoActualizacion.text = "Actualización\nRequerida"
                    binding.btnUpdateLater.text = " Cancelar y Salir"
                    binding.btnUpdateLater.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_cancel_24,null)
                    binding.btnUpdateLater.setOnClickListener {
                        viewModel.saveRememberAppUpdateAfterDate(false)
                        requireActivity().finish()
                    }
                } else {
                    binding.tvTipoActualizacion.text = "Actualización\nOpcional"
                    binding.btnUpdateLater.text = " Recordarmelo mas tarde"
                    binding.btnUpdateLater.setOnClickListener {
                        dismiss()
                        viewModel.continueNow.postValue(true)
                        viewModel.saveRememberAppUpdateAfterDate(true)
                    }
                }

                //Tamaño Fichero
                if (!actualizacion.fileSize.isNullOrEmpty()) {
                    binding.tvPesoActualizacion.text = "Tamaño\n${actualizacion.fileSize?.uppercase()}"
                } else {binding.tvPesoActualizacion.text = "Tamaño\nDesconocido"}

                //Descripcion
                if (actualizacion.description.isNullOrEmpty()) {
                    binding.scrollViewUpdateDescription.visibility = View.GONE
                } else {
                    //Pintar Descripcion
                    loadDescriptionList()
                }


                if (!actualizacion.appURL.isNullOrEmpty()) {

                    binding.btnUpdateNow.setOnClickListener {
                        checkStoragePermissionAndDownload()

                    }

                }


                if (actualizacion.playStorePackageName.isNullOrEmpty()) {
                    binding.btnUpdateNowPlayStore.visibility = View.GONE
                } else {
                    binding.btnUpdateNowPlayStore.visibility = View.VISIBLE

                    binding.btnUpdateNowPlayStore.setOnClickListener {
                        openPlayStoreApp(actualizacion.playStorePackageName)
                    }
                }


            }

        })



    }



    private fun initRecyclerView(){
        recyclerViewAdapter = AppUpdateDescriptionListAdapter(requireContext(),requireActivity(),binding.root,this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadDescriptionList(){

        initRecyclerView()

        viewModel.appUpdateLiveData.value?.description?.let { descriptionList ->
            if (descriptionList.isNotEmpty()){
                recyclerViewAdapter.setDescriptionList(descriptionList)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }

    }



    private fun startDownload(){

        viewModel.saveRememberAppUpdateAfterDate(false)

        dismiss()

        val apkUrl = getFullURL(viewModel.appUpdateLiveData.value?.appURL!!)
        val downloadApk = FileDownloadManager(requireContext(),requireActivity())

        lifecycleScope.launch {
            downloadApk.startDownloadingApk(apkUrl,"${getString(R.string.app_name)} v${viewModel.appUpdateLiveData.value?.versionString}");
        }


    }


    private fun openPlayStoreApp(packageName:String?){


        fun openPlayStore(packageName:String){
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }

        if (packageName.isNullOrEmpty()) {
            val thisAppPackageName = requireContext().packageName
            //"https://play.google.com/store/apps/details?id=com.ubercab&hl=es_419&gl=US"
            openPlayStore(thisAppPackageName)
        } else {
            openPlayStore(packageName)
        }


    }

    private fun checkStoragePermissionAndDownload() {

        if (Permissions.hasWriteExternalStoragePermission(this.requireContext())){
            startDownload()
        } else {
            Permissions.requestWriteExternalStoragePermissions(this)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                EasyPermissions.onRequestPermissionsResult(PERMISSION_WRITE_EXTERNAL_STORAGE,permissions,grantResults,this)
            }
        }

    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        val a:  List<String> = perms

        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this.requireActivity(),a)){
                    SettingsDialog.Builder(requireActivity()).build().show()
                } else {
                    Permissions.requestWriteExternalStoragePermissions(this)
                }
            }
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                startDownload()
            }

        }

    }


}