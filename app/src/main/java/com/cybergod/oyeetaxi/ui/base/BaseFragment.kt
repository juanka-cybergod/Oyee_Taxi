package com.cybergod.oyeetaxi.ui.base


import android.content.res.Configuration
import androidx.fragment.app.Fragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideBottomProgressDialog
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showBottomProgressDialog
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSimpleAlertDialog


open class BaseFragment : Fragment() {


    fun deviceInDarKMode():Boolean{
        val nightModeFlags = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> return true
            Configuration.UI_MODE_NIGHT_NO -> return false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
        }
        return false
    }


    fun showSnackBar(message: String, errorMessage: Boolean) {
        (requireActivity() as BaseActivity).showSnackBar(
            message,
            errorMessage
        )
    }


    fun showProgressDialog(message: String) {
        (requireActivity() as BaseActivity).showBottomProgressDialog(message)
    }

    fun hideProgressDialog() {
        hideBottomProgressDialog()
    }



    fun showMessage(title: String, text: String) {
       requireActivity().showSimpleAlertDialog(title, text)
    }




}