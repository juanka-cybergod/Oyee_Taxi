package com.cybergod.oyeetaxi.ui.preferences.fragments.userPreferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentPreferencesMainBinding
import com.cybergod.oyeetaxi.maps.TypeAndStyle
import com.cybergod.oyeetaxi.maps.Utils.getMapStyleByName
import com.cybergod.oyeetaxi.maps.Utils.getStyleNameByMapStyle
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.dilogs.fragments.UpdateApplicationFragment
import com.cybergod.oyeetaxi.ui.preferences.dilogs.SocialSupportFragment
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.userPreferences.PreferencesViewModel
import com.cybergod.oyeetaxi.ui.splash.viewmodel.SplashViewModel
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionInt
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAppVersionString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        (requireActivity() as BaseActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        setupMapStyleListAdapter()

        setupOnClickListener()

        setupObservers()

        loadData()


        return  binding.root
    }



    @SuppressLint("SetTextI18n")
    private fun loadData() {

        binding.tvVersion.text = "Versión ${getAppVersionString()}"
        viewModel.getServerConfiguration()

    }

    private fun setupObservers() {

        viewModel.readMapStyle.observe(viewLifecycleOwner, Observer {

            binding.tvEstilosMapa.setText(
                getStyleNameByMapStyle(it)
            )

            binding.tvEstilosMapa.clearFocus()


            fillTexViewMapStyles()

        })


    }



    private fun getAvailableUpdates() {
        lifecycleScope.launch (Dispatchers.Main){

            val actualizacion = splashViewModel.getCurrentAppUpdate()
            if (actualizacion != null ){

                if (actualizacion.errorResponse.isNullOrEmpty()) {
                    launchUpdateApplicationFragment()
                } else {
                    Toast.makeText(requireContext(),actualizacion.errorResponse, Toast.LENGTH_LONG).show()
                }

            }  else {
                Toast.makeText(requireContext(),getString(R.string.fail_server_conection), Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun setupOnClickListener() {


        binding.btnRedesSociales.setOnClickListener {

            val socialConfiguration = viewModel.serverConfiguration.value?.socialConfiguracion

            if ( socialConfiguration != null) {
                if (socialConfiguration.disponible == true) {
                    launchSocialSupportFragment()
                } else {
                    Toast.makeText(requireContext(),getString(R.string.not_abailable_for_the_moment), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(),getString(R.string.fail_server_comunication), Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnComprobarActualizacions.setOnClickListener {
            getAvailableUpdates()
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

    private val socialSupportFragment = SocialSupportFragment()
    private fun launchSocialSupportFragment(){
        if (!socialSupportFragment.isVisible) {
            socialSupportFragment.show(requireActivity().supportFragmentManager,"socialSupportFragment")
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.preferences_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return when (item.itemId) {
            R.id.action_admin -> {

                if (currentUserActive.value?.administrador == true || currentUserActive.value?.superAdministrador == true ) {
                    findNavController().navigate(R.id.action_go_to_administrationFragment)
                } else {
                    showSnackBar(
                        getString(R.string.no_access_permited),
                        true
                    )
                }

                true
            }
            R.id.action_superAdmin -> {

                if (currentUserActive.value?.superAdministrador == true ) {
                    findNavController().navigate(R.id.action_go_to_superAdminFragment)
                } else {
                    showSnackBar(
                        getString(R.string.no_access_permited),
                        true
                    )
                }

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }




//if (isCurrentFragment(R.id.preferencesFragment )) -> Use
//    private fun isCurrentFragment(destinationFragment:Int):Boolean {
//        val fragment = supportFragmentManager.findFragmentById(R.id.navPreferencesFragment)
//        return NavHostFragment.findNavController(fragment!!).currentDestination?.id == destinationFragment
//    }




}