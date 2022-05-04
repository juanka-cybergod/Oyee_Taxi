package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Configuracion
import com.cybergod.oyeetaxi.databinding.FragmentAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showInputTextMessage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
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

        val title :String
        val text :String
        val icon:Int
        if (active) {
            title = "Información - Activar Servidor"
            icon = R.drawable.ic_alert_24
            text = "Al activar el servidor para los clientes todos los usuarios habilitados podrán volver a iniciar sesión nuevamente"

        } else {
            title = "Alerta - Desactivar Servidor"
            icon = R.drawable.ic_warning_24
            text = "Al desactivar el servidor para los clientes, estos no podrán iniciar sesión nuevamente hasta volver a activar esta opción manualmente"
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
                        changeServerActiveForClientsStatus(true,"")
                    }
                    false -> {
                        requireActivity().showInputTextMessage(
                            //{ day, month, year -> binding.tvFechaNacimiento.setOnDateSelected(day, month, year)}
                            funResult =  {ok,motivo, -> changeServerActiveForClientsStatusFalse(ok,motivo)},
                            title = "Motivo Desactivación",
                            hint = "",
                            helperText = "Se notificará a los usuarios al intentar iniciar sesión con este mensaje",
                            message = "",
                            icon = R.drawable.ic_note
                        )
                    }
                }


//                showProgressDialog(getString(R.string.updatin_server_configuration))
//                viewModel.setServerActiveForClients(active)

            }
            .setNegativeButton("Cancelar"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()



    }

    private fun changeServerActiveForClientsStatusFalse(ok:Boolean,motivo:String?=null) {
        if (ok) {
            changeServerActiveForClientsStatus(false, motivo)
        }
    }

    private fun changeServerActiveForClientsStatus(active:Boolean,motivo:String?=null){
        showProgressDialog(getString(R.string.updatin_server_configuration))
        viewModel.setServerActiveForClients(active,motivo)
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