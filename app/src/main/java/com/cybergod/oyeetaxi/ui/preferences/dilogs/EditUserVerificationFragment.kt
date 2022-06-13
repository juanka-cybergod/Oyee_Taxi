package com.cybergod.oyeetaxi.ui.preferences.dilogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.cybergod.oyeetaxi.api.futures.user.model.verification.UsuarioVerificacion
import com.cybergod.oyeetaxi.databinding.DialogUserEditVerificationBinding
import com.cybergod.oyeetaxi.ui.dilogs.fragments.ImageViewFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageUserVerificacionFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showMessageDialogForResult
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_PARCELABLE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getRamdomUUID
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditUserVerificationFragment : BottomSheetDialogFragment() {

    private var _binding: DialogUserEditVerificationBinding? = null
    private val binding get() = _binding!!

    val viewModel: UsersAdministrationViewModel by activityViewModels()

    private lateinit var user: Usuario

    var removeImageUserVerificacion: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogUserEditVerificationBinding.inflate(inflater, container, false)

        setupOnClickListener()

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


    private fun loadUserDetails(user: Usuario) {

        with (binding) {

            ivImageVerificacion.loadImageUserVerificacionFromURLNoCache(
                relativeURL = user.usuarioVerificacion?.imagenIdentificaionURL,
                conductor = user.conductor ?: false
            )

            switchVerificado.isChecked = user.usuarioVerificacion?.verificado ?: false

            tvIdentificacion.editText?.setText(user.usuarioVerificacion?.identificacion ?: "")

            tvIdentificacion.hint =
                if (user.conductor == true) {
                    "Licencia de Conducción"
                } else {
                    "Número de Identidad"
                }


        }



    }


    private fun launchImageViewFragment(imageURL: String?) {
        val imageViewFragment = ImageViewFragment()
        val args = Bundle()
        args.putString(Constants.KEY_IMAGE_URL, imageURL)
        imageViewFragment.arguments = args
        imageViewFragment.show(
            requireActivity().supportFragmentManager,
            "imageViewFragment+${getRamdomUUID()}"
        )
    }

    private fun setupOnClickListener() {

        binding.ivImageVerificacion.setOnClickListener {
            if (!user.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty()) {
                launchImageViewFragment(user.usuarioVerificacion?.imagenIdentificaionURL)
            }
        }

        binding.buttonRemoveImageVerification.setOnClickListener {
            if (!user.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty()) {

                requireContext().showMessageDialogForResult(
                    funResult = { ok ->
                        if (ok) {
                            removeImageUserVerificacion = ""
                            binding.ivImageVerificacion.loadImageUserVerificacionFromURLNoCache(
                                relativeURL = "",
                                conductor = user.conductor ?: false
                            )
                        }
                    },
                    title = "Requerir fotocopia al usuario",
                    message = "Desea quitar la fotocopia del documento puesto que no cumple con los parámetros y requerir una nueva al usuario",
                    icon = R.drawable.ic_alert_24
                )

            }
        }

        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {

            (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))


            viewModel.updateUser(
                Usuario(
                    id = user.id,
                    usuarioVerificacion = UsuarioVerificacion(
                        ///Asignar Valores
                        verificado = binding.switchVerificado.isChecked,
                        identificacion = binding.tvIdentificacion.editText?.text.toString(),
                        imagenIdentificaionURL = removeImageUserVerificacion
                    )
                )
            )

            closeThisBottomSheetDialogFragment()
        }

    }


    private fun closeThisBottomSheetDialogFragment() {
        dismiss()
    }


}