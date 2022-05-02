package com.cybergod.oyeetaxi.ui.permissions.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentPermissionLocalizationBinding
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasBackgroundLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasStoragePermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.requestBackgroundLocationPermission
import com.cybergod.oyeetaxi.utils.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class LocalizationBackgroundFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPermissionLocalizationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPermissionLocalizationBinding.inflate(inflater, container, false)


        binding.continueButton.setOnClickListener {

            if (hasBackgroundLocationPermission(this.requireContext()) ){
                next()
            } else {
                requestBackgroundLocationPermission(this)
            }

        }





        return  binding.root

    }

    private fun next() {

//        if (!hasBackgroundLocationPermission(this.requireContext())) {
//            findNavController().navigate(R.id.action_Localization_Fragment_to_Localization_Background_Fragment2)
//            return
//        }
        if (!hasStoragePermission(this.requireContext())) {
            findNavController().navigate(R.id.action_Localization_Background_Fragment_to_Storage_Fragment3)
            return
        }

        requireActivity().finish()


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,permissions,grantResults,this)

    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms )){
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        next()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}