package com.cybergod.oyeetaxi.ui.preferences.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.ActivityPreferencesBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreferencesActivity : BaseActivity() {


    private lateinit var binding: ActivityPreferencesBinding
    private lateinit var navController : NavController
    val viewModel: PreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPreferencesBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.preferences_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        navController = findNavController(R.id.navPreferencesFragment)

        return when (item.itemId) {
            R.id.action_admin -> {

                if (!isCurrentFragment(R.id.administrationFragment)) {
                    navController.navigate(R.id.action_go_to_administrationFragment)
                }

                true
            }
            R.id.action_superAdmin -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }




    private fun isCurrentFragment(destinationFragment:Int):Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.navPreferencesFragment)
        return NavHostFragment.findNavController(fragment!!).currentDestination?.id == destinationFragment
    }




}