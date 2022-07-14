package com.cybergod.oyeetaxi.ui.preferences.fragments.superAdmin


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.app_update.model.Actualizacion
import com.cybergod.oyeetaxi.databinding.DialogAddEditAppUpdateBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.superAdmin.ActualizacionesViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setFicheroActualizacionSeleccionado
import com.cybergod.oyeetaxi.utils.Constants.KEY_APP_UPDATE_PARCELABLE
import com.cybergod.oyeetaxi.utils.FileManager.loadFile
import com.cybergod.oyeetaxi.utils.UtilsGlobal.converToArrayList
import com.cybergod.oyeetaxi.utils.UtilsGlobal.convertToStringList
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditAppUpdateFragment : BottomSheetDialogFragment() {

    private var _binding: DialogAddEditAppUpdateBinding? = null
    private val binding get() = _binding!!


    val viewModel: ActualizacionesViewModel by activityViewModels()

    private var actualizacion: Actualizacion?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogAddEditAppUpdateBinding.inflate(inflater, container, false)

        requireArguments().getParcelable<Actualizacion>(KEY_APP_UPDATE_PARCELABLE)?.let { actualizacion ->
            this.actualizacion = actualizacion
            loadData(actualizacion)
        }


        setupOnClickListener()

        setupObservers()

        viewModel.actualizacionFile.postValue(null)

        return  binding.root

    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {

        viewModel.actualizacionFile.observe(viewLifecycleOwner, Observer {
            binding.btnSelectApkFile.setFicheroActualizacionSeleccionado(it)
        })

        viewModel.porcentageSubida.observe(viewLifecycleOwner, Observer {
            it?.let { porcentage ->
                binding.btnSelectApkFile.text = "${viewModel.actualizacionFile.value?.name} ${viewModel.doing} $porcentage % ..."
            }

        })



    }

    private fun loadData(actualizacion: Actualizacion) {

            with(binding) {
                continueButton.text = getString(R.string.apply)
                tvVersionInt.editText?.setText(actualizacion.version?.toString())
                tvVersionString.editText?.setText(actualizacion.versionString.orEmpty())
                tvPlayStorePackageName.editText?.setText(actualizacion.playStorePackageName.orEmpty())
                switchForceUpdate.isChecked = actualizacion.forceUpdate == true
                tvDescripcion.editText?.setText(actualizacion.description.convertToStringList())
                btnSelectApkFile.visibility = View.GONE
                tvVersionInt.isEnabled = false

            }

    }


    private fun setupOnClickListener() {

        with (binding) {

            cancelButton.setOnClickListener {
                closeThisBottomSheetDialogFragment()
            }

            continueButton.setOnClickListener {

                if (actualizacion==null) {
                    //add
                    if (verifyDataToAdd()) {
                        addEditAppUpdate()
                    }
                } else {
                    //edit
                    if (verifyDataToEdit()) {
                        addEditAppUpdate()
                    }
                }


            }

            btnSelectApkFile.setOnClickListener {
                openFileChooser()
            }

        }



    }

    private fun openFileChooser() {
        startActivityForResult.launch("application/*") //application/*
    }


    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri ->

            uri?.let{
                requireContext().loadFile(it)?.let { file ->
                    viewModel.actualizacionFile.postValue(file)
                }
            }
        })



    private fun addEditAppUpdate() {

        if (actualizacion != null) {

            (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.adding_app_update))

            viewModel.editAppUpdate(
                Actualizacion(
                    id = actualizacion?.id,
                    versionString = binding.tvVersionString.editText!!.text.trim().toString(),
                    playStorePackageName = binding.tvPlayStorePackageName.editText!!.text.toString().trim(),
                    forceUpdate = binding.switchForceUpdate.isChecked,
                    description = binding.tvDescripcion.editText?.text.toString().converToArrayList(),
                )
            )

            closeThisBottomSheetDialogFragment()

        } else {

            lifecycleScope.launch(Dispatchers.Main) {

                binding.btnSelectApkFile.isClickable=false
                binding.continueButton.isClickable=false
                binding.cancelButton.isClickable=false

                this@AddEditAppUpdateFragment.isCancelable=false

                viewModel.uploadAppUpdateFile().also { urlRelativa ->
                    if (urlRelativa!=null) {
                        addAppUpdate(urlRelativa)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.fail_server_comunication),Toast.LENGTH_LONG).show()
                    }
                }


                this@AddEditAppUpdateFragment.isCancelable=true

                binding.btnSelectApkFile.isClickable=false
                binding.continueButton.isClickable=false
                binding.cancelButton.isClickable=false
            }


        }



}


    private fun addAppUpdate(urlRelativa:String) {

        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.adding_app_update))

        viewModel.addAppUpdate(
            Actualizacion(
                version  = binding.tvVersionInt.editText!!.text.toString().trim().toIntOrNull(),
                versionString = binding.tvVersionString.editText!!.text.trim().toString(),
                playStorePackageName = binding.tvPlayStorePackageName.editText!!.text.toString().trim(),
                forceUpdate = binding.switchForceUpdate.isChecked,
                description = binding.tvDescripcion.editText?.text.toString().converToArrayList(),
                appURL = urlRelativa,
            )
        )

        closeThisBottomSheetDialogFragment()

    }


    private fun verifyDataToAdd(): Boolean {

        with (binding) {
            val mVersionInt: Int? =  tvVersionInt.editText!!.text.trim().toString().toIntOrNull()
            val mVersionString: String = tvVersionString.editText!!.text.trim().toString()

            tvVersionInt.isErrorEnabled=false
            tvVersionString.isErrorEnabled=false

            return when {
                mVersionInt==null -> {
                    tvVersionInt.error = "Versión"
                    false
                }
                actualizacionExist(mVersionInt) -> {
                    tvVersionInt.error = "Ya Existe"
                    false
                }
                mVersionString.isEmptyTrim()-> {
                    tvVersionString.error = "Nombre versión"
                    false
                }
                actualizacionExist(mVersionString.trim()) -> {
                    tvVersionString.error = "Ya Existe"
                    false
                }
                viewModel.actualizacionFile.value == null -> {
                    Toast.makeText(requireContext(),"Seleccione fichero de actualización",Toast.LENGTH_LONG).show()
                    false
                }
                !viewModel.actualizacionFile.value!!.exists() -> {
                    viewModel.actualizacionFile.postValue(null)
                    Toast.makeText(requireContext(),"Seleccione nuevamente el fichero de actualización",Toast.LENGTH_LONG).show()
                    false
                }
                else -> {
                    true
                }

            }

        }



    }


    private fun verifyDataToEdit(): Boolean {

        with (binding) {
            val mVersionString: String = tvVersionString.editText!!.text.trim().toString()
            tvVersionString.isErrorEnabled=false

            return when {
                mVersionString.isEmptyTrim()-> {
                    tvVersionString.error = "Nombre versión"
                    false
                }
                else -> {
                    true
                }

            }

        }



    }


    private fun actualizacionExist(versionInt:Int):Boolean {
        return viewModel.actualizacionesList.value?.find { actualizacion -> actualizacion.version == versionInt }.let {
            it!=null
        }
    }
    private fun actualizacionExist(versionString: String):Boolean {
        return viewModel.actualizacionesList.value?.find { actualizacion -> actualizacion.versionString == versionString }.let {
            it!=null
        }
    }

    private fun closeThisBottomSheetDialogFragment(){
        this.dismiss()
    }





}