package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.DialogEditUserProfileBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showInputTextMessage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.showDropDownMenuFix
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditUserProfileFragment : BottomSheetDialogFragment() {

    private var _binding: DialogEditUserProfileBinding? = null
    private val binding get() = _binding!!

    val viewModel: UsersAdministrationViewModel by activityViewModels()

    private lateinit var user: Usuario

    var removeUserPerfil: String? = null
    var provinceSelected: Provincia? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogEditUserProfileBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onResume() {
        super.onResume()
        binding.cancelButton.isChecked = false

        requireArguments().getParcelable<Usuario>(KEY_USER_PARCELABLE)?.let { usuario ->
            user = usuario

            loadUserDetails(user)

            setupOnClickListener()
        }


    }

    private fun loadUserDetails(usuario: Usuario) {

        with(binding) {


            imagePerfil.loadImagePerfilFromURLNoCache(usuario.imagenPerfilURL)

            tvNombre.editText?.setText(usuario.nombre)
            tvApellidos.editText?.setText(usuario.apellidos)
            tvFechaNacimiento.editText?.setText(usuario.fechaDeNacimiento)


            setupProvincesListAdapter()
            tvProvincia.setText(usuario.provincia?.nombre)
            fillTexViewProvincesList()

            if (usuario.conductor == true) {
                rbConductor.isChecked = true
                rbPasajero.isChecked = false
            } else {
                rbConductor.isChecked = false
                rbPasajero.isChecked = true
            }

            switchHabilitado.isChecked = usuario.habilitado ?: true
            switchAdministrador.isChecked = usuario.administrador ?: false

            tvFechaRegistro.text = "Fecha Registro : ${usuario.fechaDeRegistro}"


        }


    }

    private fun fillTexViewProvincesList() {

        binding.tvProvincia.setAdapter(
            viewModel.arrayAdapter
        )
        binding.tfProvincia.clearFocus()
    }

    private fun setupProvincesListAdapter() {
        viewModel.arrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_map_style,
            viewModel.provincesItems
        )
    }

    private fun changeClientEnabledStatus(ok: Boolean, motivo: String?) {

        if (ok) {
            binding.switchHabilitado.isChecked = !binding.switchHabilitado.isChecked
            user.mensaje = motivo ?: ""
        }


    }


    private fun setupOnClickListener() {

        with(binding) {

            tvProvincia.setOnClickListener {
                binding.tvProvincia.showDropDownMenuFix(viewModel.arrayAdapter)
            }

            tvProvincia.setOnItemClickListener { adapterView, view, position, id ->

                val selection = adapterView.getItemAtPosition(position).toString()
                provinceSelected = viewModel.getProvinceSelectedByName(selection)

            }


            imagePerfil.setOnClickListener {

                if (!user.imagenPerfilURL.isNullOrEmpty()) {

                    requireContext().showMessageDialogForResult(
                        funResult = { ok ->
                            if (ok) {
                                removeUserPerfil = ""
                                imagePerfil.loadImagePerfilFromURLNoCache("")
                            }
                        },
                        title = "Quitar Foto de Perfil",
                        message = "Desea quitar la foto de perfil de este usuario puesto que no cumple con los parámetros requeridos",
                        icon = R.drawable.ic_alert_24
                    )

                }


            }


            switchHabilitado.setOnClickListener {
                if (!switchHabilitado.isChecked) {

                    switchHabilitado.isChecked = !switchHabilitado.isChecked

                    requireActivity().showInputTextMessage(
                        funResult = { ok, motivo -> changeClientEnabledStatus(ok, motivo) },
                        title = "Motivo Deshabilitado",
                        hint = "",
                        helperText = "Se notificará al usuario al intentar iniciar sesión el motivo por el cuál fué Deshabilitado",
                        message = user.mensaje,
                        icon = R.drawable.ic_note,
                    )
                }

            }


            etFechaNacimiento.setOnClickListener {
                requireView().hideKeyboard()
                val datePicker = DatePickerFragment({ day, month, year ->
                    tvFechaNacimiento.setOnDateSelected(
                        day,
                        month,
                        year
                    )
                }, etFechaNacimiento.text.toString(), setMaxDate = true, setMinDate = false)
                datePicker
                    .show(parentFragmentManager, "datePicker")
            }

            cancelButton.setOnClickListener {
                closeThisBottomSheetDialogFragment()
            }

            continueButton.setOnClickListener {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))

                viewModel.updateUser(
                    Usuario(
                        id = user.id,
                        nombre = tvNombre.editText?.text?.trim().toString(),
                        apellidos = tvApellidos.editText?.text?.trim().toString(),
                        fechaDeNacimiento = tvFechaNacimiento.editText?.text?.trim()
                            .toString(),
                        conductor = rbConductor.isChecked,
                        administrador = switchAdministrador.isChecked,
                        habilitado = switchHabilitado.isChecked,
                        mensaje = user.mensaje,
                        imagenPerfilURL = removeUserPerfil,
                        provincia = provinceSelected,
                    )
                )
                closeThisBottomSheetDialogFragment()

            }

        }




    }


    private fun closeThisBottomSheetDialogFragment() {
        this.isCancelable = true
        this.dismiss()
    }


}