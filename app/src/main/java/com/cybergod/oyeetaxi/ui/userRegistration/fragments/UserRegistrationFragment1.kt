package com.cybergod.oyeetaxi.ui.userRegistration.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment1Binding
import com.cybergod.oyeetaxi.ui.login.activity.LoginActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.SmsProvider
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSimpleAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRegistrationFragment1 : BaseFragment() {

    private var _binding: UserRegistrationFragment1Binding? = null
    private val binding get() = _binding!!

    val viewModel: UserRegistrationViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment1Binding.inflate(inflater, container, false)


        viewModel.userRegistered.observe(viewLifecycleOwner, Observer { userAlreadyRegitered ->

            if (userAlreadyRegitered == true) {
                binding.cancelButton.visibility = View.GONE
            } else {
                binding.cancelButton.visibility = View.VISIBLE
            }
        })



        binding.cancelButton.setOnClickListener {
            viewModel.userRegisterSuccess(true)
            launchLoginActivity()
        }


        binding.continueButton.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) {


                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.preparing_new_register))

                delay(500)

                val registerConfiguration = viewModel.getRegisterConfiguration()

                if (registerConfiguration != null) {

                    if (registerConfiguration.habilitadoRegistroConductores == false && registerConfiguration.habilitadoRegistroPasajeros == false ) {


                        requireContext().showSimpleAlertDialog(
                            title = "Información",
                            text = "Los registros para nuevos usuarios están suspendidos temporalmente, por favor intentelo mas tarde"
                        )

                    } else {

                        when(registerConfiguration.smsProvider) {
                            SmsProvider.FIREBASE-> {
                                findNavController().navigate(R.id.action_userRegistrationFragment1_to_userRegistrationFragment2byFirebase)
                            }
                            SmsProvider.TWILIO-> {
                                findNavController().navigate(R.id.action_userRegistrationFragment1_to_userRegistrationFragment2byTwilio)
                            }
                            SmsProvider.DESHABILITADO-> {
                                showSnackBar(getString(
                                    R.string.number_validation_is_disable),
                                    true,
                                )
                            }
                            else -> {

                            }
                        }.also {
                            (requireActivity() as BaseActivity).hideProgressDialog()
                        }


                    }



                } else {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true,
                    )

                }


                (requireActivity() as BaseActivity).hideProgressDialog()

            }


        }



        return  binding.root
    }



    private fun launchLoginActivity(){

        startActivity(
            Intent(requireActivity(),
                LoginActivity::class.java)
        )
        requireActivity().finish() //cerrar esta actividad
    }

}