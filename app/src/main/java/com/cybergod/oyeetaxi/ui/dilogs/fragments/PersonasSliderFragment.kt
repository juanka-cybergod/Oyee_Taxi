package com.cybergod.oyeetaxi.ui.dilogs.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cybergod.oyeetaxi.databinding.PersonasSliderFragmentBinding
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.ViajeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PersonasSliderFragment : BottomSheetDialogFragment() {

        private var _binding: PersonasSliderFragmentBinding? = null
        private val binding get() = _binding!!


    val viewModel: ViajeViewModel by activityViewModels()

    var pasajeros : Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflar la Vista
        _binding = PersonasSliderFragmentBinding.inflate(inflater, container, false)


        //cargar personas del viewmodel
       loadDataFromViewModel()


        //slider
        setupSlider()



        return  binding.root

    }

    private fun loadDataFromViewModel(){
        pasajeros = viewModel.cantidadPasajeros.value ?: 1
        val posicionAnterior : kotlin.Float = pasajeros.toFloat()!!.div(100f)
        binding.fluidSliderPasajeros.position = posicionAnterior
        prepareTextView(pasajeros)
    }


    private fun setupSlider() {


        binding.fluidSliderPasajeros.positionListener = { position ->

            pasajeros = (position * 100).toInt()

            prepareTextView(pasajeros)


        }

        binding.fluidSliderPasajeros.endTrackingListener = {
            if (pasajeros < 1) {
                pasajeros = 1
                binding.fluidSliderPasajeros.position=0.01f
            }

            viewModel.cantidadPasajeros.postValue( pasajeros )
        }

    }




    private fun prepareTextView(pasajeros:Int) {
        if (pasajeros <= 1) {
            binding.tvCantidadPasajeros.setText("Pasajeros : Solo Yo")
        } else {
            binding.tvCantidadPasajeros.setText("Pasajeros : $pasajeros Personas")
        }
    }
}