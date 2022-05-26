package com.cybergod.oyeetaxi.ui.controlPanel.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.model.TipoFichero
import com.cybergod.oyeetaxi.api.model.usuario.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelFragmentEditVerificationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageUserVerificacionFromURI
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageUserVerificacionFromURL
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.cybergod.oyeetaxi.api.model.verification.UsuarioVerificacion
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserControlPanelFragmentEditVerification: BottomSheetDialogFragment()  {

    private var _binding: UserControlPanelFragmentEditVerificationBinding? = null
    private val binding get() = _binding!!

    //Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserControlPanelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserControlPanelFragmentEditVerificationBinding.inflate(inflater, container, false)


        loadUserDetails()

        setupObservers()

        setupOnClickListener()

        return  binding.root


    }

    private fun setupObservers() {
        //observer si la imagen de verificacion fue subida satisfactoriamente
        viewModel.imagenVerificacionURL.observe(viewLifecycleOwner, Observer { imagenVerificacionURL ->
            if (imagenVerificacionURL != null) {

                if (imagenVerificacionURL.isNotEmpty()) {

                    viewModel.updateUser(
                        Usuario(
                            usuarioVerificacion = UsuarioVerificacion(
                                identificacion = viewModel.identificaion,
                                imagenIdentificaionURL = imagenVerificacionURL
                            )

                        )
                    )
                    closeThisBottomSheetDialogFragment()

                }


            } else {

                closeThisBottomSheetDialogFragment()

                (requireActivity() as BaseActivity).hideProgressDialog()

                    requireActivity().showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )


            }

        })
    }

    private fun loadUserDetails() {

        binding.ivImageVerificacion.loadImageUserVerificacionFromURL(currentUserActive.value?.usuarioVerificacion?.imagenIdentificaionURL,currentUserActive.value?.conductor!!)


        if (currentUserActive.value?.conductor == true) {
            binding.tvCarnetIdentidad.visibility = View.GONE
            binding.tvLicenciaConduccion.visibility = View.VISIBLE
            binding.tvLicenciaConduccion.editText?.setText(currentUserActive.value?.usuarioVerificacion?.identificacion ?: "")


        } else {
            binding.tvLicenciaConduccion.visibility = View.GONE
            binding.tvCarnetIdentidad.visibility = View.VISIBLE
            binding.tvCarnetIdentidad.editText?.setText(currentUserActive.value?.usuarioVerificacion?.identificacion ?: "")
        }


    }

    private fun setupOnClickListener() {


        binding.buttonSelectImageVerification.setOnClickListener {

            openImageChooser()
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {

                (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))


                if (viewModel.imagenVerificacionFile.value != null ) {

                    viewModel.uploadFile(
                        file = viewModel.imagenVerificacionFile.value,
                        id = currentUserActive.value?.id!!,
                        tipoFichero = TipoFichero.USUARIO_VERIFICACION
                    )

                } else {
                    viewModel.updateUser(
                        Usuario(
                            usuarioVerificacion = UsuarioVerificacion(
                                identificacion = viewModel.identificaion,
                            )
                        )
                    )

                    closeThisBottomSheetDialogFragment()
                }




            }
        }

    }

    private fun openImageChooser() {
        requireView().hideKeyboard()
        startActivityForResult.launch("image/jpeg")
    }

    private fun closeThisBottomSheetDialogFragment(){

        this.isCancelable=true
        this.dismiss()

        viewModel.imagenVerificacionURL.value=""
        viewModel.imagenVerificacionURI.value=null
        viewModel.imagenVerificacionFile.value=null



    }

    private fun verifyData(): Boolean {

        val mNumeroLicenciaConduccion = binding.tvLicenciaConduccion.editText!!.text.trim().toString()
        val mNumeroCarnetIdentidad = binding.tvCarnetIdentidad.editText!!.text.trim().toString()
        val identificacion :String

        binding.tvCarnetIdentidad.isErrorEnabled=false
        binding.tvLicenciaConduccion.isErrorEnabled=false


        return when {

            //mNumeroCarnetIdentidad
            (mNumeroLicenciaConduccion.length < 11) && (currentUserActive.value?.conductor == true) -> {
                binding.tvLicenciaConduccion.error = "Por favor introduzca su licencia de conducir completa"
                false
            }
            //mNumeroLicenciaConduccion
            (mNumeroCarnetIdentidad.length < 11) && (currentUserActive.value?.conductor == false) -> {
                binding.tvCarnetIdentidad.error = "Por favor introduzca su número de identidad completo"
                false
            }
            //foto Verificacion
            (viewModel.imagenVerificacionFile.value == null) && (currentUserActive.value?.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty()) -> {
                binding.tvLicenciaConduccion.error = "Por favor adjunte fotocopia del documento requerido"
                binding.tvCarnetIdentidad.error = "Por favor adjunte fotocopia del documento requerido"
                false
            }
            else -> {

                if (currentUserActive.value?.conductor == true) {
                    identificacion = mNumeroLicenciaConduccion
                } else {
                    identificacion = mNumeroCarnetIdentidad
                }
                viewModel.identificaion = identificacion


                true
            }

        }


    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri ->

            if (uri != null ) {

                viewModel.imagenVerificacionURI.value = uri

                binding.ivImageVerificacion.loadImageUserVerificacionFromURI(uri, currentUserActive.value?.conductor)

                viewModel.imagenVerificacionFile.postValue(
                    requireContext().prepareImageCompressAndGetFile(uri)
                )

                //Toast.makeText(requireContext(),getString(R.string.image_sussefuctly_selected),Toast.LENGTH_SHORT).show()

            }


        })

}