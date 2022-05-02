package com.cybergod.oyeetaxi.ui.userRegistration.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment1Binding
import com.cybergod.oyeetaxi.ui.login.activity.LoginActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRegistrationFragment1 : BaseFragment() {



    private var _binding: UserRegistrationFragment1Binding? = null
    private val binding get() = _binding!!

    //TODO Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserRegistrationViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment1Binding.inflate(inflater, container, false)


        viewModel.userRegistered.observe(viewLifecycleOwner, Observer { userAlreadyRegitered ->

            //Toast.makeText(requireContext(),"$userAlreadyRegitered",Toast.LENGTH_LONG).show()

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

            when(viewModel.smsProvider.value) {
                SmsProvider.FIREBASE-> {
                    findNavController().navigate(R.id.action_userRegistrationFragment1_to_userRegistrationFragment2byFirebase)
                }
                SmsProvider.TWILIO-> {
                    findNavController().navigate(R.id.action_userRegistrationFragment1_to_userRegistrationFragment2byTwilio)
                }
                SmsProvider.DISABLE-> {
                    showSnackBar(getString(
                        R.string.number_validation_is_disable),
                        true,
                    )
                }
                else -> {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true,
                    )
                }
            }





        }



        return  binding.root
    }



    fun launchLoginActivity(){

        startActivity(
            Intent(requireActivity(),
                LoginActivity::class.java)
        )
        requireActivity().finish() //cerrar esta actividad
    }

}