package com.cybergod.oyeetaxi.ui.permissions.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cybergod.oyeetaxi.databinding.FragmentPermissionStorageBinding
import com.cybergod.oyeetaxi.ui.main.activity.MainActivity
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_STORAGE_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class StorageFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPermissionStorageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentPermissionStorageBinding.inflate(inflater, container, false)


        binding.continueButton.setOnClickListener {

            if (Permissions.hasStoragePermission(this.requireContext())){

                next()

            } else {
                Permissions.requestStoragePermissions(this)
            }

        }


        return  binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(PERMISSION_STORAGE_REQUEST_CODE,permissions,grantResults,this)
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        val a:  List<String> = perms

        if (EasyPermissions.somePermissionPermanentlyDenied(this.requireActivity(),a)){
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            Permissions.requestLocationPermissions(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        next()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding=null
    }

    private fun next(){
        requireActivity().finish() //cerrar esta actividad
    }

}