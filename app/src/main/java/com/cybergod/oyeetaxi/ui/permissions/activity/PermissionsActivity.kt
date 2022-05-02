package com.cybergod.oyeetaxi.ui.permissions.activity


import android.os.Bundle
import androidx.navigation.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.ActivityPermissionsBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasBackgroundLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasLocationPermission
import com.cybergod.oyeetaxi.ui.permissions.utils.Permissions.hasStoragePermission
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class PermissionsActivity : BaseActivity() {


    private lateinit var binding: ActivityPermissionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.navPermissionsFragment)

        if ( !hasLocationPermission(this) ){
            //No hacer nada u quedase en el 1er fragment
            return
        }

        if ( !hasBackgroundLocationPermission(this) ){
            navController.navigate(R.id.action_Localization_Fragment_to_Localization_Background_Fragment2)
            return
        }

        if ( !hasStoragePermission(this) ){
            navController.navigate(R.id.action_Localization_Fragment_to_Storage_Fragment2)
            return
        }

        finish()




    }



    override fun onBackPressed() {

        if (1==2) {
            super.onBackPressed()
        }


    }



}