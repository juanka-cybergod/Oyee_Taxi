package com.cybergod.oyeetaxi.ui.main.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentHomeBinding
import com.cybergod.oyeetaxi.maps.*
import com.cybergod.oyeetaxi.maps.Constants.USER_LOCATION
import com.cybergod.oyeetaxi.services.ServiceController.startLocationService
import com.cybergod.oyeetaxi.services.ServiceController.stopLocationService
import com.cybergod.oyeetaxi.services.TrackerService
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.controlPanel.activity.UserControlPanelActivity
import com.cybergod.oyeetaxi.ui.main.viewmodel.HomeViewModel
import com.cybergod.oyeetaxi.ui.preferences.activity.PreferencesActivity
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.utils.Constants.DELAY_VEHICLES_UPDATE
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_ID
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.hashMapMarkers
import com.cybergod.oyeetaxi.utils.GlobalVariables.map
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationCircle
import com.cybergod.oyeetaxi.utils.GlobalVariables.userLocationMarker
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentMapStyle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener,
    MaterialSearchBar.OnSearchActionListener, TextWatcher,
    SuggestionsAdapter.OnItemViewClickListener {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val typeAndStyle by lazy { TypeAndStyle(requireContext()) }
    private val cameraControl by lazy { CameraControl() }
    private val markerControl by lazy { MarkerControl() }
    private val mapControl by lazy { MapControl() }
    private val placesControl by lazy { PlacesControl() }


    //1er Metodo Inicializar el ViewModel preferentemete en el onCreateView de esta Manera>viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    //lateinit var viewModel: HomeViewModel
    //2do Metodo Inicializa el Modelo desde el principio
    //val viewModelOld = ViewModelProvider(this)[HomeViewModel::class.java]
    /*** OK 3er Metodo Para ViewModel
    -> by  viewModels() -> Te brinda la instancia ViewModel con alcance para el fragmento actual y siempre Será diferente para todos los distintos fragmentos.
    -> by activityViewModels() -> Te brinda la instancia ViewModel con alcance para la actividad actual y todos sus Fragments. Por lo tanto, la instancia permanecerá igual en varios fragmentos en la misma actividad.
     */

    val homeViewModel: HomeViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //Inflar la Vista
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Habilita el uso del Menu *Detro de los Fragment
        //setHasOptionsMenu(true)




            setupObservers()

            //Prepara el Fragment para que soporte los Mapas de Google
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)


            setupOnClickListeners()


        return  binding.root



    }



    private fun setupObservers() {
        //Preparar el ViewModel para Escuchar los Vehiculos
        setupAvilablesVehiclesObserver()

        //Observa la Provincia Seleccionada para ir a ella en el Mapa
        goToProvinceObserver()

        //Observa si hay o no vehiculos disponibles para motrar mensaje
        setupIsVehiclesAviablesObservers()

        //CurrentuserIcoObserver
        setupCurrentUserIcoObserver()

    }

    private fun setupCurrentUserIcoObserver() {
        currentUserActive.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.imagePerfil.loadImagePerfilFromURL(it.imagenPerfilURL)
            }
        })
    }

    private fun setupOnClickListeners() {
        binding.imagePerfil.setOnClickListener {
            launchUserControlPanelActivity()
        }


        binding.imagePerfil.setOnLongClickListener {

            if (TrackerService.started.value == true) {
                requireContext().stopLocationService()
            } else {
                requireContext().startLocationService()
            }

            true
        }



        binding.materialSearchBar.setOnSearchActionListener(this)
        binding.materialSearchBar.addTextChangeListener(this)
        binding.materialSearchBar.setSuggestionsClickListener(this)
    }

    override fun OnItemClickListener(position: Int, v: View?) {
        //TODO VOLVER A HABILITAR ESTA OPCION CUANDO ESTE LISTA LA API PLACES
//        if (position >=  placesControl.sugestionListPlaces.value!!.size){
//            return
//        }

        if (placesControl.predictionListPlaces.isNotEmpty()) {
            val selectedPrediction : AutocompletePrediction = placesControl.predictionListPlaces[position]
            placesControl.getLocationFromPlace(selectedPrediction.placeId)

        }

        val suggestion : String = binding.materialSearchBar.lastSuggestions[position].toString()
        binding.materialSearchBar.text=suggestion
        //TODO Añadir un Retraso para despues Limpiar la Sugerencia
        binding.materialSearchBar.clearSuggestions()
        requireView().hideKeyboard()

    }

    override fun OnItemDeleteListener(position: Int, v: View?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
        placesControl.searchLocation(text.toString(),requireContext())
        placesControl.sugestionListPlaces.observe(viewLifecycleOwner, Observer { lugaresEncontrados ->
            if (!lugaresEncontrados.isNullOrEmpty()) {

                binding.materialSearchBar.updateLastSuggestions(lugaresEncontrados)
                if (!binding.materialSearchBar.isSuggestionsVisible) {
                    binding.materialSearchBar.showSuggestionsList()
                }

            } else {

            }

        })
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun onSearchStateChanged(enabled: Boolean) {
    }

    override fun onSearchConfirmed(text: CharSequence?) {

        Toast.makeText(requireContext(),text.toString(),Toast.LENGTH_LONG).show()
        //crearViaje()
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_NAVIGATION -> {

                launchPreferencesActivity()

            }
            MaterialSearchBar.BUTTON_BACK -> {

            }
            else -> {

            }
        }
    }

    private fun setupObserveCurrentLocationFromTrackerService(){

        TrackerService.currentLocationFromService.observe(viewLifecycleOwner, Observer { currentLocation ->
            currentLocation?.let { location ->
                homeViewModel.updateCurrentUserLocation(location)
                updateUserLocationOnMap(location)
            }
        })



    }

    private fun setupIsVehiclesAviablesObservers(){

        homeViewModel.isVehiclesAviables.observe(viewLifecycleOwner) { vehiclesAviables ->
            if (!vehiclesAviables) {
                requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),  R.color.colorSnackBarSuccess);
                binding.tvHomeErrorMessage.text = getString(R.string.no_vehicles_aviables_for_now)
                binding.llHomeError.visibility = View.VISIBLE
            } else {
                requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),  R.color.yellow_light);
                binding.llHomeError.visibility = View.GONE
            }
        }

    }

    private fun goToProvinceObserver() {
            homeViewModel.goToProvince.observe(viewLifecycleOwner, Observer { province ->
                cameraControl.moveCameraToProvince(province)
            })

    }

    private fun setupAvilablesVehiclesObserver(){

        homeViewModel.vehicleList.observe(viewLifecycleOwner, Observer {  listVehiculoResponse ->

            if (listVehiculoResponse != null) {

                //Buscar y quitar los Marcadores que ya no son vehiculos Disponibles
                markerControl.removeUnavailableVehiclesFromMapMarker(listVehiculoResponse)


                if (listVehiculoResponse.isNotEmpty()) {

                    //Alerta al View Model para quitar mensaje de que no hay vehiculos disponibles
                    homeViewModel.isVehiclesAviables.postValue(true)

                    listVehiculoResponse.forEach { vehiculo ->

                        // Evita Mostrar el Vehiculo perteneciente al propio usuario si este es Conductor
                        if (currentUserActive.value?.id != vehiculo.usuario?.id) {

                            //Actualizar los Marcadores en el Mapa en Tiempo Real (ADD/UPDATE)
                            if (hashMapMarkers.containsKey(vehiculo.id)) {
                                //Actualiza el Marcador
                                markerControl.updateVehicleMarker(vehiculo)
                            } else {
                                //Añade el nuevo Marcador
                                markerControl.addVehicleMarker(vehiculo)
                            }

                        }

                    }


                } else {
                    homeViewModel.isVehiclesAviables.postValue(false)
                }


            } else {
                homeViewModel.isVehiclesAviables.postValue(true)

            }

        })

    }

    override fun onResume() {

        //hashMapMarkers.value?.clear()

        homeViewModel.getAvailableVehicles()

        homeViewModel.stopMapVehicleUpdate = false

        continueslyUpdateVhieclesInTheMap()

        super.onResume()
    }



    override fun onStop() {

        homeViewModel.stopMapVehicleUpdate = true

        userLocationMarker?.remove()
        userLocationMarker=null

        userLocationCircle?.remove()
        userLocationCircle = null

        super.onStop()
    }


    fun continueslyUpdateVhieclesInTheMap(){

        homeViewModel.coroutine.launch(Dispatchers.IO) {
            //Log.d("CoroutineScope","Started ...")
            while (!homeViewModel.stopMapVehicleUpdate) {
                //Log.d("CoroutineScope","Running ...")
                delay(DELAY_VEHICLES_UPDATE)
                homeViewModel.getAvailableVehicles()
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        //Esto es lo 1ro Asignar el Mapa
        map = googleMap


        //Si esta en Modo Obscuro o no para definir el Estilo del Mapa
        if (deviceInDarKMode()) {
           typeAndStyle.setMapType(mapStyle = TypeAndStyle.MapStyle.NIGHT)
        } else {
            typeAndStyle.setMapType(currentMapStyle)
        }


        //Establece la configuracion del UI para el mapa
        mapControl.setupUISettings()


        //Prepara la Actualizacion de la Ubicacion Actual Personalizada y no la por Defecto
        try {
            map.isMyLocationEnabled = true
        } catch (e :Exception) {
            Log.d("HomeFragment","imposible Activar la Ubicacion Motivo : $e")
        }

        //Ejecuta Solo una Vez
        goToProvinceOneTime()


        //Prepara los Click Listener sobre el Mapa
        setupMapOnClicksListeners()


        //Observa la Localizacion en Servicio de Localizacion
        setupObserveCurrentLocationFromTrackerService()



    }


    fun updateUserLocationOnMap(location :Location){

        if (map != null) {
            if (userLocationMarker == null) {
                //Añadir Marcador de Usuario en la Ubicacion Actual
                markerControl.addUserLocationMarker(location)
            } else {
                //Actualizar el Marcador de Usuario a la nueva Posicion
                markerControl.updateUserLocationMarker(location)
            }
        }

    }




    @SuppressLint("PotentialBehaviorOverride")
    private fun setupMapOnClicksListeners() {

        map.setOnMapClickListener {
            // toastText("Seleccione vehículo para ver Detalles")
            //markerControl.addDefaultMarker("Default",map,it,this)
            //pauseMapClearForUpdate = false
        }


        map.setOnMapLongClickListener {
             //markerControl.addDefaultMarker("",mapViewModel.map,it,)
        }

        //Poner a Trabajor los Eventos de Toque ETC y Establece los Click Listener para el Mapa
        map.setOnMarkerClickListener(this)
    }


    //Al cargar el mapa la 1ra vez aplica efecto de acercamiento a la provincia que cada cual
    private fun goToProvinceOneTime() {

        if (!homeViewModel.aleadyRuned) {
                currentUserActive.value?.provincia?.ubicacion?.let { location ->
                    cameraControl.goToUbicacionInicialProvincia( location)
                }

        }

        homeViewModel.aleadyRuned = true

    }


    //Escucha el Evento onClick sobre los Marcadores para mostrar el infoWindows Personalizado
    override fun onMarkerClick(marker: Marker): Boolean {

            //Lanzar el infoWindows Personallizado
            //marker.showInfoWindow()


            lifecycleScope.launch {
                //delay(100L)
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.position) , 300, object: GoogleMap.CancelableCallback{
                    override fun onCancel() {
                        //Nada
                    }

                    override fun onFinish() {
                        if (marker.tag.toString().equals(USER_LOCATION,true)) {

                            Toast.makeText(requireContext(),"Tu Ubicación Actual",Toast.LENGTH_SHORT).show()
                        } else {
                            marker.tag.let {
                                it.toString().let { vehicleId->
                                    if (vehicleId.isNotEmpty()) {
                                        findNavController().navigate(R.id.action_navigation_home_to_vehicleDetailFragment,Bundle().apply {
                                            putString(KEY_VEHICLE_ID,vehicleId)
                                        })
                                    }
                                }
                            }


                        }


                    }
                })
            }



            return true//Retornar True para hacer Animaciones y mostrar informacion predefinida



        //Retornar false para Retorna el infoWindows por defecto **No Usar
        //return false/




    }


    private fun crearViaje(){
        findNavController().navigate(R.id.action_navigation_home_to_viajeFragment,Bundle().apply {
            //putString(KEY_VEHICLE_ID,vehicleId)
        })
    }

    private fun launchPreferencesActivity(){

        startActivity(
            Intent(requireActivity(),
                PreferencesActivity::class.java)
        )

    }


    private fun launchUserControlPanelActivity(){

        startActivity(
            Intent(requireActivity(),
                UserControlPanelActivity::class.java)
        )

    }


//    //Escucha la opcionSeleccionada en el Menu
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        typeAndStyle.setMapType(item,map,this.requireContext())
//
//        when(item.itemId){
//
//            R.id.provinces -> {
//                findNavController().navigate(R.id.action_navigation_home_to_provincesFragment)
//            }
//            R.id.addVehicle -> {
//                //launchVehicleRegisterActivity()
//            }
//
//        }
//
//        return super.onOptionsItemSelected(item)
//    }


//    //Inflar los Menus para es
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_provinces,menu)
//        inflater.inflate(R.menu.menu_map_types,menu)
//        inflater.inflate(R.menu.menu_service,menu)
//        inflater.inflate(R.menu.menu_test,menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }



}