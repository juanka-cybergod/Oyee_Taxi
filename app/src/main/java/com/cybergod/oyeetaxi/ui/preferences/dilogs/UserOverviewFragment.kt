package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.api.futures.share.model.RedSocial
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.model.SocialConfiguracion
import com.cybergod.oyeetaxi.databinding.DialogSocialSoporteBinding
import com.cybergod.oyeetaxi.ui.preferences.adapters.RedesSocialesListAdapterForClients
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE
import com.cybergod.oyeetaxi.utils.Intents.launchIntentCallPhone
import com.cybergod.oyeetaxi.utils.Intents.launchIntentOpenSendEmailTo
import com.cybergod.oyeetaxi.utils.Intents.launchIntentOpenWebURL
import com.cybergod.oyeetaxi.utils.Intents.launchIntentSendSMS
import com.cybergod.oyeetaxi.utils.Intents.launchIntentToAddContact
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class UserOverviewFragment : BottomSheetDialogFragment() {

    private var _binding: DialogSocialSoporteBinding? = null
    private val binding get() = _binding!!


    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RedesSocialesListAdapterForClients

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogSocialSoporteBinding.inflate(inflater, container, false)

        return  binding.root

    }

    private lateinit var user:Usuario

    override fun onResume() {
        super.onResume()

        requireArguments().getParcelable<Usuario>(KEY_USER_PARCELABLE)?.let { usuario ->

            user = usuario

            val listaRedSocial = ArrayList<RedSocial>()
            listaRedSocial.add(
                RedSocial(
                    disponible = true,
                    nombre = "WhatsApp",
                    ico = "ficheros/descarga/SOCIAL_WHATSAPP.PNG",
                    url = usuario.telefonoMovil,
                    ayuda = null,
                )
            )

            val socialConfiguracion = SocialConfiguracion(
                disponible  = true,
                phone = usuario.telefonoMovil,
                email = usuario.correo,
                web = null,
                redesSociales = listaRedSocial,
            )

            initRecyclerView()
            loadData(socialConfiguracion)
            setupOnClickListener(socialConfiguracion)
        }


    }



    private fun loadData(socialConfiguracion: SocialConfiguracion) {



        socialConfiguracion.let {

            val socialConfiguration = it
            val listRedesSocialesDisponibles = socialConfiguration.redesSociales?.toMutableList()?.filter { itemRedSocial ->
                itemRedSocial.disponible == true and
                        !itemRedSocial.url.isNullOrEmpty()
            }


            with(binding) {
                btnPhone.text = user.nombre?: socialConfiguration.phone
                btnEmail.text = socialConfiguration.email


                if (!socialConfiguration.web.isNullOrEmpty()) {
                    btnWeb.text = socialConfiguration.web
                } else {
                    clWeb.visibility = View.GONE
                }

                if (!listRedesSocialesDisponibles.isNullOrEmpty()) {

                    recyclerViewAdapter.setRedSocialList(listRedesSocialesDisponibles)
                    recyclerViewAdapter.notifyDataSetChanged()

                } else {
                    clRedesSociales.visibility = View.GONE
                }

            }

        }

    }


    private fun initRecyclerView(){
        recyclerViewAdapter = RedesSocialesListAdapterForClients(requireContext())
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun setupOnClickListener(socialConfiguracion: SocialConfiguracion) {

       with(binding) {

           if (!socialConfiguracion.phone.isNullOrEmpty()) {
               btnCall.setOnClickListener {
                   requireContext().launchIntentCallPhone(btnPhone.text.toString())
               }
               btnSms.setOnClickListener {
                   requireContext().launchIntentSendSMS(btnPhone.text.toString())
               }
               btnContact.setOnClickListener {
                   requireContext().launchIntentToAddContact(nombre = "${user.nombre?.replace(" ","_",true)} ${user.apellidos?.replace(" ","_",true)} " ,socialConfiguracion = socialConfiguracion)
               }
           } else clPhone.visibility = View.GONE

           if (!socialConfiguracion.email.isNullOrEmpty()) {
               btnEmail.setOnClickListener {
                   requireContext().launchIntentOpenSendEmailTo(btnEmail.text.toString())
               }
           } else clEmail.visibility = View.GONE

           if (!socialConfiguracion.web.isNullOrEmpty()) {
               btnWeb.setOnClickListener {
                   requireContext().launchIntentOpenWebURL(btnWeb.text.toString())
               }
           } else clWeb.visibility = View.GONE


       }







    }








}