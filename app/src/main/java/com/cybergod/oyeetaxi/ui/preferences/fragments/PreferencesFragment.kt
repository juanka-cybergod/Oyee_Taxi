package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentPreferencesMainBinding
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.cybergod.oyeetaxi.maps.Utils
import com.cybergod.oyeetaxi.maps.Utils.getMapStyleByName
import com.cybergod.oyeetaxi.maps.Utils.getStyleNameByMapStyle
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.dilogs.fragments.UpdateApplicationFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.PreferencesViewModel
import com.cybergod.oyeetaxi.ui.splash.viewmodel.SplashViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionInt
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionString
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class PreferencesFragment : BaseFragment() {

    private val typeAndStyle by lazy { TypeAndStyle(requireContext()) }

    private var _binding: FragmentPreferencesMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreferencesViewModel by activityViewModels()


    private val splashViewModel: SplashViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPreferencesMainBinding.inflate(inflater, container, false)


        setupMapStyleListAdapter()

        setupOnClickListener()

        setupObservers()

        loadData()


        return  binding.root
    }



    @SuppressLint("SetTextI18n")
    private fun loadData() {
        binding.tvVersion.text = "Versión ${getAppVersionString()}"



    }

    private fun setupObservers() {

        setupMapStyleObserver()

        updateConfigurationObserver()

    }

    private fun setupMapStyleObserver(){
        viewModel.readMapStyle.observe(viewLifecycleOwner, Observer {

            binding.tvEstilosMapa.setText(
                getStyleNameByMapStyle(it)
            )

            binding.tvEstilosMapa.clearFocus()


            fillTexViewMapStyles()

        })
    }

    private fun updateConfigurationObserver(){

        splashViewModel.updateConfiguration.observe(viewLifecycleOwner, Observer { updateConfiguration ->
            if (updateConfiguration != null ){
                if (updateConfiguration.available == true) {

                    if (getAppVersionInt() < updateConfiguration.version?:1){

                        launchUpdateApplicationFragment()

                    } else {
                        Toast.makeText(requireContext(),"Su aplicación está actualizada",Toast.LENGTH_SHORT).show()
                    }



                } else {
                    Toast.makeText(requireContext(),"Actualizaciones desactivadas temporalmente",Toast.LENGTH_SHORT).show()
                }

            }  else {
                Toast.makeText(requireContext(),"Falló la conexion con el Servidor para obtener actualizacions",Toast.LENGTH_SHORT).show()
            }

        })



    }


    private fun setupOnClickListener() {

        binding.btnComprobarActualizacions.setOnClickListener {
            splashViewModel.getUpdateConfiguration()
        }



        binding.tvEstilosMapa.setOnItemClickListener { adapterView, view, position, id ->

            val selectedMapStyle = getMapStyleByName(
                adapterView.getItemAtPosition(position).toString()
            )

            if (typeAndStyle.setMapType(selectedMapStyle)) {
                viewModel.saveMapStyle(selectedMapStyle)
                currentMapStyle = selectedMapStyle
            }

            fillTexViewMapStyles()

        }




    }


    override fun onResume() {


        if (deviceInDarKMode()) {

            binding.tvEstilosMapa.setText(
                getStyleNameByMapStyle(TypeAndStyle.MapStyle.NIGHT)
            )
            binding.tfEstilosMapa.isEnabled = false
        } else {
            binding.tfEstilosMapa.isEnabled = true
        }

        fillTexViewMapStyles()

        super.onResume()
    }



    private fun fillTexViewMapStyles() {
        binding.tvEstilosMapa.setAdapter(
            viewModel.arrayAdapter
        )
    }

    private fun setupMapStyleListAdapter(){
        viewModel.mapsStylesItems = resources.getStringArray(R.array.map_styles_items)
        viewModel.arrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_map_style,
            viewModel.mapsStylesItems
        )
    }


    private val dialogUpdateApplication = UpdateApplicationFragment()
    private fun launchUpdateApplicationFragment() {

        if (!dialogUpdateApplication.isVisible) {
            dialogUpdateApplication.show(requireActivity().supportFragmentManager,"updateApplicationFragment")
        }

    }


}