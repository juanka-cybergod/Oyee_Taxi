package com.cybergod.oyeetaxi.ui.userRegistration.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.UserRegistrationFragment4Binding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.userRegistration.viewmodel.UserRegistrationViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidEmail
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURI
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.setOnDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.wordCount
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserRegistrationFragment4 : BaseFragment() {

    private var _binding: UserRegistrationFragment4Binding? = null
    private val binding get() = _binding!!


    //Prepara el View model para que se alcanzable desde todos los Fragments con una solo instancia
    val viewModel: UserRegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserRegistrationFragment4Binding.inflate(inflater, container, false)

        loadTempDatafromViewModel()

        setupOnClickListeners()

        viewModel.provincia.observe(viewLifecycleOwner, Observer { selectedProvince ->
            binding.tvProvincia.editText!!.setText(selectedProvince.nombre)
        })

        return  binding.root
    }

    private fun setupOnClickListeners() {
        binding.continueButton.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) {
                if (verifyData()) {
                    goToNextFragment()
                }
            }

        }

        binding.imageViewSelect.setOnClickListener {
            requireView().hideKeyboard()
            openImageChooser()
        }

        binding.etFechaNacimiento.setOnClickListener {
            requireView().hideKeyboard()
            val datePicker: DatePickerFragment = DatePickerFragment({ day, month, year -> binding.tvFechaNacimiento.setOnDateSelected(day, month, year)},binding.etFechaNacimiento.text.toString(),true,false)
            datePicker.show(parentFragmentManager,"datePicker")
        }

        binding.etProvincia.setOnClickListener {
            requireView().hideKeyboard()
            findNavController().navigate(R.id.action_userRegistrationFragment4_to_provincesFragment2)
        }
    }


    private fun loadTempDatafromViewModel() {

        //Imagen de Perfil
        binding.imagePerfil.loadImagePerfilFromURI(viewModel.imagenPerfilURI.value)

        if (viewModel.conductor.value == true) {
            "CONDUCTOR\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
        } else {
            "PASAJERO\n${viewModel.telefonoMovil.value}".also { binding.tvUserInfo.text = it }
        }


        binding.tvNombre.editText?.setText(viewModel.nombre.value ?: "")
        binding.tvApellidos.editText?.setText(viewModel.apellidos.value ?: "")
        binding.tvFechaNacimiento.editText?.setText(viewModel.fechaNacimiento.value ?: "")
        binding.tvCorreo.editText?.setText(viewModel.correo.value ?: "")
        //binding.tvProvincia.editText?.setText(viewModel.provincia.value ?: "")


    }


    private fun goToNextFragment(){
        findNavController().navigate(R.id.action_userRegistrationFragment4_to_userRegistrationFragment5)
    }


    private suspend fun verifyData(): Boolean {
        val mNombre = binding.tvNombre.editText!!.text.trim().toString()
        val mApellidos: String = binding.tvApellidos.editText!!.text.trim().toString()
        val mfechaNacimiento = binding.tvFechaNacimiento.editText!!.text.trim().toString()
        val mCorreo = binding.tvCorreo.editText!!.text.trim().toString()
        val mProvincia = binding.tvProvincia.editText!!.text.trim().toString()


        binding.tvNombre.isErrorEnabled=false
        binding.tvApellidos.isErrorEnabled=false
        binding.tvCorreo.isErrorEnabled=false
        binding.tvFechaNacimiento.isErrorEnabled=false
        binding.tvProvincia.isErrorEnabled=false


        var dataOK :Boolean = when {

            //foto de perfil
            viewModel.imagenPerfilFile.value == null  -> {
                showSnackBar(
                    "Por favor seleccione una foto para su perfil",
                    true,
                )
                false
            }

            //nombre
            mNombre.isEmptyTrim()-> {
                binding.tvNombre.error = "Por favor introduzca su nombre completo"
                false
            }
            //apellidos
            mApellidos.isEmptyTrim()-> {
                binding.tvApellidos.error = "Por favor introduzca sus apellidos"
                false
            }
            //los 2 apellidos
            mApellidos.wordCount() < 2 -> {
                binding.tvApellidos.error = "Por favor introduzca su segundo apellido"
                false
            }
            //correo electronico
            !mCorreo.isValidEmail() -> {
                binding.tvCorreo.error = "Por favor introduzca una dirección válida de correo"
                false
            }
            //fecha de nacimiento
            mfechaNacimiento.isEmptyTrim()-> {
                binding.tvFechaNacimiento.error = "Por favor introduzca su fecha de nacimiento"
                false
            }
            //provincia
            mProvincia.isEmptyTrim()-> {
                binding.tvProvincia.error = "Por favor introduzca su provincia de recidencia"
                false
            }
            else -> {

                viewModel.nombre.postValue(mNombre)
                viewModel.apellidos.postValue(mApellidos)
                viewModel.fechaNacimiento.postValue(mfechaNacimiento)
                viewModel.correo.postValue(mCorreo)
                //viewModel.provincia.postValue(mProvincia)
                true
            }
        }

        if (dataOK) {

            (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.validating_email))

            delay(500)

            dataOK = when (viewModel.emailExist(mCorreo)) {
                true -> {
                    binding.tvCorreo.error = "Este correo ya está siendo usado"
                    false
                }
                null -> {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                    false
                }
                false -> {
                    true
                }

            }.also {
                (requireActivity() as BaseActivity).hideProgressDialog()
            }


        }

    return dataOK

    }


    private fun openImageChooser() {
        startActivityForResult.launch("image/jpeg")
    }


    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri ->

            if (uri != null ) {

                viewModel.imagenPerfilURI.value = uri

                //Imagen de Perfil
                binding.imagePerfil.loadImagePerfilFromURI(uri)

                viewModel.imagenPerfilFile.postValue(
                    requireContext().prepareImageCompressAndGetFile(uri)
                )

//                showSnackBar(
//                    getString(R.string.image_sussefuctly_selected),
//                    false
//                )


            }


        })








}









/*


    // Nuevo metodo para StartActivityForResult
    var newStartActivityForResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null ) {
           //doSomeOperations()

        }
    }


    private fun uploadImage(selectedImageURI: Uri) {

        val parcelFileDescriptor = requireContext().contentResolver.openAssetFileDescriptor(selectedImageURI,"r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        var file = File(requireContext().cacheDir,requireContext().contentResolver.getFileName(selectedImageURI))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        //Compress File
        file = compressToFile(file)


        //Si lo quiero haver con barra de progreso
        //binding.proggressBar.progress = 0
        viewModel.uploadSingleFile(file,this.requireActivity() as UserRegistrationActivity)



    }

    private fun compressToFile(file: File) : File{

        //Toast.makeText(requireContext(),file.absolutePath,Toast.LENGTH_LONG).show()

        val fullSizeBitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val reducedSizeBitmap: Bitmap = ImageResizer.reduceBitmapSize(fullSizeBitmap,2000000) //Varia la Resolucion Final de la Imagen
        val file  = File(file.absolutePath)
        //Omitir Crear Fichero pq se Utilizara la misa direccion q el Original Temporal
        //file.createNewFile()
        val compress : ByteArrayOutputStream = ByteArrayOutputStream()
        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG,90,compress) //Varia la Calidad de la Imagen
        val bitmap = compress.toByteArray()
        val outpud = FileOutputStream(file)
        outpud.write(bitmap)
        outpud.flush()
        outpud.close()

        return file

    }


//    private fun prepareImage(selectedImageURI: Uri){
//
//
//
//        val parcelFileDescriptor = requireContext().contentResolver.openAssetFileDescriptor(selectedImageURI,"r", null) ?: return
//
//        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//        var file = File(requireContext().cacheDir,requireContext().contentResolver.getFileName(selectedImageURI))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//
//
//
//        //Compress File
//        try {
//            file = compressToFile(file)
//            viewModel.imagenPerfilFile.postValue(file)
//
//        } catch (e: Exception) {
//            viewModel.imagenPerfilFile.postValue(null)
//        }
//
//
//
//
//    }






 */

