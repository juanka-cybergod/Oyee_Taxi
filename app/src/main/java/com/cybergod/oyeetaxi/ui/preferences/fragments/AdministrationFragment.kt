package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.Configuracion
import com.cybergod.oyeetaxi.databinding.FragmentAdministrationBinding
import com.cybergod.oyeetaxi.maps.Utils
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.dilogs.fragments.PasswordRecoveryFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showInputTextMessage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.GlobalVariables
import com.cybergod.oyeetaxi.utils.UtilsGlobal.showDropDownMenuFix
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdministrationFragment : BaseFragment() {


    private var _binding: FragmentAdministrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdministrationViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdministrationBinding.inflate(inflater, container, false)


        setupSmSProviderListAdapter()

        setupOnClickListener()

        setupObservers()

        getServerConfiguration()


        return  binding.root
    }



    private fun setupSmSProviderListAdapter(){
        viewModel.arrayAdapter =  ArrayAdapter(
            requireActivity(),
            R.layout.item_map_style,
            viewModel.smsProviderItems
        )

    }



    private fun fillTexViewSmSProviderList() {

        binding.tvSmsProvider.setAdapter(
            viewModel.arrayAdapter
        )
        binding.tfSmsProvider.clearFocus()
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




        binding.tvSmsProvider.setText(serverConfiguration.smsProvider?.name ?: SmsProvider.DESHABILITADO.name,false)

        fillTexViewSmSProviderList()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupOnClickListener() {

        binding.tvSmsProvider.setOnClickListener {
            binding.tvSmsProvider.showDropDownMenuFix(viewModel.arrayAdapter)
        }


        binding.switchServerActive.setOnCheckedChangeListener { button, boolean ->
           if (boolean != viewModel.serverConfiguration.value?.servidorActivoClientes) {
               showMessageDialogSetServerActiveForClients(boolean)
               binding.switchServerActive.isChecked = !binding.switchServerActive.isChecked
           }

        }

        binding.tvSmsProvider.setOnItemClickListener { adapterView, view, position, id ->

            selectedSmsProvider = SmsProvider.valueOf(
                adapterView.getItemAtPosition(position).toString()
            )

            binding.tvSmsProvider.setText(viewModel.serverConfiguration.value?.smsProvider?.name,false)

            if (selectedSmsProvider != viewModel.serverConfiguration.value?.smsProvider ) {

                requireContext().showMessageDialogForResult(
                    funResult =  {ok -> setServerSmsProvider(ok)},
                    title = "Proveedor de Mensajeria",
                    message = "Desea cambiar el proveedor del servicio de mensajeria usado para enviar los códigos de verificación a los clientes para activar sus cuantas",
                    icon = R.drawable.ic_warning_24
                )

            }



        }



    }

    private lateinit var selectedSmsProvider : SmsProvider
    private fun setServerSmsProvider(ok:Boolean) {
        if (ok) {
            showProgressDialog(getString(R.string.updatin_server_configuration))
            viewModel.setServerSmsProvider(selectedSmsProvider)
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
                            funResult =  {ok,motivo, -> changeServerActiveForClientsStatusFalse(ok,motivo)},
                            title = "Motivo Desactivación",
                            hint = "",
                            helperText = "Se notificará a los usuarios al intentar iniciar sesión con este mensaje",
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

    private fun changeServerActiveForClientsStatusFalse(ok:Boolean,motivo:String?=null) {
        if (ok) {
            changeServerActiveForClientsStatus(false, motivo)
        }
    }

    private fun changeServerActiveForClientsStatus(active:Boolean,motivo:String?=null){
        showProgressDialog(getString(R.string.updatin_server_configuration))
        viewModel.setServerActiveForClients(active,motivo)
    }






    private fun launchTwillioConfigurationFragment(){
        val dialogPersonas = PasswordRecoveryFragment()
        dialogPersonas.show(requireActivity().supportFragmentManager,"twillioConfigurationFragment")
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