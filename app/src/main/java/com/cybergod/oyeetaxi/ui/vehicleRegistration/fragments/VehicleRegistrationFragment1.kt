package com.cybergod.oyeetaxi.ui.vehicleRegistration.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.VehicleRegistrationFragment1Binding
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.vehicleRegistration.viewmodel.VehicleRegistrationViewModel
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentYear
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURI
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*


@AndroidEntryPoint
class VehicleRegistrationFragment1 : BaseFragment() {


    private var _binding: VehicleRegistrationFragment1Binding? = null
    private val binding get() = _binding!!


    val viewModel: VehicleRegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VehicleRegistrationFragment1Binding.inflate(inflater, container, false)


        loadTempDataFromViewModel()

        setupOnClickListeners()

        setupObservers()


        return  binding.root
    }

    private fun setupObservers() {

        viewModel.tipoVehiculo.observe(viewLifecycleOwner, Observer { selectedVehicleType ->
            selectedVehicleType?.let {
                binding.tvTipo.editText!!.setText(it.tipoVehiculo)
            } ?: binding.tvTipo.editText!!.setText("")


        })
    }


    private fun setupOnClickListeners() {
        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                goToNextFragment()
            }
        }

        binding.imageViewSelect.setOnClickListener {

            openImageChooser()
        }

        binding.etTipo.setOnClickListener {
            requireView().hideKeyboard()
            findNavController().navigate(R.id.action_vehicleRegistrationFragment1_to_vehicleTypeFragment)


        }

        binding.etAno.setOnClickListener {
            var initialYear : Int? = null
            try {
                initialYear = binding.tvAno.editText?.text.toString().toInt()
            } catch (e: Exception){}
            showDialogYearPicker(initialYear)
        }


    }


    private fun showDialogYearPicker(initialYear:Int? = null) {



        val alertDialog: AlertDialog?
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogStyle)
        val inflater = requireActivity().layoutInflater

        val cal = Calendar.getInstance()

        val dialog = inflater.inflate(R.layout.year_picker_dialog, null)
        val monthPicker = dialog.findViewById(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH) + 1

        val year = initialYear ?: cal.get(Calendar.YEAR)

        yearPicker.minValue = 1900
        yearPicker.maxValue = UtilsGlobal.getCurrentYear()

        yearPicker.value = year

        builder.setView(dialog).setPositiveButton(Html.fromHtml("<font color='#FF4081'>Ok</font>")){ dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            val value = yearPicker.value
            binding.tvAno.editText?.setText(value.toString())
            dialogInterface.cancel()
        }

        builder.setNegativeButton(Html.fromHtml("<font color='#FF4081'>Cancel</font>")){ dialogInterface, which ->
            dialogInterface.cancel()
        }

        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }



    private fun loadTempDataFromViewModel() {


        binding.imageFrontal.loadImageVehiculoFrontalFromURI(viewModel.imagenFrontalVehiculoURI.value)

        binding.tvMarca.editText?.setText(viewModel.marca.value ?: "")

        binding.tvModelo.editText?.setText(viewModel.modelo.value ?: "")

        binding.tvAno.editText?.setText(viewModel.ano.value ?: "")


    }


    private fun openImageChooser() {
        requireView().hideKeyboard()
        startActivityForResult.launch("image/jpeg")
    }


    private fun goToNextFragment(){
        findNavController().navigate(R.id.action_vehicleRegistrationFragment1_to_vehicleRegistrationFragment2)
    }


    private fun verifyData(): Boolean {

        val mTipo = binding.tvTipo.editText!!.text.trim().toString()
        val mMarca: String = binding.tvMarca.editText!!.text.trim().toString()
        val mModelo = binding.tvModelo.editText!!.text.trim().toString()
        val mAno = binding.tvAno.editText!!.text.trim().toString()




        return when {

            //foto frontal del vehiculo
            viewModel.imagenFrontalVehiculoFile.value == null  -> {
                showSnackBar(
                    "Por favor seleccione una imágen frontal del vehículo",
                    true,
                )
                false
            }

            //Tipo
            TextUtils.isEmpty(mTipo.trim { it <= ' ' }) -> {
                showSnackBar(
                    "Por favor seleccione el tipo de vehículo",
                    true,
                )
                false
            }
            //Marca
            TextUtils.isEmpty(mMarca.trim { it <= ' ' }) -> {
                showSnackBar(
                    "Por favor introduzca la marca del vehículo",
                    true,
                )
                false
            }
            //Modelo
            TextUtils.isEmpty(mModelo.trim { it <= ' ' }) -> {
                showSnackBar(
                    "Por favor introduzca el modelo del vehiculo",
                    true,
                )
                false
            }
            //Año
            TextUtils.isEmpty(mAno.trim { it <= ' ' })  -> {
                showSnackBar(
                    "Por favor introduzca el año de fabricación del vehículo",
                    true
                )
                false
            }
            //Año
            ( mAno.length != 4 ) -> {
                showSnackBar(
                    "El año de fabricación está incompleto",
                    true
                )
                false
            }
            //Año
            (mAno.toInt() < 1900 ) or (mAno.toInt() > getCurrentYear() ) -> {
                showSnackBar(
                    "El año de fabricación está incorrecto",
                    true
                )
                false
            }
            else -> {

                viewModel.marca.postValue(mMarca)
                viewModel.modelo.postValue(mModelo)
                viewModel.ano.postValue(mAno)

                true
            }

        }




    }

    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri ->
            if (uri != null ) {

                viewModel.imagenFrontalVehiculoURI.value = uri

                binding.imageFrontal.loadImageVehiculoFrontalFromURI(uri)

                binding.tvFrontInfo.visibility = View.INVISIBLE

                viewModel.imagenFrontalVehiculoFile.postValue(
                    requireContext().prepareImageCompressAndGetFile(uri)
                )

//                showSnackBar(
//                    getString(R.string.image_sussefuctly_selected),
//                    false
//                )


            }

        })


}