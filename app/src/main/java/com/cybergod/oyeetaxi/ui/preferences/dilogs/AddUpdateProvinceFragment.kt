package com.cybergod.oyeetaxi.ui.preferences.dilogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.activityViewModels
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.cybergod.oyeetaxi.databinding.DialogAddupdateProvinceBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.administration.ProvincesAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants.KEY_PROVINCE_PARCELABLE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidLatitude
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isValidLongitude
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddUpdateProvinceFragment : BottomSheetDialogFragment() {

    private var _binding: DialogAddupdateProvinceBinding? = null
    private val binding get() = _binding!!


    val viewModel: ProvincesAdministrationViewModel by activityViewModels()

    private var provinceSelected: Provincia?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogAddupdateProvinceBinding.inflate(inflater, container, false)

        requireArguments().getParcelable<Provincia>(KEY_PROVINCE_PARCELABLE)?.let { provincia ->
            provinceSelected = provincia
            loadData(provincia)
        }


        setupOnClickListener()


        return  binding.root

    }

    private fun loadData(provincia: Provincia) {

            with(binding) {
                continueButton.text = getString(R.string.apply)
                tvProvinceName.editText?.setText(provincia.nombre?:"")
                tvProvinceName.isEnabled = false
                tvLatitude.editText?.setText(provincia.ubicacion?.latitud.toString())
                tvLongitude.editText?.setText(provincia.ubicacion?.longitud.toString())
            }

    }


    private fun setupOnClickListener() {
        binding.cancelButton.setOnClickListener {
            closeThisBottomSheetDialogFragment()
        }

        binding.continueButton.setOnClickListener {
            if (verifyData()) {
                addUpdateProvince()

            }
        }

    }

    private fun addUpdateProvince() {

        if (provinceSelected != null) {
            (requireActivity() as BaseActivity).showProgressDialog("Actualizando provincia ...")

                viewModel.setProvinceLocation(
                    Provincia(
                        nombre = provinceSelected?.nombre,
                        ubicacion = Ubicacion(
                            latitud = newLatitude,
                            longitud = newLongitude,
                        )

                    )
                )

        } else {
            (requireActivity() as BaseActivity).showProgressDialog("Añadiendo provincia ...")

                viewModel.addProvince(
                    Provincia(
                        nombre = binding.tvProvinceName.editText?.text.toString().trim(),
                        visible = true,
                        ubicacion = Ubicacion(
                            latitud = newLatitude,
                            longitud = newLongitude,
                            alturaMapa = 8,
                        )

                    )
                )

        }




        closeThisBottomSheetDialogFragment()

    }



    private var newLatitude:Double?=null
    private var newLongitude:Double?=null

    private fun verifyData(): Boolean {

        with (binding) {
            val mProvinceName: String = tvProvinceName.editText!!.text.trim().toString()
            val mLatitud: String = tvLatitude.editText!!.text.trim().toString()
            val mLongitud:String = tvLongitude.editText!!.text.trim().toString()

            tvProvinceName.isErrorEnabled=false
            tvLatitude.isErrorEnabled=false
            tvLongitude.isErrorEnabled=false


            return when {
                mProvinceName.isEmptyTrim() -> {
                    tvProvinceName.error = "Por favor introduzca el nombre de la provincia"
                    false
                }
                provinceExist(mProvinceName) && tvProvinceName.isEnabled -> {
                    tvProvinceName.error = "Ya existe una provincia con este nombre"
                    false
                }
                mLatitud.isEmptyTrim()-> {
                    tvLatitude.error = "Por favor introduzca latitud"
                    false
                }
                !mLatitud.isValidLatitude() -> {
                    tvLatitude.error = "Este valor de latitud es incorrecto"
                    false
                }
                mLongitud.isEmptyTrim()-> {
                    tvLongitude.error = "Por favor introduzca longitud"
                    false
                }
                !mLongitud.isValidLongitude() -> {
                    tvLongitude.error = "Este valor de longitud es incorrecto"
                    false
                }
                else -> {
                    newLatitude = mLatitud.toDouble()
                    newLongitude = mLongitud.toDouble()

                    true
                }

            }

        }



    }





    private fun provinceExist(provinceName:String):Boolean {
        var found = false
        viewModel.provincesList.value?.let { listaProvincias ->
            listaProvincias.find { provincia -> provincia.nombre!!.contains(provinceName,true) }
        }?.let {
            found = true
        }
        return found
    }


    private fun closeThisBottomSheetDialogFragment(){
        this.isCancelable=true
        this.dismiss()
    }





}