package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.DialogConfigurationSocialBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.adapters.RedesSocialesListAdapter
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.AdministrationViewModel
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.RedesSocialesViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVisibilityIcon
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Intents.launchRedSocialIntent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SocialConfigurationFragment : BottomSheetDialogFragment() {

    private var _binding: DialogConfigurationSocialBinding? = null
    private val binding get() = _binding!!


    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RedesSocialesListAdapter

    val viewModel: AdministrationViewModel by activityViewModels()

    lateinit var viewModelRedesSociales : RedesSocialesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogConfigurationSocialBinding.inflate(inflater, container, false)


        viewModelRedesSociales = ViewModelProvider(this)[RedesSocialesViewModel::class.java]

        initRecyclerView()

        setupOnClickListener()

        setupObservers()

        loadData()

        return  binding.root

    }

    private fun setupObservers() {
        viewModelRedesSociales.redSocialSeleccionada.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                with(binding) {
                    clRedSocialDetail.visibility = View.INVISIBLE

                }


            }




            it?.let { redSocial ->
                with(binding){
                    clRedSocialDetail.visibility = View.VISIBLE


                    tvSocialLink.editText?.setText(redSocial.url)
                    tvSocialLink.hint = redSocial.nombre
                    tvHelperText.text = redSocial.ayuda ?: ""


                    btnVisibilidad.setButtonVisibilityIcon(redSocial.disponible?:true)






                }


            }
        })
    }


    private fun loadData() {


        viewModel.serverConfiguration.value?.socialConfiguracion?.let {

            viewModelRedesSociales.newSocialConfiguracion = it

            val newSocialConfiguracion = viewModelRedesSociales.newSocialConfiguracion

            with(binding) {
                switchRedesDisponibles.isChecked = newSocialConfiguracion.disponible ?: true
                tvPhone.editText?.setText(newSocialConfiguracion.phone?:"")
                tvEmail.editText?.setText(newSocialConfiguracion.email?:"")
                tvWeb.editText?.setText(newSocialConfiguracion.web?:"")
            }



            val listaRedesSociales = newSocialConfiguracion.redesSociales

            if (!listaRedesSociales.isNullOrEmpty()) {
                with(binding) {
                    clRedesSociales.visibility = View.VISIBLE
                }

                recyclerViewAdapter.setRedSocialList(listaRedesSociales)
                recyclerViewAdapter.notifyDataSetChanged()


            } else {
                with(binding) {
                    clRedesSociales.visibility = View.GONE

                }

            }



        }







    }

    private fun initRecyclerView(){
        recyclerViewAdapter = RedesSocialesListAdapter(this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }



    private fun openSocialLink(ok:Boolean){

        if (ok) {
            requireContext().launchRedSocialIntent(
                viewModelRedesSociales.redSocialSeleccionada.value?.nombre,
                binding.tvSocialLink.editText?.text.toString()
            )
        }

    }




    private fun setupOnClickListener() {

       with(binding) {

           continueButton.setOnClickListener {
               if (verifyData()) {
                   setServerSocialConfiguration()
               }
           }

           cancelButton.setOnClickListener {
               closeThisBottomSheetDialogFragment()
           }

           tvSocialLink.setStartIconOnClickListener {
               if (binding.tvSocialLink.editText?.text.toString().isNotEmpty()) {
                   requireContext().showMessageDialogForResult(
                       funResult = {ok -> openSocialLink(ok)},
                       title = "Comprobar este link",
                       message = "Desea probar si el link funciona correctamente para ${viewModelRedesSociales.redSocialSeleccionada.value?.nombre} antes de guardar",
                       icon = R.drawable.ic_alert_24

                   )
               } else {
                   //Toast.makeText(requireContext(),"",Toast.LENGTH_LONG).show()
                    tvSocialLink.error = "El link está vacio"
                    lifecycleScope.launch() {
                        delay(1000)
                        tvSocialLink.isErrorEnabled = false
                    }

               }


           }

           tvSocialLink.setEndIconOnClickListener {



               viewModelRedesSociales.newSocialConfiguracion.redesSociales?.let { listaRedesSociales ->

                   val mutableListaRedesSociales = listaRedesSociales.toMutableList()

                   mutableListaRedesSociales.find { itemRedSocial ->
                       itemRedSocial == viewModelRedesSociales.redSocialSeleccionada.value

                   }?.let {
                       it.url = binding.tvSocialLink.editText?.text.toString().trim()
                   }

                   viewModelRedesSociales.redSocialSeleccionada.postValue(null)


               }



           }

           btnVisibilidad.setOnClickListener {
               viewModelRedesSociales.newSocialConfiguracion.redesSociales?.let { listaRedesSociales ->

                   //val mutableListaRedesSociales = listaRedesSociales.toMutableList()

                   listaRedesSociales.find { itemRedSocial ->
                       itemRedSocial == viewModelRedesSociales.redSocialSeleccionada.value

                   }?.let {
                       it.disponible = !it.disponible!!

                       when (it.disponible){
                           true -> Toast.makeText(requireContext(),"Red social visible",Toast.LENGTH_SHORT).show()
                           false -> Toast.makeText(requireContext(),"Red social invisible",Toast.LENGTH_SHORT).show()
                           else -> {}
                       }


                       btnVisibilidad.setButtonVisibilityIcon(it.disponible!!)

                   }

               }
           }

       }







    }


    private fun setServerSocialConfiguration() {

        with(binding){
            viewModelRedesSociales.newSocialConfiguracion.apply {
                disponible = switchRedesDisponibles.isChecked
                phone = tvPhone.editText?.text.toString().trim()
                email = tvEmail.editText?.text.toString().trim()
                web = tvWeb.editText?.text.toString().trim()

            }
        }

        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.appling_server_configuration))

        viewModel.setServerSocialConfiguration(viewModelRedesSociales.newSocialConfiguracion)
        dismiss()
    }



    private fun verifyData(): Boolean {
        return true


    }




    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}