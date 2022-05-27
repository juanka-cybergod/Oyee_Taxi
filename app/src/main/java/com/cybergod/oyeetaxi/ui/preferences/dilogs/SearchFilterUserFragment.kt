package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.api.futures.user.model.requestFilter.UserFilterOptions
import com.cybergod.oyeetaxi.databinding.DialogFilterUserBinding
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_FILTER_OPTIONS

class SearchFilterUserFragment : DialogFragment() {

    private var _binding: DialogFilterUserBinding? = null
    private val binding get() = _binding!!


    val viewModel: UsersAdministrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogFilterUserBinding.inflate(inflater, container, false)


        requireArguments().getParcelable<UserFilterOptions>(KEY_USER_FILTER_OPTIONS)?.let { filterOptions ->
            loadData(filterOptions)
        }


        setupOnClickListener()


        return  binding.root

    }


    private fun loadData(filterOptions: UserFilterOptions) {

            with (binding) {
                switchConductores.isChecked = filterOptions.condutores?:false
                switchDeshabilitados.isChecked = filterOptions.deshabilitados?:false
                switchAdministradores.isChecked = filterOptions.administradores?:false
                switchVerificacionPendiente.isChecked = filterOptions.verificacionesPendientes?:false
            }



    }


    private fun setupOnClickListener() {

        with(binding) {
            btnClose.setOnClickListener {
                closethisfragment()
            }


            btnApply.setOnClickListener {

                viewModel.userFilterOptions.apply {
                    condutores = if (switchConductores.isChecked) {true} else {null}
                    deshabilitados = if (switchDeshabilitados.isChecked) {true} else {null}
                    administradores = if (switchAdministradores.isChecked) {true} else {null}
                    verificacionesPendientes =if (switchVerificacionPendiente.isChecked) {true} else {null}
                }

                search()

                closethisfragment()
            }


        }

    }

    private fun search() {
        viewModel.getPage=1
        viewModel.getUsersPaginated()
    }


    private fun closethisfragment(){
        this.dismiss()
    }




}