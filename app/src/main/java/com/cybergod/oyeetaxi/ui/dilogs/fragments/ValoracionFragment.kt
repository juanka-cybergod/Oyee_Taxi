package com.cybergod.oyeetaxi.ui.dilogs.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.databinding.FragmentValoracionBinding
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.ValoracionViewModel
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_RESPONSE_PARCELABLE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ValoracionFragment : DialogFragment(), RatingBar.OnRatingBarChangeListener {

    private var _binding: FragmentValoracionBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ValoracionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentValoracionBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ValoracionViewModel::class.java]



        binding.animationView.visibility = View.VISIBLE
        binding.clMain.visibility = View.INVISIBLE

        //Obtener el VehiculoResponse pasado por Argumentos
        requireArguments().getParcelable<VehiculoResponse>(KEY_VEHICLE_RESPONSE_PARCELABLE)?.let { vehiculoResponse ->


            binding.userRatingBar.onRatingBarChangeListener = this

            viewModel.usuarioValoradoImagenPerfilURL = vehiculoResponse.usuario?.imagenPerfilURL
            viewModel.idUsuarioValorado = vehiculoResponse.usuario?.id
            viewModel.idUsuarioValora = currentUserActive.value?.id

            binding.tvValoracionSobre.text = "Valora a ${vehiculoResponse.usuario?.nombre}"

            //Imagen Perfil
            binding.imageUsuario.loadImagePerfilFromURL(viewModel.usuarioValoradoImagenPerfilURL)

        }


        setupObservers()

        setupOnClickListeners()


        return  binding.root

    }

    private fun setupOnClickListeners() {

        binding.btnOk.setOnClickListener {

            (requireActivity() as BaseActivity).showProgressDialog("Enviando su valoración")
            viewModel.opinion = binding.tvOpinion.editText?.text.toString().trim()
            viewModel.addUpdateValoration()


        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setupObservers() {
        viewModel.currentValoracion.observe(viewLifecycleOwner, Observer { valoracion ->

            binding.clMain.visibility = View.VISIBLE
            binding.animationView.visibility = View.INVISIBLE


            valoracion?.let {
                binding.tvOpinion.editText?.setText(valoracion.opinion ?: "" )
                binding.userRatingBar.rating = valoracion.valoracion ?: 0f

            }

        })

        viewModel.getValorationByUsersId()


        viewModel.valoracionUpdatedSussefuctly.observe(viewLifecycleOwner, Observer { valoraActualizada ->
            (requireActivity() as BaseActivity).hideProgressDialog()
            if (valoraActualizada != null) {
                Toast.makeText(requireContext(),"Valoración enviada",Toast.LENGTH_LONG).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(),"Error al enviar su valoración",Toast.LENGTH_LONG).show()
            }
        })


    }




    override fun onRatingChanged(p0: RatingBar?, rating: Float, change: Boolean) {
        //Toast.makeText(requireContext(),rating.toString(),Toast.LENGTH_SHORT).show()
        viewModel.valoracion = rating
    }


}