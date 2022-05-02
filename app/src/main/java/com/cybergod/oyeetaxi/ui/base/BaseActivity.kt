package com.cybergod.oyeetaxi.ui.base

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.connectivity.ConnectionManager
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideBottomProgressDialog
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showBottomProgressDialog
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {


    @Inject
    lateinit var connectionManager: ConnectionManager


    override fun onStart() {
        connectionManager.registerConnectionObserver(this)
        super.onStart()
    }

    override fun onDestroy() {
        connectionManager.unregisterConnectionObserver(this)
        super.onDestroy()
    }


    fun showProgressDialog(message: String) {
        showBottomProgressDialog(message)
    }


    fun hideProgressDialog() {
        hideBottomProgressDialog()
    }





    private var doubleBackToExitPressedOnce = false
    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            //super.finish()
            return
        }

        this.doubleBackToExitPressedOnce = true


        showSnackBar(resources.getString(R.string.please_click_back_again_to_exit),false)

        @Suppress("DEPRECATION")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }









}

