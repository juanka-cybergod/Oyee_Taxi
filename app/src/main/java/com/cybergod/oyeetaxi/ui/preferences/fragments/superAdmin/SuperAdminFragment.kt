package com.cybergod.oyeetaxi.ui.preferences.fragments.superAdmin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showInputTextMessage
import com.cybergod.oyeetaxi.databinding.FragmentSuperAdminBinding
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.AdministrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuperAdminFragment : BaseFragment() {


    private var _binding: FragmentSuperAdminBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: SuperAdminViewModel by activityViewModels()
    private val viewModel: AdministrationViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSuperAdminBinding.inflate(inflater, container, false)


        setupOnClickListener()

        setupObservers()

        getServerConfiguration()


        return  binding.root
    }





    private fun setupObservers() {

        viewModel.updatedConfiguracion.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()

            if (it == null) {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
            }

        })

        viewModel.serverConfiguration.observe(viewLifecycleOwner, Observer { serverConfiguration ->

            if (serverConfiguration != null) {
                    paintView(serverConfiguration)
            } else {
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

        binding.switchServerActiveForAdmins.isChecked = serverConfiguration.servidorActivoAdministradores ?: true


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupOnClickListener() {

        with (binding) {

            btnConfigCommunicationRate.setOnClickListener {
                launchIntervalTimerConfiguracionFragment()
            }

            btnConfigurarActualizaciones.setOnClickListener{
                launchAppUpdateAdminFragment()
            }


            switchServerActiveForAdmins.setOnCheckedChangeListener { button, boolean ->
                if (boolean != viewModel.serverConfiguration.value?.servidorActivoAdministradores) {
                    showMessageDialogSetServerActiveForAdmins(boolean)
                    binding.switchServerActiveForAdmins.isChecked = !binding.switchServerActiveForAdmins.isChecked
                }

            }


        }

    }




    private fun showMessageDialogSetServerActiveForAdmins(active: Boolean) {

        val title :String
        val text :String
        val icon:Int
        if (active) {
            title = "Información - Activar Servidor para Administradores"
            icon = R.drawable.ic_alert_24
            text = "Al activar el servidor para los administradores podrán volver a iniciar sesión nuevamente"

        } else {
            title = "Alerta - Desactivar Servidor para Administradores"
            icon = R.drawable.ic_warning_24
            text = "Al desactivar el servidor para los administradores, estos no podrán iniciar sesión nuevamente hasta volver a activar esta opción manualmente"
        }

        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)
            .setMessage(text)
            .setIcon(icon)
            .setPositiveButton("Continuar"){ dialogInterface , _ ->
                dialogInterface.dismiss()

                when (active) {
                    true -> {
                        changeServerActiveForAdminsStatus(true,"")
                    }
                    false -> {
                        requireActivity().showInputTextMessage(
                            funResult =  {ok,motivo, -> changeServerActiveForAdminsStatusFalse(ok,motivo)},
                            title = "Motivo Desactivación",
                            hint = "",
                            helperText = "Se notificará a los administradores al intentar iniciar sesión con este mensaje",
                            message = "",
                            icon = R.drawable.ic_note
                        )
                    }
                }



            }
            .setNegativeButton("Cancelar"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()



    }

    private fun changeServerActiveForAdminsStatusFalse(ok:Boolean, motivo:String?=null) {
        if (ok) {
            changeServerActiveForAdminsStatus(false, motivo)
        }
    }

    private fun changeServerActiveForAdminsStatus(active:Boolean, motivo:String?=null){
        showProgressDialog(getString(R.string.appling_server_configuration))
        viewModel.setServerActiveForAdmins(active,motivo)
    }



    private fun launchAppUpdateAdminFragment(){
        findNavController().navigate(R.id.action_superAdminFragment_to_appUpdateAdminFragment)
    }


    private fun launchIntervalTimerConfiguracionFragment(){
        findNavController().navigate(R.id.action_superAdminFragment_to_intervalTimerConfiguracionFragment)
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