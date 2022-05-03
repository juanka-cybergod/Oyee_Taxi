package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Configuracion
import com.cybergod.oyeetaxi.databinding.FragmentAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdministrationFragment : BaseFragment() {


    private var _binding: FragmentAdministrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdministrationViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdministrationBinding.inflate(inflater, container, false)



        setupOnClickListener()

        setupObservers()

        getServerConfiguration()


        return  binding.root
    }




    private fun setupObservers() {

        viewModel.serverConfiguration.observe(viewLifecycleOwner, Observer { serverConfiguration ->

            hideProgressDialog()


            if (serverConfiguration != null) {
                    paintView(serverConfiguration)


            } else {

                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
                binding.animationView.visibility = View.INVISIBLE
                binding.scrollView3.visibility = View.INVISIBLE

            }


        })





    }

    private fun getServerConfiguration(){
        binding.animationView.visibility = View.VISIBLE
        binding.scrollView3.visibility = View.INVISIBLE
        viewModel.getServerConfiguration()
    }

    private fun paintView(serverConfiguration: Configuracion) {

        binding.animationView.visibility = View.INVISIBLE
        binding.scrollView3.visibility = View.VISIBLE

        binding.switchServerActive.isChecked = serverConfiguration.servidorActivoClientes ?: true


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupOnClickListener() {


        binding.switchServerActive.setOnCheckedChangeListener { button, boolean ->
           if (boolean != viewModel.serverConfiguration.value?.servidorActivoClientes) {
               showMessageDialogSetServerActiveForClients(boolean)
               binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked
           }

        }




    }

    private fun showMessageDialogSetServerActiveForClients(active: Boolean) {





        showProgressDialog(getString(R.string.updatin_server_configuration))

        viewModel.setServerActiveForClients(active)

    }


}
















/*

        binding.switchServerActive.setOnClickListener {
            binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked

        }

                binding.switchServerActive.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                if (viewModel.serverConfiguration.value?.servidorActivoClientes != binding.switchServerActive.isChecked) {
                    binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked
                }

            }
            true
        }
 */