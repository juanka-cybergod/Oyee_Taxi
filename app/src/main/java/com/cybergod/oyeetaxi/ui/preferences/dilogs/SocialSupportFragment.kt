package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.databinding.DialogSocialSoporteBinding
import com.cybergod.oyeetaxi.ui.preferences.adapters.RedesSocialesListAdapterForClients
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.PreferencesViewModel
import com.cybergod.oyeetaxi.utils.Intents.launchIntentCallPhone
import com.cybergod.oyeetaxi.utils.Intents.launchIntentOpenSendEmailTo
import com.cybergod.oyeetaxi.utils.Intents.launchIntentOpenWebURL
import com.cybergod.oyeetaxi.utils.Intents.launchIntentSendSMS
import com.cybergod.oyeetaxi.utils.Intents.launchIntentToAddContact
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class SocialSupportFragment : BottomSheetDialogFragment() {

    private var _binding: DialogSocialSoporteBinding? = null
    private val binding get() = _binding!!


    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RedesSocialesListAdapterForClients

    val viewModel: PreferencesViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogSocialSoporteBinding.inflate(inflater, container, false)


        initRecyclerView()

        loadData()


        setupOnClickListener()


        return  binding.root

    }



    private fun loadData() {



        viewModel.serverConfiguration.value?.socialConfiguracion?.let {

            val socialConfiguration = it
            val listRedesSocialesDisponibles = socialConfiguration.redesSociales?.toMutableList()?.filter { itemRedSocial ->
                itemRedSocial.disponible == true and
                        !itemRedSocial.url.isNullOrEmpty()
            }


            with(binding) {
                btnPhone.text = socialConfiguration.phone
                btnEmail.text = socialConfiguration.email
                btnWeb.text = socialConfiguration.web

                if (!listRedesSocialesDisponibles.isNullOrEmpty()) {

                    recyclerViewAdapter.setRedSocialList(listRedesSocialesDisponibles)
                    recyclerViewAdapter.notifyDataSetChanged()

                } else {
                    with(binding) {
                        clRedesSociales.visibility = View.GONE

                    }

                }



            }

        }

    }


    private fun initRecyclerView(){
        recyclerViewAdapter = RedesSocialesListAdapterForClients(this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun setupOnClickListener() {

       with(binding) {

           if (!viewModel.serverConfiguration.value?.socialConfiguracion?.phone.isNullOrEmpty()) {
               btnCall.setOnClickListener {
                   requireContext().launchIntentCallPhone(btnPhone.text.toString())
               }
               btnSms.setOnClickListener {
                   requireContext().launchIntentSendSMS(btnPhone.text.toString())
               }
               btnContact.setOnClickListener {
                   requireContext().launchIntentToAddContact(viewModel.serverConfiguration.value?.socialConfiguracion!!)
               }
           } else clPhone.visibility = View.GONE

           if (!viewModel.serverConfiguration.value?.socialConfiguracion?.email.isNullOrEmpty()) {
               btnEmail.setOnClickListener {
                   requireContext().launchIntentOpenSendEmailTo(btnEmail.text.toString())
               }
           } else clEmail.visibility = View.GONE

           if (!viewModel.serverConfiguration.value?.socialConfiguracion?.web.isNullOrEmpty()) {
               btnWeb.setOnClickListener {
                   requireContext().launchIntentOpenWebURL(btnWeb.text.toString())
               }
           } else clWeb.visibility = View.GONE


       }







    }








}