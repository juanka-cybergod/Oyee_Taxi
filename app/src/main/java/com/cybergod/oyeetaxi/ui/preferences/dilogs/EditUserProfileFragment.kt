package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
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
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.wordCount

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditUserProfileFragment: BottomSheetDialogFragment()  {

    private var _binding: DialogEditUserProfileBinding? = null
    private val binding get() = _binding!!

    val viewModel: UsersAdministrationViewModel by activityViewModels()

    private lateinit var user : Usuario

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogEditUserProfileBinding.inflate(inflater, container, false)


        return  binding.root

    }

    override fun onResume() {
        super.onResume()
        binding.cancelButton.isChecked=false

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

            if (usuario.conductor==true) {
                rbConductor.isChecked=true
                rbPasajero.isChecked=false
            } else {
                rbConductor.isChecked=false
                rbPasajero.isChecked=true
            }

            switchHabilitado.isChecked = usuario.habilitado?:true
            switchAdministrador.isChecked = usuario.administrador?:false


        }




    }

    private fun changeClientEnabledStatus(ok:Boolean,motivo:String?){

        if (ok) {
            binding.switchHabilitado.isChecked = !binding.switchHabilitado.isChecked
            user.mensaje = motivo?:""
        }


    }

    var removeUserPerfil:String? = null

    private fun setupOnClickListener() {


            binding.imagePerfil.setOnClickListener {

                if (!user.imagenPerfilURL.isNullOrEmpty()) {

                    requireContext().showMessageDialogForResult(
                        funResult = {ok ->
                            if (ok) {
                                removeUserPerfil = ""
                                binding.imagePerfil.loadImagePerfilFromURLNoCache("")
                            }
                        },
                        title = "Quitar Foto de Perfil",
                        message = "Desea quitar la foto de perfil de este usuario puesto que no cumple con los parámetros requeridos",
                        icon = R.drawable.ic_alert_24
                    )

                }



            }


        binding.switchHabilitado.setOnClickListener {
            if (!binding.switchHabilitado.isChecked) {

                binding.switchHabilitado.isChecked = !binding.switchHabilitado.isChecked

                requireActivity().showInputTextMessage(
                    funResult =  {ok,motivo, -> changeClientEnabledStatus(ok,motivo)},
                    title = "Motivo Deshabilitado",
                    hint = "",
                    helperText = "Se notificará al usuario al intentar iniciar sesión el motivo por el cuál fué Deshabilitado",
                    message = user.mensaje,
                    icon = R.drawable.ic_note,
                )
            }

        }


        binding.etFechaNacimiento.setOnClickListener {
            requireView().hideKeyboard()
            val datePicker = DatePickerFragment({ day, month, year -> binding.tvFechaNacimiento.setOnDateSelected(day, month, year)},binding.etFechaNacimiento.text.toString(), setMaxDate = true, setMinDate = false)
            datePicker
                .show(parentFragmentManager,"datePicker")
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))

                viewModel.updateUser(
                    Usuario(
                        id = user.id,
                        nombre = binding.tvNombre.editText?.text?.trim().toString(),
                        apellidos = binding.tvApellidos.editText?.text?.trim().toString(),
                        fechaDeNacimiento = binding.tvFechaNacimiento.editText?.text?.trim().toString(),
                        conductor = binding.rbConductor.isChecked,
                        administrador = binding.switchAdministrador.isChecked,
                        habilitado = binding.switchHabilitado.isChecked,
                        mensaje = user.mensaje,
                        imagenPerfilURL = removeUserPerfil,
                    )
                )
                closeThisBottomSheetDialogFragment()

            }
        }




    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }


    private fun verifyData(): Boolean {
        val mNombre = binding.tvNombre.editText!!.text.trim().toString()
        val mApellidos: String = binding.tvApellidos.editText!!.text.trim().toString()


        binding.tvNombre.isErrorEnabled=false
        binding.tvApellidos.isErrorEnabled=false
        binding.tvFechaNacimiento.isErrorEnabled=false

        return when {

            mNombre.isEmptyTrim() -> {
                binding.tvNombre.error = "Por favor introduzca el nombre completo"
                false
            }
            mApellidos.isEmptyTrim() -> {
                binding.tvApellidos.error = "Por favor introduzca los apellidos"
                false
            }
            mApellidos.wordCount() < 2 -> {
                binding.tvApellidos.error = "Por favor introduzca el segundo apellido"
                false
            }
            else -> {
                true
            }

        }

    }



}